package htw.kbe.maumau.deck.fixtures;
import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Label;
import htw.kbe.maumau.card.domain.Suit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardsFixture {

    public static List<Suit> suits = Arrays.asList(
            Suit.CLUBS,
            Suit.DIAMONDS,
            Suit.HEARTS,
            Suit.SPADES
    );

    public static List<Label> labels = Arrays.asList(
            Label.ASS,
            Label.KING,
            Label.QUEEN,
            Label.JACK,
            Label.TEN,
            Label.NINE,
            Label.EIGHT,
            Label.SEVEN
    );

    public static List<Card> cards() {
        List<Card> cards = new ArrayList<Card>();
        for(Suit suit : suits) {
            for(Label label : labels) {
                cards.add(new Card(suit, label));
            }
        }

        return cards;
    }

}
