package monopoly.gameplay.cards;

import lombok.Getter;
import lombok.Setter;
import monopoly.gameplay.GamePlayer;
import monopoly.gameplay.properties.Tradeable;

@Getter
public class GetOutOfJailCard extends Card implements Tradeable {
	@Setter
	private GamePlayer owner;

	public GetOutOfJailCard(String description, CardType cardType) {
		super(description, cardType);
	}

	@Override
	public void trade(GamePlayer from, GamePlayer to) {
		if (from.equals(owner))
			owner = to;
	}
}
