package monopoly.gameplay.properties;

/**
 * Utility property class
 *
 * @author Alper Sari
 * @version Dec 16, 2020
 */

public class UtilityProperty extends Property {
	public UtilityProperty(String color) {
		super(color);
	}

	@Override
	public int getRentCost() {
		int ownedUtilities = 0;

		for (int i = 0; i < getOwner().getProperties().size(); i++) {
			if (getOwner().getProperties().get(i) instanceof RailroadProperty)
				ownedUtilities++;
		}

		return getTile().getTitleDeed().getRentTierPrice(ownedUtilities);
	}
}
