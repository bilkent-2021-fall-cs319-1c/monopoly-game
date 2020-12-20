package monopoly.gameplay.properties;

import monopoly.gameplay.GamePlayer;

/**
 * Trading interface
 *
 * @author Alper Sari
 * @version Dec 16, 2020
 */

public interface Tradeable {
    public void trade(GamePlayer from, GamePlayer to);
}
