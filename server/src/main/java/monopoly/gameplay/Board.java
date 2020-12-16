package monopoly.gameplay;

import lombok.Getter;
import lombok.Setter;
import monopoly.gameplay.tiles.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * The board class with an arraylist of tiles
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */

public class Board {
    @Setter
    @Getter
    private ArrayList<Tile> tiles;
    private int[] tokenLocations;
    private final int MAX_TILES = 40;

    public Board()
    {
        tokenLocations = new int[6]; //Assuming max player count of 6
        for (int i = 0; i < 6; i++)
            tokenLocations[i] = -1; //-1 means that the token slot for the player is empty
        tiles = new ArrayList<>();

        JSONObject obj = new JSONObject("tile_data");
        JSONArray arr = obj.getJSONArray("tiles");
        for (int i = 0; i < arr.length(); i++) {
            String tileName = arr.getJSONObject(i).getString("tile_name");
            String tileDesc = arr.getJSONObject(i).getString("tile_description");
            tiles.add(new Tile(tileName,tileDesc));
        }
    }

    /**
     * Moves a player a certain amount of steps forwards
     *
     * @param player player to be moved
     * @param steps amount of steps
     */
    public void move(GamePlayer player, int steps)
    {
        for (int i = 0; i < steps; i++)
        {
            if (player.getTileIndex() < MAX_TILES)
                player.setTileIndex(player.getTileIndex()++);
            else
            {
                player.setTileIndex(0);
                player.getTile().doAction();
            }

            if(!(player.getTile() instanceof GoTile))
                player.getTile().doAction();
        }
    }

    /**
     * Inserts the board tiles in the correct order
     */
    private void initialize()
    {
        // Duplicate for now
        JSONObject obj = new JSONObject("tile_data");
        JSONArray arr = obj.getJSONArray("tiles");
        for (int i = 0; i < arr.length(); i++) {
            String tileName = arr.getJSONObject(i).getString("tile_name");
            String tileDesc = arr.getJSONObject(i).getString("tile_description");
            tiles.add(new Tile(tileName,tileDesc));
        }
    }
}
