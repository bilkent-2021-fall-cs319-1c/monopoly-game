package tr.com.bilkent.monopoly.network.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.com.bilkent.monopoly.network.Server;
import tr.com.bilkent.monopoly.network.packet.BufferedImagePacket;
import tr.com.bilkent.monopoly.network.packet.MicSoundPacket;
import tr.com.bilkent.monopoly.network.packet.NetworkPacket;

/**
 * A demo server for video/audio chat.
 * 
 * @author Ziya Mukhtarov
 * @version Nov 18, 2020
 */
public class ServerDemo {
	private static Logger logger = LoggerFactory.getLogger(ServerDemo.class);

	public static void main(String[] args) throws IOException {
		List<Integer> connections = Collections.synchronizedList(new ArrayList<Integer>());

		Server server = new Server() {
			@Override
			public void connected(int connectionID) {
				connections.add(connectionID);
				logger.info("Connected: {}", connectionID);
			}

			@Override
			public void disconnected(int connectionID) {
				logger.info("Disconnected: {}", connectionID);
				connections.remove((Integer) connectionID);
			}

			@Override
			public void receivedPacket(int connectionID, NetworkPacket packet) {
				if (packet instanceof BufferedImagePacket) {
					logger.info("Received image from {}", connectionID);
					connections.parallelStream().forEach(id -> sendPacket(packet, id));
				} else if (packet instanceof MicSoundPacket) {
					logger.info("Received sound from {}", connectionID);
					connections.parallelStream().forEach(id -> {
						if (id != connectionID)
							sendPacket(packet, id);
					});
				}
			}

			@Override
			public void receivedNotPacket(int connectionID, Object object) {
				logger.warn("Received unknown object {} from id {}", object, connectionID);
			}
		};

		try (Scanner scanner = new Scanner(System.in)) {
			while (scanner.hasNext()) {
				String msg = scanner.nextLine();
				connections.parallelStream().forEach(id -> server.sendString(msg, id));
			}
		}
	}
}
