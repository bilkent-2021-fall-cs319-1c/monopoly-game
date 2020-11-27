package monopoly.network;

import java.io.IOException;

import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import lombok.Getter;
import monopoly.network.packet.important.ImportantNetworkPacket;
import monopoly.network.packet.realtime.BufferedImagePacket;
import monopoly.network.packet.realtime.MicSoundPacket;
import monopoly.network.packet.realtime.RealTimeNetworkPacket;

/**
 * A client for TCP and UDP connection
 * 
 * @author Ziya Mukhtarov
 * @version Nov 27, 2020
 */
public abstract class Client {
	@Getter
	private int connectionID;

	private com.esotericsoftware.kryonet.Client kryoClient;

	/**
	 * Connects the client to the specified server
	 * 
	 * @param serverAddress The IP address of the server
	 * @throws IOException if an I/O error occurs when connecting
	 */
	public Client(String serverAddress) throws IOException {
		connectionID = -11;

		kryoClient = new com.esotericsoftware.kryonet.Client(32768, 32768);

		kryoClient.getKryo().register(BufferedImagePacket.class, new JavaSerializer());
		kryoClient.getKryo().register(byte[].class);
		kryoClient.getKryo().register(MicSoundPacket.class);

		kryoClient.addListener(new Listener() {
			@Override
			public void connected(Connection connection) {
				connectionID = connection.getID();
				Client.this.connected(connection.getID());
			}

			@Override
			public void disconnected(Connection connection) {
				Client.this.disconnected(connection.getID());
			}

			@Override
			public void received(Connection connection, Object object) {
				if (object instanceof RealTimeNetworkPacket) {
					receivedRealTimePacket(connection.getID(), (RealTimeNetworkPacket) object);
				} else if (object instanceof ImportantNetworkPacket) {
					receivedImportantPacket(connection.getID(), (ImportantNetworkPacket) object);
				}
			}
		});

		kryoClient.start();
		kryoClient.connect(5000, serverAddress, ServerInfo.PORT, ServerInfo.PORT);
	}

	/**
	 * Called when client successfully connects to a server
	 * 
	 * @param connectionID the server assigned unique ID
	 */
	public abstract void connected(int connectionID);

	/**
	 * Called when client disconnects from the server
	 * 
	 * @param connectionID the server assigned unique ID
	 */
	public abstract void disconnected(int connectionID);

	/**
	 * Called when client receives a {@link RealTimeNetworkPacket} from the server
	 * 
	 * @param connectionID the server assigned unique ID
	 * @param packet       the received packet
	 */
	public abstract void receivedRealTimePacket(int connectionID, RealTimeNetworkPacket packet);

	/**
	 * Called when client receives an {@link ImportantNetworkPacket} from the server
	 * 
	 * @param connectionID the server assigned unique ID
	 * @param object       the received object
	 */
	public abstract void receivedImportantPacket(int connectionID, ImportantNetworkPacket object);

	/**
	 * Sends a {@link RealTimeNetworkPacket} to the server
	 * 
	 * @param packet Packet to send
	 */
	public void sendRealTimePacket(RealTimeNetworkPacket packet) {
		kryoClient.sendUDP(packet);
	}

	/**
	 * Sends an {@link ImportantNetworkPacket} to the server
	 * 
	 * @param packet Packet to send
	 */
	public void sendImportantPacket(ImportantNetworkPacket packet) {
		kryoClient.sendTCP(packet);
	}

	/**
	 * Closes the connection with the server
	 */
	public void close() {
		kryoClient.close();
	}
}
