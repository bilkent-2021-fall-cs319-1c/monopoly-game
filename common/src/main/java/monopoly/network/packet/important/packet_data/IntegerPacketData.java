package monopoly.network.packet.important.packet_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class IntegerPacketData extends PacketData {
	private static final long serialVersionUID = -7875864865726624488L;

	private int data;
}
