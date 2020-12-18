package monopoly.network.packet.important.packet_data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * String data holder that implements the generic packet data
 * 
 * @author Ziya Mukhtarov
 * @version Dec 13, 2020
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StringPacketData implements PacketData {
	private static final long serialVersionUID = -1125691830137391654L;

	private String data;
}
