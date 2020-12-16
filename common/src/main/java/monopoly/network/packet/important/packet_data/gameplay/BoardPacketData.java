package monopoly.network.packet.important.packet_data.gameplay;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import monopoly.network.packet.important.packet_data.PacketData;

@Getter
@NoArgsConstructor
public class BoardPacketData extends PacketData{
	private static final long serialVersionUID = 6634230326522093081L;
	
	private List<TilePacketData> tiles;
	@Setter
	private int[] tokenLocations;
	
	public BoardPacketData( List<TilePacketData> tiles) {
		this.tiles = tiles;
	}
}
