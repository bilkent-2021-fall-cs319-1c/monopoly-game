package tr.com.bilkent.monopoly.network.sender;

import java.io.IOException;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamResolution;

import tr.com.bilkent.monopoly.network.SerializationUtil;
import tr.com.bilkent.monopoly.network.client.Client;
import tr.com.bilkent.monopoly.network.packet.BufferedImagePacket;

/**
 * Can continuously send the webcam screenshots over the network.
 * 
 * @author Ziya Mukhtarov
 * @version Nov 14, 2020
 */
public class WebcamSender {
	/**
	 * The FPS limit of webcam screenshots
	 */
	public static final int FPS = 10;

	/**
	 * The resolution of webcam captures
	 */
	public static final WebcamResolution DEFAULT_RESOLUTION = WebcamResolution.QVGA;

	private Webcam webcam;

	/**
	 * Creates a new WebcamSender that uses default webcam. Initially, it is not
	 * sending any images.
	 * 
	 * @param client The client through which to send the images
	 */
	public WebcamSender(Client client) {
		webcam = Webcam.getDefault();
		webcam.setCustomViewSizes(DEFAULT_RESOLUTION.getSize());
		webcam.setViewSize(DEFAULT_RESOLUTION.getSize());
		stop();

		webcam.addWebcamListener(new WebcamListener() {

			@Override
			public void webcamOpen(WebcamEvent we) {
				// Do nothing
			}

			@Override
			public void webcamImageObtained(WebcamEvent we) {
				byte[] data = SerializationUtil
						.serialize(new BufferedImagePacket(we.getImage(), client.getLocalAddress()));
				try {
					client.sendByteArrayUdp(data);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void webcamDisposed(WebcamEvent we) {
				// Do nothing
			}

			@Override
			public void webcamClosed(WebcamEvent we) {
				// Do nothing
			}
		});
	}

	/**
	 * Start sending webcam images
	 */
	public void start() {
		webcam.open(true,
				(long snapshotDuration, double deviceFps) -> (long) Math.max(1000.0 / FPS - snapshotDuration, 0));
	}

	/**
	 * Stop sending webcam images. Can be resumed later using start()
	 */
	public void stop() {
		webcam.close();
	}

}
