package monopoly.gameplay.tiles;

import monopoly.gameplay.GamePlayer;
import monopoly.network.packet.important.packet_data.gameplay.property.TileType;

/**
 * Card tile
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */
public class CardTile extends Tile {
	public CardTile(String name, String description, TileType type, int index) {
		super(name, description, type, index);
	}

	@Override
	public void doAction(GamePlayer player) {
		// TODO proper implementation
		player.changeBalance(200);
	}
}
