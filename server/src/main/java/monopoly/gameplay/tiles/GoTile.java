package monopoly.gameplay.tiles;

import lombok.Getter;
import monopoly.gameplay.GamePlayer;
import monopoly.network.packet.important.packet_data.gameplay.property.TileType;

/**
 * GO tile
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */
@Getter
public class GoTile extends Tile {
	private static final int MONEY_GAIN = 200;

	public GoTile(String name, String description, TileType type, int index) {
		super(name, description, type, index);
	}

	@Override
	public void doAction(GamePlayer player) {
		player.setBalance(player.getBalance() + MONEY_GAIN);
	}
}
