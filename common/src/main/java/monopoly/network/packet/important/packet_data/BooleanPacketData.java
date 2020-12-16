package monopoly.network.packet.important.packet_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Boolean data holder that extends the generic packet data
 * 
 * @author Ziya Mukhtarov
 * @version Dec 13, 2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BooleanPacketData extends PacketData {
	private static final long serialVersionUID = -2802650519101034572L;

	private boolean data;
}
