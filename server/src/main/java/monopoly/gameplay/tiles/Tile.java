package monopoly.gameplay.tiles;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import monopoly.gameplay.Actionable;
import monopoly.network.packet.important.packet_data.gameplay.property.TilePacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.TileType;
import monopoly.network.packet.important.packet_data.gameplay.property.TitleDeedPacketData;

/**
 * Tile parent class
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Tile implements Actionable {
	private final String name;
	private final String description;
	private final TileType type;
	private final int index;

	public TilePacketData getAsPacket() {
		return new TilePacketData(new TitleDeedPacketData(name, 0, 0, null), name, description, type, index);
	}
}
