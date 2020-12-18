package monopoly.gameplay.tiles;

import lombok.Getter;
import lombok.Setter;
import monopoly.MonopolyException;
import monopoly.gameplay.GamePlayer;
import monopoly.gameplay.Property;
import monopoly.gameplay.TitleDeedData;
import monopoly.network.packet.important.packet_data.gameplay.property.TilePacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.TileType;

/**
 * Tile with property
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */
@Getter
@Setter
public class PropertyTile extends Tile {

	private Property property;

	public PropertyTile(TitleDeedData titleDeed, String name, String description, TileType type, int index,
			Property property) {
		super(titleDeed, name, description, type, index);
		this.property = property;
	}

	@Override
	public void doAction(GamePlayer player) {

		if (!player.equals(property.getOwner())) {
			try {
				player.setBalance(player.getBalance() - property.getRentCost());
			} catch (MonopolyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public TilePacketData getAsTilePacket() {
		return new TilePacketData(getTitleDeed().getAsTitleDeedPacket(), getName(), getDescription(), getType(),
				getIndex());
	}
}
