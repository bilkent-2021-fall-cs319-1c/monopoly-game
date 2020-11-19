package monopoly.network.sender;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Port;
import javax.sound.sampled.TargetDataLine;

import monopoly.common.network.packet.MicSoundPacket;
import monopoly.network.Client;

/**
 * Can send the default microphone input over the network. Can be
 * paused/resumed.
 * 
 * @author Ziya Mukhtarov
 * @version Nov 18, 2020
 */
public class MicSender {

	private int sourceConnectionID;

	private TargetDataLine microphone;
	private volatile boolean open;

	/**
	 * Create a new microphone sender. Initially, it is paused.
	 * 
	 * @param client The client through which to send the input
	 * @throws LineUnavailableException If a microphone could not be opened
	 */
	public MicSender(Client client) throws LineUnavailableException {
		sourceConnectionID = client.getConnectionID();

		if (AudioSystem.isLineSupported(Port.Info.MICROPHONE)) {
			DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, MicSoundPacket.AUDIO_FORMAT);
			microphone = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			microphone.open(MicSoundPacket.AUDIO_FORMAT);
			stop();

			new MicSenderThread(client).start();
		} else {
			throw new LineUnavailableException();
		}
	}

	/**
	 * A thread that continuously checks whether the MicSender is open (not paused)
	 * and sends the input
	 * 
	 * @author Ziya Mukhtarov
	 * @version Nov 14, 2020
	 */
	private class MicSenderThread extends Thread {
		private Client client;

		public MicSenderThread(Client client) {
			this.client = client;
		}

		@Override
		public void run() {
			while (true) {
				if (!open) {
					try {
						// Sleeping for optimization
						Thread.sleep(250);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						e.printStackTrace();
					}
					continue;
				}

				byte[] micData = new byte[MicSoundPacket.DATA_LENGTH];
				microphone.read(micData, 0, MicSoundPacket.DATA_LENGTH);

				client.sendPacket(new MicSoundPacket(micData, sourceConnectionID));
			}
		}
	}

	/**
	 * Starts sending the microphone input
	 */
	public void start() {
		if (open) {
			return;
		}
		microphone.start();
		open = true;
	}

	/**
	 * Stops sending the microphone input. Can be resumed later using start()
	 */
	public void stop() {
		if (!open) {
			return;
		}
		open = false;
		microphone.stop();
		microphone.flush();
	}

}
