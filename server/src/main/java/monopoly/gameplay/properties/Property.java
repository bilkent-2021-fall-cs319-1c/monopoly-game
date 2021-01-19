package monopoly.gameplay.properties;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import monopoly.MonopolyException;
import monopoly.gameplay.Auctionable;
import monopoly.gameplay.GamePlayer;
import monopoly.gameplay.Tradeable;
import monopoly.gameplay.tiles.PropertyTile;
import monopoly.network.packet.important.PacketType;
import monopoly.network.packet.important.packet_data.gameplay.property.PropertyPacketData;

/**
 * Property class that takes a deed data class and has an owner
 *
 * @author Alper Sari, Ziya Mukhtarov
 * @version Jan 17, 2021
 */
@Getter
public abstract class Property implements Auctionable, Tradeable {
	private final String title;
	private final int buyCost;
	private final int mortgageCost;
	private final List<Integer> rentPrices;
	private final ColorSet colorSet;

	@Setter
	private PropertyTile tile;
	private GamePlayer owner;
	private boolean mortgaged;

	public Property(String title, int buyCost, int mortgageCost, List<Integer> rentPrices, ColorSet colorSet) {
		this.title = title;
		this.buyCost = buyCost;
		this.mortgageCost = mortgageCost;
		this.rentPrices = rentPrices;
		this.colorSet = colorSet;

		colorSet.registerProperty(this);
	}

	public void mortgage() throws MonopolyException {
		if (mortgaged) {
			throw new MonopolyException(PacketType.ERR_MORTGAGED);
		}

		owner.changeBalance(mortgageCost);
		mortgaged = true;
	}

	public void unmortgage() throws MonopolyException {
		if (!mortgaged) {
			throw new MonopolyException(PacketType.ERR_NOT_MORTGAGED);
		}

		owner.changeBalance((int) (-mortgageCost * 1.1));
		mortgaged = false;
	}

	public abstract int getRentCost();

	public boolean isOwned() {
		return owner != null;
	}

	public void setOwner(GamePlayer owner) {
		this.owner = owner;
		owner.getGame().sendPropertyBoughtToPlayers(tile, owner);
	}

	public int getRentTierPrice(int tier) {
		return rentPrices.get(tier);
	}

	public PropertyPacketData getAsPacket() {
		return new PropertyPacketData(tile.getAsPacket(), owner.getAsPlayerPacket(), mortgaged);
	}
}
