package monopoly.gameplay;

import lombok.Getter;

/**
 * The tile where the jail is
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */

public class JailTile extends Tile{
    @Getter
    private final int TILE_POSITION = 10;

    public JailTile(String name, String description) {
        super(name, description);
    }
}