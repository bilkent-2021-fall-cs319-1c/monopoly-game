package monopoly.network.packet.important.packet_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Player data holder that extends the generic packet data
 * 
 * @author Ziya Mukhtarov
 * @version Dec 13, 2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserPacketData extends PacketData {
	private static final long serialVersionUID = 4417160076301413745L;

	private int connectionId;
	private String username;
	private boolean owner;
	private boolean ready;
}
