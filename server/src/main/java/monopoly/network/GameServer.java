package monopoly.network;

import java.io.IOException;
import java.util.List;

import lombok.Setter;
import monopoly.Model;
import monopoly.MonopolyException;
import monopoly.gameplay.Game;
import monopoly.gameplay.GamePlayer;
import monopoly.lobby.Lobby;
import monopoly.lobby.User;
import monopoly.network.packet.important.ImportantNetworkPacket;
import monopoly.network.packet.important.PacketType;
import monopoly.network.packet.important.packet_data.BooleanPacketData;
import monopoly.network.packet.important.packet_data.IntegerPacketData;
import monopoly.network.packet.important.packet_data.StringPacketData;
import monopoly.network.packet.important.packet_data.gameplay.DicePacketData;
import monopoly.network.packet.important.packet_data.gameplay.PlayerPacketData;
import monopoly.network.packet.important.packet_data.gameplay.TilePacketData;
import monopoly.network.packet.important.packet_data.lobby.LobbyListPacketData;
import monopoly.network.packet.important.packet_data.lobby.LobbyPacketData;
import monopoly.network.packet.realtime.RealTimeNetworkPacket;

/**
 * Game Server class for server-side operations
 *
 * @author Alper Sarı, Javid Baghirov
 * @version Nov 29, 2020
 */
public class GameServer extends Server {
	private static GameServer instance;

	@Setter
	private Model model;

	/**
	 * Creates a TCP and UDP server
	 *
	 * @throws IOException if the server could not be opened
	 */
	private GameServer() throws IOException {
		super();
	}

