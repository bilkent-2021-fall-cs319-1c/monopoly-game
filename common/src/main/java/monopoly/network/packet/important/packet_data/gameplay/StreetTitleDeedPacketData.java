package monopoly.network.packet.important.packet_data.gameplay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StreetTitleDeedPacketData extends TitleDeedPacketData {
	private static final long serialVersionUID = 2506393575254715525L;
	
	private int housePrice;
	private int hotelPrice;
}
