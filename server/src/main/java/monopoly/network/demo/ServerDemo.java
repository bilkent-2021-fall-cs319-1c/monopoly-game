package monopoly.network.demo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import monopoly.network.Server;
import monopoly.network.packet.important.ImportantNetworkPacket;
import monopoly.network.packet.important.PacketType;
import monopoly.network.packet.realtime.BufferedImagePacket;
import monopoly.network.packet.realtime.MicSoundPacket;
import monopoly.network.packet.realtime.RealTimeNetworkPacket;

/**
 * A demo server for video/audio chat.
 * 
 * @author Ziya Mukhtarov
 * @version Nov 18, 2020
 */
public class ServerDemo {
	private static Logger logger = LoggerFactory.getLogger(ServerDemo.class);

	public static int getPacketSize(RealTimeNetworkPacket packet) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			oos.writeObject(packet);
			oos.flush();
			return baos.toByteArray().length;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static void main(String[] args) throws IOException {
		List<Integer> connections = Collections.synchronizedList(new ArrayList<Integer>());

		new Server() {
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
			public void receivedRealTimePacket(int connectionID, RealTimeNetworkPacket packet) {
				if (packet instanceof BufferedImagePacket) {
					logger.info("Received image from {}", connectionID);
					connections.parallelStream().forEach(id -> sendRealTimePacket(packet, id));
				} else if (packet instanceof MicSoundPacket) {
					logger.info("Received sound from {}", connectionID);
					connections.parallelStream().forEach(id -> {
						if (id != connectionID)
							sendRealTimePacket(packet, id);
					});
				}
			}

			@Override
			public void receivedImportantPacket(int connectionID, ImportantNetworkPacket packet) {
				logger.warn("Received important packet {} from id {}", packet, connectionID);
				sendImportantPacket(new ImportantNetworkPacket(PacketType.ACCEPTED), connectionID);
				sendImportantPacket(new ImportantNetworkPacket(PacketType.ERR_UNKNOWN), connectionID);
			}
		};

		try (Scanner scanner = new Scanner(System.in)) {
			while (scanner.hasNext()) {
				scanner.nextLine();
			}
		}
	}
}
