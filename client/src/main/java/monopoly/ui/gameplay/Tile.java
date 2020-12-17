package monopoly.ui.gameplay;

import javafx.geometry.Point2D;

/**
 * A generic tile that can hold player tokens
 * 
 * @author Ziya Mukhtarov
 * @version Dec 17, 2020
 */
public interface Tile {
	/**
	 * @return An array of size 6 that contains the current tokens in this tile. If
	 *         a place is empty, then the corresponding array element must be null.
	 */
	Token[] getTileTokens();

	/**
	 * Calculates the position for the given token
	 * 
	 * @param tokenNum The token's order number, should be from 0 to 5
	 * @return The coordinate for the token
	 */
	Point2D getTokenLocation(int tokenNum);

	/**
	 * Finds the index number of the given token in this Tile
	 * 
	 * @param token The token to search for
	 * @return the token number of the given token in this tile, or -1 if it is not
	 *         found
	 */
	default int getTokenIndex(Token token) {
		if (token == null)
			return -1;

		Token[] tokens = getTileTokens();
		for (int i = 0; i < tokens.length; i++) {
			if (token.equals(tokens[i])) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Marks a free token position as occupied by the given token and returns the
	 * center coordinates where the token should be located
	 * 
	 * @param token The token which should occupy a free position in this tile.
	 * @return The center location for a token that was free before and now is
	 *         occupied by the given token, or null if something is really wrong.
	 */
	default Point2D occupyNextFreePlace(Token token) {
		Token[] tokens = getTileTokens();
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i] == null) {
				tokens[i] = token;
				return getTokenLocation(i);
			}
		}

		// Should not really reach here. There should be at least 1 free place
		return null;
	}

	/**
	 * Marks the location that was occupied by the given token as a free place
	 * 
	 * @param token The token which was occupying the place
	 */
	default void freeTokenLocation(Token token) {
		int tokenNum = getTokenIndex(token);
		if (tokenNum == -1)
			return;

		getTileTokens()[tokenNum] = null;
	}
}
