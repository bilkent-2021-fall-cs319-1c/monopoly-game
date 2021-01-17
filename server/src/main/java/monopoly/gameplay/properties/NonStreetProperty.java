package monopoly.gameplay.properties;

import java.util.List;

/**
 * Utility property class
 *
 * @author Alper Sari, Ziya Mukhtarov
 * @version Jan 17, 2021
 */
public class NonStreetProperty extends Property {
	public NonStreetProperty(String title, int buyCost, int mortgageCost, List<Integer> rentPrices, ColorSet colorSet) {
		super(title, buyCost, mortgageCost, rentPrices, colorSet);
	}

	@Override
	public int getRentCost() {
		if (!isOwned() || isMortgaged()) {
			return 0;
		}

		return getTile().getProperty().getRentTierPrice(getColorSet().countPropertiesOwnedBy(getOwner()));
	}
}
