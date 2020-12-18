package monopoly.network.packet.important.packet_data.gameplay;

import java.util.ArrayList;
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
public class BoardPacketData implements PacketData {
	private static final long serialVersionUID = 6634230326522093081L;

	private List<TilePacketData> tiles;
	
	public BoardPacketData() {
		tiles = new ArrayList<>();
	}

	public void add(TilePacketData tilePacketData) {
		tiles.add(tilePacketData);
	}

	public void remove(TilePacketData tilePacketData) {
		tiles.remove(tilePacketData);
	}
}
