package monopoly;

import java.io.IOException;

import monopoly.lobby.Lobby;
import monopoly.lobby.User;
import monopoly.network.Server;
import monopoly.network.packet.important.ImportantNetworkPacket;
import monopoly.network.packet.important.PacketType;
import monopoly.network.packet.important.packet_data.BooleanPacketData;
import monopoly.network.packet.important.packet_data.IntegerPacketData;
import monopoly.network.packet.important.packet_data.LobbyListPacketData;
import monopoly.network.packet.important.packet_data.LobbyPacketData;
import monopoly.network.packet.important.packet_data.PlayerPacketData;
import monopoly.network.packet.important.packet_data.StringPacketData;
import monopoly.network.packet.realtime.RealTimeNetworkPacket;

/**
 * Game Server class for server-side operations
 *
 * @author Alper Sarı, Javid Baghirov
 * @version Nov 29, 2020
 */

public class GameServer extends Server {
	private Model model;

	/**
	 * Creates a TCP and UDP server
	 *
	 * @throws IOException if the server could not be opened
	 */
	public GameServer(Model model) throws IOException {
		super();
		this.model = model;
	}

	@Override
	public void connected(int connectionID) {
		// To be done in later versions
	}

	@Override
	public void disconnected(int connectionID) {
		// To be done in later versions
	}

	@Override
	public void receivedRealTimePacket(int connectionID, RealTimeNetworkPacket packet) {
		if (model.findLobby(connectionID) == null) {
			return;
		}

		// Send the network data of current user to everyone except himself in the lobby
		new Thread(() -> {
			for (int i = 0; i < model.findLobby(connectionID).getPlayers().size(); i++) {
				if (!model.findLobby(connectionID).getPlayers().get(i).equals(model.getByID(connectionID))) {
					sendRealTimePacket(new RealTimeNetworkPacket(connectionID),
							model.findLobby(connectionID).getPlayers().get(i).getConnectionId());
				}
			}
		}).start();
	}

	@Override
	public void receivedImportantPacket(int connectionID, ImportantNetworkPacket packet) {

		// Take action depending on the received packet type
		if (packet.getType() == PacketType.CONNECT) {
			handleConnect(connectionID);
		} else if (packet.getType() == PacketType.GET_LOBBY_COUNT) {
			sendImportantPacket(new ImportantNetworkPacket(PacketType.LOBBY_COUNT,
					new IntegerPacketData(model.getLobbies().size())), connectionID);

		} else if (packet.getType() == PacketType.GET_LOBBIES) {
			handleLobbies(connectionID, packet);

		} else if (packet.getType() == PacketType.CREATE_LOBBY) {
			handleCreate(connectionID, packet);

		} else if (packet.getType() == PacketType.JOIN_LOBBY) {
			handleJoin(connectionID, packet);

		} else if (packet.getType() == PacketType.LEAVE_LOBBY) {
			handleLeave(connectionID);

		} else if (packet.getType() == PacketType.SET_READY) {
			handleReady(connectionID);

		}
	}

	/**
	 * Handles the connect request from server side
	 * 
	 * @param connectionID the id of the user
	 */
	private void handleConnect(int connectionID) {
		String username = "Player" + connectionID;

		model.userLogin(username, connectionID);

		sendImportantPacket(new ImportantNetworkPacket(PacketType.ACCEPTED, new StringPacketData(username)),
				connectionID);
	}

	/**
	 * Handles the get lobbies request from server side
	 * 
	 * @param connectionID the id of the user
	 * @param packet       the received network packet
	 */
	private void handleLobbies(int connectionID, ImportantNetworkPacket packet) {
		// Get the lobby numbers to be displayed as minimum and maximum
		int min = ((IntegerPacketData) packet.getData().get(0)).getData();
		int max = ((IntegerPacketData) packet.getData().get(1)).getData();

		if (min < 0) {
			min = 0;
		}

		if (max > model.getLobbies().size()) {
			max = model.getLobbies().size();
		}

		// Create lobby list packet data to be sent to the user
		LobbyListPacketData lobbyList = new LobbyListPacketData();

		for (int i = min; i < max; i++) {
			String name = model.getLobbies().get(i).getName();
			String password = model.getLobbies().get(i).getPassword();
			Boolean isPublic = model.getLobbies().get(i).getPublic();
			int limit = model.getLobbies().get(i).getPlayerLimit();
			long id = model.getLobbies().get(i).getId();
			int playerCount = model.getLobbies().get(i).getPlayerCount();
			String hostName = model.getLobbies().get(i).getHost().getUsername();

			// Create a lobby packet data to be added to lobby data list
			LobbyPacketData lobbyData = new LobbyPacketData(id, name, password, isPublic, hostName, playerCount, limit);
			lobbyList.add(lobbyData);
		}

		sendImportantPacket(new ImportantNetworkPacket(PacketType.LOBBY_LIST, lobbyList), connectionID);
	}

