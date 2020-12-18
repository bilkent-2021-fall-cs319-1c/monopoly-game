package monopoly.gameplay;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import lombok.Getter;
import lombok.Setter;
import monopoly.gameplay.tiles.GoTile;
import monopoly.gameplay.tiles.PropertyTile;
import monopoly.gameplay.tiles.Tile;
import monopoly.network.packet.important.packet_data.gameplay.BoardPacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.TileType;

/**
 * The board class with an arraylist of tiles
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */

@Setter
@Getter
public class Board {
	private final int MAX_TILE_INDEX = 39;
	private final int JAIL_POSITION = 10;
	private List<Tile> tiles;

	public Board() {
		tiles = new ArrayList<>();

		initialize();
	}

	/**
	 * Moves a player a certain amount of steps forwards
	 *
	 * @param player player to be moved
	 * @param steps  amount of steps
	 */
	public void move(GamePlayer player, int steps) {

		if (!player.isInJail() || (player.isRolledDouble() && player.isInJail())) {
			for (int i = 0; i < steps; i++) {
				if (player.getTileIndex() < MAX_TILE_INDEX) {
					player.setTileIndex(player.getTileIndex() + 1);

				} else {
					player.setTileIndex(0);
					player.getTile().doAction(player);
				}

				if (!(player.getTile() instanceof GoTile))
					player.getTile().doAction(player);
			}

			if (player.isInJail())
				player.setInJail(false);
		}
	}

	/**
	 * Inserts the board tiles in the correct order
	 */
	private void initialize() {
		System.out.println(Board.class.getResourceAsStream("monopoly_board.json"));
		JSONTokener tokener = new JSONTokener(Board.class.getResourceAsStream("monopoly_board.json"));
		JSONArray arr = new JSONArray(tokener);

		for (int i = 0; i < arr.length(); i++) {
			JSONObject tile = arr.getJSONObject(i);

			String name = tile.getString("name");
			String description = tile.getString("description");
			String type = tile.getString("type");
			TileType tileType = TileType.getAsTileType(type);

			if (type.equals("STREET")) {
				int buyPrice = tile.getInt("cost");
				int houseBuildPrice = tile.getInt("house");
				// TODO Find and write hotel build prices to the json file
				int hotelBuildPrice = 200;
				String color = tile.getString( "color");

				JSONArray rentCosts = tile.getJSONArray("rent");

				List<Integer> rentPrice = new ArrayList<>();
				for (int j = 0; j < rentCosts.length(); j++) {
					rentPrice.add(rentCosts.getInt(j));
				}

				tiles.add(new PropertyTile(
						new StreetTitleDeedData(name, buyPrice, 0, rentPrice, houseBuildPrice, hotelBuildPrice, color), name,
						description, tileType, i, null));

			} else if (tile.getString("type").equals("RAILROAD") || tile.getString("type").equals("UTILITY")) {
				int buyPrice = tile.getInt("cost");

				tiles.add(new PropertyTile(new TitleDeedData(name, buyPrice, 0, null), name, description, tileType, i,
						null));
			} else {
				tiles.add(new Tile(new TitleDeedData(name, 0, 0, null), name, description, tileType, i));
			}

		}
	}

	public BoardPacketData getAsBoardPacket() {
		BoardPacketData boardData = new BoardPacketData();

		for (int i = 0; i < tiles.size(); i++) {
			boardData.add(tiles.get(i).getAsTilePacket());
		}

		return boardData;
	}
}
