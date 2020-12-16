package monopoly.gameplay;

import java.util.ArrayList;
import monopoly.gameplay.tiles.*;
import org.json.JSONArray;
import org.json.JSONObject;

import lombok.Getter;
import lombok.Setter;
import monopoly.EntitiesWithId;

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
	private ArrayList<Tile> tiles;

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

		if (!player.isInJail() || (player.isRolledDouble() && player.isInJail()))
		{
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

			if(player.isInJail())
				player.setInJail(false);
		}
	}

	/**
	 * Inserts the board tiles in the correct order
	 */
	private void initialize() {
		// Duplicate for now
		JSONObject obj = new JSONObject("tile_data");
		JSONArray arr = obj.getJSONArray("tiles");
		for (int i = 0; i < arr.length(); i++) {
			String tileName = arr.getJSONObject(i).getString("tile_name");
			String tileDesc = arr.getJSONObject(i).getString("tile_description");
			tiles.add(new Tile(tileName, tileDesc, i));
		}
	}
}
