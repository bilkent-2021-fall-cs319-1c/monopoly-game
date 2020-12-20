package monopoly.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import monopoly.Error;
import monopoly.ErrorListener;
import monopoly.network.packet.important.ImportantNetworkPacket;
import monopoly.network.packet.important.PacketType;
import monopoly.network.packet.important.packet_data.BooleanPacketData;
import monopoly.network.packet.important.packet_data.IntegerPacketData;
import monopoly.network.packet.important.packet_data.UserListPacketData;
import monopoly.network.packet.important.packet_data.UserPacketData;
import monopoly.network.packet.important.packet_data.gameplay.BoardPacketData;
import monopoly.network.packet.important.packet_data.gameplay.DicePacketData;
import monopoly.network.packet.important.packet_data.gameplay.PlayerListPacketData;
import monopoly.network.packet.important.packet_data.gameplay.PlayerPacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.TilePacketData;
import monopoly.network.packet.important.packet_data.lobby.LobbyListPacketData;
import monopoly.network.packet.important.packet_data.lobby.LobbyPacketData;
import monopoly.network.packet.realtime.RealTimeNetworkPacket;
import monopoly.ui.ClientApplication;
import monopoly.ui.controller.gameplay.GameplayController;
import monopoly.ui.controller.gameplay.GameplayDataManager;
import monopoly.ui.controller.in_lobby.LobbyController;

/**
 * Manages network communication for monopoly client
 * 
 * @author Ziya Mukhtarov
 * @version Dec 13, 2020
 */
public class NetworkManager {
	private static Logger logger = LoggerFactory.getLogger(NetworkManager.class);

	@Getter
	private Client client;
	private ClientApplication app;
	@Getter
	private Integer selfConnectionId;

	private PacketType waitingForResponseType;
	private ImportantNetworkPacket responsePacket;

	private List<ErrorListener> errorListeners;

	/**
	 * Creates a new network manager for the given application. Initiates the
	 * connection with the server
	 * 
	 * @param app The client application
	 * @throws IOException if an I/O error occurs when connecting to the server
	 */
	public NetworkManager(ClientApplication app) throws IOException {
		this.app = app;

		selfConnectionId = null;
		waitingForResponseType = null;
		errorListeners = Collections.synchronizedList(new ArrayList<ErrorListener>());
		client = new MonopolyClient(ServerInfo.IP);
	}

	/**
	 * Adds a new error listener. Uniqueness is not checked, meaning that the
	 * listener would be notified the number of times it is registered
	 */
	public void addErrorListener(ErrorListener listener) {
		errorListeners.add(listener);
	}

	/**
	 * Removes the given listener. If the listener was not added, nothing happens.
	 * If multiple instances were added, only the first one is removed.
	 */
	public void removeErrorListener(ErrorListener listener) {
		errorListeners.remove(listener);
	}

	/**
	 * Queries the server for the number of open and not-in-game lobbies. This
	 * method blocks until the response is received, or an error packet is received,
	 * in which case the error listeners are notified. Note that, this method may
	 * return before all the listeners have been notified.
	 * 
	 * @return The number of lobbies, or -1 if an error packet is received.
	 */
	public int getNumberOfLobbies() {
		ImportantNetworkPacket request = new ImportantNetworkPacket(PacketType.GET_LOBBY_COUNT);
		ImportantNetworkPacket response = askAndGetResponse(request, PacketType.LOBBY_COUNT);

		if (response == null) {
			return -1;
		}
		return ((IntegerPacketData) response.getData().get(0)).getData();
	}

	/**
	 * Queries the server for the open and not-in-game lobbies in the given range.
	 * This method blocks until the response is received, or an error packet is
	 * received, in which case the error listeners are notified. Note that, this
	 * method may return before all the listeners have been notified.
	 * 
	 * @param from The starting index of the lobbies to request, inclusive
	 * @param to   The ending index of the lobbies to request, exclusive
	 * @return The list of requested lobbies, or null if an error packet is
	 *         received.
	 */
	public LobbyListPacketData getLobbies(int from, int to) {
		ImportantNetworkPacket request = new ImportantNetworkPacket(PacketType.GET_LOBBIES, new IntegerPacketData(from),
				new IntegerPacketData(to));
		ImportantNetworkPacket response = askAndGetResponse(request, PacketType.LOBBY_LIST);

		if (response == null) {
			return null;
		}
		return (LobbyListPacketData) response.getData().get(0);
	}

