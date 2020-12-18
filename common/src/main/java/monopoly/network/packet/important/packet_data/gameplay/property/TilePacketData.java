package monopoly.network.packet.important.packet_data.gameplay.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import monopoly.network.packet.important.packet_data.PacketData;

/**
 * Tile data holder that implements the generic packet data
 * 
 * @author Javid Baghirov
 * @version Dec 18, 2020
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TilePacketData implements PacketData {
	private static final long serialVersionUID = -580359519872171665L;

	/**
	 * The title deed of the given tile
	 */
	private TitleDeedPacketData titleDeed;
	private String title;
	private String description;
	/**
	 * The type of the tile which is one of the 10 tile types
	 */
	private TileType type;
	/**
	 * The position of the tile in the board: 0 - Shows GO tile, moving on in
	 * clockwise direction, 39 - shows the last tile before/above GO tile
	 */
	private int index;
}
