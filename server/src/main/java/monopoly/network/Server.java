package monopoly.network;

import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import monopoly.network.packet.PacketUtil;
import monopoly.network.packet.important.ImportantNetworkPacket;
import monopoly.network.packet.realtime.RealTimeNetworkPacket;

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
	protected Server() throws IOException {
		kryoServer = new com.esotericsoftware.kryonet.Server(WRITE_BUFFER_SIZE, OBJECT_BUFFER_SIZE);

		PacketUtil.registerPackets(kryoServer.getKryo());

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
				if (object instanceof RealTimeNetworkPacket) {
					receivedRealTimePacket(connection.getID(), (RealTimeNetworkPacket) object);
				} else if (object instanceof ImportantNetworkPacket) {
					receivedImportantPacket(connection.getID(), (ImportantNetworkPacket) object);
				}
			}
		});

		kryoServer.start();
		kryoServer.bind(ServerInfo.PORT, ServerInfo.PORT);
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
	 * Called when the server receives a {@link RealTimeNetworkPacket} from a client
	 * 
	 * @param connectionID the server assigned unique connection ID
	 * @param packet       the received packet
	 */
	public abstract void receivedRealTimePacket(int connectionID, RealTimeNetworkPacket packet);

	/**
	 * Called when the server receives an {@link ImportantNetworkPacket} from a
	 * client
	 * 
	 * @param connectionID the server assigned unique connection ID
	 * @param packet       the received packet
	 */
	public abstract void receivedImportantPacket(int connectionID, ImportantNetworkPacket packet);

	/**
	 * Sends a {@link RealTimeNetworkPacket} to the specified client
	 * 
	 * @param packet       Packet to send
	 * @param connectionID The ID of the connection with the client to which the
	 *                     packet should be sent
	 */
	public void sendRealTimePacket(RealTimeNetworkPacket packet, int connectionID) {
		System.out.println("Send to " + connectionID + " Packet: " + packet);
		kryoServer.sendToUDP(connectionID, packet);
	}

	/**
	 * Sends an {@link ImportantNetworkPacket} to the specified client
	 * 
	 * @param packet       Packet to send
	 * @param connectionID The ID of the connection with the client to which the
	 *                     string should be sent
	 */
	public void sendImportantPacket(ImportantNetworkPacket packet, int connectionID) {
		System.out.println("Sending " + packet.getType());
		kryoServer.sendToTCP(connectionID, packet);
	}
}
