package monopoly.network.packet.important.packet_data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User data holder that implements the generic packet data
 * 
 * @author Ziya Mukhtarov
 * @version Dec 13, 2020
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPacketData implements PacketData {
	private static final long serialVersionUID = 4417160076301413745L;

	private int connectionId;
	private String username;
	private boolean owner;
	private boolean ready;
}
