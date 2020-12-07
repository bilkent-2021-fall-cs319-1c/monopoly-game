package monopoly.network.packet.important.packet_data;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LobbyListPacketData extends PacketData {
	private static final long serialVersionUID = 1595679842616701669L;

	private List<LobbyPacketData> lobbies;
	
	public LobbyListPacketData() {
		lobbies = new ArrayList<>();
	}
	
	public void add(LobbyPacketData lobbyPacketData) {
		lobbies.add(lobbyPacketData);
	}
	
	public void remove(LobbyPacketData lobbyPacketData) {
		lobbies.remove(lobbyPacketData);
	}
}
