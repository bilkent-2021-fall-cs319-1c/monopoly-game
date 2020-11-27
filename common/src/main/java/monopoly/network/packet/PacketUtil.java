package monopoly.network.packet;

import java.util.ArrayList;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.JavaSerializer;

import monopoly.network.packet.important.ImportantNetworkPacket;
import monopoly.network.packet.important.PacketType;
import monopoly.network.packet.important.packet_data.IntegerPacketData;
import monopoly.network.packet.important.packet_data.LobbyListPacketData;
import monopoly.network.packet.important.packet_data.LobbyPacketData;
import monopoly.network.packet.important.packet_data.StringPacketData;
import monopoly.network.packet.realtime.BufferedImagePacket;
import monopoly.network.packet.realtime.MicSoundPacket;

public class PacketUtil {
	private PacketUtil() {
	}

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
		kryo.register(ImportantNetworkPacket.class);
	}
}
