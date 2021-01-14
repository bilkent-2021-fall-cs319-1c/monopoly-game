package monopoly.gameplay.properties;

import java.util.List;

/**
 * Utility property class
 *
 * @author Alper Sari
 * @version Dec 16, 2020
 */

public class NonStreetProperty extends Property {
	public NonStreetProperty(String title, int buyCost, int mortgageCost, List<Integer> rentPrices, String colorSet) {
		super(title, buyCost, mortgageCost, rentPrices, colorSet);
	}

	@Override
	public int getRentCost() {
		int ownedColorsetProperties = 0;

		for (int i = 0; i < getOwner().getProperties().size(); i++) {
			if (getOwner().getProperties().get(i).getTile().getType() == getTile().getType())
				ownedColorsetProperties++;
		}

		return getTile().getProperty().getRentTierPrice(ownedColorsetProperties);
	}
}
