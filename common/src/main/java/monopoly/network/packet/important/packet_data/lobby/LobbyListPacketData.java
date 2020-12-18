package monopoly.network.packet.important.packet_data.lobby;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import monopoly.network.packet.important.packet_data.PacketData;

/**
 * Lobby list data holder that implements the generic packet data
 * 
 * @author Ziya Mukhtarov
 * @version Dec 13, 2020
 */

@Data
@AllArgsConstructor
public class LobbyListPacketData implements PacketData {
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
