package monopoly.gameplay.tiles;

import lombok.Getter;
import lombok.Setter;
import monopoly.gameplay.GamePlayer;
import monopoly.gameplay.properties.TitleDeedData;
import monopoly.network.packet.important.packet_data.gameplay.property.TileType;

/**
 * Tile which takes taxes from the player
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */
@Getter
@Setter
public class TaxTile extends Tile {

	private int amount;

	public TaxTile(TitleDeedData titleDeed, String name, String description, TileType type, int index, int amount) {
		super(titleDeed, name, description, type, index);
		this.amount = amount;
	}

	@Override
	public void doAction(GamePlayer player) {
		player.setBalance(player.getBalance() - amount);
		player.getCurrentGame().sendBalanceChangeToPlayers(player);
	}
}
