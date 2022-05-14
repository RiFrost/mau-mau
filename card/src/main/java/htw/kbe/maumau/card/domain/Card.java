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

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }
}
