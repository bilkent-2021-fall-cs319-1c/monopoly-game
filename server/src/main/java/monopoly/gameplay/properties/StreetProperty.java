package monopoly.gameplay.properties;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import monopoly.MonopolyException;
import monopoly.network.packet.important.PacketType;

/**
 * A property which can have have an amount of hotels and houses on it
 *
 * @author Alper Sari, Ziya Mukhtarov
 * @version Jan 17, 2021
 */
@Getter
public class StreetProperty extends Property {
	private final int houseCost;
	private final int hotelCost;

	private int houseCount;
	private boolean hasHotel;

	public StreetProperty(String title, int buyCost, int mortgageCost, List<Integer> rentPrices, ColorSet colorSet,
			int houseCost, int hotelCost) {
		super(title, buyCost, mortgageCost, rentPrices, colorSet);

		this.houseCost = houseCost;
		this.hotelCost = hotelCost;

		houseCount = 0;
		hasHotel = false;
	}

	@Override
	public int getRentCost() {
		if (!isOwned() || isMortgaged()) {
			return 0;
		}

		if (hasHotel) {
			// Tier 6
			return getRentTierPrice(6);
		}
		if (houseCount > 0) {
			// Tier 5, 4, 3, 2
			return getRentTierPrice(houseCount + 1);
		}
		if (getColorSet().isOwnedBy(getOwner())) {
			// Tier 1
			return getRentTierPrice(1);
		}
		// Tier 0
		return getRentTierPrice(0);
	}

	private void checkBuildingConditions() throws MonopolyException {
		if (!getColorSet().isOwnedBy(getOwner())) {
			throw new MonopolyException(PacketType.ERR_NOT_OWNED);
		}
		if (isMortgaged()) {
			throw new MonopolyException(PacketType.ERR_MORTGAGED);
		}
	}

	public void buildHouse() throws MonopolyException {
		checkBuildingConditions();

		// Color Set Check
		List<StreetProperty> colorSetProperties = getColorSet().getProperties().parallelStream()
				.map(StreetProperty.class::cast).collect(Collectors.toList());
		for (StreetProperty property : colorSetProperties) {
			if (houseCount + 1 - property.houseCost > 1) {
				throw new MonopolyException(PacketType.ERR_BUILDINGS_NOT_BALANCED);
			}
		}

		if (houseCount < 4) {
			houseCount++;
		} else
			throw new MonopolyException();
	}

	public void buildHotel() throws MonopolyException {
		checkBuildingConditions();

		// Color Set Check
		List<StreetProperty> colorSetProperties = getColorSet().getProperties().parallelStream()
				.map(StreetProperty.class::cast).collect(Collectors.toList());
		for (StreetProperty property : colorSetProperties) {
			if (!property.hasHotel && property.houseCount < 4) {
				throw new MonopolyException(PacketType.ERR_BUILDINGS_NOT_BALANCED);
			}
		}

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

	@Override
	public void mortgage() throws MonopolyException {
		super.mortgage();
		sellAllBuildingsToBank();
	}

	/**
	 * Sells all the buildings on this property and its color set to the bank at
	 * their half price.
	 */
	public void sellAllBuildingsToBank() {
		if (hasHotel) {
			getOwner().changeBalance(hotelCost / 2);
			houseCount = 4;
			hasHotel = false;
		}

		if (houseCount > 0) {
			getOwner().changeBalance(houseCost / 2 * houseCount);
			houseCount = 0;

			getColorSet().getProperties().parallelStream().map(StreetProperty.class::cast)
					.forEach(StreetProperty::sellAllBuildingsToBank);
		}
	}
}
