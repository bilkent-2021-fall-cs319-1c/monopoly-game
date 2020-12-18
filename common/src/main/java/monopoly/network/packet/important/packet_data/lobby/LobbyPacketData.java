package monopoly.network.packet.important.packet_data.lobby;

import lombok.AllArgsConstructor;
import lombok.Data;
import monopoly.network.packet.important.packet_data.PacketData;

/**
 * Lobby data holder that implements the generic packet data
 * 
 * @author Ziya Mukhtarov
 * @version Dec 13, 2020
 */

@Data
@AllArgsConstructor
public class LobbyPacketData implements PacketData {
	private static final long serialVersionUID = 1586964682050981428L;

	private int lobbyId;
	private String name;
	private String password;
	private boolean isPublic;

	private String ownerUsername;
	private int playerCount;
	private int playerLimit;
}
