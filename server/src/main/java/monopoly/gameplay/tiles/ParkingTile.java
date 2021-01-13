package monopoly.gameplay.tiles;

import monopoly.gameplay.GamePlayer;
import monopoly.gameplay.properties.TitleDeedData;
import monopoly.network.packet.important.packet_data.gameplay.property.TileType;

public class ParkingTile extends Tile {

	public ParkingTile(TitleDeedData titleDeed, String name, String description, TileType type, int index) {
		super(titleDeed, name, description, type, index);
	}

	@Override
	public void doAction(GamePlayer player) {
		// TODO Auto-generated method stub
	}
}
