package monopoly.network.packet.important.packet_data.gameplay;

import lombok.AllArgsConstructor;
import lombok.Data;
import monopoly.network.packet.important.packet_data.PacketData;

/**
 * Dice data holder that implements the generic packet data
 * 
 * @author Javid Baghirov
 * @version Dec 18, 2020
 */

@Data
@AllArgsConstructor
public class DicePacketData implements PacketData{
	private static final long serialVersionUID = 3443305946524937257L;
	
	private int firstDieValue;
	private int secondDieValue;
}
