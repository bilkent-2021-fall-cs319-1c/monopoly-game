package monopoly.network.packet.important.packet_data;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserListPacketData extends PacketData {
	private static final long serialVersionUID = -5426017285982696364L;
	
	private List<UserPacketData> users;
	
	public UserListPacketData() {
		users = new ArrayList<>();
	}
	
	public void add(UserPacketData userPacketData) {
		users.add(userPacketData);
	}
	
	public void remove(UserPacketData userPacketData) {
		users.remove(userPacketData);
	}
}
