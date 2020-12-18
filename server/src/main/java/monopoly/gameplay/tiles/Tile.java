package monopoly.gameplay.tiles;

import lombok.Getter;
import lombok.Setter;
import monopoly.gameplay.Actionable;
import monopoly.gameplay.GamePlayer;
import monopoly.gameplay.TitleDeedData;
import monopoly.network.packet.important.packet_data.gameplay.property.TilePacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.TileType;

/**
 * Tile parent class
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */
@Getter
@Setter
public class Tile implements Actionable{

    private String name;
    private String description;
    private int index;
    
    private TileType type;
    private TitleDeedData titleDeed;

    public Tile(TitleDeedData titleDeed, String name, String description, TileType type, int index) {
    	this.titleDeed = titleDeed;
        this.name = name;
        this.description = description;
        this.type = type;
        this.index = index;
    }

    /**
     * Performs the tile action
     *
     * @param player player subject
     */
    @Override
    public void doAction(GamePlayer player) {
        //Do nothing in parent
    }
    
    public TilePacketData getAsTilePacket() {
    	return new TilePacketData( titleDeed.getAsTitleDeedPacket(), name, description, type, index);
    }
}
