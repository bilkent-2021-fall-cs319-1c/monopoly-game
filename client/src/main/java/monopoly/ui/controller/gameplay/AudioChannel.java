package monopoly.ui.controller.gameplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import monopoly.network.packet.realtime.MicSoundPacket;

/**
 * An audio channel to play microphone sound packet to the default speaker.
 * Queues audio and plays them in the given order.
 * 
 * @author Ziya Mukhtarov
 * @version Nov 14, 2020
 */
public class AudioChannel {
	private List<MicSoundPacket> audioQueue;
	private SourceDataLine speaker;

	/**
	 * Create a new audio channel
	 * 
	 * @throws LineUnavailableException If there is no available speaker
	 */
	public AudioChannel() throws LineUnavailableException {
		audioQueue = Collections.synchronizedList(new ArrayList<MicSoundPacket>());

		DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, MicSoundPacket.AUDIO_FORMAT);
		speaker = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
		speaker.open(MicSoundPacket.AUDIO_FORMAT);
		speaker.start();
	}

	/**
	 * Queues a microphone sound packet to be played. If the queue is empty, it will
	 * be played immediately.
	 * 
	 * @param audio The sound packet to queue
	 */
	public void addToQueue(MicSoundPacket audio) {
		audioQueue.add(audio);

		playAudioQueue();
	}

	private synchronized void playAudioQueue() {
		while (!audioQueue.isEmpty()) {
			MicSoundPacket soundPacket = audioQueue.get(0);
			audioQueue.remove(0);

			speaker.write(soundPacket.getData(), 0, soundPacket.getData().length);
		}
	}
}
