package tr.com.bilkent.monopoly.network.demo;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import tr.com.bilkent.monopoly.network.packet.BufferedImagePacket;
import tr.com.bilkent.monopoly.network.packet.MicSoundPacket;

/**
 * A demo Swing GUI for a single client. Can play its audio as well. Displays
 * video FPS in the title
 * 
 * @author Ziya Mukhtarov
 * @version Nov 14, 2020
 */
public class OneClientView {
	/**
	 * Custom delay to be added to the webcam view for audio - video synchronization
	 */
	public static final int WEBCAM_DELAY_MS = 500;

	private JFrame frm;
	private JPanel pnl;
	private JLabel lbl;
	private double fps;
	private long lastFpsUpdate;
	private long previousFrameTime;
	private String ip;

	private AudioChannel audioChannel;

	/**
	 * Create a new GUI and make it visible
	 * 
	 * @param ip - The IP of the client. Will be the window's title
	 */
	public OneClientView(String ip) {
		this.ip = ip;
		lbl = new JLabel();

		pnl = new JPanel();
		pnl.add(lbl);

		frm = new JFrame(ip);
		frm.add(pnl);
		frm.setResizable(true);
		frm.pack();
		frm.setSize(640, 480);
		frm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frm.setVisible(true);

		fps = 0;
		lastFpsUpdate = System.currentTimeMillis();
		previousFrameTime = System.currentTimeMillis();

		try {
			audioChannel = new AudioChannel();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Queues the image to be displayed after WEBCAM_DELAY_MS
	 * 
	 * @param image - The image to queue
	 */
	public void queueImage(BufferedImagePacket image) {
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(WEBCAM_DELAY_MS);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					e.printStackTrace();
				}
				updateImage(image);
			}
		}.start();
	}

	/**
	 * Updates the current image on GUI
	 * 
	 * @param image - The image to display
	 */
	private synchronized void updateImage(BufferedImagePacket image) {
		lbl.setIcon(new ImageIcon(image.getScaledImage(pnl.getWidth(), pnl.getHeight())));

		long time = System.currentTimeMillis();
		fps = 1000.0 / (time - previousFrameTime);
		if ((time - lastFpsUpdate) >= 1000) {
			frm.setTitle(ip + " --- FPS: " + fps);
			lastFpsUpdate = time;
		}
		previousFrameTime = time;
	}

	/**
	 * Queues the given microphone audio to be played. If the queue is empty, audio
	 * is immediately played
	 * 
	 * @param audio The microphone audio to queue
	 */
	public void queueAudio(MicSoundPacket audio) {
		audioChannel.addToQueue(audio);
	}
}
