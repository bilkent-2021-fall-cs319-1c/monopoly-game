package monopoly.gameplay.properties;

import lombok.Getter;
import monopoly.MonopolyException;
import monopoly.network.packet.important.PacketType;

/**
 * A property which can have have an amount of hotels and houses on it
 *
 * @author Alper Sari
 * @version Dec 16, 2020
 */
@Getter
public class StreetProperty extends Property {
	private int houseCount;
	private boolean hasHotel;

	public StreetProperty(String color) {
		super(color);
		houseCount = 0;
		hasHotel = false;
	}

	@Override
	public int getRentCost() {
		if (!isOwned()) {
			return 0;
		}

		// TODO implement rent tier 1 (color set tier)
//		int propertyCountWithColor = getOwner().getPropertyCountWithColor(getColorSet());

		if (houseCount > 0) {
			return getTitleDeed().getRentTierPrice(houseCount + 1);
		}

		if (hasHotel) {
			return getTitleDeed().getRentTierPrice(6);
		}

		return getTitleDeed().getRentTierPrice(0);
	}

	public void buildHouse() throws MonopolyException {
		if (houseCount < 4) {
			houseCount++;
		} else
			throw new MonopolyException(PacketType.ERR_UNKNOWN);
	}

	public void buildHotel() throws MonopolyException {
		if (houseCount == 4) {
			if (!hasHotel) {
				hasHotel = true;
			} else {
				throw new MonopolyException(PacketType.ERR_ALREADY_HAS_HOTEL);
			}
		} else {
			throw new MonopolyException(PacketType.ERR_NOT_ALL_HOUSES_BUILT);
		}
	}

	public StreetTitleDeedData getTitleDeed() {
		return (StreetTitleDeedData) getTile().getTitleDeed();
	}
}
