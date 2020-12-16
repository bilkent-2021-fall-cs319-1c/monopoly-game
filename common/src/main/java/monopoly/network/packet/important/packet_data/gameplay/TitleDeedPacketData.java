package monopoly.network.packet.important.packet_data.gameplay;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import monopoly.network.packet.important.packet_data.PacketData;

/**
 * Title deed data holder that extends the generic packet data
 * 
 * @author Javid Baghirov
 * @version Dec 13, 2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TitleDeedPacketData extends PacketData {
	private static final long serialVersionUID = 7109973357504301200L;
	
	private String title;
	private String buyPrice;
	private String mortgagePrice;
	private Map<Integer, Integer> rentCost;
}
