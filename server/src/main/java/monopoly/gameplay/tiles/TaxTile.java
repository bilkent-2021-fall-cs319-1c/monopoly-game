package monopoly.gameplay.tiles;

import lombok.Getter;
import monopoly.gameplay.GamePlayer;
import monopoly.network.packet.important.packet_data.gameplay.property.TileType;

/**
 * Tile which takes taxes from the player
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */
@Getter
public class TaxTile extends Tile {
	private int amount;

	public TaxTile(String name, String description, TileType type, int index, int amount) {
		super(name, description, type, index);
		this.amount = amount;
	}

	@Override
	public void doAction(GamePlayer player) {
		player.changeBalance(-amount);
	}
}