	/**
	 * Asks the server to allow this user join the lobby. This method blocks until
	 * the response is received, or an error packet is received, in which case the
	 * error listeners are notified. Note that, this method may return before all
	 * the listeners have been notified.
	 * 
	 * @param lobby The lobby to join
	 * @return true if the player could join the lobby, false if an error packet is
	 *         received
	 */
	public boolean joinLobby(LobbyPacketData lobby) {
		ImportantNetworkPacket request = new ImportantNetworkPacket(PacketType.JOIN_LOBBY, lobby);
		ImportantNetworkPacket response = askAndGetResponse(request, PacketType.JOIN_SUCCESS);

		return response != null;
	}

	/**
	 * Queries the server for the users in the lobby this player is in. This method
	 * blocks until the response is received, or an error packet is received, in
	 * which case the error listeners are notified. Note that, this method may
	 * return before all the listeners have been notified.
	 * 
	 * @return The list of requested users, or null if an error packet is received.
	 */
	public UserListPacketData getUsersInLobby() {
		ImportantNetworkPacket request = new ImportantNetworkPacket(PacketType.GET_USERS_IN_LOBBY);
		ImportantNetworkPacket response = askAndGetResponse(request, PacketType.USERS_IN_LOBBY);

		if (response == null) {
			return null;
		}
		return ((UserListPacketData) response.getData().get(0));
	}

	/**
	 * Asks the server to allow this user leave the lobby. This method blocks until
	 * the response is received, or an error packet is received, in which case the
	 * error listeners are notified. Note that, this method may return before all
	 * the listeners have been notified.
	 * 
	 * @return true if the player could leave the lobby, false if an error packet is
	 *         received
	 */
	public boolean leaveLobby() {
		ImportantNetworkPacket request = new ImportantNetworkPacket(PacketType.LEAVE_LOBBY);
		ImportantNetworkPacket response = askAndGetResponse(request, PacketType.LEAVE_SUCCESS);

		return response != null;
	}

	/**
	 * Asks the server to create a lobby with the given data. This method blocks
	 * until the response is received, or an error packet is received, in which case
	 * the error listeners are notified. Note that, this method may return before
	 * all the listeners have been notified.
	 * 
	 * @return true if the player could create the lobby, false if an error packet
	 *         is received
	 */
	public boolean createLobby(String lobbyName, boolean isPublic, String password, int playerLimit) {
		ImportantNetworkPacket request = new ImportantNetworkPacket(PacketType.CREATE_LOBBY,
				new LobbyPacketData(0, lobbyName, password, isPublic, "", 0, playerLimit));
		ImportantNetworkPacket response = askAndGetResponse(request, PacketType.JOIN_SUCCESS);

		return response != null;
	}

	/**
	 * Asks the server to change this user's readiness status. This method blocks
	 * until the response is received, or an error packet is received, in which case
	 * the error listeners are notified. Note that, this method may return before
	 * all the listeners have been notified.
	 * 
	 * @param ready User's readiness status
	 * @return true if the player could change the status, false if an error packet
	 *         is received
	 */
	public boolean setReady(boolean ready) {
		ImportantNetworkPacket request = new ImportantNetworkPacket(PacketType.SET_READY, new BooleanPacketData(ready));
		ImportantNetworkPacket response = askAndGetResponse(request, PacketType.SET_READY_SUCCESS);

		return response != null;
	}

	/**
	 * Queries the server for the current game's data. This method blocks until the
	 * response is received, or an error packet is received, in which case the error
	 * listeners are notified. Note that, this method may return before all the
	 * listeners have been notified.
	 * 
	 * @param gameplayController The gameplay controller which requests this game
	 *                           data
	 * @return The game data
	 */
	public GameplayDataManager getGameData(GameplayController gameplayController) {
		ImportantNetworkPacket request = new ImportantNetworkPacket(PacketType.GET_GAME_DATA);
		ImportantNetworkPacket response = askAndGetResponse(request, PacketType.GAME_DATA);

		if (response == null) {
			return null;
		}
		return new GameplayDataManager((PlayerListPacketData) response.getData().get(0),
				(BoardPacketData) response.getData().get(1), gameplayController);
	}

