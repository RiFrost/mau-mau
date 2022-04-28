package htw.kbe.maumau.domain;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    private List<Card> deckCards;
    private long totalAmount;
    private List<Card> discardPile;

    private Deck(List<Card> cards) {
        this.deckCards = cards;
        this.totalAmount = cards.stream().count();
    }

    public static Deck build() {
        List<Card> cards = new ArrayList<Card>();

        for(Suit suit : Suit.values()) {
            for(Value value : Value.values()) {
                cards.add(new Card(suit, value));
            }
        }

        return new Deck(cards);
    }

    public void shuffle() {
        for(int i = 0; i < this.deckCards.size(); i++) {
            int randomIndex = (int) (Math.random() * this.deckCards.size());
            Card newCard = this.deckCards.get(randomIndex);
            this.deckCards.set(randomIndex, this.deckCards.get(i));
            this.deckCards.set(i, newCard);
        }
    }

    public Card draw() {
        if(this.deckCards.size() > 0) return this.deckCards.remove(0);
        return null;
    }

    public Card getCard(int index) {
        return this.deckCards.get(index);
    }

    public List<Card> getCards() {
        return this.deckCards;
    }

    public long getTotalAmount() {
        return this.deckCards.stream().count();
    }
}
