package monopoly.network.packet;

import java.util.ArrayList;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.JavaSerializer;

import monopoly.network.packet.important.ImportantNetworkPacket;
import monopoly.network.packet.important.PacketType;
import monopoly.network.packet.important.packet_data.BooleanPacketData;
import monopoly.network.packet.important.packet_data.IntegerPacketData;
import monopoly.network.packet.important.packet_data.StringPacketData;
import monopoly.network.packet.important.packet_data.UserListPacketData;
import monopoly.network.packet.important.packet_data.UserPacketData;
import monopoly.network.packet.important.packet_data.gameplay.BankPacketData;
import monopoly.network.packet.important.packet_data.gameplay.BoardPacketData;
import monopoly.network.packet.important.packet_data.gameplay.DicePacketData;
import monopoly.network.packet.important.packet_data.gameplay.PlayerListPacketData;
import monopoly.network.packet.important.packet_data.gameplay.PlayerPacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.PropertyPacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.StreetPacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.StreetTitleDeedPacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.TilePacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.TitleDeedPacketData;
import monopoly.network.packet.important.packet_data.lobby.LobbyListPacketData;
import monopoly.network.packet.important.packet_data.lobby.LobbyPacketData;
import monopoly.network.packet.realtime.BufferedImagePacket;
import monopoly.network.packet.realtime.MicSoundPacket;

/**
 * This class is for registering all the new data classes using a java
 * serializer
 * 
 * @author Ziya Mukhtarov
 * @version Dec 13, 2020
 */

public class PacketUtil {
	private PacketUtil() {
	}

	/**
	 * Registers new classes using java serializer
	 * 
	 * @param kryo the serialization framework for registering the classes
	 */
	public static void registerPackets(Kryo kryo) {
		kryo.register(BufferedImagePacket.class, new JavaSerializer());
		kryo.register(byte[].class);
		kryo.register(MicSoundPacket.class);

		kryo.register(ArrayList.class);
		kryo.register(PacketType.class);
		kryo.register(IntegerPacketData.class);
		kryo.register(LobbyPacketData.class);
		kryo.register(LobbyListPacketData.class);
		kryo.register(StringPacketData.class);
		kryo.register(BooleanPacketData.class);
		kryo.register(UserPacketData.class);
		kryo.register(UserListPacketData.class);
		kryo.register(ImportantNetworkPacket.class);
		kryo.register(BankPacketData.class);
		kryo.register(PropertyPacketData.class);
		kryo.register(StreetPacketData.class);
		kryo.register(TitleDeedPacketData.class);
		kryo.register(StreetTitleDeedPacketData.class);
		kryo.register(PlayerPacketData.class);
		kryo.register(PlayerListPacketData.class);
		kryo.register(TilePacketData.class);
		kryo.register(BoardPacketData.class);
		kryo.register(DicePacketData.class);
	}
}
