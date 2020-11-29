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
public class PlayerPacketData extends PacketData {
	private static final long serialVersionUID = 4417160076301413745L;

	private int connectionId;
	private String username;
	private boolean admin;
}
