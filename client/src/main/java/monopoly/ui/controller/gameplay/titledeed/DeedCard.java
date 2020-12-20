package monopoly.ui.controller.gameplay.titledeed;

public interface DeedCard {
	/**
	 * @return The name of the card.
	 */
	String getName();

	/**
	 * @return The cost of buying this property
	 */
	int getBuyCost();
}
