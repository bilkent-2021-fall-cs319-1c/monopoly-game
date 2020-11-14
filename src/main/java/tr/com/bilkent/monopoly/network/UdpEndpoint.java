package tr.com.bilkent.monopoly.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

/**
 * A UDP endpoint for network communication. Can send/receive byte arrays
 * 
 * @author Ziya Mukhtarov
 * @version Nov 14, 2020
 */
public abstract class UdpEndpoint {

	// TODO Maybe merge these to 1 socket?
	private DatagramSocket outgoingSocket;
	private DatagramSocket incomingSocket;

	private Thread listenerThread;
	private volatile boolean open;

	/**
	 * Creates a new UDP endpoint in the specified ports.
	 * 
	 * @param incomingPort The port where to receive packets
	 * @param outgoingPort The port from where to send packets
	 * @throws SocketException if the socket could not be opened,or the socket could
	 *                         not bind to the specified local port.
	 */
	public UdpEndpoint(int incomingPort, int outgoingPort) throws SocketException {
		open = true;
		incomingSocket = new DatagramSocket(incomingPort);
		outgoingSocket = new DatagramSocket(outgoingPort);

		listenerThread = new Thread(this::readByteArray);
		listenerThread.start();
	}

	/**
	 * Sends the given byte array to the given address
	 * 
	 * @param data    The byte array to send
	 * @param address The destination ip address
	 * @param port    The destination port
	 * @throws IOException if an I/O error occurs.
	 */
	public void sendByteArray(byte[] data, String address, int port) throws IOException {
		sendByteArray(data, InetAddress.getByName(address), port);
	}

	/**
	 * Sends the given byte array to the given address
	 * 
	 * @param data    The byte array to send
	 * @param address The destination address
	 * @param port    The destination port
	 * @throws IOException if an I/O error occurs.
	 */
	public void sendByteArray(byte[] data, InetAddress address, int port) throws IOException {
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		outgoingSocket.send(packet);
	}

	/**
	 * Handles the received byte array
	 * 
	 * @param data   - The byte array received
	 * @param packet - The packet which has been received
	 */
	public abstract void byteArrayReceivedUdp(byte[] data, DatagramPacket packet);

	/**
	 * Receives the byte arrays sent to this endpoint
	 */
	private void readByteArray() {
		while (open) {
			byte[] buf = new byte[65000];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try {
				incomingSocket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}

			byteArrayReceivedUdp(Arrays.copyOf(packet.getData(), packet.getLength()), packet);
		}
	}

	/**
	 * Closes this endpoint
	 */
	public void close() {
		open = false;
		incomingSocket.close();
		outgoingSocket.close();
	}
}
