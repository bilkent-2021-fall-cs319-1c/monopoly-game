package monopoly.gameplay;

import lombok.Getter;
import lombok.Setter;
import monopoly.gameplay.tiles.PropertyTile;

/**
 * Property class that takes a deed data class and has an owner
 *
 * @author Alper Sari
 * @version Dec 16, 2020
 */

@Getter
@Setter
public abstract class Property implements Auctionable,Tradeable{

    private PropertyTile tile;
    private GamePlayer owner;
    private boolean mortgaged;
    private TitleDeedData titleDeed;
    private ColorSet colorSet   ;


    Property(PropertyTile tile, GamePlayer owner, TitleDeedData titleDeed, ColorSet colorSet)
    {
        this.tile = tile;
        this.owner = owner;
        this.titleDeed = titleDeed;
        this.colorSet = colorSet;
        mortgaged = false;
        tile.setProperty(this);
        titleDeed.setProperty(this);
    }

    public void mortgage()
    {
        mortgaged = !mortgaged;
    }

    public int getRentCost()
    {
        //Parent method does nothing
        return -1;
    }

    @Override
    public void give(GamePlayer player) {
        owner = player;
    }

    @Override
    public void trade(GamePlayer from, GamePlayer to) {
        if (from.equals(owner))
            give(to);
    }

}