	/**
	 * Singleton getter method for server. Creates a TCP and UDP server if there is
	 * no server instance. Terminates if the server could not be opened
	 * 
	 * @return the game server instance
	 */
	public static GameServer getInstance() {
		if (instance == null) {
			try {
				instance = new GameServer();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		return instance;
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
		Lobby lobby = model.getLobbyOfUser(connectionID);
		if (lobby == null) {
			return;
		}

		lobby.sendRealtimePacket(connectionID, packet);
	}

	@Override
	public void receivedImportantPacket(int connectionID, ImportantNetworkPacket packet) {
		// Take action depending on the received packet type
		if (packet.getType() == PacketType.CONNECT) {
			handleConnect(connectionID);
		} else if (packet.getType() == PacketType.GET_LOBBY_COUNT) {
			handleGetLobbyCount(connectionID);

		} else if (packet.getType() == PacketType.GET_LOBBIES) {
			handleGetLobbies(connectionID, packet);

		} else if (packet.getType() == PacketType.CREATE_LOBBY) {
			handleCreate(connectionID, packet);

		} else if (packet.getType() == PacketType.JOIN_LOBBY) {
			handleJoin(connectionID, packet);

		} else if (packet.getType() == PacketType.GET_USERS_IN_LOBBY) {
			handleGetUsers(connectionID, packet);

		} else if (packet.getType() == PacketType.LEAVE_LOBBY) {
			handleLeave(connectionID);

		} else if (packet.getType() == PacketType.SET_READY) {
			handleReady(connectionID);

		} else if (packet.getType() == PacketType.ROLL_DICE) {
			handleDiceRoll(connectionID);
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

	private void handleGetLobbyCount(int connectionID) {
		sendImportantPacket(
				new ImportantNetworkPacket(PacketType.LOBBY_COUNT, new IntegerPacketData(model.getLobbyCount())),
				connectionID);
	}

	/**
	 * Handles the get lobbies request from server side
	 * 
	 * @param connectionID the id of the user
	 * @param packet       the received network packet
	 */
	private void handleGetLobbies(int connectionID, ImportantNetworkPacket packet) {
		// Get the lobby numbers to be displayed as minimum and maximum
		int min = ((IntegerPacketData) packet.getData().get(0)).getData();
		int max = ((IntegerPacketData) packet.getData().get(1)).getData();
		List<Lobby> lobbies = model.getWaitingLobbies(min, max);

		// Create lobby list packet data to be sent to the user
		LobbyListPacketData lobbyList = new LobbyListPacketData();
		lobbies.forEach(lobby -> lobbyList.add(lobby.getAsPacket()));

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
		boolean isPublic = lobbyData.isPublic();
		String password = lobbyData.getPassword();
		int limit = lobbyData.getPlayerLimit();

		// Create the lobby
		try {
			model.createLobby(name, limit, isPublic, password, connectionID);
		} catch (MonopolyException e) {
			sendImportantPacket(e.getAsPacket(), connectionID);
		}

	}

	/**
	 * Handles the join lobby request from server side
	 * 
	 * @param connectionID the id of the user
	 * @param packet       the received network packet
	 */
	private void handleJoin(int connectionID, ImportantNetworkPacket packet) {
		LobbyPacketData lobbyData = (LobbyPacketData) packet.getData().get(0);
		int lobbyId = lobbyData.getLobbyId();
		String password = lobbyData.getPassword();

		Lobby lobby = model.getLobbyByID(lobbyId);
		User user = model.getUserByID(connectionID);

		try {
			user.joinLobby(lobby, password);
		} catch (MonopolyException e) {
			sendImportantPacket(e.getAsPacket(), connectionID);
		}

	}

	/**
	 * Handles the get users request from server side
	 * 
	 * @param connectionID the id of the user
	 * @param packet       the received network packet
	 */
	private void handleGetUsers(int connectionID, ImportantNetworkPacket packet) {
		LobbyPacketData lobbyData = (LobbyPacketData) packet.getData().get(0);
		int lobbyId = lobbyData.getLobbyId();

		Lobby lobby = model.getLobbyByID(lobbyId);

		sendImportantPacket(new ImportantNetworkPacket(PacketType.USERS_IN_LOBBY, lobby.getPlayersAsPacket()),
				connectionID);
	}

	/**
	 * Handles the leave lobby request from server side
	 * 
	 * @param connectionID the id of the user
	 */
	private void handleLeave(int connectionID) {
		User user = model.getUserByID(connectionID);

		try {
			user.leaveLobby();
			sendImportantPacket(new ImportantNetworkPacket(PacketType.LEAVE_SUCCESS), connectionID);
		} catch (MonopolyException e) {
			sendImportantPacket(e.getAsPacket(), connectionID);
		}
	}

	/**
	 * Handles the set ready request from server side
	 * 
	 * @param connectionID the id of the user
	 */
	private void handleReady(int connectionID) {
		User user = model.getUserByID(connectionID);
		try {
			user.setReady(true);
			sendImportantPacket(new ImportantNetworkPacket(PacketType.SET_READY_SUCCESS), connectionID);
		} catch (MonopolyException e) {
			sendImportantPacket(e.getAsPacket(), connectionID);
		}
	}

	private void handleDiceRoll(int connectionID) {
		Game game = model.getLobbyOfUser(connectionID).getGame();
		GamePlayer player = game.getCurrentPlayer();

		try {
			DicePacketData diceData = player.rollDice();
			game.sendDiceResultToPlayers(diceData);
			game.playTurn();
		} catch (MonopolyException e) {
			sendImportantPacket(e.getAsPacket(), connectionID);
		}
	}

	/**
	 * Sends playerJoin notification to the client
	 * 
	 * @param userJoined   the joined user
	 * @param userToNotify the user to be notified
	 */
	public void sendPlayerJoinNotification(User userJoined, User userToNotify) {
		sendImportantPacket(new ImportantNetworkPacket(PacketType.PLAYER_JOIN, userJoined.getAsPacket()),
				userToNotify.getId());
	}

	/**
	 * Sends join success notification to the client
	 * 
	 * @param userJoined the user to be notified
	 */
	public void sendSuccessfulJoinNotification(User userJoined) {
		sendImportantPacket(new ImportantNetworkPacket(PacketType.JOIN_SUCCESS), userJoined.getId());
	}

	/**
	 * Sends playerLeft notification to the client
	 * 
	 * @param userLeft     the left user
	 * @param userToNotify the user to be notified
	 */
	public void sendPlayerLeaveNotification(User userLeft, User userToNotify) {
		sendImportantPacket(new ImportantNetworkPacket(PacketType.PLAYER_LEFT, userLeft.getAsPacket()),
				userToNotify.getId());
	}

	/**
	 * Sends playerReady notification to the client
	 * 
	 * @param userReady    the ready user
	 * @param userToNotify the user to be notified
	 */
	public void sendPlayerReadyNotification(User userReady, User userToNotify) {
		sendImportantPacket(new ImportantNetworkPacket(PacketType.PLAYER_READY, userReady.getAsPacket(),
				new BooleanPacketData(userReady.isReady())), userToNotify.getId());
	}

	/**
	 * Sends game start notification to the client
	 * 
	 * @param user the user to be notified
	 */
	public void sendGameStartNotification(User user) {
		sendImportantPacket(new ImportantNetworkPacket(PacketType.GAME_START), user.getId());
	}

	public void sendPlayersTurnOrderNotification(GamePlayer player) {
		Game game = model.getLobbyOfUser(player.getId()).getGame();

		sendImportantPacket(new ImportantNetworkPacket(PacketType.PLAYER_TURN_ORDER, game.getPlayersAsPacket()),
				player.getId());
	}

	public void sendPlayerTurnNotification(GamePlayer player) {
		Game game = model.getLobbyOfUser(player.getId()).getGame();
		PlayerPacketData playerData = game.getCurrentPlayer().getPlayerAsPacket();

		sendImportantPacket(new ImportantNetworkPacket(PacketType.PLAYER_TURN, playerData), player.getId());
	}

	public void sendDiceRollNotification(GamePlayer player, DicePacketData data) {
		sendImportantPacket(new ImportantNetworkPacket(PacketType.DICE_RESULT, data), player.getId());
	}

	public void sendTokenMovedNotification(GamePlayer playerMoved, TilePacketData tileData, GamePlayer playerToNotify) {
		sendImportantPacket(new ImportantNetworkPacket(PacketType.TOKEN_MOVED, playerMoved.getAsPacket(), tileData),
				playerToNotify.getId());
	}

	public void sendTurnCompleteNotification(GamePlayer player) {
		sendImportantPacket(new ImportantNetworkPacket(PacketType.TURN_COMPLETE), player.getId());

	}
}
