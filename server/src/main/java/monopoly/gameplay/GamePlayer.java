package monopoly.gameplay;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import monopoly.MonopolyException;
import monopoly.lobby.User;
import monopoly.network.packet.important.packet_data.gameplay.DicePacketData;
import monopoly.network.packet.important.packet_data.gameplay.PlayerPacketData;

/**
 * A game player
 * 
 * @author Javid Baghirov
 * @version Dec 13, 2020
 */

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class GamePlayer extends User {
	private static final int START_BALANCE = 1500;
	
	private int balance;
	private int tileIndex;
	private Tile tile;
	
	//private Game game;

	public GamePlayer(User user) {
		super(user);
		user.setGamePlayerInstance(this);
		setGamePlayerInstance( this);

		balance = START_BALANCE;
		//this.game = game;
	}
	
	public DicePacketData rollDice() throws MonopolyException {
		return getLobby().getGame().rollDice( this);
	}
	
	public PlayerPacketData getPlayerAsPacket() {
		return new PlayerPacketData( getId(), getUsername(), balance);
	}
}
