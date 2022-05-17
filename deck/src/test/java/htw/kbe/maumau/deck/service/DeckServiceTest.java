package htw.kbe.maumau.deck.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.deck.domain.Deck;
import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;
import htw.kbe.maumau.deck.fixtures.CardsFixture;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DeckServiceTest {

    private DeckService service;
    private List<Card> cards;

    @BeforeEach
    public void setUp() {
        this.service = new DeckServiceImpl();
        this.cards = CardsFixture.cards();
    }

    @Test
    @DisplayName("should randomly mix the discard pile")
    public void testShuffleDiscardPile() {
        Deck deck = new Deck(new ArrayList<>());
        deck.setDiscardPile(new ArrayList<>(cards));
        List <Card> actualDiscardPile = service.shuffleDiscardPile(deck);

        Assert.assertNotEquals(cards, actualDiscardPile);
    }

    @Test
    @DisplayName("should create a deck validate check passes")
    public void testCreateDeckWhenValidCardStack() throws IllegalDeckSizeException {
        Deck expectedDeck = new Deck(cards);

        Deck actualDeck = service.createDeck(cards); // maybe have to mock cardservice because it is used from validateDeck (to test component isolated)

        assertAll(
                () -> assertEquals(expectedDeck.getDrawPile(), actualDeck.getDrawPile()),
                () -> assertEquals(expectedDeck.getDiscardPile(), actualDeck.getDiscardPile())
        );
    }

    @Test
    @DisplayName("should throw exception when card stack is to low to create a deck")
    public void testThrowExceptionWhenCardStackIsLow() throws IllegalDeckSizeException {
        Exception exception = assertThrows(IllegalDeckSizeException.class, () -> {
            service.createDeck(new ArrayList<>());
        });

        String expectedMessage = "The number of cards for the deck fall below the limit of 32";
        String actualMessage = exception.getMessage();

        Assert.assertTrue(actualMessage.contains(expectedMessage));
    }

}
