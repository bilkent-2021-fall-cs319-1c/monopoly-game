package monopoly.gameplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import monopoly.MonopolyException;
import monopoly.network.GameServer;
import monopoly.network.packet.important.PacketType;

/**
 * Trade functionality class
 *
 * @author Alper Sari, Ziya Mukhtarov
 * @version Jan 19, 2021
 */
@Getter
public class Trade {
	private GamePlayer player1;
	private GamePlayer player2;
	private List<Tradeable> itemsOfPlayer1;
	private List<Tradeable> itemsOfPlayer2;
	private boolean agreePlayer1;
	private boolean agreePlayer2;
	private boolean completed;

	public Trade(GamePlayer player1, GamePlayer player2) {
		this.player1 = player1;
		this.player2 = player2;

		itemsOfPlayer1 = Collections.synchronizedList(new ArrayList<Tradeable>());
		itemsOfPlayer2 = Collections.synchronizedList(new ArrayList<Tradeable>());

		agreePlayer1 = false;
		agreePlayer2 = false;
		completed = false;

		sendTradeInitiatedToPlayers();
	}

	/**
	 * Adds the item to this trade. The item must be owned by the requesting player.
	 * 
	 * @param player The player who wants to add to this trade
	 * @param item   The item to be added
	 * @throws MonopolyException if the trade has already been completed, player
	 *                           does not participate in this trade, or the item is
	 *                           not owned by the player
	 */
	public void addItem(GamePlayer player, Tradeable item) throws MonopolyException {
		if (completed) {
			throw new MonopolyException();
		}
		if (!player.equals(item.getOwner())) {
			throw new MonopolyException(PacketType.ERR_NOT_OWNED);
		}
		if (!player.equals(player1) && !player.equals(player2)) {
			throw new MonopolyException();
		}

		tradeItemsChanged();
		sendItemAddedToPlayers(player, item);
		if (player.equals(player1)) {
			itemsOfPlayer1.add(item);
		} else {
			itemsOfPlayer2.add(item);
		}
	}

	/**
	 * Removes the item from this trade. The item must be owned by the requesting
	 * player.
	 * 
	 * @param player The player who wants to remove from this trade
	 * @param item   The item to be removed
	 * @throws MonopolyException if the trade has already been completed, player
	 *                           does not participate in this trade, or the item is
	 *                           not owned by the player
	 */
	public void removeItem(GamePlayer player, Tradeable item) throws MonopolyException {
		if (completed) {
			throw new MonopolyException();
		}
		if (!player.equals(item.getOwner())) {
			throw new MonopolyException(PacketType.ERR_NOT_OWNED);
		}
		if (!player.equals(player1) && !player.equals(player2)) {
			throw new MonopolyException();
		}

		tradeItemsChanged();
		sendItemRemovedToPlayers(player, item);
		if (player.equals(player1)) {
			itemsOfPlayer1.remove(item);
		} else {
			itemsOfPlayer2.remove(item);
		}
	}

	public void agree(GamePlayer player) throws MonopolyException {
		if (completed) {
			throw new MonopolyException();
		}
		if (!player.equals(player1) && !player.equals(player2)) {
			throw new MonopolyException();
		}

		sendTradeAcceptedToPlayers(player);
		if (player.equals(player1))
			agreePlayer1 = true;
		else
			agreePlayer2 = true;

		checkAndCompleteTrade();
	}

	public void reject(GamePlayer player) throws MonopolyException {
		if (completed) {
			throw new MonopolyException();
		}
		if (!player.equals(player1) && !player.equals(player2)) {
			throw new MonopolyException();
		}

		player1.getGame().finishTrade();
		sendTradeCompleteToPlayers();
	}

	private boolean checkAndCompleteTrade() throws MonopolyException {
		if (!agreePlayer1 || !agreePlayer2)
			return false;
		if (completed)
			throw new MonopolyException();

		completed = true;

		itemsOfPlayer1.forEach(item -> item.setOwner(player2));
		itemsOfPlayer2.forEach(item -> item.setOwner(player1));

		player1.getGame().finishTrade();
		sendTradeCompleteToPlayers();

		return true;
	}

	private void tradeItemsChanged() {
		agreePlayer1 = false;
		agreePlayer2 = false;
	}

	private void sendTradeInitiatedToPlayers() {
		player1.getGame().getPlayersByTurn().parallelStream()
				.forEach(player -> GameServer.getInstance().sendTradeInitiatedNotification(player1, player2, player));
	}

	private void sendTradeAcceptedToPlayers(GamePlayer playerAccepting) {
		player1.getGame().getPlayersByTurn().parallelStream()
				.forEach(player -> GameServer.getInstance().sendTradeAcceptedNotification(playerAccepting, player));
	}

	private void sendItemAddedToPlayers(GamePlayer playerAddingItem, Tradeable item) {
		player1.getGame().getPlayersByTurn().parallelStream().forEach(
				player -> GameServer.getInstance().sendAddedToTradeNotification(playerAddingItem, item, player));
	}

	private void sendItemRemovedToPlayers(GamePlayer playerRemovingItem, Tradeable item) {
		player1.getGame().getPlayersByTurn().parallelStream().forEach(
				player -> GameServer.getInstance().sendRemovedFromTradeNotification(playerRemovingItem, item, player));
	}

	private void sendTradeCompleteToPlayers() {
		player1.getGame().getPlayersByTurn().parallelStream()
				.forEach(player -> GameServer.getInstance().sendTradeCompleteNotification(player));
	}
}
