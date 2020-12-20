package monopoly.gameplay.properties;

import lombok.Getter;
import lombok.Setter;
import monopoly.MonopolyException;
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
@Setter
public abstract class Property implements Auctionable, Tradeable {
	private PropertyTile tile;
	private GamePlayer owner;
	private boolean mortgaged;
	private TitleDeedData titleDeed;
	private String colorSet;

	Property(String colorSet) {
		owner = null;
		this.colorSet = colorSet;
		mortgaged = false;
	}

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
	}

	@Override
	public void trade(GamePlayer from, GamePlayer to) {
		if (from.equals(owner))
			give(to);
	}
	
	public void setTile( PropertyTile tile) {
		this.tile = tile;
		tile.setProperty( this);
	}
	
	public void setTitleDeed( TitleDeedData titleDeed) {
		this.titleDeed = titleDeed;
		titleDeed.setProperty( this);
	}
	
	
	
	public PropertyPacketData getAsPropertyPacket() {
		return new PropertyPacketData(tile.getAsTilePacket(), owner.getAsPlayerPacket(), mortgaged);
	}
}
