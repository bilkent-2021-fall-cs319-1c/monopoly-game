package monopoly.gameplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import monopoly.MonopolyException;
import monopoly.gameplay.properties.Property;
import monopoly.gameplay.properties.StreetProperty;
import monopoly.gameplay.tiles.PropertyTile;
import monopoly.gameplay.tiles.Tile;
import monopoly.lobby.User;
import monopoly.network.packet.important.PacketType;
import monopoly.network.packet.important.packet_data.gameplay.PlayerPacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.PropertyPacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.TileType;

/**
 * A game player
 * 
 * @author Javid Baghirov
 * @version Dec 13, 2020
 */
@Getter
public class GamePlayer extends User {
	private static final int START_BALANCE = 1500;

	private Game game;

	private int balance;
	private Tile tile;
	private boolean inJail;
	private List<Property> properties;
	private boolean rolledDouble;
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

	// TODO Should be moved to Board or Game?
	public void goToJail() {
		if (!isInJail()) {
			tile = game.getBoard().getJailTile();
			inJail = true;
		}
	}

	public void setTile(Tile tile) {
		this.tile = tile;
		inJail = false;
	}

	public void buyProperty() throws MonopolyException {
		game.buyProperty(this);
	}

	public int getPropertyCountWithColor(String colorSet) {
		int[] counter = { 0 };

		if (properties != null) {
			properties.parallelStream().forEach(property -> {
				if (property.getColorSet().equals(colorSet)) {
					counter[0]++;
				}
			});
		}

		return counter[0];
	}

	public void buildHouse() throws MonopolyException {
		if (tile.getType() != TileType.STREET) {
			throw new MonopolyException();
		}

		PropertyTile propertyTile = (PropertyTile) tile;
		StreetProperty streetProperty = (StreetProperty) propertyTile.getProperty();
		int cost = streetProperty.getHouseCost();
		if (cost > balance) {
			throw new MonopolyException(PacketType.ERR_NOT_ENOUGH_BALANCE);
		}

		streetProperty.buildHouse();
		setBalance(balance - cost);
	}

	public void buildHotel() throws MonopolyException {
		if (tile.getType() != TileType.STREET) {
			throw new MonopolyException();
		}

		PropertyTile propertyTile = (PropertyTile) tile;
		StreetProperty streetProperty = (StreetProperty) propertyTile.getProperty();
		int cost = streetProperty.getHotelCost();
		if (cost > balance) {
			throw new MonopolyException(PacketType.ERR_NOT_ENOUGH_BALANCE);
		}

		streetProperty.buildHotel();
		setBalance(balance - cost);
	}

	public void bid(int bidAmount) throws MonopolyException {
		game.getAuction().bid(bidAmount, this);
	}

	public void skipBid() throws MonopolyException {
		game.getAuction().skip(this);
	}

	public void setBalance(int balance) {
		this.balance = balance;
		game.sendBalanceChangeToPlayers(this);
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
