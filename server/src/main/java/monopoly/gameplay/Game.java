package monopoly.gameplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import monopoly.MonopolyException;
import monopoly.gameplay.properties.Auction;
import monopoly.gameplay.properties.Property;
import monopoly.gameplay.properties.StreetProperty;
import monopoly.gameplay.properties.Trade;
import monopoly.gameplay.tiles.PropertyTile;
import monopoly.gameplay.tiles.Tile;
import monopoly.lobby.User;
import monopoly.network.GameServer;
import monopoly.network.packet.important.PacketType;
import monopoly.network.packet.important.packet_data.gameplay.PlayerListPacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.TileType;

/**
 * The main game class with all the gameplay functionalities
 * 
 * @author Javid Baghirov
 * @version Dec 12, 2020
 */
public class Game {
	private Dice dice;
	@Getter
	private Board board;

	@Getter
	private List<GamePlayer> playersByTurn;
	private int turnNumber;

	@Getter
	private Auction auction;

	@Getter
	private Trade trade;

	/**
	 * Creates a game object
	 * 
	 * @param players the players that will play in this game
	 */
	public Game(List<User> players) {
		turnNumber = 0;
		dice = new Dice();
		board = new Board();
		auction = null;
		trade = null;

		initializeGamePlayers(players);
	}

	private void initializeGamePlayers(List<User> players) {
		playersByTurn = new ArrayList<>();

		for (int i = 0; i < players.size(); i++) {
			playersByTurn.add(new GamePlayer(players.get(i), this));
		}

		// Randomize the turns
		Collections.shuffle(playersByTurn);
	}

	public GamePlayer nextTurn() {
		turnNumber = (turnNumber + 1) % playersByTurn.size();

		return getCurrentPlayer();
	}

	public GamePlayer getCurrentPlayer() {
		return playersByTurn.get(turnNumber);
	}

	private boolean isPlayerTurn(GamePlayer player) {
		return getCurrentPlayer().equals(player);
	}

	public void rollDice(GamePlayer player) throws MonopolyException {
		if (!isPlayerTurn(player)) {
			throw new MonopolyException(PacketType.ERR_NOT_PLAYER_TURN);
		}

		dice.roll();
		dice.sendDiceResultToPlayers(playersByTurn);

		// TODO dice can be rolled for getting out of jail, or other situations.
		moveCurrentPlayerToken(dice.getResult());
	}

	/**
	 * Moves the token of current player
	 * 
	 * @param steps The number of steps to move the token
	 */
	private void moveCurrentPlayerToken(int steps) {
		board.move(getCurrentPlayer(), steps);

		// Notify everyone about the token move
		sendTokenMoveToPlayers(getCurrentPlayer());

		decideOnAction(getCurrentPlayer());
	}

	private void decideOnAction(GamePlayer player) {
		Tile tile = player.getTile();

		if (tile instanceof PropertyTile) {
			PropertyTile propertyTile = (PropertyTile) player.getTile();

			if (propertyTile.getProperty().getOwner() == null) {
				GameServer.getInstance().sendBuyOrAuctionNotification(player);
			} else {
				tile.doAction(player);
				completeTurn();
			}
		} else {
			// TODO maybe tile.doAction() is enough?
			int balance = player.getBalance();
			int amount;

			if (tile.getType() == TileType.GO_TO_JAIL || tile.getType() == TileType.TAX) {
				amount = 200;
			} else
				amount = -200;

			if (balance >= amount) {
				player.setBalance(balance - amount);
			} else {
				bankrupt(player);
			}
			completeTurn();
		}
	}

	public void buyProperty(GamePlayer player) throws MonopolyException {
		if (!isPlayerTurn(player)) {
			throw new MonopolyException(PacketType.ERR_NOT_PLAYER_TURN);
		}

		Tile tile = player.getTile();
		if (!(tile instanceof PropertyTile)) {
			throw new MonopolyException();
		}

		PropertyTile propertyTile = (PropertyTile) tile;
		int buyCost = propertyTile.getProperty().getBuyCost();

		if (propertyTile.getProperty().isOwned()) {
			throw new MonopolyException();
		}
		if (buyCost > player.getBalance()) {
			throw new MonopolyException(PacketType.ERR_NOT_ENOUGH_BALANCE);
		}

		propertyTile.getProperty().setOwner(player);
		player.setBalance(player.getBalance() - buyCost);

		sendPropertyBoughtToPlayers((PropertyTile) player.getTile(), player);
		completeTurn();
	}

