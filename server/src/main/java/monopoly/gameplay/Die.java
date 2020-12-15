package monopoly.gameplay;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;

/**
 * A single die with a roll functionality
 * 
 * @author Javid Baghirov
 * @version Dec 12, 2020
 */
public class Die {
	@Getter
	@Setter
	private int value;
	
	private Random random;
	
	/**
	 * Creates a die object
	 * 
	 * @throws NoSuchAlgorithmException when the requested algorithm is not
	 *                                  available
	 */
	public Die () {
		value = 0;
		try {
			random = SecureRandom.getInstanceStrong();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public int roll() {	
		value = random.nextInt(6) + 1;
		
		return value;
	}
}
