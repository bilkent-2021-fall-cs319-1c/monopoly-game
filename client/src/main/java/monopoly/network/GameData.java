package monopoly.network;

import lombok.AllArgsConstructor;
import lombok.Data;
import monopoly.network.packet.important.packet_data.gameplay.BoardPacketData;
import monopoly.network.packet.important.packet_data.gameplay.PlayerListPacketData;

@Data
@AllArgsConstructor
public class GameData {
	private PlayerListPacketData playerData;
	private BoardPacketData boardData;
}
