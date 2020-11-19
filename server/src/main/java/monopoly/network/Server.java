package monopoly.network;

import java.io.IOException;

import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import monopoly.common.network.ServerPort;
import monopoly.common.network.packet.BufferedImagePacket;
import monopoly.common.network.packet.MicSoundPacket;
import monopoly.common.network.packet.NetworkPacket;

/**
 * A TCP and UDP server
 * 
 * @author Ziya Mukhtarov
 * @version Nov 18, 2020
 */
public abstract class Server {
	private static final int WRITE_BUFFER_SIZE = 32768;
	private static final int OBJECT_BUFFER_SIZE = 32768;

	private com.esotericsoftware.kryonet.Server kryoServer;

	/**
	 * Creates a TCP and UDP server
	 * 
	 * @throws IOException if the server could not be opened
	 */
	public Server() throws IOException {
		kryoServer = new com.esotericsoftware.kryonet.Server(WRITE_BUFFER_SIZE, OBJECT_BUFFER_SIZE);

		kryoServer.getKryo().register(BufferedImagePacket.class, new JavaSerializer());
		kryoServer.getKryo().register(byte[].class);
		kryoServer.getKryo().register(MicSoundPacket.class);

		kryoServer.addListener(new Listener() {
			@Override
			public void connected(Connection connection) {
				Server.this.connected(connection.getID());
			}

			@Override
			public void disconnected(Connection connection) {
				Server.this.disconnected(connection.getID());
			}

			@Override
			public void received(Connection connection, Object object) {
				if (object instanceof NetworkPacket) {
					receivedPacket(connection.getID(), (NetworkPacket) object);
				} else {
					receivedNotPacket(connection.getID(), object);
				}
			}
		});

		kryoServer.start();
		kryoServer.bind(ServerPort.PORT, ServerPort.PORT);
	}

	/**
	 * Called when a client successfully connects to a server
	 * 
	 * @param connectionID the server assigned unique connection ID
	 */
	public abstract void connected(int connectionID);

	/**
	 * Called when a client disconnects from the server
	 * 
	 * @param connectionID the server assigned unique connection ID
	 */
	public abstract void disconnected(int connectionID);

	/**
	 * Called when the server receives a {@link NetworkPacket} from a client
	 * 
	 * @param connectionID the server assigned unique connection ID
	 * @param packet       the received packet
	 */
	public abstract void receivedPacket(int connectionID, NetworkPacket packet);

	/**
	 * Called when the server receives anything other than a {@link NetworkPacket}
	 * from a client
	 * 
	 * @param connectionID the server assigned unique connection ID
	 * @param object       the received object
	 */
	public abstract void receivedNotPacket(int connectionID, Object object);

	/**
	 * Sends a {@link NetworkPacket} to the specified client
	 * 
	 * @param packet       Packet to send
	 * @param connectionID The ID of the connection with the client to which the
	 *                     packet should be sent
	 */
	public void sendPacket(NetworkPacket packet, int connectionID) {
		kryoServer.sendToUDP(connectionID, packet);
	}

	/**
	 * Sends a String to the specified client
	 * 
	 * @param string       String to send
	 * @param connectionID The ID of the connection with the client to which the
	 *                     string should be sent
	 */
	public void sendString(String string, int connectionID) {
		kryoServer.sendToTCP(connectionID, string);
	}
}
