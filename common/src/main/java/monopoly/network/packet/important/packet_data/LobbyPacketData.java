package monopoly.network.packet.important.packet_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LobbyPacketData extends PacketData {
	private static final long serialVersionUID = 1586964682050981428L;

	private long lobbyId;
	private String name;
	private String password;
	private boolean isPublic;

	private String ownerUsername;
	private int playerCount;
	private int playerLimit;
}
