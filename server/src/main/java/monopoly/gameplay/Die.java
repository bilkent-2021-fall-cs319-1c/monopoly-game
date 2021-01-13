package monopoly.gameplay;

import java.util.Random;

import lombok.Getter;

/**
 * A single die with a roll functionality
 * 
 * @author Javid Baghirov
 * @version Dec 12, 2020
 */
public class Die {
	@Getter
	private int value;
	private Random random;

	/**
	 * Creates a die object
	 */
	public Die() {
		value = 0;
		random = new Random();
	}

	public int roll() {
		value = random.nextInt(6) + 1;
		return value;
	}
}
