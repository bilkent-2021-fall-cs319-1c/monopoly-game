package monopoly.gameplay;

import lombok.Getter;
import lombok.Setter;
import monopoly.MonopolyException;
import monopoly.gameplay.tiles.PropertyTile;
import monopoly.network.packet.important.PacketType;

import java.util.ArrayList;

/**
 * A property which can have have an amount of hotels and houses on it
 *
 * @author Alper Sari
 * @version Dec 16, 2020
 */
@Getter
@Setter
public class Street extends Property {

	private int houseCount;
	private int hotelCount;

	public Street(PropertyTile tile, StreetTitleDeedData titleDeed, ColorSet colorSet) {
		super(tile, titleDeed, colorSet);
		houseCount = 0;
		hotelCount = 0;
	}

	@Override
	public int getRentCost() throws MonopolyException {
		if (!isOwned()) {
			return 0;
		} 
		
		//TODO Check color set
		
		if (houseCount > 0) {
			return getTitleDeed().getRentTierPrice( houseCount + 1);
		}
		
		if (hotelCount > 0) {
			return getTitleDeed().getRentTierPrice( 6);
		}
		
		return getTitleDeed().getRentTierPrice( 0);
		
		
	}

	public boolean buildHouse() {
		if (houseCount < 5) {
			houseCount++;
			getOwner().setBalance(getOwner().getBalance() - ((StreetTitleDeedData) getTitleDeed()).getHouseCost());
			return true;
		} else
			return false;
	}

	public boolean buildHotel() {
		if (houseCount == 4 && hotelCount == 0) {
			hotelCount++;
			getOwner().setBalance(getOwner().getBalance() - ((StreetTitleDeedData) getTitleDeed()).getHotelCost());
			return true;
		} else
			return false;
	}
}
