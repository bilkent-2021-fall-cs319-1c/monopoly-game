package monopoly.gameplay.properties;

import lombok.Getter;
import lombok.Setter;
import monopoly.MonopolyException;
import monopoly.network.packet.important.PacketType;

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
	private boolean hasHotel;

	public Street(String color) {
		super(color);
		houseCount = 0;
		hasHotel = false;
	}

	@Override
	public int getRentCost() {
		if (!isOwned()) {
			return 0;
		}

		int propertyCountWithColor = getOwner().getPropertyCountWithColor(getColorSet());

		if (houseCount > 0) {
			return getTitleDeed().getRentTierPrice(houseCount + 1);
		}

		if (hasHotel) {
			return getTitleDeed().getRentTierPrice(6);
		}

		return getTitleDeed().getRentTierPrice(0);

	}

	public boolean buildHouse() throws MonopolyException {
		if (houseCount < 4) {
			houseCount++;
			getOwner().setBalance(getOwner().getBalance() - ((StreetTitleDeedData) getTitleDeed()).getHouseCost());
			getOwner().getCurrentGame().sendBalanceChangeToPlayers(getOwner());

			return true;
		} else
			throw new MonopolyException(PacketType.ERR_UNKNOWN);
	}

	public boolean buildHotel() throws MonopolyException {
		if (houseCount == 4) {
			if (hasHotel) {
				hasHotel = true;
				getOwner().setBalance(getOwner().getBalance() - ((StreetTitleDeedData) getTitleDeed()).getHotelCost());
				getOwner().getCurrentGame().sendBalanceChangeToPlayers(getOwner());

				return true;
			} else {
				throw new MonopolyException(PacketType.ERR_ALREADY_HAS_HOTEL);
			}
		} else {
			throw new MonopolyException(PacketType.ERR_NOT_ALL_HOUSES_BUILT);
		}
	}
}
