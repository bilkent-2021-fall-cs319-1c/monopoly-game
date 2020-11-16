package tr.com.bilkent.monopoly.network.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.UnknownHostException;

import tr.com.bilkent.monopoly.network.UdpEndpoint;
import tr.com.bilkent.monopoly.network.server.Server;

/**
 * A client for TCP and UDP connection
 * 
 * @author Ziya Mukhtarov
 * @version Nov 14, 2020
 */
public abstract class Client {
	public static final int PORT = 3455;

	private String serverAddress;
	private TcpClient tcp;
	private UdpEndpoint udp;

	/**
	 * Connects the client to the specified server
	 * 
	 * @param serverAddress The IP address of the server
	 * @throws IOException          if an I/O error occurs when creating the socket.
	 * @throws UnknownHostException if no IP address for the host could be found, or
	 *                              if a scope_id was specified for a global
	 *                              IPv6address
	 */
	public Client(String serverAddress) throws IOException {
		this.serverAddress = serverAddress;

		tcp = new TcpClient(serverAddress, Server.PORT) {
			public void received(String msg) {
				messageReceived(msg);
			}
		};

		udp = new UdpEndpoint(PORT) {
			@Override
			public void byteArrayReceivedUdp(byte[] data, DatagramPacket packet) {
				Client.this.byteArrayReceivedUdp(data);
			}
		};
	}

	/**
	 * Processes the received message through TCP
	 * 
	 * @param msg The contents of the message
	 */
	public abstract void messageReceived(String msg);

	/**
	 * Processes the received byte array through UDP
	 * 
	 * @param data The byte array received
	 */
	public abstract void byteArrayReceivedUdp(byte[] data);

	/**
	 * Sends a message to the server via TCP
	 * 
	 * @param msg The message to be sent
	 */
	public void sendMessage(String msg) {
		tcp.sendMessage(msg);
	}

	/**
	 * Sends a byte array to the server via UDP
	 * 
	 * @param screenshot The Screenshot to be sent
	 * @throws IOException
	 */
	public void sendByteArrayUdp(byte[] data) throws IOException {
		udp.sendByteArray(data, serverAddress, Server.PORT);
	}

	/**
	 * Closes the connection with the server
	 */
	public void close() {
		udp.close();
		tcp.close();
	}

	/**
	 * @return The local address to which this client is bound.
	 */
	public String getLocalAddress() {
		return tcp.getLocalAddress();
	}
}
