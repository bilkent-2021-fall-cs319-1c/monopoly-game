package monopoly.gameplay.properties;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import monopoly.gameplay.GamePlayer;
import monopoly.gameplay.tiles.PropertyTile;
import monopoly.network.packet.important.packet_data.gameplay.property.PropertyPacketData;

/**
 * Property class that takes a deed data class and has an owner
 *
 * @author Alper Sari
 * @version Dec 16, 2020
 */
@Getter
@RequiredArgsConstructor
public abstract class Property implements Auctionable, Tradeable {
	private final String title;
	private final int buyCost;
	private final int mortgageCost;
	private final List<Integer> rentPrices;
	private final String colorSet;

	@Setter
	private PropertyTile tile;

	@Setter
	private GamePlayer owner;
	private boolean mortgaged;

	public void mortgage() {
		mortgaged = !mortgaged;
	}

	public abstract int getRentCost();

	public boolean isOwned() {
		return owner != null;
	}

	@Override
	public void give(GamePlayer player) {
		owner = player;
		player.getGame().sendPropertyBoughtToPlayers(tile, player);
	}

	@Override
	public void trade(GamePlayer from, GamePlayer to) {
		if (from.equals(owner))
			give(to);
	}

	public int getRentTierPrice(int tier) {
		return rentPrices.get(tier);
	}

	public PropertyPacketData getAsPacket() {
		return new PropertyPacketData(tile.getAsPacket(), owner.getAsPlayerPacket(), mortgaged);
	}
}
