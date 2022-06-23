package htw.kbe.maumau.deck.export;

import htw.kbe.maumau.card.export.Card;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Deck {

    @Id
    @GeneratedValue
    private Long id;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private List<Card> drawPile;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private List<Card> discardPile;
    @Column(nullable = false)
    private long limitOfCardStack = 32;
    @Column(nullable = false)
    private int numberOfInitialCardsPerPlayer = 5;
    @OneToOne
    private Card topCard;

    public Deck() {
        this.drawPile = new ArrayList<>();
        this.discardPile = new ArrayList<>();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLimitOfCardStack(long limitOfCardStack) {
        this.limitOfCardStack = limitOfCardStack;
    }

    public void setNumberOfInitialCardsPerPlayer(int numberOfInitialCardsPerPlayer) {
        this.numberOfInitialCardsPerPlayer = numberOfInitialCardsPerPlayer;
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
        return "drawPile=" + drawPile +
                ", discardPile=" + discardPile +
                ", limitOfCardStack=" + limitOfCardStack +
                ", numberOfInitialCardsPerPlayer=" + numberOfInitialCardsPerPlayer +
                ", topCard=" + topCard;
    }
}
