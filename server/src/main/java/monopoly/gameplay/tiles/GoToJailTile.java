package monopoly.gameplay.tiles;

import monopoly.gameplay.GamePlayer;
import monopoly.gameplay.properties.TitleDeedData;
import monopoly.network.packet.important.packet_data.gameplay.property.TileType;

/**
 * Tile that sends player to jail
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */
public class GoToJailTile extends Tile {
	public GoToJailTile(TitleDeedData titleDeed, String name, String description, TileType type, int index) {
		super(titleDeed, name, description, type, index);
	}

	@Override
	public void doAction(GamePlayer player) {
		player.goToJail();
	}
}
