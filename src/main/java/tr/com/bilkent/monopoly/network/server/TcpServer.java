package tr.com.bilkent.monopoly.network.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple TCP Server for sending and receiving String messages
 * 
 * @author Ziya Mukhtarov
 * @version Nov 14, 2020
 */
public abstract class TcpServer {
	private static Logger logger = LoggerFactory.getLogger(TcpServer.class);

	private ServerSocket server;
	private List<Socket> sockets;
	private Map<Socket, Boolean> keepAlive;

	private List<Thread> msgListenerThreads;
	private Thread connectionListenerThread;
	private Thread deadConnectionKillerThread;
	private volatile boolean open;

	/**
	 * Opens a new socket for receiving TCP connections
	 * 
	 * @param port The port to listen on
	 * @throws IOException if an I/O error occurs when opening the socket.
	 */
	public TcpServer(int port) throws IOException {
		open = true;
		sockets = Collections.synchronizedList(new ArrayList<Socket>());
		keepAlive = Collections.synchronizedMap(new HashMap<Socket, Boolean>());
		msgListenerThreads = Collections.synchronizedList(new ArrayList<Thread>());
		server = new ServerSocket(port);

		// Listens for connections
		connectionListenerThread = new Thread(this::listenForConnections);
		connectionListenerThread.start();

		// Kill connections that did not send keep alive signals
		deadConnectionKillerThread = new Thread(this::killDeadConnections);
		deadConnectionKillerThread.start();
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
	 * @param socket The socket that was closed
	 */
	public abstract void connectionTerminated(Socket socket);

	private void connectionTerminatedHelper(Socket socket) {
		connectionTerminated(socket);
		sockets.remove(socket);
		keepAlive.remove(socket);
	}

	/**
	 * Processes the incoming message
	 * 
	 * @param message The message received
	 * @param socket  The socket that the message received from
	 */
	public abstract void received(String message, Socket socket);

	/**
	 * Kills all connections that did not send anything in the last 61 seconds.
	 */
	private void killDeadConnections() {
		while (open) {
			try {
				Thread.sleep(61000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}

			keepAlive.entrySet().parallelStream().forEach(entry -> {
				Socket socket = entry.getKey();
				Boolean alive = entry.getValue();
				
				if (Boolean.FALSE.equals(alive)) {
					logger.info("No communication received from {} during last 61 seconds. Closing the connection.",
							socket.getInetAddress());
					connectionTerminatedHelper(socket);
				}
				keepAlive.put(socket, false);
			});
		}
	}

	/**
	 * Listens for connections and creates a new socket for them
	 */
	private void listenForConnections() {
		while (open) {
			Socket socket;
			try {
				socket = server.accept();
			} catch (IOException e) {
				break;
			}

			if (!open)
				return;

			sockets.add(socket);
			keepAlive.put(socket, true);
			connectionEstablished(socket);

			// Listens for messages
			msgListenerThreads.add(new Thread(() -> listenForMessages(socket)));
			msgListenerThreads.get(msgListenerThreads.size() - 1).start();
		}
	}

	/**
	 * Listens for incoming messages from the specified socket
	 * 
	 * @param socket The socket to listen on
	 */
	private void listenForMessages(Socket socket) {
		StringBuilder message = new StringBuilder();
		boolean isAlive = true;

		while (isAlive) {
			try {
				message.append((char) socket.getInputStream().read());
			} catch (IOException e) {
				isAlive = false;
			}

			if (message.toString().equals(Server.TERMINATION)) {
				// Keep Alive Message
				keepAlive.put(socket, true);
				message.setLength(0);
			} else if (message.indexOf(Server.TERMINATION) != -1) {
				received(message.substring(0, message.length() - 3), socket);
				message.setLength(0);
			}
		}

		connectionTerminatedHelper(socket);
	}

	/**
	 * Sends a message over all available connections
	 * 
	 * @param msg The message to send
	 */
	public void sendMessageToAll(String msg) {
		byte[] data = msg.getBytes();
		for (Socket socket : sockets) {
			try {
				socket.getOutputStream().write(data);
			} catch (IOException e) {
				connectionTerminatedHelper(socket);
			}
		}
	}

	/**
	 * Send a message to the specified address
	 * 
	 * @param msg     The message to send
	 * @param address The address of the intended receiver
	 */
	public void sendMessage(String msg, InetAddress address) {
		byte[] data = msg.getBytes();
		for (Socket socket : sockets) {
			if (address.equals(socket.getInetAddress())) {
				try {
					socket.getOutputStream().write(data);
				} catch (IOException e) {
					e.printStackTrace();
					connectionTerminatedHelper(socket);
				}
			}
		}
	}

	/**
	 * Send a message to the specified address
	 * 
	 * @param msg     The message to send
	 * @param address The address of the intended receiver
	 * @throws UnknownHostException if no IP address for the host could be found, or
	 *                              if a scope_id was specified for a global IPv6
	 *                              address
	 */
	public void sendMessage(String msg, String address) throws UnknownHostException {
		sendMessage(msg, InetAddress.getByName(address));
	}

	/**
	 * Closes the TCP server
	 */
	public void close() {
		// Closing listeners
		for (Thread msgListenerThread : msgListenerThreads) {
			msgListenerThread.interrupt();
		}
		connectionListenerThread.interrupt();
		deadConnectionKillerThread.interrupt();

		// Closing connections
		for (Socket socket : sockets) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Closing the server
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
