package monopoly.gameplay.properties;

/**
 * Railroad property, can't take any buildings
 *
 * @author Alper Sari
 * @version Dec 16, 2020
 */

public class Railroad extends Property {
	public Railroad(String color){
		super(color);
	}

	@Override
	public int getRentCost() {
		int ownedRailroads = 0;

		for (int i = 0; i < getOwner().getProperties().size(); i++) {
			if (getOwner().getProperties().get(i) instanceof Railroad)
				ownedRailroads++;
		}
		return getTitleDeed().getRentTierPrice(ownedRailroads);
	}
}
