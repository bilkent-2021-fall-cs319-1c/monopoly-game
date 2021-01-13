package monopoly.gameplay.tiles;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import monopoly.gameplay.properties.TitleDeedData;
import monopoly.network.packet.important.packet_data.gameplay.property.TilePacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.TileType;

/**
 * Tile parent class
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Tile implements Actionable {
	private TitleDeedData titleDeed;
	private String name;
	private String description;
	private TileType type;
	private int index;

	public TilePacketData getAsPacket() {
		return new TilePacketData(titleDeed.getAsPacket(), name, description, type, index);
	}
}
