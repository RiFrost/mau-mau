package htw.kbe.maumau.card.service;

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
            assertEquals(suits.get(i).toString(), expectedSuits[i]);
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
            assertEquals(labels.get(i).toString(), expectedLabels[i]);
        }
    }
}
