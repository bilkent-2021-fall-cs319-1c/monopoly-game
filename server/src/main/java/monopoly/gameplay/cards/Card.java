package monopoly.gameplay.cards;

import lombok.Getter;
import lombok.Setter;
import monopoly.gameplay.GamePlayer;
import monopoly.gameplay.tiles.Actionable;

@Getter
@Setter
public class Card implements Actionable{

    private String description;
    private CardType cardType;
    private int cardVersion;

    public Card(String description, CardType cardType, int cardVersion)
    {
        this.description = description;
        this.cardType = cardType;
        this.cardVersion = cardVersion;
    }

    //For filler cards and get out of jail card
    public Card(String description, CardType cardType) {
        cardVersion = -1;
    }

    @Override
    public void doAction(GamePlayer player) {
        if(cardType == CardType.CHANCE)
            chanceAction(player);
        else
            communityAction(player);
    }

    //You have to type individual implementations for each card/card version

    private void chanceAction(GamePlayer player)
    {
        switch (cardVersion)
        {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            default:
                System.out.println("Card version out of index");
        }

    }

    private void communityAction(GamePlayer player)
    {
        switch (cardVersion)
        {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            default:
                System.out.println("Card version out of index");
        }
    }
}
