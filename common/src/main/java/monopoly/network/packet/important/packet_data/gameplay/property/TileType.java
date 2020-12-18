package monopoly.network.packet.important.packet_data.gameplay.property;

/**
 * Contains all the types of tiles in the game board
 * 
 * @author Javid Baghirov
 * @version Dec 18, 2020
 */
public enum TileType {
	GO, JAIL, GO_TO_JAIL, PARKING, TAX, RAILROAD, STREET, UTILITY, COMMUNITY_CHEST, CHANCE;
	
	public static TileType getAsTileType(String type) {
		return TileType.valueOf( type);
	}
}
