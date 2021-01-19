package monopoly.gameplay.cards;

import lombok.Getter;
import monopoly.gameplay.GamePlayer;
import monopoly.gameplay.Tradeable;

@Getter
public class GetOutOfJailCard extends Card implements Tradeable {
	private GamePlayer owner;

	public GetOutOfJailCard(String description, CardType cardType) {
		super(description, cardType);
	}

	@Override
	public void setOwner(GamePlayer player) {
		owner = player;
	}
}