	/**
	 * Handles the create lobby request from server side
	 * 
	 * @param connectionID the id of the user
	 * @param packet       the received network packet
	 */
	private void handleCreate(int connectionID, ImportantNetworkPacket packet) {
		LobbyPacketData lobbyData = ((LobbyPacketData) packet.getData().get(0));
		String name = lobbyData.getName();
		Boolean isPublic = lobbyData.isPublic();
		String password = lobbyData.getPassword();
		int limit = lobbyData.getPlayerLimit();

		// Create a lobby and get the connection id of the host
		int hostID = model.createLobby(name, limit, isPublic, password, connectionID).getConnectionId();

		sendImportantPacket(new ImportantNetworkPacket(PacketType.LOBBY_CREATED), hostID);
	}

	/**
	 * Handles the join lobby request from server side
	 * 
	 * @param connectionID the id of the user
	 * @param packet       the received network packet
	 */
	private void handleJoin(int connectionID, ImportantNetworkPacket packet) {
		long id = ((LobbyPacketData) packet.getData().get(0)).getLobbyId();
		String password = ((LobbyPacketData) packet.getData().get(0)).getPassword();

		// Check if the user can join the lobby
		if (model.getByID(connectionID).getLobby() != null) {
			sendImportantPacket(new ImportantNetworkPacket(PacketType.ERR_ALREADY_IN_LOBBY), connectionID);

		} else if (model.getByID(id).getPlayerCount() == model.getByID(id).getPlayerLimit()) {
			sendImportantPacket(new ImportantNetworkPacket(PacketType.ERR_LOBBY_FULL), connectionID);

		} else {
			sendImportantPacket(new ImportantNetworkPacket(PacketType.JOIN_SUCCESS), connectionID);

			model.joinLobby(model.getByID(id), password, connectionID);

			// Send the join status of the current user to everyone except himself in the
			model.sendJoinedNotification(model.getByID(connectionID));
		}
	}

	/**
	 * Handles the leave lobby request from server side
	 * 
	 * @param user         the user to be removed from the lobby
	 * @param connectionID the id of the user
	 */
	private void handleLeave(int connectionID) {
		Lobby currentLobby = model.getByID(connectionID).getLobby();

		if (currentLobby == null) {
			sendImportantPacket(new ImportantNetworkPacket(PacketType.ERR_NOT_IN_LOBYY), connectionID);
		} else {
			model.leaveLobby(currentLobby, connectionID);

			sendImportantPacket(new ImportantNetworkPacket(PacketType.LEAVE_SUCCESS), connectionID);

			// Send the left status of the current user to everyone in the lobby
			model.sendLeftNotification(model.getByID(connectionID));
		}
	}

	/**
	 * Handles the set ready request from server side
	 * 
	 * @param user         the user to be set ready
	 * @param connectionID the id of the user
	 */
	private void handleReady(int connectionID) {
		User user = model.getByID(connectionID);

		if (user.getLobby() == null) {
			sendImportantPacket(
					new ImportantNetworkPacket(PacketType.ERR_NOT_IN_LOBYY, new BooleanPacketData(user.isReady())),
					connectionID);
		} else {
			user.setReady(true);

			sendImportantPacket(new ImportantNetworkPacket(PacketType.SET_READY_SUCCESS), connectionID);
			// Send the ready status of the current user to everyone expect himself in the
			model.sendReadyNotification(user);
		}
	}

	public void sendPlayerJoinNotification(User playerJoined, User playerToNotify) {
		sendImportantPacket(
				new ImportantNetworkPacket(PacketType.PLAYER_JOIN,
						new PlayerPacketData(playerJoined.getConnectionId(), playerJoined.getUsername(), false)),
				playerToNotify.getConnectionId());
	}

	public void sendPlayerLeaveNotification(User playerLeft, User playerToNotify) {
		sendImportantPacket(
				new ImportantNetworkPacket(PacketType.PLAYER_LEFT, new StringPacketData(playerLeft.getUsername())),
				playerToNotify.getConnectionId());
	}

	public void sendPlayerReadyNotification(User playerReady, User playerToNotify) {
		sendImportantPacket(
				new ImportantNetworkPacket(PacketType.PLAYER_READY,
						new PlayerPacketData(playerReady.getConnectionId(), playerReady.getUsername(),
								playerReady.isHost()),
						new BooleanPacketData(playerReady.isReady())),
				playerToNotify.getConnectionId());
	}

	public void sendGameStartNotification(User user) {
		sendImportantPacket(new ImportantNetworkPacket(PacketType.GAME_START), user.getConnectionId());
	}
}
