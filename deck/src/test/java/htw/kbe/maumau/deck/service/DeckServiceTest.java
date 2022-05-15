package htw.kbe.maumau.deck.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Label;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.deck.domain.Deck;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

public class DeckServiceTest {

    private DeckService service;

    @Before
    public void setUp() {
        this.service = new DeckServiceImpl();
    }

    @Test
    @DisplayName("should randomly mix the discard pile")
    public void testShuffleDiscardPile() {
        List<Card> cards = new ArrayList<>();
        List<Suit> suits = Suit.getSuits();
        List<Label> labels = Label.getLabels();
        for (Suit suit : suits) {
            for (Label label : labels) {
                cards.add(new Card(suit, label));
            }
        }
        Deck deck = new Deck(new ArrayList<>(cards), 32);

        List <Card> actualDrawPile = service.shuffleDiscardPile(deck);

        Assert.assertNotEquals(cards, actualDrawPile);
    }

}
