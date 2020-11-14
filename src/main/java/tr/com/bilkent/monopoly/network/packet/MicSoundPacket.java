package tr.com.bilkent.monopoly.network.packet;

import java.io.Serializable;

import javax.sound.sampled.AudioFormat;

import lombok.Getter;

/**
 * A network packet that contains microphone audio
 * 
 * @author Ziya Mukhtarov
 * @version Nov 14, 2020
 */
@Getter
public class MicSoundPacket extends NetworkPacket implements Serializable {
	private static final long serialVersionUID = 650861908884172142L;
	public static final AudioFormat AUDIO_FORMAT = new AudioFormat(11025f, 16, 1, true, true);
	public static final int DATA_LENGTH = 1024;

	private byte[] data;

	public MicSoundPacket(byte[] data, String ipAddress) {
		super(ipAddress);
		this.data = data;
	}
}
