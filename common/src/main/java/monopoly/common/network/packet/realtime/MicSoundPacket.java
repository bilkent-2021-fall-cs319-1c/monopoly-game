package monopoly.common.network.packet.realtime;

import javax.sound.sampled.AudioFormat;

import lombok.Getter;

/**
 * A network packet that contains microphone audio
 * 
 * @author Ziya Mukhtarov
 * @version Nov 18, 2020
 */
@Getter
public class MicSoundPacket extends RealTimeNetworkPacket {
	private static final long serialVersionUID = 650861908884172142L;
	public static final AudioFormat AUDIO_FORMAT = new AudioFormat(11025f, 16, 1, true, true);
	public static final int DATA_LENGTH = 1024;

	private byte[] data;

	/**
	 * Only used for deserialization. Should not be used anywhere else.
	 */
	private MicSoundPacket() {
		super(-10); // Random negative number
		data = new byte[0];
	}

	public MicSoundPacket(byte[] data, int sourceConnectionID) {
		super(sourceConnectionID);
		this.data = data;
	}
}
