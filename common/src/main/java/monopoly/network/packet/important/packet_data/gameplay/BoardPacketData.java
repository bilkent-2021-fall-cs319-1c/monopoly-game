package monopoly.network.packet.important.packet_data.gameplay;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import monopoly.network.packet.important.packet_data.PacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.TilePacketData;

/**
 * Board data holder that implements the generic packet data
 * 
 * @author Javid Baghirov
 * @version Dec 18, 2020
 */

@Data
@AllArgsConstructor
public class BoardPacketData implements PacketData{
	private static final long serialVersionUID = 6634230326522093081L;
	
	private List<TilePacketData> tiles;
}
