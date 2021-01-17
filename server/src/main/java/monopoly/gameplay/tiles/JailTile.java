package monopoly.gameplay.tiles;

import monopoly.gameplay.GamePlayer;
import monopoly.network.packet.important.packet_data.gameplay.property.TileType;

/**
 * The tile where the jail is
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */
public class JailTile extends Tile {
	public JailTile(String name, String description, TileType type, int index) {
		super(name, description, type, index);
	}

	@Override
	public void doAction(GamePlayer player) {
		// TODO proper implementation
		player.changeBalance(200);
	}
}
