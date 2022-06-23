package htw.kbe.maumau.card.export;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Card implements Comparable<Card> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long id;
    @Column(nullable = false)
    private Suit suit;
    @Column(nullable = false)
    private Label label;

    public Card() {
    }

    public Card(Suit suit, Label label) {
        this.suit = suit;
        this.label = label;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public void setLabel(Label label) {
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

    @Override
    public int hashCode() {
        return Objects.hash(suit, label);
    }

    @Override
    public int compareTo(Card o) {
        if(this.equals(o)) return 0;
        if(this.suit.value > o.suit.value) return -1;
        else if(this.suit.value == o.suit.value) {
            return this.label.value > o.label.value ? -1 : 1;
        }

        return 1;
    }
}
