package monopoly.network.packet.important.packet_data;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * User list data holder that implements the generic packet data
 * 
 * @author Ziya Mukhtarov
 * @version Dec 18, 2020
 */

@Data
@AllArgsConstructor
public class UserListPacketData implements PacketData {
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
