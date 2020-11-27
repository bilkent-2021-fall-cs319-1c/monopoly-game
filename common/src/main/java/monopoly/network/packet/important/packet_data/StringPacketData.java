package monopoly.network.packet.important.packet_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class StringPacketData extends PacketData {
	private static final long serialVersionUID = -1125691830137391654L;

	private String data;
}
