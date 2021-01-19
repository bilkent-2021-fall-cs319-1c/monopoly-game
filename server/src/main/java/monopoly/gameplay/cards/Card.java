package monopoly.gameplay.cards;

import lombok.Getter;
import monopoly.gameplay.Actionable;
import monopoly.gameplay.GamePlayer;
import monopoly.network.packet.important.packet_data.PacketData;

@Getter
public class Card implements Actionable {
	private String description;
	private CardType cardType;
	private int cardVersion;

	public Card(String description, CardType cardType, int cardVersion) {
		this.description = description;
		this.cardType = cardType;
		this.cardVersion = cardVersion;
	}

	// For filler cards and get out of jail card
	public Card(String description, CardType cardType) {
		this(description, cardType, -1);
	}

	@Override
	public void doAction(GamePlayer player) {
		throw new UnsupportedOperationException();
	}

	public PacketData getAsPacket() {
		// TODO
		return null;
	}
}
