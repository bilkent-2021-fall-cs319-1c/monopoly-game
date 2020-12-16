package monopoly.network.packet.important.packet_data.gameplay;

import lombok.Getter;
import monopoly.network.packet.important.packet_data.PacketData;

@Getter
public class TilePacketData extends PacketData{
	private static final long serialVersionUID = -580359519872171665L;
	
	private String name;
	private String description;
	private String color;
	private String type;
}
