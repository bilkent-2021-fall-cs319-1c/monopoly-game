package monopoly.gameplay;

import monopoly.network.packet.important.packet_data.PacketData;

/**
 * An item that can be owned by a player
 * 
 * @author Ziya Mukhtarov
 * @version Jan 19, 2021
 */
public interface Ownable {
	void setOwner(GamePlayer player);

	GamePlayer getOwner();

	PacketData getAsPacket();
}
