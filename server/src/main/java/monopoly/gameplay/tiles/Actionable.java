package monopoly.gameplay.tiles;

import monopoly.gameplay.GamePlayer;

/**
 * Interface for cards and tiles which perform an action
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */

public interface Actionable {

    public void doAction(GamePlayer player);
}
