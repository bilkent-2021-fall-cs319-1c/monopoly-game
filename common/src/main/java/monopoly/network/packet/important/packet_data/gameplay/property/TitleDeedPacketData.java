package monopoly.network.packet.important.packet_data.gameplay.property;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import monopoly.network.packet.important.packet_data.PacketData;

/**
 * Title deed data holder that implements the generic packet data
 * 
 * @author Javid Baghirov
 * @version Dec 13, 2020
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TitleDeedPacketData implements PacketData {
	private static final long serialVersionUID = 7109973357504301200L;

	private String title;
	private int buyPrice;
	private int mortgagePrice;
	/**
	 * The rent cost that depends on the count of the properties of the same kind
	 * the owner has
	 */
	private List<Integer> rentCost;
}
