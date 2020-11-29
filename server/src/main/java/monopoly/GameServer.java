package monopoly;

import java.io.IOException;

import monopoly.lobby.Lobby;
import monopoly.lobby.LobbyOwner;
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
	private GameServer() throws IOException {
		super();
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
		User currentUser = model.getByID(connectionID);
		Lobby currentLobby = currentUser.getLobby();

		if (currentLobby == null) {
			return;
		}

		for (int i = 0; i < currentLobby.getPlayers().size(); i++) {
			if (!currentLobby.getPlayers().get(i).equals(currentUser)) {
				sendRealTimePacket(new RealTimeNetworkPacket(connectionID),
						currentLobby.getPlayers().get(i).getConnectionId());
			}
		}
	}

	@Override
	public void receivedImportantPacket(int connectionID, ImportantNetworkPacket packet) {
		User currentUser = model.getByID(connectionID);

		if (currentUser != null) {
			//Take action depending on the received packet type
			if (packet.getType() == PacketType.CONNECT) {
				handleConnect(currentUser, connectionID);
				
			} else if (packet.getType() == PacketType.GET_LOBBY_COUNT) {
				sendImportantPacket(new ImportantNetworkPacket(PacketType.LOBBY_COUNT,
						new IntegerPacketData(model.getLobbies().size())), connectionID);

			} else if (packet.getType() == PacketType.GET_LOBBIES) {
				handleLobbies( connectionID, packet);

			} else if (packet.getType() == PacketType.CREATE_LOBBY) {
				handleCreate( currentUser, connectionID, packet);

			} else if (packet.getType() == PacketType.JOIN_LOBBY) {
				handleJoin( currentUser, connectionID, packet);

			} else if (packet.getType() == PacketType.LEAVE_LOBBY) {
				handleLeave( currentUser, connectionID);

			} else if (packet.getType() == PacketType.SET_READY) {
				handleReady( currentUser, connectionID);
				
			} else {
				handleStart( currentUser);
			}
		} else {
			sendImportantPacket(new ImportantNetworkPacket(PacketType.ERR_UNKNOWN), connectionID);
		}
	}
	
	/**
     * Handles the connect request from server side
     * 
     * @param user 			the user to be connected
     * @param connectionID 	the id of the user
     */
	private void handleConnect(User user, int connectionID) {
		model.userLogin(user.getUsername(), connectionID);

		sendImportantPacket(new ImportantNetworkPacket(PacketType.ACCEPTED, new StringPacketData(user.getUsername())),
				connectionID);
	}
	
	/**
     * Handles the get lobbies request from server side
     * 
     * @param connectionID 	the id of the user
     * @param packet 		the received network packet
     */
	private void handleLobbies(int connectionID, ImportantNetworkPacket packet) {
		//Get the lobby numbers to be displayed as minimum and maximum
		int min = ((IntegerPacketData) packet.getData().get(0)).getData();
		int max = ((IntegerPacketData) packet.getData().get(1)).getData();

		if (min < 0) {
			min = 0;
		}

		if (max > model.getLobbies().size()) {
			max = model.getLobbies().size();
		}
		
		//Create lobby list packet data to be sent to the user
		LobbyListPacketData lobbyList = new LobbyListPacketData();

		for (int i = min; i < max; i++) {
			String name = model.getLobbies().get(i).getName();
			String password = model.getLobbies().get(i).getPassword();
			Boolean isPublic = model.getLobbies().get(i).getPublic();
			int limit = model.getLobbies().get(i).getPlayerLimit();
			long id = model.getLobbies().get(i).getId();
			
			//Create a lobby packet data to be added to lobby data list
			LobbyPacketData lobbyData = new LobbyPacketData(id, name, isPublic, password, limit);

			lobbyList.add(lobbyData);
		}

		sendImportantPacket(new ImportantNetworkPacket(PacketType.LOBBY_LIST, lobbyList), connectionID);
	}
	
	/**
     * Handles the create lobby request from server side
     * 
     * @param user 			the user to create the lobby
     * @param connectionID 	the id of the user
     * @param packet		the received network packet
     */
	private void handleCreate( User user, int connectionID, ImportantNetworkPacket packet) {
		long id = ((IntegerPacketData) packet.getData().get(0)).getData();
		String name = ((StringPacketData) packet.getData().get(1)).getData();
		Boolean isPublic = ((BooleanPacketData) packet.getData().get(2)).getData();
		String password = ((StringPacketData) packet.getData().get(3)).getData();
		int limit = ((IntegerPacketData) packet.getData().get(4)).getData();
		
		//Create a lobby and make current user the host
		LobbyOwner host = user.createLobby(name, limit, isPublic, password);
		user = host;
		model.addLobby( user.getLobby());

		sendImportantPacket(new ImportantNetworkPacket(PacketType.LOBBY_CREATED,
				new LobbyPacketData(id, name, isPublic, password, limit)), connectionID);
	}
	
	/**
     * Handles the join lobby request from server side
     * 
     * @param user 			the user to be added to the lobby
     * @param connectionID 	the id of the user
     * @param packet		the received network packet
     */
	private void handleJoin( User user, int connectionID, ImportantNetworkPacket packet) {
		long id = ((LobbyPacketData) packet.getData().get(0)).getLobbyId();
		String password = ((LobbyPacketData) packet.getData().get(0)).getPassword();
		Lobby lobby = model.getByID(id);
		
		//Check if the user can join the lobby
		if (user.getLobby() != null) {
			sendImportantPacket(new ImportantNetworkPacket(PacketType.ERR_ALREADY_IN_LOBBY), connectionID);
			
		} else if (lobby.getPlayerCount() == lobby.getPlayerLimit()) {
			sendImportantPacket(new ImportantNetworkPacket(PacketType.ERR_LOBBY_FULL), connectionID);
			
		} else if (!password.equals(lobby.getPassword())) {
			sendImportantPacket(new ImportantNetworkPacket(PacketType.ERR_INVALID_PASSWORD), connectionID);
			
		} else if (!lobby.getPublic() && password.equals("")) {
			sendImportantPacket(new ImportantNetworkPacket(PacketType.ERR_LOBBY_PRIVATE), connectionID);
			
		} else {
			user.joinLobby(lobby, password);
			
			//Send to join status of the current user to everyone in the lobby
			for (int i = 0; i < lobby.getPlayerCount(); i++) {
				if (!lobby.getPlayers().get(i).equals(user)) {
					sendImportantPacket(
							new ImportantNetworkPacket(PacketType.PLAYER_JOIN,
									new PlayerPacketData(connectionID, user.getUsername())),
							lobby.getPlayers().get(i).getConnectionId());
				}
			}

			sendImportantPacket(new ImportantNetworkPacket(PacketType.JOIN_SUCCESS), connectionID);
		}
	}
	
	/**
     * Handles the leave lobby request from server side
     * 
     * @param user 			the user to be removed from the lobby
     * @param connectionID 	the id of the user
     */
	private void handleLeave( User user, int connectionID) {
		Lobby currentLobby = user.getLobby();

		if (currentLobby == null) {
			sendImportantPacket(new ImportantNetworkPacket(PacketType.ERR_NOT_IN_LOBYY), connectionID);
		} else {
			user.leaveLobby();
			
			//Send the left status of the current user to everyone in the lobby
			for (int i = 0; i < currentLobby.getPlayerCount(); i++) {
				sendImportantPacket(
						new ImportantNetworkPacket(PacketType.PLAYER_LEFT,
								new StringPacketData(user.getUsername())),
						currentLobby.getPlayers().get(i).getConnectionId());
			}

			sendImportantPacket(new ImportantNetworkPacket(PacketType.LEAVE_SUCCESS), connectionID);
		}
	}
	
	/**
     * Handles the set ready request from server side
     * 
     * @param user 			the user to be set ready
     * @param connectionID 	the id of the user
     */
	private void handleReady( User user, int connectionID) {
		if (user.getLobby() == null) {
			sendImportantPacket(new ImportantNetworkPacket(PacketType.ERR_NOT_IN_LOBYY,
					new BooleanPacketData(user.isReady())), connectionID);
		} else {
			Lobby currentLobby = user.getLobby();

			user.setReady(true);
			
			//Send the ready status of the current user to everyone in the lobby
			for (int i = 0; i < currentLobby.getPlayerCount(); i++) {
				if (!currentLobby.getPlayers().get(i).equals(user)) {
					sendImportantPacket(
							new ImportantNetworkPacket(PacketType.PLAYER_READY,
									new StringPacketData(user.getUsername()),
									new BooleanPacketData(user.isReady())),
							currentLobby.getPlayers().get(i).getConnectionId());
				}
			}

			sendImportantPacket(new ImportantNetworkPacket(PacketType.SET_READY_SUCCESS), connectionID);
		}
	}
	
	/**
     * Handles the game start request from server side
     * 
     * @param user the user to get the current lobby from
     */
	private void handleStart( User user) {
		Lobby currentLobby = user.getLobby();
		
		if (currentLobby != null) {
			//Check if everyone is ready
			for (int i = 0; i < currentLobby.getPlayerCount(); i++) {
				if (!currentLobby.getPlayers().get(i).isReady()) {
					return;
				}
			}
			currentLobby.startGame();
			
			//Send game start packet to everyone in the lobby
			for (int i = 0; i < currentLobby.getPlayerCount(); i++) {
				sendImportantPacket(new ImportantNetworkPacket(PacketType.GAME_START),
						currentLobby.getPlayers().get(i).getConnectionId());
			}

		}
	}
	

}