	/**
	 * Informs the server that this player wants to roll the dice
	 */
	public void askRollDice() {
		ImportantNetworkPacket request = new ImportantNetworkPacket(PacketType.ROLL_DICE);
		client.sendImportantPacket(request);
	}

	/**
	 * Informs the server that this player wants to buy current property
	 */
	public void buyProperty() {
		ImportantNetworkPacket request = new ImportantNetworkPacket(PacketType.BUY_PROPERTY);
		client.sendImportantPacket(request);
	}

	/**
	 * Informs the server that this player wants to auction current property
	 */
	public void auctionProperty() {
		ImportantNetworkPacket request = new ImportantNetworkPacket(PacketType.INITIATE_AUCTION);
		client.sendImportantPacket(request);
	}

	/**
	 * Informs the server that this player wants to pass this auction turn
	 */
	public void passOnAuction() {
		ImportantNetworkPacket request = new ImportantNetworkPacket(PacketType.SKIP_BID);
		client.sendImportantPacket(request);
	}

	/**
	 * Increases the bid by the given amount
	 */
	public void bidOnAuction(Integer value) {
		ImportantNetworkPacket request = new ImportantNetworkPacket(PacketType.INCREASE_BID,
				new IntegerPacketData(value));
		client.sendImportantPacket(request);
	}

	/**
	 * Only one of this method should run at once during the runtime, hence the
	 * synchronized keyword. It blocks until the desired network packet, or an error
	 * packet is received.
	 * 
	 * @param packet       The packet to use as a prompt
	 * @param responseType The response type to wait for
	 * @return The packet that was received as a response in the desired type, or
	 *         null if an error packet is received
	 */
	private synchronized ImportantNetworkPacket askAndGetResponse(ImportantNetworkPacket packet,
			PacketType responseType) {
		waitingForResponseType = responseType;
		responsePacket = null;
		client.sendImportantPacket(packet);
		logger.debug("Sending {} Waiting for {}", packet.getType(), responseType);

		while (responsePacket == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}
		logger.debug("Response: {}", responsePacket.getType());

		if (responsePacket.isErrorPacket()) {
			return null;
		} else {
			return responsePacket;
		}
	}

	/**
	 * Handles low-level monopoly communication
	 * 
	 * @author Ziya Mukhtarov
	 * @version Dec 13, 2020
	 */
	private class MonopolyClient extends Client {

		/**
		 * Connects to the server
		 * 
		 * @param serverAddress if an I/O error occurs when connecting
		 * @throws IOException if an I/O error occurs when connecting
		 */
		public MonopolyClient(String serverAddress) throws IOException {
			super(serverAddress);
		}

		@Override
		public void connected(int connectionID) {
			selfConnectionId = connectionID;
			new Thread(() -> askAndGetResponse(new ImportantNetworkPacket(PacketType.CONNECT), PacketType.ACCEPTED))
					.start();
		}

		@Override
		public void disconnected(int connectionID) {
			notifyAllErrorListeners(new ImportantNetworkPacket(PacketType.ERR_DISCONNECTED));
		}

		@Override
		public void receivedRealTimePacket(int connectionID, RealTimeNetworkPacket packet) {
			Object uiController = app.getMainController();
			if (uiController instanceof GameplayController) {
				GameplayController gameplayController = (GameplayController) uiController;
				gameplayController.realTimePacketReceived(packet);
			} else {
				logger.error("Wrong controller. Expected GameplayController, displaying {}", uiController);
			}
		}

