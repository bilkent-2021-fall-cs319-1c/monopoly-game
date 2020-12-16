package monopoly.network.packet.important.packet_data.gameplay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import monopoly.network.packet.important.packet_data.PacketData;

@Getter
@AllArgsConstructor
public class DicePacketData extends PacketData{
	private static final long serialVersionUID = 3443305946524937257L;
	
	private int firstDieValue;
	private int secondDieValue;
}
