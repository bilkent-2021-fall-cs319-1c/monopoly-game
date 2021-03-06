package monopoly.ui.controller.gameplay;

import java.awt.Dimension;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.WebcamListener;

import lombok.Getter;
import monopoly.network.Client;
import monopoly.network.packet.realtime.BufferedImagePacket;

/**
 * Can continuously send the webcam screenshots over the network.
 * 
 * @author Ziya Mukhtarov
 * @version Nov 18, 2020
 */
public class WebcamSender {
	/**
	 * The FPS limit of webcam screenshots
	 */
	public static final int FPS = 15;

	/**
	 * The resolution of webcam captures
	 */
	public static final Dimension DEFAULT_RESOLUTION = new Dimension(BufferedImagePacket.IMAGE_WIDTH,
			BufferedImagePacket.IMAGE_HEIGHT);

	@Getter
	private Webcam webcam;

	/**
	 * Creates a new WebcamSender using the given webcam. Initially, it is not
	 * sending any images.
	 * 
	 * @param client The client through which to send the images
	 * @param webcam The webcam to use
	 */
	public WebcamSender(Client client, Webcam webcam) {
		this.webcam = webcam;
		webcam.setCustomViewSizes(DEFAULT_RESOLUTION);
		webcam.setViewSize(DEFAULT_RESOLUTION);
		stop();

		webcam.addWebcamListener(new WebcamListener() {

			@Override
			public void webcamOpen(WebcamEvent we) {
				// Do nothing
			}

			@Override
			public void webcamImageObtained(WebcamEvent we) {
				client.sendRealTimePacket(new BufferedImagePacket(we.getImage(), client.getConnectionID()));
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
		try {
			webcam.open(true,
					(long snapshotDuration, double deviceFps) -> (long) Math.max(1000.0 / FPS - snapshotDuration, 0));
		} catch (WebcamException e) {
			e.printStackTrace();
			// TODO show error
		}
	}

	/**
	 * Stop sending webcam images. Can be resumed later using start()
	 */
	public void stop() {
		webcam.close();
	}

}
