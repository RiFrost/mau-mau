package htw.kbe.maumau.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void shouldBuildDeck() {
        Deck deck = Deck.build();
        assertEquals(32, deck.getTotalAmount());

        int i = 0;
        for(Suit suit : Suit.values()) {
            for(Value value : Value.values()) {
                Card card = deck.getCard(i);
                assertEquals(suit, card.getSuit());
                assertEquals(value, card.getValue());
                i++;
            }
        }
    }

    @Test
    void drawCards() {
    }
}