package monopoly.gameplay.tiles;

import lombok.Getter;
import lombok.Setter;
import monopoly.gameplay.properties.TitleDeedData;
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
public abstract class Tile implements Actionable{

    private String name;
    private String description;
    private int index;
    
    private TileType type;
    private TitleDeedData titleDeed;

    protected Tile(TitleDeedData titleDeed, String name, String description, TileType type, int index) {
    	this.titleDeed = titleDeed;
        this.name = name;
        this.description = description;
        this.type = type;
        this.index = index;
    }

    public TilePacketData getAsTilePacket() {
    	return new TilePacketData( titleDeed.getAsTitleDeedPacket(), name, description, type, index);
    }
}
