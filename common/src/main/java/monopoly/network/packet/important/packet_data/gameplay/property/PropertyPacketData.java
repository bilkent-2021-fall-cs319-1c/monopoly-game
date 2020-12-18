package monopoly.network.packet.important.packet_data.gameplay.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import monopoly.network.packet.important.packet_data.PacketData;
import monopoly.network.packet.important.packet_data.gameplay.PlayerPacketData;

/**
 * Property data holder that implements the generic packet data
 * 
 * @author Javid Baghirov
 * @version Dec 18, 2020
 */

@Data
@AllArgsConstructor
public class PropertyPacketData implements PacketData{
	private static final long serialVersionUID = 27242078625602573L;
	
	/**
	 * The tile that has the property
	 */
	private TilePacketData tileData;
	private PlayerPacketData owner;
	private boolean mortgaged;
}
