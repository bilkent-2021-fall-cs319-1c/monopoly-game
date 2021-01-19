package monopoly.gameplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import monopoly.MonopolyException;
import monopoly.network.GameServer;
import monopoly.network.packet.important.PacketType;

/**
 * Auction functionality class
 *
 * @author Alper Sari, Javid Baghirov, Ziya Mukhtarov
 * @version Jan 17, 2021
 */
@Getter
public class Auction {
	private static final int INITIAL_BID = 10;

	private Game game;
	private Auctionable item;

	private int currentBid;
	private int bidderIndex;
	private GamePlayer lastBidder;
	private List<GamePlayer> satisfiedPlayers;

	public Auction(Game game, Auctionable item) {
		this.game = game;
		this.item = item;

		bidderIndex = 0;
		currentBid = INITIAL_BID;
		lastBidder = null;
		satisfiedPlayers = Collections.synchronizedList(new ArrayList<GamePlayer>());

		sendAuctionInitiatedToPlayers();
		sendAuctionTurnToPlayers();
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

		if (!checkAndCompleteAuction()) {
			moveToNextBidder();
		}
	}

	public void skip(GamePlayer player) throws MonopolyException {
		if (!getCurrentBidder().equals(player)) {
			throw new MonopolyException(PacketType.ERR_NOT_BID_TURN);
		}

		setSatisfied(player);
		sendBidSkipToPlayers(player);

		if (!checkAndCompleteAuction()) {
			moveToNextBidder();
		}
	}

	private void setSatisfied(GamePlayer player) {
		if (!satisfiedPlayers.contains(player))
			satisfiedPlayers.add(player);
	}

	/**
	 * @return 0 if everyone skipped, 1 if there is a bidder and everyone else has
	 *         skipped, or -1 if the auction should continue
	 */
	private int checkAuctionStatus() {
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

	private void moveToNextBidder() {
		do {
			bidderIndex = (bidderIndex + 1) % game.getPlayersByTurn().size();
		} while (satisfiedPlayers.contains(getCurrentBidder()));
		sendAuctionTurnToPlayers();
	}

	private boolean checkAndCompleteAuction() throws MonopolyException {
		int status = checkAuctionStatus();

		if (status == -1) {
			// Auction should continue
			return false;
		}

		if (status == 1) {
			// lastbidder won the auction
			lastBidder.changeBalance(-currentBid);
			item.setOwner(lastBidder);
		}

		game.finishAuction();
		sendAuctionCompleteToPlayers();
		return true;
	}

	private GamePlayer getCurrentBidder() {
		return game.getPlayersByTurn().get(bidderIndex);
	}

	private void sendAuctionInitiatedToPlayers() {
		game.getPlayersByTurn().parallelStream()
				.forEach(player -> GameServer.getInstance().sendAuctionInitiatedNotification(item, player));
	}

	private void sendAuctionTurnToPlayers() {
		game.getPlayersByTurn().parallelStream()
				.forEach(player -> GameServer.getInstance().sendAuctionTurnNotification(getCurrentBidder(), player));
	}

	private void sendBidUpdateToPlayers(GamePlayer playerBid, int bidAmount) {
		game.getPlayersByTurn().parallelStream()
				.forEach(player -> GameServer.getInstance().sendUpdatedBidNotification(playerBid, player, bidAmount));
	}

	private void sendBidSkipToPlayers(GamePlayer playerSkipped) {
		game.getPlayersByTurn().parallelStream()
				.forEach(player -> GameServer.getInstance().sendSkipBidNotification(playerSkipped, player));
	}

	private void sendAuctionCompleteToPlayers() {
		game.getPlayersByTurn().parallelStream()
				.forEach(player -> GameServer.getInstance().sendAuctionCompleteNotification(player));
	}
}
