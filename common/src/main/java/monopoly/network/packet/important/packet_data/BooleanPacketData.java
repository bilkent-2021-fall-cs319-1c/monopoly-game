package monopoly.network.packet.important.packet_data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Boolean data holder that implements the generic packet data
 * 
 * @author Ziya Mukhtarov
 * @version Dec 13, 2020
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BooleanPacketData implements PacketData {
	private static final long serialVersionUID = -2802650519101034572L;

	@Getter(AccessLevel.NONE)
	private boolean data;

	public boolean getData() {
		return data;
	}
}
