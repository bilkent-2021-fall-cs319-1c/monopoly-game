package monopoly.network.packet.important.packet_data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Integer data holder that implements the generic packet data
 * 
 * @author Ziya Mukhtarov
 * @version Dec 13, 2020
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntegerPacketData implements PacketData {
	private static final long serialVersionUID = -7875864865726624488L;

	private int data;
}
