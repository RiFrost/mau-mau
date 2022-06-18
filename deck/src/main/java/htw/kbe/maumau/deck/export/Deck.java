package htw.kbe.maumau.deck.export;

import htw.kbe.maumau.card.export.Card;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    private List<Card> drawPile;
    private List<Card> discardPile;
    private long limitOfCardStack = 32;
    private int numberOfInitialCardsPerPlayer = 5;
    private Card topCard;

    public Deck() {
        this.drawPile = new ArrayList<>();
        this.discardPile = new ArrayList<>();
    }

    public List<Card> getDrawPile() {
        return drawPile;
    }

    public void setDrawPile(List<Card> drawPile) {
        this.drawPile = drawPile;
    }

    public List<Card> getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(List<Card> discardPile) {
        this.discardPile = discardPile;
    }

    public long getLimitOfCardStack() {
        return limitOfCardStack;
    }

    public Card getTopCard() {
        return topCard;
    }

    public void setTopCard(Card topCard) {
        this.topCard = topCard;
    }

    public int getNumberOfInitialCardsPerPlayer() {
        return numberOfInitialCardsPerPlayer;
    }

    @Override
    public String toString() {
        return  "drawPile=" + drawPile +
                ", discardPile=" + discardPile +
                ", limitOfCardStack=" + limitOfCardStack +
                ", numberOfInitialCardsPerPlayer=" + numberOfInitialCardsPerPlayer +
                ", topCard=" + topCard;
    }
}