	public void buildHouse(GamePlayer player) throws MonopolyException {
		if (!isPlayerTurn(player)) {
			throw new MonopolyException(PacketType.ERR_NOT_PLAYER_TURN);
		}
		if (player.getTile().getType() != TileType.STREET) {
			throw new MonopolyException();
		}

		PropertyTile propertyTile = (PropertyTile) player.getTile();
		StreetProperty streetProperty = (StreetProperty) propertyTile.getProperty();
		int cost = streetProperty.getHouseCost();
		if (cost > player.getBalance()) {
			throw new MonopolyException(PacketType.ERR_NOT_ENOUGH_BALANCE);
		}

		streetProperty.buildHouse();
		player.setBalance(player.getBalance() - cost);
		sendHouseBuiltToPlayers();
		completeTurn();
	}

	public void buildHotel(GamePlayer player) throws MonopolyException {
		if (!isPlayerTurn(player)) {
			throw new MonopolyException(PacketType.ERR_NOT_PLAYER_TURN);
		}
		if (player.getTile().getType() != TileType.STREET) {
			throw new MonopolyException();
		}

		PropertyTile propertyTile = (PropertyTile) player.getTile();
		StreetProperty streetProperty = (StreetProperty) propertyTile.getProperty();
		int cost = streetProperty.getHotelCost();
		if (cost > player.getBalance()) {
			throw new MonopolyException(PacketType.ERR_NOT_ENOUGH_BALANCE);
		}

		streetProperty.buildHotel();
		player.setBalance(player.getBalance() - cost);
		sendHotelBuiltToPlayers();
		completeTurn();
	}

	public void initiateAuction(GamePlayer player) throws MonopolyException {
		Tile tile = player.getTile();
		if (!(tile instanceof PropertyTile)) {
			throw new MonopolyException();
		}

		PropertyTile propertyTile = (PropertyTile) tile;
		initiateAuction(propertyTile.getProperty());
	}

	private void initiateAuction(Property item) throws MonopolyException {
		if (auction != null) {
			throw new MonopolyException();
		}
		auction = new Auction(this, item);
	}

	public void finishAuction() throws MonopolyException {
		if (auction == null) {
			throw new MonopolyException();
		}
		auction = null;
		completeTurn();
	}

	public void trade(GamePlayer playerFrom, GamePlayer playerTo) {
		// TODO implement trading
	}

	public void completeTurn() {
		// Notify everyone about turn complete status
		sendTurnCompleteToPlayers();

		nextTurn();

		// Notify everyone about who is playing next
		sendPlayerTurnToPlayers();
	}

	public void bankrupt(GamePlayer player) {
		sendPlayerBankruptToPlayers(player);
	}

	public void sendPlayerTurnToPlayers() {
		new Thread(() -> playersByTurn.parallelStream()
				.forEach(player -> GameServer.getInstance().sendPlayerTurnNotification(player))).start();
	}

	public void sendTokenMoveToPlayers(GamePlayer playerMoved) {
		playersByTurn.parallelStream()
				.forEach(player -> GameServer.getInstance().sendTokenMovedNotification(playerMoved, player));
	}

	public void sendBalanceChangeToPlayers(GamePlayer playerWithNewBalance) {
		playersByTurn.parallelStream()
				.forEach(p -> GameServer.getInstance().sendBalanceChangeNotification(playerWithNewBalance, p));
	}

	public void sendPropertyBoughtToPlayers(PropertyTile tile, GamePlayer playerBuying) {
		playersByTurn.parallelStream()
				.forEach(player -> GameServer.getInstance().sendPropertyBoughtNotification(tile, playerBuying, player));
	}

	public void sendHouseBuiltToPlayers() {
		playersByTurn.parallelStream().forEach(player -> GameServer.getInstance().sendHouseBuiltNotification(player));
	}

	public void sendHotelBuiltToPlayers() {
		playersByTurn.parallelStream().forEach(player -> GameServer.getInstance().sendHotelBuiltNotification(player));
	}

	public void sendTurnCompleteToPlayers() {
		playersByTurn.parallelStream().forEach(player -> GameServer.getInstance().sendTurnCompleteNotification(player));
	}

	public void sendPlayerBankruptToPlayers(GamePlayer playerBankrupt) {
		playersByTurn.parallelStream()
				.forEach(player -> GameServer.getInstance().sendPlayerBankruptNotification(playerBankrupt, player));
	}

	public PlayerListPacketData getPlayersAsPacket() {
		PlayerListPacketData playerList = new PlayerListPacketData();
		playersByTurn.forEach(player -> playerList.add(player.getAsPlayerPacket()));
		return playerList;
	}
}
