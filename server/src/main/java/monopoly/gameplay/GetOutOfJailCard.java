package monopoly.gameplay;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetOutOfJailCard extends Card implements Tradeable{
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
