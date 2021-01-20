package monopoly.gameplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import monopoly.MonopolyException;
import monopoly.gameplay.properties.Property;
import monopoly.gameplay.tiles.Tile;
import monopoly.lobby.User;
import monopoly.network.packet.important.packet_data.gameplay.PlayerPacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.PropertyPacketData;

/**
 * A game player
 * 
 * @author Javid Baghirov, Ziya Mukhtarov
 * @version Jan 17, 2021
 */
@Getter
public class GamePlayer extends User {
	private static final int START_BALANCE = 1500;

	private Game game;

	private int balance;
	private Tile tile;
	@Setter
	private boolean inJail;
	private List<Property> properties;
	private boolean micOpen;
	private boolean camOpen;

	public GamePlayer(User user, Game game) {
		super(user);
		user.setGamePlayerInstance(this);
		setGamePlayerInstance(this);

		this.game = game;
		tile = game.getBoard().getStartTile();
		inJail = false;

		balance = START_BALANCE;
		properties = Collections.synchronizedList(new ArrayList<>());

		micOpen = false;
		camOpen = false;
	}

	public void rollDice() throws MonopolyException {
		game.rollDice(this);
	}

	public void setTile(Tile tile) {
		this.tile = tile;
		inJail = false;

		game.sendTokenMoveToPlayers(this);
	}

	public void buyProperty() throws MonopolyException {
		game.buyProperty(this);
	}

	public void buildHouse() throws MonopolyException {
		game.buildHouse(this);
	}

	public void buildHotel() throws MonopolyException {
		game.buildHotel(this);
	}

	public void initiateAuction() throws MonopolyException {
		game.initiateAuction(this);
	}

	public void bid(int bidAmount) throws MonopolyException {
		Auction auction = game.getAuction();
		if (auction == null) {
			throw new MonopolyException();
		}

		auction.bid(bidAmount, this);
	}

	public void skipBid() throws MonopolyException {
		Auction auction = game.getAuction();
		if (auction == null) {
			throw new MonopolyException();
		}

		auction.skip(this);
	}

	public void initiateTrade(GamePlayer player2) throws MonopolyException {
		game.initiateTrade(this, player2);
	}

	public void addToTrade(Tradeable item) throws MonopolyException {
		getCurrentTrade().addItem(this, item);
	}

	public void removeFromTrade(Tradeable item) throws MonopolyException {
		getCurrentTrade().removeItem(this, item);
	}

	public void agreeToTrade() throws MonopolyException {
		getCurrentTrade().agree(this);
	}

	public void rejectTrade() throws MonopolyException {
		getCurrentTrade().reject(this);
	}

	private Trade getCurrentTrade() throws MonopolyException {
		Trade trade = game.getTrade();
		if (trade == null) {
			throw new MonopolyException();
		}
		return trade;
	}

	public void setBalance(int balance) {
		this.balance = balance;
		game.sendBalanceChangeToPlayers(this);
	}

	public void changeBalance(int amount) {
		setBalance(balance + amount);
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public PlayerPacketData getAsPlayerPacket() {
		return new PlayerPacketData(getAsPacket(), balance, tile.getAsPacket(), getPropertiesAsPacket(), micOpen,
				camOpen);
	}

	public List<PropertyPacketData> getPropertiesAsPacket() {
		return properties.parallelStream().map(Property::getAsPacket).collect(Collectors.toList());
	}
}
