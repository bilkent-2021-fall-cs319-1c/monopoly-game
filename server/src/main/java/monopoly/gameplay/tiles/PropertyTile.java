package monopoly.gameplay.tiles;

import lombok.Getter;
import monopoly.gameplay.Game;
import monopoly.gameplay.GamePlayer;
import monopoly.gameplay.properties.Property;
import monopoly.gameplay.properties.StreetProperty;
import monopoly.network.packet.important.packet_data.gameplay.property.StreetTitleDeedPacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.TilePacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.TileType;
import monopoly.network.packet.important.packet_data.gameplay.property.TitleDeedPacketData;

/**
 * Tile with property
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */
@Getter
public class PropertyTile extends Tile {
	private Property property;

	public PropertyTile(String name, String description, TileType type, int index, Property property) {
		super(name, description, type, index);

		this.property = property;
		property.setTile(this);
	}

	@Override
	public void doAction(GamePlayer player) {
		Game game = player.getGame();
		GamePlayer owner = property.getOwner();
		int balance = player.getBalance();
		int rent = property.getRentCost();

		if (!player.equals(owner)) {
			if (balance >= rent) {
				player.setBalance(balance - rent);
				owner.setBalance(owner.getBalance() + rent);
			} else {
				game.bankrupt(player);
			}
		}
	}

	@Override
	public TilePacketData getAsPacket() {
		if (getType() == TileType.STREET) {
			StreetProperty street = (StreetProperty) property;
			return new TilePacketData(
					new StreetTitleDeedPacketData(getName(), street.getBuyCost(), street.getMortgageCost(),
							street.getRentPrices(), street.getHouseCost(), street.getHotelCost(), street.getColorSet()),
					getName(), getDescription(), getType(), getIndex());
		}

		return new TilePacketData(new TitleDeedPacketData(getName(), property.getBuyCost(), property.getMortgageCost(),
				property.getRentPrices()), getName(), getDescription(), getType(), getIndex());
	}
}
