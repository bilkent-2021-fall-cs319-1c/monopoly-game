package tr.com.bilkent.monopoly.network.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tr.com.bilkent.monopoly.network.UdpEndpoint;
import tr.com.bilkent.monopoly.network.client.Client;

/**
 * A server containing TCP and UDP servers
 * 
 * @author Ziya Mukhtarov
 * @version Nov 14, 2020
 */
public abstract class Server {
	public static final String TERMINATION = "###";
	public static final int UDP_INCOMING_PORT = 4454;
	public static final int UDP_OUTGOING_PORT = 4455;
	public static final int TCP_PORT = 4456;

	private TcpServer tcp;
	private UdpEndpoint udp;
	private List<InetAddress> connections;

	/**
	 * Starts a TCP and UDP server
	 * 
	 * @throws IOException if an I/O error occurs when opening the socket, if the
	 *                     socket could not be opened, or the socket could not bind
	 *                     to the specified local port.
	 */
	public Server() throws IOException {
		connections = Collections.synchronizedList(new ArrayList<InetAddress>());

		tcp = new TcpServer(TCP_PORT) {
			@Override
			public void connectionEstablished(Socket socket) {
				connections.add(socket.getInetAddress());
				Server.this.connectionEstablished(socket);
			}

			@Override
			public void connectionTerminated(Socket socket) {
				Server.this.connectionTerminated(socket);
				connections.remove(socket.getInetAddress());
			}

			@Override
			public void received(String message, Socket socket) {
				Server.this.messageReceived(message, socket);
			}
		};

		udp = new UdpEndpoint(UDP_INCOMING_PORT, UDP_OUTGOING_PORT) {
			@Override
			public void byteArrayReceivedUdp(byte[] data, DatagramPacket packet) {
				Server.this.byteArrayReceivedUdp(data, packet);
			}
		};
	}

	/**
	 * Handles the new opened connection
	 * 
	 * @param socket The new socket
	 */
	public abstract void connectionEstablished(Socket socket);

	/**
	 * Handles the closed connection
	 * 
	 * @param socket the socket that has been closed
	 */
	public abstract void connectionTerminated(Socket socket);

	/**
	 * Processes the received message through TCP
	 * 
	 * @param msg    the contents of the message
	 * @param socket the connection that received the message from
	 */
	public abstract void messageReceived(String msg, Socket socket);

	/**
	 * Processes the received byte array through UDP
	 * 
	 * @param img    the received Screenshot via packet
	 * @param packet The packet which has been received
	 */
	public abstract void byteArrayReceivedUdp(byte[] data, DatagramPacket packet);

	/**
	 * Sends a message to all connected clients via TCP
	 * 
	 * @param msg The message to be sent
	 */
	public void sendMessageToAll(String msg) {
		msg += TERMINATION;
		tcp.sendMessageToAll(msg);
	}

	/**
	 * Sends a message to the specified client via TCP
	 * 
	 * @param msg     The message to be sent
	 * @param address The InetAddress of the client
	 */
	public void sendMessage(String msg, InetAddress address) {
		msg += TERMINATION;
		tcp.sendMessage(msg, address);
	}

	/**
	 * Sends a message to the specified client via TCP
	 * 
	 * @param msg     The message to be sent
	 * @param address The IP of the client
	 * @throws UnknownHostException if no IP address for the host could be found, or
	 *                              if a scope_id was specified for a global IPv6
	 *                              address
	 */
	public void sendMessage(String msg, String address) throws UnknownHostException {
		msg += TERMINATION;
		tcp.sendMessage(msg, address);
	}

	/**
	 * Sends a byte array to all connected clients via UDP
	 * 
	 * @param data The byte array to be sent
	 */
	public void sendByteArrayToAll(byte[] data) {
		connections.forEach(addr -> {
			try {
				sendByteArray(data, addr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Sends a byte array to all connected clients except the specified one via UDP
	 * 
	 * @param data   The byte array to be sent
	 * @param except The IP address to not send the data to
	 */
	public void sendByteArrayToAllExcept(byte[] data, String except) throws UnknownHostException {
		sendByteArrayToAllExcept(data, InetAddress.getByName(except));
	}

	/**
	 * Sends a byte array to all connected clients except the specified one via UDP
	 * 
	 * @param data   The byte array to be sent
	 * @param except The address to not send the data to
	 */
	public void sendByteArrayToAllExcept(byte[] data, InetAddress except) {
		connections.forEach(addr -> {
			if (!addr.equals(except)) {
				try {
					sendByteArray(data, addr);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Sends a byte array to a client via UDP
	 * 
	 * @param data    The byte array to be sent
	 * @param address The address to send the data to
	 */
	public void sendByteArray(byte[] data, InetAddress address) throws IOException {
		udp.sendByteArray(data, address, Client.UDP_INCOMING_PORT);
	}

	public void sendByteArray(byte[] data, String address) throws IOException {
		udp.sendByteArray(data, address, Client.UDP_INCOMING_PORT);
	}

	/**
	 * Closes the server
	 */
	public void close() {
		udp.close();
		tcp.close();
	}
}
