package htw.kbe.maumau.card.domain;

import java.util.Objects;

public class Card {

    private Suit suit;
    private Label label;

    public Card(Suit suit, Label label) {
        this.suit = suit;
        this.label = label;
    }

    public Suit getSuit() {
        return suit;
    }

    public Label getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return String.format("%s of %s", label, suit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return suit == card.suit && label == card.label;
    }

}
