package monopoly;

import java.io.IOException;

/**
 * A test runner for game server
 * 
 * @author Javid Baghirov
 * @version Nov 29, 2020
 */
public class Runner {
	public static void main(String[] args) {
		try {
			new Model();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
