package monopoly.common.network.packet;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;

import lombok.Getter;

/**
 * A network packet that contains a BufferedImage
 * 
 * @author Ziya Mukhtarov
 * @version Nov 18, 2020
 */
@Getter
public class BufferedImagePacket extends NetworkPacket {
	private static final long serialVersionUID = 650861908884172142L;

	private transient BufferedImage img;

	/**
	 * Only used for deserialization. Should not be used anywhere else.
	 */
	private BufferedImagePacket() {
		super(-10); // Random negative number
		img = null;
	}

	public BufferedImagePacket(BufferedImage img, int sourceConnectionID) {
		super(sourceConnectionID);
		this.img = img;
	}

	/**
	 * Custom Serialization
	 */
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		ImageIO.write(img, "jpg", out);
	}

	/**
	 * Custom Deserialization
	 */
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		img = ImageIO.read(in);
	}

	/**
	 * Returns a scaled version of the stored image.
	 * 
	 * @param width  The width of the resulting image
	 * @param height The height of the resulting image
	 * @return The scaled image
	 */
	public BufferedImage getScaledImage(int width, int height) {
		Image result = img.getScaledInstance(width, height, Image.SCALE_FAST);
		BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		Graphics2D g2d = newImg.createGraphics();
		g2d.drawImage(result, 0, 0, null);
		g2d.dispose();

		return newImg;
	}
}