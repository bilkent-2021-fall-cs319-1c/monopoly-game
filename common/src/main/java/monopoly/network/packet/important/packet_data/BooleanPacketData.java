package monopoly.network.packet.important.packet_data;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Boolean data holder that implements the generic packet data
 * 
 * @author Ziya Mukhtarov
 * @version Dec 13, 2020
 */

@Data
@AllArgsConstructor
public class BooleanPacketData implements PacketData {
	private static final long serialVersionUID = -2802650519101034572L;

	private boolean data;
}
