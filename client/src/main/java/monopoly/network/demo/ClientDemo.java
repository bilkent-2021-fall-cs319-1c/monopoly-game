package monopoly.network.demo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sarxos.webcam.Webcam;

import monopoly.network.Client;
import monopoly.network.ServerInfo;
import monopoly.network.packet.important.ImportantNetworkPacket;
import monopoly.network.packet.realtime.BufferedImagePacket;
import monopoly.network.packet.realtime.MicSoundPacket;
import monopoly.network.packet.realtime.RealTimeNetworkPacket;
import monopoly.network.sender.MicSender;
import monopoly.network.sender.WebcamSender;

/**
 * Simple demo of the video/voice chat client
 * 
 * @author Ziya Mukhtarov
 * @version Nov 18, 2020
 */
public class ClientDemo {
	private static Logger logger = LoggerFactory.getLogger(ClientDemo.class);
	private static Map<Integer, OneClientView> clientViews = new HashMap<>();

	private static OneClientView getClientView(int connectionID) {
		if (!clientViews.containsKey(connectionID)) {
			clientViews.put(connectionID, new OneClientView(connectionID));
		}
		return clientViews.get(connectionID);
	}

	private static void startSending(Client client, Scanner scanner) {
		MicSender micSender;
		try {
			micSender = new MicSender(client);
			micSender.start();
		} catch (LineUnavailableException e1) {
			logger.error("Unable to access a microphone. Will not send microphone input");
		}

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// Default Look And Feel
		}
		JFrame webcamSelectorFrame = new JFrame();
		webcamSelectorFrame.setVisible(true);
		Webcam webcam = (Webcam) JOptionPane.showInputDialog(webcamSelectorFrame, "Select your webcam device:",
				"Webcam Selector", JOptionPane.PLAIN_MESSAGE, null, Webcam.getWebcams().toArray(), null);
		webcamSelectorFrame.setVisible(false);

		WebcamSender webcamSender = new WebcamSender(client, webcam);
		webcamSender.start();

		while (scanner.hasNext()) {
			scanner.nextLine();
		}

		webcamSender.stop();
	}

	public static void main(String[] args) throws IOException {
		try (Scanner scanner = new Scanner(System.in)) {
			new Client(ServerInfo.IP) {
				@Override
				public void connected(int connectionID) {
					logger.info("Connected to Server with id: {}", connectionID);
					new Thread(() -> startSending(this, scanner)).start();
				}

				@Override
				public void disconnected(int connectionID) {
					logger.info("Disconnected from Server with id: {}", connectionID);
				}

				@Override
				public void receivedRealTimePacket(int connectionID, RealTimeNetworkPacket packet) {
					if (packet instanceof BufferedImagePacket) {
						BufferedImagePacket screenshot = (BufferedImagePacket) packet;
						logger.debug("Image Received from id: {}", packet.getSourceConnectionID());
						getClientView(packet.getSourceConnectionID()).queueImage(screenshot);

					} else if (packet instanceof MicSoundPacket) {
						MicSoundPacket micSoundPacket = (MicSoundPacket) packet;
						logger.debug("Mic Received from id: {}", packet.getSourceConnectionID());
						getClientView(packet.getSourceConnectionID()).queueAudio(micSoundPacket);
					}
				}

				@Override
				public void receivedImportantPacket(int connectionID, ImportantNetworkPacket packet) {
					logger.warn("Received important packet {} from id {}", packet, connectionID);
				}
			};

			while (!Thread.interrupted()) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

}