		@Override
		public void receivedImportantPacket(int connectionID, ImportantNetworkPacket packet) {
			PacketType type = packet.getType();
			logger.debug("Received: {}", packet);
			System.out.println(packet);

			if (packet.isErrorPacket()) {
				new Thread(() -> notifyAllErrorListeners(responsePacket)).start();
			}

			if ((type == waitingForResponseType && responsePacket == null) || packet.isErrorPacket()) {
				responsePacket = packet;
			} else {
				if (type == PacketType.PLAYER_JOIN) {
					handleUserJoin((UserPacketData) packet.getData().get(0));

				} else if (type == PacketType.PLAYER_LEFT) {
					handleUserLeft((UserPacketData) packet.getData().get(0));

				} else if (type == PacketType.PLAYER_READY) {
					handleUserReadyChange((UserPacketData) packet.getData().get(0));

				} else if (type == PacketType.GAME_START) {
					handleGameStart();

				} else if (type == PacketType.PLAYER_TURN) {
					handlePlayerTurn((PlayerPacketData) packet.getData().get(0));

				} else if (type == PacketType.DICE_RESULT) {
					handleDiceRoll((DicePacketData) packet.getData().get(0));

				} else if (type == PacketType.TOKEN_MOVED) {
					handleTokenMove((PlayerPacketData) packet.getData().get(0),
							(TilePacketData) packet.getData().get(1));

				} else if (type == PacketType.ACT_BUY_OR_AUCTION) {
					handleBuyOrAuctionOption((TilePacketData) packet.getData().get(0));

				} else if (type == PacketType.PROPERTY_BOUGHT) {
					handlePropertyBought((TilePacketData) packet.getData().get(0),
							(PlayerPacketData) packet.getData().get(1));

				} else if (type == PacketType.AUCTION_INITIATED) {
					handleAuctionStart((TilePacketData) packet.getData().get(0));

				} else if (type == PacketType.AUCTION_TURN) {
					handleAuctionTurnChange((PlayerPacketData) packet.getData().get(0));

				} else if (type == PacketType.BID_INCREASED) {
					handleAuctionBidIncrease((PlayerPacketData) packet.getData().get(0),
							(IntegerPacketData) packet.getData().get(1));

				} else if (type == PacketType.BID_SKIPPED) {
					handleAuctionBidSkip((PlayerPacketData) packet.getData().get(0));

				} else if (type == PacketType.AUCTION_COMPLETE) {
					handleAuctionComplete();

				} else if (type == PacketType.PLAYER_BALANCE) {
					handleBalanceChange((PlayerPacketData) packet.getData().get(0));
				}
			}
		}

		/**
		 * Notifies all error listeners about the error.
		 * 
		 * @param errorPacket The packet containing error details
		 */
		private void notifyAllErrorListeners(ImportantNetworkPacket errorPacket) {
			errorListeners.parallelStream().forEach(l -> l.errorOccured(new Error(errorPacket.getType())));
		}

		/**
		 * Handles a new user joining the lobby this client is in
		 */
		private void handleUserJoin(UserPacketData user) {
			Object uiController = app.getMainController();
			if (uiController instanceof LobbyController) {
				LobbyController lobbyController = (LobbyController) uiController;
				lobbyController.userJoined(user);
			} else {
				logger.warn("Wrong controller. Expected LobbyController, displaying {}", uiController);
			}
		}

		/**
		 * Handles a user leaving the lobby this client is in
		 */
		private void handleUserLeft(UserPacketData user) {
			// TODO
			logger.error("{} left, but it is not implemented", user);
		}

		/**
		 * Handles a user changing his/her readiness status for starting the game
		 */
		private void handleUserReadyChange(UserPacketData user) {
			Object uiController = app.getMainController();
			if (uiController instanceof LobbyController) {
				LobbyController lobbyController = (LobbyController) uiController;
				lobbyController.userReadyChange(user);
			} else {
				logger.error("Wrong controller. Expected LobbyController, displaying {}", uiController);
			}
		}

		/**
		 * Handles the game start for the lobby this client is in
		 */
		private void handleGameStart() {
			Object uiController = app.getMainController();
			if (uiController instanceof LobbyController) {
				LobbyController lobbyController = (LobbyController) uiController;
				lobbyController.gameStart();
			} else {
				logger.error("Wrong controller. Expected LobbyController, displaying {}", uiController);
			}
		}

		/**
		 * Handles a player's turn change
		 */
		private void handlePlayerTurn(PlayerPacketData playerPacketData) {
			Object uiController = app.getMainController();
			if (uiController instanceof GameplayController) {
				GameplayController gameplayController = (GameplayController) uiController;
				gameplayController.getGameData().changePlayerTurn(playerPacketData);
			} else {
				logger.error("Wrong controller. Expected GameplayController, displaying {}", uiController);
			}
		}

		/**
		 * Handles a rolled dice result
		 */
		private void handleDiceRoll(DicePacketData dicePacketData) {
			Object uiController = app.getMainController();
			if (uiController instanceof GameplayController) {
				GameplayController gameplayController = (GameplayController) uiController;
				gameplayController.getGameData().rollDiceAndshowResult(dicePacketData);
			} else {
				logger.error("Wrong controller. Expected GameplayController, displaying {}", uiController);
			}
		}

