package monopoly.gameplay;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

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

        itemsOfPlayerFrom = new ArrayList<Tradeable>();
        itemsOfPlayerTo = new ArrayList<Tradeable>();

        agreePlayerFrom = false;
        agreePlayerTo = false;
    }

    public boolean addItem(GamePlayer player, Tradeable item)
    {
        if(player.equals(playerFrom))
        {
            itemsOfPlayerFrom.add(item);
            return true;
        }
        else if(player.equals(playerTo))
        {
            itemsOfPlayerTo.add(item);
            return true;
        }
        else
            return false;
    }

    public boolean removeItem(GamePlayer player, Tradeable item)
    {
        if(player.equals(playerFrom))
        {
            itemsOfPlayerFrom.remove(item);
            return true;
        }
        else if(player.equals(playerTo))
        {
            itemsOfPlayerTo.remove(item);
            return true;
        }
        else
            return false;
    }

    public void agree(GamePlayer player)
    {
        if(player.equals(playerFrom))
            agreePlayerFrom = true;
        else if(player.equals(playerTo))
            agreePlayerTo = true;
    }

    public void disagree(GamePlayer player)
    {
        if(player.equals(playerFrom))
            agreePlayerFrom = false;
        else if(player.equals(playerTo))
            agreePlayerTo = false;
    }

    public boolean completeTrade()
    {
        if (agreePlayerTo && agreePlayerFrom)
        {
            for (Tradeable tradeable : itemsOfPlayerFrom) tradeable.trade(playerFrom, playerTo);
            for (Tradeable tradeable : itemsOfPlayerTo) tradeable.trade(playerTo, playerFrom);
            return true;
        }
        else
            return false;
    }
}
