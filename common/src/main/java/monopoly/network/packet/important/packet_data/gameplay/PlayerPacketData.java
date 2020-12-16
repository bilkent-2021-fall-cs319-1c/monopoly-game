package monopoly.network.packet.important.packet_data.gameplay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import monopoly.network.packet.important.packet_data.PacketData;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerPacketData extends PacketData {
	private static final long serialVersionUID = -4414030689393401443L;
	
	private int connectionID;
	private String username;
	private int balance;
}
