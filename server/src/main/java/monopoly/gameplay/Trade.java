package monopoly.gameplay;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Trade {

    private GamePlayer playerFrom;
    private GamePlayer playerTo;
    private ArrayList<Tradeable> itemsOfPlayerFrom;
    private ArrayList<Tradeable> itemsOfPlayerTo;
    private boolean agreePlayerFrom;
    private boolean agreePlayerTo;

    public Trade(GamePlayer playerFrom, GamePlayer playerTo)
    {
        this.playerFrom = playerFrom;
        this.playerTo = playerFrom;

        itemsOfPlayerFrom.addAll(playerFrom.getProperties());
        itemsOfPlayerFrom.addAll(playerFrom.getGetOutOfJailCards());

        itemsOfPlayerTo.addAll(playerTo.getProperties());
        itemsOfPlayerTo.addAll(playerTo.getGetOutOfJailCards());

        agreePlayerFrom = false;
        agreePlayerTo = false;
    }

    public void addItem(GamePlayer player, Tradeable item)
    {
        
    }

}
