package monopoly.gameplay;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import lombok.Getter;
import monopoly.network.GameServer;
import monopoly.network.packet.important.packet_data.gameplay.DicePacketData;

/**
 * Dice with a roll functionality
 * 
 * @author Javid Baghirov
 * @version Dec 12, 2020
 */
public class Dice {
	@Getter
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

	public void roll() {
		result = firstDie.roll() + secondDie.roll();
	}

	public DicePacketData getDiceAsPacket() {
		return new DicePacketData(getFirstDieValue(), getSecondDieValue());
	}

	public int getFirstDieValue() {
		return firstDie.getValue();
	}

	public int getSecondDieValue() {
		return secondDie.getValue();
	}

	public void sendDiceResultToPlayers(List<GamePlayer> players) {
		players.parallelStream()
				.forEach(player -> GameServer.getInstance().sendDiceRollNotification(player, getDiceAsPacket()));
	}
}
