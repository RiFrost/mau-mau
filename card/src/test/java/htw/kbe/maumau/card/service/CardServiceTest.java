package htw.kbe.maumau.card.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Label;
import htw.kbe.maumau.card.domain.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.List;
import static org.junit.Assert.*;

public class CardServiceTest {

    private CardService cardService;

    @BeforeEach
    public void setUp() {
        cardService = new CardServiceImpl();
    }

    @Test
    @DisplayName("should return a list of all suits")
    public void getAllSuits() {
        int expectedLength = 4;
        String[] expectedSuits = {"CLUBS", "SPADES", "HEARTS", "DIAMONDS"};

        List<Suit> suits = cardService.getSuits();

        assertEquals(expectedLength, suits.size());
        for(int i = 0; i < suits.size(); i++) {
            assertEquals(expectedSuits[i], suits.get(i).toString());
        }
    }

    @Test
    @DisplayName("should return a list of all labels")
    public void getAllLabels() {
        int expectedLength = 8;
        String[] expectedLabels = {
            "ASS",
            "KING",
            "QUEEN",
            "JACK",
            "TEN",
            "NINE",
            "EIGHT",
            "SEVEN"
        };

        List<Label> labels = cardService.getLabels();

        assertEquals(expectedLength, labels.size());
        for(int i = 0; i < labels.size(); i++) {
            assertEquals(expectedLabels[i], labels.get(i).toString());
        }
    }

    @Test
    @DisplayName("should return a card list with 32 cards for every suit and label combination")
    public void getCardList() {
        int expectedLength = 32;
        Card topCard = new Card(Suit.CLUBS, Label.ASS);
        Card bottomCard = new Card(Suit.DIAMONDS, Label.SEVEN);

        List<Card> cards = cardService.getCards();

        assertEquals(expectedLength, cards.size());
        assertEquals(topCard, cards.get(0));
        assertEquals(bottomCard, cards.get(cards.size() - 1));
    }
}
