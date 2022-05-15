package htw.kbe.maumau.deck.domain;

import htw.kbe.maumau.card.domain.Card;

import java.util.List;

public class Deck {

    private List<Card> drawPile;
    private List<Card> discardPile;
    private long limitOfCardStack = 32;

    public Deck(List<Card> drawPile) {

        this.drawPile = drawPile;
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

    public void setLimitOfCardStack(long limitOfCardStack) {
        this.limitOfCardStack = limitOfCardStack;
    }

}
