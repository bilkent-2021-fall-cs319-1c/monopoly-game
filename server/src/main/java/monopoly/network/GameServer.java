package monopoly.network;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Setter;
import monopoly.Model;
import monopoly.MonopolyException;
import monopoly.gameplay.Game;
import monopoly.gameplay.GamePlayer;
import monopoly.gameplay.properties.Auction;
import monopoly.gameplay.tiles.PropertyTile;
import monopoly.lobby.Lobby;
import monopoly.lobby.User;
import monopoly.network.packet.important.ImportantNetworkPacket;
import monopoly.network.packet.important.PacketType;
import monopoly.network.packet.important.packet_data.BooleanPacketData;
import monopoly.network.packet.important.packet_data.IntegerPacketData;
import monopoly.network.packet.important.packet_data.StringPacketData;
import monopoly.network.packet.important.packet_data.gameplay.DicePacketData;
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
	private static Logger logger = LoggerFactory.getLogger(GameServer.class);
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
				logger.error("The server could not be opened", e);
				System.exit(0);
			}
		}
		return instance;
	}

	@Override
	public void connected(int connectionID) {
		// Do nothing, since connected packet will be received later
	}

	@Override
	public void disconnected(int connectionID) {
		// TODO Handle unexpected disconnection
	}

	@Override
	public void receivedRealTimePacket(int connectionID, RealTimeNetworkPacket packet) {
		Lobby lobby = model.getLobbyOfUser(connectionID);
		if (lobby == null) {
			return;
		}

		lobby.sendRealtimePacket(connectionID, packet);
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
				new ImportantNetworkPacket(PacketType.LOBBY_COUNT, new IntegerPacketData(model.getWaitingLobbyCount())),
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
	private void handleGetUsers(int connectionID) {
		Lobby lobby = model.getLobbyOfUser(connectionID);

		sendImportantPacket(new ImportantNetworkPacket(PacketType.USERS_IN_LOBBY, lobby.getUsersAsPacket()),
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
	private void handleReady(int connectionID, ImportantNetworkPacket packet) {
		User user = model.getUserByID(connectionID);
		boolean ready = ((BooleanPacketData) packet.getData().get(0)).getData();

		try {
			user.setReady(ready);
			sendImportantPacket(new ImportantNetworkPacket(PacketType.SET_READY_SUCCESS), connectionID);
		} catch (MonopolyException e) {
			sendImportantPacket(e.getAsPacket(), connectionID);
		}
	}

	private void handleGetGameData(int connectionID) {
		Game game = model.getGameOfPlayer(connectionID);

		sendImportantPacket(new ImportantNetworkPacket(PacketType.GAME_DATA, game.getPlayersAsPacket(),
				game.getBoard().getAsPacket()), connectionID);
	}

	private void handleDiceRoll(int connectionID) {
		GamePlayer player = model.getUserByID(connectionID).asPlayer();

		try {
			player.rollDice();
		} catch (MonopolyException e) {
			sendImportantPacket(e.getAsPacket(), connectionID);
		}
	}

	public void handleBuyProperty(int connectionID) {
		GamePlayer player = model.getUserByID(connectionID).asPlayer();

		try {
			player.buyProperty();
		} catch (MonopolyException e) {
			sendImportantPacket(e.getAsPacket(), connectionID);
		}
	}

	public void handleBuildHouse(int connectionID) {
		GamePlayer player = model.getUserByID(connectionID).asPlayer();

		try {
			player.buildHouse();
		} catch (MonopolyException e) {
			sendImportantPacket(e.getAsPacket(), connectionID);
		}
	}

	public void handleBuildHotel(int connectionID) {
		GamePlayer player = model.getUserByID(connectionID).asPlayer();

		try {
			player.buildHotel();
		} catch (MonopolyException e) {
			sendImportantPacket(e.getAsPacket(), connectionID);
		}
	}

	public void handleInitiateAuction(int connectionID) {
		GamePlayer player = model.getUserByID(connectionID).asPlayer();

		try {
			player.initiateAuction();
		} catch (MonopolyException e) {
			sendImportantPacket(e.getAsPacket(), connectionID);
		}
	}

	public void handleIncreaseBid(int connectionID, ImportantNetworkPacket packet) {
		int bidAmount = ((IntegerPacketData) packet.getData().get(0)).getData();
		GamePlayer player = model.getUserByID(connectionID).asPlayer();

		try {
			player.bid(bidAmount);
		} catch (MonopolyException e) {
			sendImportantPacket(e.getAsPacket(), connectionID);
		}
	}

	public void handleSkipBid(int connectionID) {
		GamePlayer player = model.getUserByID(connectionID).asPlayer();

		try {
			player.skipBid();
		} catch (MonopolyException e) {
			sendImportantPacket(e.getAsPacket(), connectionID);
		}
	}

	public void handleInitiateTrade(int connectionID) {
		// TODO implement trading
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
		Lobby lobby = model.getLobbyOfUser(userJoined.getId());
		sendImportantPacket(new ImportantNetworkPacket(PacketType.JOIN_SUCCESS, lobby.getAsPacket()),
				userJoined.getId());
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
		Game game = model.getGameOfPlayer(player.getId());

		sendImportantPacket(
				new ImportantNetworkPacket(PacketType.PLAYER_TURN, game.getCurrentPlayer().getAsPlayerPacket()),
				player.getId());
	}

	public void sendDiceRollNotification(GamePlayer player, DicePacketData data) {
		sendImportantPacket(new ImportantNetworkPacket(PacketType.DICE_RESULT, data), player.getId());
	}

	public void sendTokenMovedNotification(GamePlayer playerMoved, GamePlayer playerToNotify) {
		sendImportantPacket(new ImportantNetworkPacket(PacketType.TOKEN_MOVED, playerMoved.getAsPlayerPacket(),
				playerMoved.getTile().getAsPacket()), playerToNotify.getId());
	}

	public void sendBuyOrAuctionNotification(GamePlayer player) {
		sendImportantPacket(new ImportantNetworkPacket(PacketType.ACT_BUY_OR_AUCTION, player.getTile().getAsPacket()),
				player.getId());
	}

	public void sendBalanceChangeNotification(GamePlayer playerWithNewBalance, GamePlayer playerToNotify) {
		sendImportantPacket(
				new ImportantNetworkPacket(PacketType.PLAYER_BALANCE, playerWithNewBalance.getAsPlayerPacket()),
				playerToNotify.getId());
	}

	public void sendPropertyBoughtNotification(PropertyTile tile, GamePlayer playerBuying, GamePlayer playerToNotify) {
		sendImportantPacket(new ImportantNetworkPacket(PacketType.PROPERTY_BOUGHT, tile.getAsPacket(),
				playerBuying.getAsPlayerPacket()), playerToNotify.getId());
	}

	public void sendAuctionInitiatedNotification(PropertyTile tile, GamePlayer playerToNotify) {
		sendImportantPacket(new ImportantNetworkPacket(PacketType.AUCTION_INITIATED, tile.getAsPacket()),
				playerToNotify.getId());
	}

	public void sendAuctionTurnNotification(GamePlayer playerTurn, GamePlayer playerToNotify) {
		sendImportantPacket(new ImportantNetworkPacket(PacketType.AUCTION_TURN, playerTurn.getAsPlayerPacket()),
				playerToNotify.getId());
	}

	public void sendUpdatedBidNotification(GamePlayer playerBid, GamePlayer playerToNotify, int bidAmount) {
		sendImportantPacket(new ImportantNetworkPacket(PacketType.BID_INCREASED, playerBid.getAsPlayerPacket(),
				new IntegerPacketData(bidAmount)), playerToNotify.getId());
	}

	public void sendSkipBidNotification(GamePlayer playerSkipped, GamePlayer playerToNotify) {
		sendImportantPacket(new ImportantNetworkPacket(PacketType.BID_SKIPPED, playerSkipped.getAsPlayerPacket()),
				playerToNotify.getId());
	}

	public void sendAuctionCompleteNotification(GamePlayer player) {
		sendImportantPacket(new ImportantNetworkPacket(PacketType.AUCTION_COMPLETE), player.getId());
	}

	public void sendCurrentBidNotification(GamePlayer player) {
		Auction auction = model.getGameOfPlayer(player.getId()).getAuction();

		sendImportantPacket(
				new ImportantNetworkPacket(PacketType.CURRENT_BID, new IntegerPacketData(auction.getCurrentBid())),
				player.getId());
	}

	public void sendHouseBuiltNotification(GamePlayer player) {
		sendImportantPacket(new ImportantNetworkPacket(PacketType.HOUSE_BUILT), player.getId());
	}

	public void sendHotelBuiltNotification(GamePlayer player) {
		sendImportantPacket(new ImportantNetworkPacket(PacketType.HOTEL_BUILT), player.getId());
	}

	public void sendTurnCompleteNotification(GamePlayer player) {
		sendImportantPacket(new ImportantNetworkPacket(PacketType.TURN_COMPLETE), player.getId());

	}

	public void sendPlayerBankruptNotification(GamePlayer playerBankrupt, GamePlayer playerToNotify) {
		sendImportantPacket(new ImportantNetworkPacket(PacketType.PLAYER_BANKRUPT, playerBankrupt.getAsPlayerPacket()),
				playerToNotify.getId());
	}

	@Override
	public void receivedImportantPacket(int connectionID, ImportantNetworkPacket packet) {
		logger.debug("From {} received {}", connectionID, packet);

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
			handleGetUsers(connectionID);

		} else if (packet.getType() == PacketType.LEAVE_LOBBY) {
			handleLeave(connectionID);

		} else if (packet.getType() == PacketType.SET_READY) {
			handleReady(connectionID, packet);

		} else if (packet.getType() == PacketType.GET_GAME_DATA) {
			handleGetGameData(connectionID);

		} else if (packet.getType() == PacketType.ROLL_DICE) {
			handleDiceRoll(connectionID);

		} else if (packet.getType() == PacketType.BUY_PROPERTY) {
			handleBuyProperty(connectionID);

		} else if (packet.getType() == PacketType.BUILD_HOUSE) {
			handleBuildHouse(connectionID);

		} else if (packet.getType() == PacketType.BUILD_HOTEL) {
			handleBuildHotel(connectionID);

		} else if (packet.getType() == PacketType.INITIATE_AUCTION) {
			handleInitiateAuction(connectionID);

		} else if (packet.getType() == PacketType.INCREASE_BID) {
			handleIncreaseBid(connectionID, packet);

		} else if (packet.getType() == PacketType.SKIP_BID) {
			handleSkipBid(connectionID);

		} else if (packet.getType() == PacketType.INITIATE_TRADE) {
			handleInitiateTrade(connectionID);
		}
	}
}