		/**
		 * Handles a player's token moving
		 */
		private void handleTokenMove(PlayerPacketData playerPacketData, TilePacketData tilePacketData) {
			Object uiController = app.getMainController();
			if (uiController instanceof GameplayController) {
				GameplayController gameplayController = (GameplayController) uiController;
				gameplayController.getGameData().moveToken(playerPacketData, tilePacketData);
			} else {
				logger.error("Wrong controller. Expected GameplayController, displaying {}", uiController);
			}
		}

		/**
		 * Handles the option of buying or auctioning the property that is not owned by
		 * anyone
		 */
		private void handleBuyOrAuctionOption(TilePacketData tilePacketData) {
			Object uiController = app.getMainController();
			if (uiController instanceof GameplayController) {
				GameplayController gameplayController = (GameplayController) uiController;
				gameplayController.getGameData().displayBuyOrAuctionPane(tilePacketData);
			} else {
				logger.error("Wrong controller. Expected GameplayController, displaying {}", uiController);
			}
		}

		/**
		 * Handles a property being bought by the given player
		 */
		private void handlePropertyBought(TilePacketData tilePacketData, PlayerPacketData playerPacketData) {
			Object uiController = app.getMainController();
			if (uiController instanceof GameplayController) {
				GameplayController gameplayController = (GameplayController) uiController;
				gameplayController.getGameData().propertyBought(tilePacketData, playerPacketData);
			} else {
				logger.error("Wrong controller. Expected GameplayController, displaying {}", uiController);
			}
		}

		/**
		 * Handles a property being bought by the given player
		 */
		private void handleBalanceChange(PlayerPacketData playerPacketData) {
			Object uiController = app.getMainController();
			if (uiController instanceof GameplayController) {
				GameplayController gameplayController = (GameplayController) uiController;
				gameplayController.getGameData().balanceChange(playerPacketData);
			} else {
				logger.error("Wrong controller. Expected GameplayController, displaying {}", uiController);
			}
		}

		/**
		 * Handles auction starting
		 */
		private void handleAuctionStart(TilePacketData tilePacketData) {
			Object uiController = app.getMainController();
			if (uiController instanceof GameplayController) {
				GameplayController gameplayController = (GameplayController) uiController;
				gameplayController.getGameData().auctionStarted(tilePacketData);
			} else {
				logger.error("Wrong controller. Expected GameplayController, displaying {}", uiController);
			}
		}

		/**
		 * Handles the turn change during auction
		 */
		private void handleAuctionTurnChange(PlayerPacketData playerPacketData) {
			Object uiController = app.getMainController();
			if (uiController instanceof GameplayController) {
				GameplayController gameplayController = (GameplayController) uiController;
				gameplayController.getGameData().auctionTurnChange(playerPacketData);
			} else {
				logger.error("Wrong controller. Expected GameplayController, displaying {}", uiController);
			}
		}

		/**
		 * Handles auction bid change
		 */
		private void handleAuctionBidIncrease(PlayerPacketData playerPacketData, IntegerPacketData integerPacketData) {
			Object uiController = app.getMainController();
			if (uiController instanceof GameplayController) {
				GameplayController gameplayController = (GameplayController) uiController;
				gameplayController.getGameData().auctionBidChange(playerPacketData, integerPacketData.getData());
			} else {
				logger.error("Wrong controller. Expected GameplayController, displaying {}", uiController);
			}
		}

		/**
		 * Handles someone skipping the bid
		 */
		private void handleAuctionBidSkip(PlayerPacketData playerPacketData) {
			Object uiController = app.getMainController();
			if (uiController instanceof GameplayController) {
				GameplayController gameplayController = (GameplayController) uiController;
				gameplayController.getGameData().auctionSkipped(playerPacketData);
			} else {
				logger.error("Wrong controller. Expected GameplayController, displaying {}", uiController);
			}
		}

		/**
		 * Handles the end of an auction
		 */
		private void handleAuctionComplete() {
			Object uiController = app.getMainController();
			if (uiController instanceof GameplayController) {
				GameplayController gameplayController = (GameplayController) uiController;
				gameplayController.getGameData().auctionEnded();
			} else {
				logger.error("Wrong controller. Expected GameplayController, displaying {}", uiController);
			}
		}

	}
}
