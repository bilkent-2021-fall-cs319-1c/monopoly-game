package monopoly.gameplay.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import monopoly.MonopolyException;
import monopoly.gameplay.Game;
import monopoly.gameplay.GamePlayer;
import monopoly.gameplay.tiles.PropertyTile;
import monopoly.network.GameServer;
import monopoly.network.packet.important.PacketType;

/**
 * Auction functionality class
 *
 * @author Alper Sari, Javid Baghirov
 * @version Dec 20, 2020
 */

@Getter
@Setter
public class Auction {
	private int currentBid;
	private Auctionable item;
	private int bidderIndex;
	private Game game;
	private GamePlayer lastBidder;
	private List<GamePlayer> satisfiedPlayers;

	public Auction(Game game, Property item) {
		this.game = game;
		this.item = item;

		bidderIndex = 0;
		currentBid = 10;
		lastBidder = null;
		satisfiedPlayers = Collections.synchronizedList(new ArrayList<GamePlayer>());

		sendAuctionInitiatedToPlayers(item.getTile());
		sendAuctionTurnToPlayers(getCurrentBidder());
	}

	public void bid(int increaseAmount, GamePlayer player) throws MonopolyException {
		if (!getCurrentBidder().equals(player)) {
			throw new MonopolyException(PacketType.ERR_NOT_BID_TURN);
		}

		if (currentBid + increaseAmount > player.getBalance()) {
			throw new MonopolyException(PacketType.ERR_NOT_ENOUGH_BALANCE);
		}

		currentBid += increaseAmount;
		lastBidder = player;

		sendBidUpdateToPlayers(player, currentBid);
		updateCurrentBidder();
		sendAuctionTurnToPlayers(getCurrentBidder());
	}

	public void skip(GamePlayer player) throws MonopolyException {
		if (!getCurrentBidder().equals(player)) {
			throw new MonopolyException(PacketType.ERR_NOT_BID_TURN);
		}

		setSatisfied(player);

		sendBidSkipToPlayers(player);
		updateCurrentBidder();

		int result = isEveryoneSatisfied();
		if (!completeAuction(result)) {
			sendAuctionTurnToPlayers(getCurrentBidder());
		}
	}

	public void setSatisfied(GamePlayer player) {
		if (!satisfiedPlayers.contains(player))
			satisfiedPlayers.add(player);
	}

	public boolean isSatisfied(GamePlayer player) {
		return satisfiedPlayers.contains(player);
	}

	public int isEveryoneSatisfied() {
		int playerCount = game.getPlayersByTurn().size();

		if (lastBidder == null && satisfiedPlayers.size() == playerCount) {
			// No one bids
			return 0;
		} else if (lastBidder != null && satisfiedPlayers.size() == (playerCount - 1)) {
			// Someone bids and gets the property
			return 1;
		} else
			// Bid is still going
			return -1;
	}

	public void updateCurrentBidder() {
		increaseBidderIndex();

		while (satisfiedPlayers.contains(getCurrentBidder())) {
			increaseBidderIndex();
		}
	}

	private void increaseBidderIndex() {
		bidderIndex = (bidderIndex + 1) % game.getPlayersByTurn().size();
	}

	public boolean completeAuction(int result) {
		if (result == 0) {
			// Everyone skipped the auction
			game.completeTurn();

			game.setAuction(null);
			sendAuctionCompleteToPlayers();

			return true;
		} else if (result == 1) {
			// Someone won the auction
			lastBidder.setBalance(lastBidder.getBalance() - currentBid);

			game.completeTurn();

			item.give(lastBidder);
			game.sendPropertyBoughtToPlayers(((Property) item).getTile(), lastBidder);

			game.setAuction(null);
			sendAuctionCompleteToPlayers();

			return true;
		} else
			// Auction is still going
			return false;
	}

	public void sendAuctionInitiatedToPlayers(PropertyTile tile) {
		game.getPlayersByTurn().parallelStream()
				.forEach(player -> GameServer.getInstance().sendAuctionInitiatedNotification(tile, player));
	}

	public void sendAuctionTurnToPlayers(GamePlayer playerTurn) {
		game.getPlayersByTurn().parallelStream()
				.forEach(player -> GameServer.getInstance().sendAuctionTurnNotification(playerTurn, player));
	}

	public void sendBidUpdateToPlayers(GamePlayer playerBid, int bidAmount) {
		game.getPlayersByTurn().parallelStream()
				.forEach(player -> GameServer.getInstance().sendUpdatedBidNotification(playerBid, player, bidAmount));
	}

	public void sendBidSkipToPlayers(GamePlayer playerSkipped) {
		game.getPlayersByTurn().parallelStream()
				.forEach(player -> GameServer.getInstance().sendSkipBidNotification(playerSkipped, player));
	}

	public void sendAuctionCompleteToPlayers() {
		game.getPlayersByTurn().parallelStream()
				.forEach(player -> GameServer.getInstance().sendAuctionCompleteNotification(player));
	}

	private GamePlayer getCurrentBidder() {
		return game.getPlayersByTurn().get(bidderIndex);
	}
}
