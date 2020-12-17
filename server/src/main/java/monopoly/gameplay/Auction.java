package monopoly.gameplay;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Auction {

    private int currentBid;
    private Auctionable item;
    private GamePlayer lastBidder;
    private ArrayList<GamePlayer> satisfiedPlayers;

    public Auction(Auctionable item)
    {
        currentBid = 0;
        this.item = item;
        lastBidder = null;
        satisfiedPlayers = new ArrayList<GamePlayer>();
    }

    public boolean bid(int increaseAmount, GamePlayer player)
    {
        if(currentBid + increaseAmount <= player.getBalance())
        {
            currentBid += increaseAmount;
            lastBidder = player;
            return true;
        }
        else
            return false;
    }
    public void setSatisfied(GamePlayer player)
    {
        if (!satisfiedPlayers.contains(player))
            satisfiedPlayers.add(player);
    }
    public boolean isSatisfied(GamePlayer player)
    {
        return satisfiedPlayers.contains(player);
    }

    //No way to implement this without unnecessary coupling
    //Just compare the satisfied list to the player list

    /*
    public boolean isEveryoneSatisfied()
    {

    }
    */

    
    public void closeAuction()
    {
        lastBidder.setBalance(lastBidder.getBalance() - currentBid);
        item.give(lastBidder);
    }


}
