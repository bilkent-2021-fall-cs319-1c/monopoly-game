package tr.com.bilkent.monopoly.network.demo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.sound.sampled.LineUnavailableException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.com.bilkent.monopoly.network.SerializationUtil;
import tr.com.bilkent.monopoly.network.client.Client;
import tr.com.bilkent.monopoly.network.packet.BufferedImagePacket;
import tr.com.bilkent.monopoly.network.packet.MicSoundPacket;
import tr.com.bilkent.monopoly.network.sender.MicSender;
import tr.com.bilkent.monopoly.network.sender.WebcamSender;

/**
 * Simple demo of the video/voice chat client
 * 
 * @author Ziya Mukhtarov
 * @version Nov 14, 2020
 */
public class ClientDemo {
	private static Logger logger = LoggerFactory.getLogger(ClientDemo.class);

	public static void main(String[] args) throws IOException, LineUnavailableException {
		Map<String, OneClientView> clientViews = new HashMap<>();

		String serverIp = "localhost";
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Server ip: ");
			serverIp = scanner.next();
			logger.info("Entered ip: {}", serverIp);

			Client client = new Client(serverIp) {
				@Override
				public void messageReceived(String msg) {
					logger.info("Incoming message: {}", msg);
				}

				@Override
				public void byteArrayReceivedUdp(byte[] data) {
					Object obj;
					try {
						obj = SerializationUtil.deserialize(data);
					} catch (ClassNotFoundException e) {
						logger.error("Invalid Object Data Received", e);
						return;
					}
					if (obj instanceof BufferedImagePacket) {
						BufferedImagePacket screenshot = (BufferedImagePacket) obj;
						String ip = screenshot.getSourceAddress();
						logger.debug("Webcam Received from {};\t Size is {}", ip, data.length);

						if (!clientViews.containsKey(ip)) {
							clientViews.put(ip, new OneClientView(ip));
						}
						clientViews.get(ip).queueImage(screenshot);

					} else if (obj instanceof MicSoundPacket) {
						MicSoundPacket micSoundPacket = (MicSoundPacket) obj;
						String ip = micSoundPacket.getSourceAddress();
						logger.debug("Mic Received from {};\t Size is {}", ip, micSoundPacket.getData().length);

						if (!clientViews.containsKey(ip)) {
							clientViews.put(ip, new OneClientView(ip));
						}
						clientViews.get(ip).queueAudio(micSoundPacket);
					}
				}
			};

			MicSender micSender = new MicSender(client);
			micSender.start();

			WebcamSender webcamSender = new WebcamSender(client);
			webcamSender.start();

			while (scanner.hasNext()) {
				client.sendMessage(scanner.nextLine());
			}

			webcamSender.stop();
		}
	}

}
