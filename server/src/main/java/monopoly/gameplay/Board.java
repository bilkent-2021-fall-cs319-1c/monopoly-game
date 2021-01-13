package monopoly.gameplay;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import monopoly.gameplay.properties.RailroadProperty;
import monopoly.gameplay.properties.StreetProperty;
import monopoly.gameplay.properties.StreetTitleDeedData;
import monopoly.gameplay.properties.TitleDeedData;
import monopoly.gameplay.properties.UtilityProperty;
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
public class Board {
	private List<Tile> tiles;

	public Board() {
		initialize();
	}

	/**
	 * Moves a player a certain amount of steps forwards
	 *
	 * @param player player to be moved
	 * @param steps  amount of steps
	 */
	public void move(GamePlayer player, int steps) {
		if (!player.isInJail() || player.isRolledDouble()) {
			for (int i = 0; i < steps; i++) {
				int currentTokenIndex = player.getTile().getIndex();
				player.setTile(tiles.get((currentTokenIndex + 1) % tiles.size()));

				if (player.getTile().getType() == TileType.GO)
					player.getTile().doAction(player);
			}
		}
	}

	/**
	 * Inserts the board tiles in the correct order
	 */
	private void initialize() {
		JSONTokener tokener = new JSONTokener(Board.class.getResourceAsStream("monopoly_board.json"));
		JSONArray gameData = new JSONArray(tokener);
		tiles = new ArrayList<>();

		for (int i = 0; i < gameData.length(); i++) {
			Tile tile;
			JSONObject tileJSONData = gameData.getJSONObject(i);

			String name = tileJSONData.getString("name").toUpperCase(Locale.ENGLISH);
			String description = tileJSONData.getString("description");
			TileType tileType = TileType.getAsTileType(tileJSONData.getString("type"));

			if (tileType == TileType.STREET) {
				int buyPrice = tileJSONData.getInt("cost");
				int buildPrice = tileJSONData.getInt("house");
				// TODO Find and write mortgage prices to the JSON file
				int mortgagePrice = 0;
				String color = tileJSONData.getString("color");

				tile = new PropertyTile(new StreetTitleDeedData(name, buyPrice, mortgagePrice,
						getRentPrices(tileJSONData), buildPrice, buildPrice, color), name, description, tileType, i,
						new StreetProperty(color));

			} else if (tileType == TileType.RAILROAD) {
				int buyPrice = tileJSONData.getInt("cost");
				int mortgagePrice = 0;

				tile = new PropertyTile(new TitleDeedData(name, buyPrice, mortgagePrice, getRentPrices(tileJSONData)),
						name, description, tileType, i, new RailroadProperty("RAILROAD"));

			} else if (tileType == TileType.UTILITY) {
				int buyPrice = tileJSONData.getInt("cost");
				int mortgagePrice = 0;

				tile = new PropertyTile(new TitleDeedData(name, buyPrice, mortgagePrice, null), name, description,
						tileType, i, new UtilityProperty("UTILITY"));

			} else {
				// Not a property tile
				TitleDeedData titleDeed = new TitleDeedData(name, 0, 0, null);

				if (tileType == TileType.TAX) {
					int taxCost = tileJSONData.getInt("cost");
					tile = new TaxTile(titleDeed, name, description, tileType, i, taxCost);

				} else if (tileType == TileType.GO) {
					tile = new GoTile(titleDeed, name, description, tileType, i);

				} else if (tileType == TileType.JAIL) {
					tile = new JailTile(titleDeed, name, description, tileType, i);

				} else if (tileType == TileType.PARKING) {
					tile = new ParkingTile(titleDeed, name, description, tileType, i);

				} else if (tileType == TileType.GO_TO_JAIL) {
					tile = new GoToJailTile(titleDeed, name, description, tileType, i);

				} else {
					tile = new CardTile(titleDeed, name, description, tileType, i);
				}
			}

			tiles.add(tile);
		}
	}

	public List<Integer> getRentPrices(JSONObject object) {
		JSONArray rentCosts = object.getJSONArray("rent");
		List<Integer> rentPrice = new ArrayList<>();

		for (int j = 0; j < rentCosts.length(); j++) {
			rentPrice.add(rentCosts.getInt(j));
		}
		// Rent with color set is twice the amount of regular rent price
		rentPrice.add(1, 2 * rentPrice.get(0));

		return rentPrice;
	}

	/**
	 * @return A tile with {@link TileType}.JAIL
	 */
	public Tile getJailTile() {
		return tiles.stream().filter(tile -> tile.getType() == TileType.JAIL).findAny().orElse(null);
	}

	public Tile getStartTile() {
		return tiles.get(0);
	}

	public BoardPacketData getAsPacket() {
		BoardPacketData boardData = new BoardPacketData();

		for (int i = 0; i < tiles.size(); i++) {
			boardData.add(tiles.get(i).getAsPacket());
		}

		return boardData;
	}
}
