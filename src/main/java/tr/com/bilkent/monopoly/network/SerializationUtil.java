package tr.com.bilkent.monopoly.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * A utility class to ease serialization/deserialization
 * 
 * @author Ziya Mukhtarov
 * @version Nov 14, 2020
 */
public class SerializationUtil {

	private SerializationUtil() {
	}

	/**
	 * Serialize the object
	 * 
	 * @param obj The object to serialize
	 * @return The byte array containing object data
	 */
	public static byte[] serialize(Serializable obj) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			oos.writeObject(obj);
			oos.flush();
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return new byte[0];
		}
	}

	/**
	 * Serialize the object
	 * 
	 * @param data The byte array containing object data
	 * @return The resulting object
	 * @throws ClassNotFoundException Class of a serialized object cannot be found
	 */
	public static Object deserialize(byte[] data) throws ClassNotFoundException {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
				ObjectInputStream ois = new ObjectInputStream(bais)) {
			return ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
