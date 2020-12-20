package monopoly.network.packet.important.packet_data.gameplay;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import monopoly.network.packet.important.packet_data.PacketData;

/**
 * Player list data holder that implements the generic packet data
 * 
 * @author Javid Baghirov
 * @version Dec 18, 2020
 */

@Data
@AllArgsConstructor
public class PlayerListPacketData implements PacketData {
	private static final long serialVersionUID = 8041786114593360155L;

	private List<PlayerPacketData> players;

	public PlayerListPacketData() {
		players = new ArrayList<>();
	}

	public void add(PlayerPacketData playerPacketData) {
		players.add(playerPacketData);
	}

	public void remove(PlayerPacketData playerPacketData) {
		players.remove(playerPacketData);
	}
}
