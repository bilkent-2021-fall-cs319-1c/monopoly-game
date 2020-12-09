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
public class PropertyPacketData extends PacketData{
	private static final long serialVersionUID = 27242078625602573L;
	
	private String name;
	private String ownerUsername;
	private boolean mortgaged;
	private int rentCost;
}
