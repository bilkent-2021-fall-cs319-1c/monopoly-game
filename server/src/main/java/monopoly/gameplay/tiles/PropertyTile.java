package monopoly.gameplay.tiles;

import lombok.Getter;
import lombok.Setter;
import monopoly.gameplay.Game;
import monopoly.gameplay.GamePlayer;
import monopoly.gameplay.properties.Property;
import monopoly.gameplay.properties.TitleDeedData;
import monopoly.network.packet.important.packet_data.gameplay.property.TilePacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.TileType;

/**
 * Tile with property
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */
@Getter
@Setter
public class PropertyTile extends Tile {

	private Property property;

	public PropertyTile(TitleDeedData titleDeed, String name, String description, TileType type, int index,
			Property property) {
		super(titleDeed, name, description, type, index);
		this.property = property;
		property.setTile(this);
		property.setTitleDeed(titleDeed);
	}

	@Override
	public void doAction(GamePlayer player) {
		Game game = player.getCurrentGame();
		GamePlayer owner = property.getOwner();
		int balance = player.getBalance();
		int rent = property.getRentCost();

		if (!player.equals(owner)) {
			if (balance >= rent) {
				player.setBalance(balance - rent);
				game.sendBalanceChangeToPlayers(player);

				owner.setBalance(owner.getBalance() + rent);
				game.sendBalanceChangeToPlayers(owner);
			} else {
				game.bankrupt(player);
			}

		}
	}

	@Override
	public TilePacketData getAsTilePacket() {
		return new TilePacketData(getTitleDeed().getAsTitleDeedPacket(), getName(), getDescription(), getType(),
				getIndex());
	}
}
