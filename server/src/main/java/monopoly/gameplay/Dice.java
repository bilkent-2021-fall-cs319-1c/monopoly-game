package monopoly.gameplay;

import java.security.NoSuchAlgorithmException;

import lombok.Getter;
import lombok.Setter;

/**
 * Dice with a roll functionality
 * 
 * @author Javid Baghirov
 * @version Dec 12, 2020
 */
public class Dice {
	@Getter
	@Setter
	private int result;

	private Die firstDie;
	private Die secondDie;

	/**
	 * Creates a dice object
	 * 
	 * @throws NoSuchAlgorithmException when the requested algorithm is not
	 *                                  available
	 */
	public Dice() {
		firstDie = new Die();
		secondDie = new Die();
	}

	public int roll() {
		result = firstDie.roll() + secondDie.roll();

		return result;
	}

	public DiceData getDiceData() {
		return new DiceData(getFirstDieValue(), getSecondDieValue());
	}

	public int getFirstDieValue() {
		return firstDie.getValue();
	}

	public int getSecondDieValue() {
		return secondDie.getValue();
	}
}
