package monopoly.gameplay.cards;

import java.util.ArrayList;
import java.util.Collections;

import lombok.Getter;
import monopoly.gameplay.GamePlayer;

@Getter
public class Deck {
	private ArrayList<Card> cards;

	public Deck() {
		cards = new ArrayList<>();
		// Json parser for adding card descriptions etc go here
	}

	public void shuffle() {
		Collections.shuffle(cards);
	}

	public Card takeTop(GamePlayer owner) {
		Card returned = cards.get(0);
		if (returned instanceof GetOutOfJailCard)
			((GetOutOfJailCard) returned).setOwner(owner);
		cards.remove(returned);
		return returned;
	}

	public void returnBottom(Card card) {
		cards.add(card);
	}
}
