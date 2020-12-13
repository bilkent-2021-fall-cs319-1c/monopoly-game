package monopoly.network.packet.important.packet_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * String data holder that extends the generic packet data
 * 
 * @author Ziya Mukhtarov
 * @version Dec 13, 2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StringPacketData extends PacketData {
	private static final long serialVersionUID = -1125691830137391654L;

	private String data;
}
