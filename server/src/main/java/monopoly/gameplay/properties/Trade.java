package monopoly.gameplay.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import monopoly.gameplay.Game;
import monopoly.gameplay.GamePlayer;

/**
 * Trade functionality class
 *
 * @author Alper Sari
 * @version Dec 18, 2020
 */

@Getter
@Setter
public class Trade {

	private Game game;
	private GamePlayer playerFrom;
	private GamePlayer playerTo;
	private List<Tradeable> itemsOfPlayerFrom;
	private List<Tradeable> itemsOfPlayerTo;
	private boolean agreePlayerFrom;
	private boolean agreePlayerTo;

	public Trade(Game game, GamePlayer playerFrom, GamePlayer playerTo) {
		this.game = game;
		this.playerFrom = playerFrom;
		this.playerTo = playerTo;

		itemsOfPlayerFrom = Collections.synchronizedList(new ArrayList<Tradeable>());
		itemsOfPlayerTo = Collections.synchronizedList(new ArrayList<Tradeable>());

		agreePlayerFrom = false;
		agreePlayerTo = false;
	}

	public boolean addItem(GamePlayer player, Tradeable item) {
		if (player.equals(playerFrom)) {
			itemsOfPlayerFrom.add(item);
			return true;
		} else if (player.equals(playerTo)) {
			itemsOfPlayerTo.add(item);
			return true;
		} else
			return false;
	}

	public boolean removeItem(GamePlayer player, Tradeable item) {
		if (player.equals(playerFrom)) {
			itemsOfPlayerFrom.remove(item);
			return true;
		} else if (player.equals(playerTo)) {
			itemsOfPlayerTo.remove(item);
			return true;
		} else
			return false;
	}

	public void agree(GamePlayer player) {
		if (player.equals(playerFrom))
			agreePlayerFrom = true;
		else if (player.equals(playerTo))
			agreePlayerTo = true;
	}

	public void disagree(GamePlayer player) {
		if (player.equals(playerFrom))
			agreePlayerFrom = false;
		else if (player.equals(playerTo))
			agreePlayerTo = false;
	}

	public boolean completeTrade() {
		if (agreePlayerTo && agreePlayerFrom) {
			for (Tradeable tradeable : itemsOfPlayerFrom)
				tradeable.trade(playerFrom, playerTo);
			for (Tradeable tradeable : itemsOfPlayerTo)
				tradeable.trade(playerTo, playerFrom);
			return true;
		} else
			return false;
	}
}
