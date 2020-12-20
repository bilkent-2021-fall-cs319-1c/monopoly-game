package monopoly.gameplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import monopoly.MonopolyException;
import monopoly.gameplay.properties.Property;
import monopoly.gameplay.properties.Street;
import monopoly.gameplay.properties.StreetTitleDeedData;
import monopoly.gameplay.tiles.PropertyTile;
import monopoly.gameplay.tiles.Tile;
import monopoly.lobby.User;
import monopoly.network.GameServer;
import monopoly.network.packet.important.PacketType;
import monopoly.network.packet.important.packet_data.gameplay.PlayerPacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.PropertyPacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.TilePacketData;

/**
 * A game player
 * 
 * @author Javid Baghirov
 * @version Dec 13, 2020
 */

@Getter
@Setter
public class GamePlayer extends User {
	private static final int START_BALANCE = 1500;

	private int balance;
	private int tileIndex;
	private Tile tile;
	private List<Property> properties;
	private boolean inJail;
	private boolean rolledDouble;
	private boolean micOpen;
	private boolean camOpen;

	public GamePlayer(User user) {
		super(user);
		user.setGamePlayerInstance(this);
		setGamePlayerInstance(this);

		balance = START_BALANCE;
		tileIndex = 0;
		tile = null;
		properties = Collections.synchronizedList(new ArrayList<>());
		inJail = false;
		micOpen = false;
		camOpen = false;
	}

	public void rollDice() throws MonopolyException {
		getCurrentGame().rollDice(this);
	}

	public TilePacketData move() {
		try {
			getCurrentGame().move(this);
		} catch (MonopolyException e) {
			GameServer.getInstance().sendImportantPacket(e.getAsPacket(), getId());
		}

		return tile.getAsTilePacket();
	}

	public void updateTile() {
		Board board = getCurrentGame().getBoard();

		tile = board.getTiles().get(tileIndex);
	}

	public void goToJail() {
		if (!inJail) {
			Board board = getCurrentGame().getBoard();
			tile = board.getTiles().get(board.getJAIL_POSITION());
			tileIndex = tile.getIndex();
			inJail = true;
		}
	}

	public boolean buyProperty() throws MonopolyException {
		if (tile instanceof PropertyTile) {
			if (properties.contains(((PropertyTile) tile).getProperty())) {
				throw new MonopolyException(PacketType.ERR_UNKNOWN);

			} else if (((PropertyTile) tile).getProperty().getTitleDeed().getBuyCost() > balance) {
				throw new MonopolyException(PacketType.ERR_NOT_ENOUGH_BALANCE);
			}

			((PropertyTile) tile).getProperty().setOwner(this);
			balance -= ((PropertyTile) tile).getProperty().getTitleDeed().getBuyCost();
			getCurrentGame().sendBalanceChangeToPlayers(this);

			return true;
		}

		return false;
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

	public boolean buildHouse() throws MonopolyException {
		boolean success = false;

		if (tile instanceof PropertyTile && ((PropertyTile) tile).getProperty() instanceof Street) {
			if ((((StreetTitleDeedData) (((PropertyTile) tile).getProperty().getTitleDeed()))
					.getHouseCost() > balance)) {
				throw new MonopolyException(PacketType.ERR_NOT_ENOUGH_BALANCE);
			}
			success = ((Street) (((PropertyTile) tile).getProperty())).buildHouse();
		}

		return success;
	}

	public boolean buildHotel() throws MonopolyException {
		boolean flag = false;

		if (tile instanceof PropertyTile && ((PropertyTile) tile).getProperty() instanceof Street
				&& ((StreetTitleDeedData) (((PropertyTile) tile).getProperty().getTitleDeed()))
						.getHotelCost() <= balance) {
			flag = ((Street) (((PropertyTile) tile).getProperty())).buildHotel();
		}

		return flag;
	}

	public void bid(int bidAmount) throws MonopolyException {
		getCurrentGame().getAuction().bid(bidAmount, this);
	}

	public void skipBid() throws MonopolyException {
		getCurrentGame().getAuction().skip(this);
	}

	public Game getCurrentGame() {
		return getLobby().getGame();
	}

	public List<PropertyPacketData> getPropertiesAsPacket() {
		List<PropertyPacketData> propertiesData = new ArrayList<>();

		if (!properties.isEmpty()) {
			properties.forEach(property -> propertiesData.add(property.getAsPropertyPacket()));
		}

		return propertiesData;
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
		return new PlayerPacketData(getAsPacket(), balance, tile.getAsTilePacket(), getPropertiesAsPacket(), micOpen,
				camOpen);
	}
}
