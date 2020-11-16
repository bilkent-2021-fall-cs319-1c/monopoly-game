package tr.com.bilkent.monopoly.network.demo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Socket;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.com.bilkent.monopoly.network.SerializationUtil;
import tr.com.bilkent.monopoly.network.packet.BufferedImagePacket;
import tr.com.bilkent.monopoly.network.packet.MicSoundPacket;
import tr.com.bilkent.monopoly.network.server.Server;

/**
 * A demo server for video/audio chat.
 * 
 * @author Ziya Mukhtarov
 * @version Nov 14, 2020
 */
public class ServerDemo {
	private static Logger logger = LoggerFactory.getLogger(ServerDemo.class);

	public static void main(String[] args) throws IOException {

		Server server = new Server() {

			@Override
			public void messageReceived(String msg, Socket socket) {
				logger.info("Message from {}: {}", socket.getInetAddress(), msg);
			}

			@Override
			public void connectionTerminated(Socket socket) {
				logger.info("Disconnected: {}", socket.getInetAddress());
			}

			@Override
			public void connectionEstablished(Socket socket) {
				String ip = socket.getInetAddress().toString();
				logger.info("New connection: {}", ip);
			}

			@Override
			public void byteArrayReceivedUdp(byte[] data, DatagramPacket packet) {
				Object obj;
				try {
					obj = SerializationUtil.deserialize(data);
				} catch (ClassNotFoundException e) {
					logger.error("Invalid Object Data Received", e);
					return;
				}

				if (obj instanceof BufferedImagePacket) {
					logger.info("Received image from {}    Size: {}", packet.getAddress(), data.length);
					sendByteArrayToAll(data);
				} else if (obj instanceof MicSoundPacket) {
					logger.info("Received sound from {}    Size: {}", packet.getAddress(), data.length);
					sendByteArrayToAllExcept(data, packet.getAddress());
				}
			}
		};

		try (Scanner scanner = new Scanner(System.in)) {
			while (scanner.hasNext()) {
				server.sendMessageToAll(scanner.nextLine());
			}
		}
	}
}
