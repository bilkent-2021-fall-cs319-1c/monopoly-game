package monopoly.network.packet.important.packet_data.gameplay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StreetPacketData extends PropertyPacketData {
	private static final long serialVersionUID = -3484595840480253292L;

	private int houseCount;
	private boolean hasHotel;
	// Color set?
}
