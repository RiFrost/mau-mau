package htw.kbe.maumau.card.domain;

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
}
