package monopoly.gameplay;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import monopoly.MonopolyException;
import monopoly.lobby.User;
import monopoly.network.GameServer;
import monopoly.network.packet.important.packet_data.gameplay.DicePacketData;
import monopoly.network.packet.important.packet_data.gameplay.PlayerPacketData;
import monopoly.network.packet.important.packet_data.gameplay.TilePacketData;

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

	// private Game game;

	public GamePlayer(User user) {
		super(user);
		user.setGamePlayerInstance(this);
		setGamePlayerInstance(this);

		balance = START_BALANCE;
		tileIndex = 0;
		updateTile();
		// this.game = game;
	}

	public DicePacketData rollDice() throws MonopolyException {
		return getLobby().getGame().rollDice(this);
	}

	public TilePacketData move() {
		try {
			getLobby().getGame().move(this);
		} catch (MonopolyException e) {
			GameServer.getInstance().sendImportantPacket( e.getAsPacket(), getId());
		}

		return new TilePacketData(tile.getName(), tile.getDescription(), "", "", tileIndex);
	}

	public void updateTile() {
		Board board = getLobby().getGame().getBoard();

		tile = board.getTiles().get(tileIndex);
	}

	public PlayerPacketData getPlayerAsPacket() {
		return new PlayerPacketData(getId(), getUsername(), balance);
	}
}
