package htw.kbe.maumau.domain;

import java.util.ArrayList;
import java.util.List;

public class Deck implements IDeck {

    private List<Card> cards;
    private long totalAmount;

    private Deck(List<Card> cards) {
        this.cards = cards;
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

    @Override
    public void shuffle() {
        for(int i = 0; i < this.cards.size(); i++) {
            int randomIndex = (int) (Math.random() * this.cards.size());
            Card newCard = this.cards.get(randomIndex);
            this.cards.set(randomIndex, this.cards.get(i));
            this.cards.set(i, newCard);
        }
    }

    @Override
    public List<Card> drawCards(int amount) {
        List<Card> returnList = new ArrayList<Card>();

        while(amount > 0) {
            try {
                returnList.add(cards.remove(0));
            } catch(Exception e) {
                System.out.println("error deck is empty");
            }

            amount--;
        }

        return returnList;
    }

    public List<Card> getCards() {
        return this.cards;
    }

    public long getTotalAmount() {
        return this.cards.stream().count();
    }
}
