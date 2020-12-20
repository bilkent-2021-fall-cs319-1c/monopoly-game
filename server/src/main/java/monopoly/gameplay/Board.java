package monopoly.gameplay;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import lombok.Getter;
import lombok.Setter;
import monopoly.gameplay.properties.Railroad;
import monopoly.gameplay.properties.Street;
import monopoly.gameplay.properties.StreetTitleDeedData;
import monopoly.gameplay.properties.TitleDeedData;
import monopoly.gameplay.properties.Utility;
import monopoly.gameplay.tiles.CardTile;
import monopoly.gameplay.tiles.GoTile;
import monopoly.gameplay.tiles.GoToJailTile;
import monopoly.gameplay.tiles.JailTile;
import monopoly.gameplay.tiles.ParkingTile;
import monopoly.gameplay.tiles.PropertyTile;
import monopoly.gameplay.tiles.TaxTile;
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
					// TODO player.getTile().doAction(player);
				}

				// TODO
//				if (!(player.getTile() instanceof GoTile))
//					//player.getTile().doAction(player);
			}

			if (player.isInJail())
				player.setInJail(false);
		}
	}

	/**
	 * Inserts the board tiles in the correct order
	 */
	private void initialize() {
		JSONTokener tokener = new JSONTokener(Board.class.getResourceAsStream("monopoly_board.json"));
		JSONArray gameData = new JSONArray(tokener);

		for (int i = 0; i < gameData.length(); i++) {
			JSONObject tile = gameData.getJSONObject(i);

			String name = tile.getString("name").toUpperCase(Locale.ENGLISH);
			String description = tile.getString("description");
			String type = tile.getString("type");
			TileType tileType = TileType.getAsTileType(type);
			TitleDeedData titleDeed = new TitleDeedData(name, 0, 0, null);

			if (tileType == TileType.STREET) {
				int buyPrice = tile.getInt("cost");
				int buildPrice = tile.getInt("house");
				// TODO Find and write mortgage prices to the json file
				int mortgagePrice = 0;
				String color = tile.getString("color");

				tiles.add(new PropertyTile(new StreetTitleDeedData(name, buyPrice, mortgagePrice, getRentPrices(tile),
						buildPrice, buildPrice, color), name, description, tileType, i, new Street(color)));

			} else if (tileType == TileType.RAILROAD) {
				int buyPrice = tile.getInt("cost");
				int mortgagePrice = 0;

				tiles.add(new PropertyTile(new TitleDeedData(name, buyPrice, mortgagePrice, getRentPrices(tile)), name,
						description, tileType, i, new Railroad("RAILROAD")));

			} else if (tileType == TileType.UTILITY) {
				int buyPrice = tile.getInt("cost");
				int mortgagePrice = 0;

				tiles.add(new PropertyTile(new TitleDeedData(name, buyPrice, mortgagePrice, null), name, description,
						tileType, i, new Utility("UTILITY")));

			}

			else if (tileType == TileType.GO) {
				tiles.add(new GoTile(titleDeed, name, description, tileType, i));

			} else if (tileType == TileType.TAX) {
				int taxCost = tile.getInt("cost");

				tiles.add(new TaxTile(titleDeed, name, description, tileType, i, taxCost));

			} else if (tileType == TileType.JAIL) {
				tiles.add(new JailTile(titleDeed, name, description, tileType, i));

			} else if (tileType == TileType.PARKING) {
				tiles.add(new ParkingTile(titleDeed, name, description, tileType, i));

			} else if (tileType == TileType.GO_TO_JAIL) {
				tiles.add(new GoToJailTile(titleDeed, name, description, tileType, i));

			} else {
				tiles.add(new CardTile(titleDeed, name, description, tileType, i));
			}
		}
	}

	public List<Integer> getRentPrices(JSONObject object) {
		JSONArray rentCosts = object.getJSONArray("rent");
		List<Integer> rentPrice = new ArrayList<>();

		for (int j = 0; j < rentCosts.length(); j++) {
			rentPrice.add(rentCosts.getInt(j));
		}
		rentPrice.add(1, 2 * rentPrice.get(0));

		return rentPrice;
	}

	public BoardPacketData getAsBoardPacket() {
		BoardPacketData boardData = new BoardPacketData();

		for (int i = 0; i < tiles.size(); i++) {
			boardData.add(tiles.get(i).getAsTilePacket());
		}

		return boardData;
	}
}
