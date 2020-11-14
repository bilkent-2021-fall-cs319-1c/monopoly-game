package tr.com.bilkent.monopoly.network.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import lombok.Getter;
import tr.com.bilkent.monopoly.network.server.Server;

/**
 * Simple TCP client for sending and receiving String messages
 * 
 * @author Ziya Mukhtarov
 * @version Nov 14, 2020
 */
public abstract class TcpClient {
	/**
	 * The local address to which this client is bound.
	 */
	@Getter
	private String localAddress;

	private Socket socket;
	private Thread msgListenerThread;
	private volatile boolean open;

	/**
	 * Creates a TCP client, connects it to the specified server and listens for
	 * incoming messages
	 * 
	 * @param serverAddress The IP address of the server
	 * @param serverPort    The port of the server
	 * @throws IOException          if an I/O error occurs when creating the socket.
	 * @throws UnknownHostException if no IP address for the host could be found, or
	 *                              if a scope_id was specified for a global IPv6
	 *                              address
	 */
	public TcpClient(String serverAddress, int serverPort) throws IOException {
		socket = new Socket(serverAddress, serverPort);
		localAddress = socket.getLocalAddress().toString();

		open = true;
		msgListenerThread = new Thread(this::listenForMessages);
		msgListenerThread.start();

	}

	/**
	 * Handles the received message
	 * 
	 * @param msg The message received
	 */
	public abstract void received(String msg);

	/**
	 * Sends a message to the server
	 * 
	 * @param msg The message to be sent
	 */
	public synchronized void sendMessage(String msg) {
		msg += Server.TERMINATION;
		byte[] data = msg.getBytes();
		try {
			socket.getOutputStream().write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Continuously listens for any message on the socket. Calls received(message)
	 * once a message arrives. <br>
	 * Warning: This method blocks until the socket is closed or some error occurs
	 */
	private void listenForMessages() {
		StringBuilder msg;
		InputStream stream;

		try {
			stream = socket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		while (open) {
			msg = new StringBuilder();
			while (msg.indexOf(Server.TERMINATION) == -1) {
				try {
					msg.append((char) stream.read());
				} catch (SocketException e) {
					close();
					return;
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}

			received(msg.substring(0, msg.length() - 3));
		}
	}

	/**
	 * Closes the TCP connection
	 */
	public void close() {
		open = false;
		msgListenerThread.interrupt();
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
