package monopoly.network.packet.important.packet_data;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PlayerListPacketData extends PacketData {
	private static final long serialVersionUID = -5426017285982696364L;
	
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
