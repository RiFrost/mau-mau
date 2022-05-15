package htw.kbe.maumau.deck.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Label;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.deck.domain.Deck;
import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        suits.stream().forEach(suit -> {
            labels.stream().forEach(label -> cards.add((new Card(suit, label))));
        });

        Deck deck = new Deck(new ArrayList<>());
        deck.setDiscardPile(new ArrayList<>(cards));
        List <Card> actualDiscardPile = service.shuffleDiscardPile(deck);

        Assert.assertNotEquals(cards, actualDiscardPile);
    }

    @Test
    @DisplayName("")
    public void testCreateDeckWhenValidCardStack() throws IllegalDeckSizeException {
        List<Card> cards = new ArrayList<>();
        List<Suit> suits = Suit.getSuits();
        List<Label> labels = Label.getLabels();
        suits.stream().forEach(suit -> {
            labels.stream().forEach(label -> cards.add((new Card(suit, label))));
        });
        Deck expectedDeck = new Deck(cards);

        Deck actualDeck = service.createDeck(cards);

        assertAll(() -> assertEquals(expectedDeck.getDrawPile(), actualDeck.getDrawPile()),
                () -> assertEquals(expectedDeck.getDiscardPile(), actualDeck.getDiscardPile()));
    }

    @Test
    @DisplayName("")
    public void testThrowExceptionWhenCardStackIsLow() throws IllegalDeckSizeException {
        Exception exception = assertThrows(IllegalDeckSizeException.class, () -> {
            service.createDeck(new ArrayList<>());
        });

        String expectedMessage = "The number of cards for the deck fall below the limit of 32";
        String actualMessage = exception.getMessage();

        Assert.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("")
    public void testThrowExceptionWhenLabelAndSuitIsInvalid() throws IllegalDeckSizeException {
        List<Card> cards = new ArrayList<>();
        List<Suit> suits = Suit.getSuits();
        List<Label> labels = Label.getLabels();
        suits.remove(0);
        labels.remove(0);
        suits.stream().forEach(suit -> {
            labels.stream().forEach(label -> cards.add((new Card(suit, label))));
        });

        Exception exception = assertThrows(IllegalDeckSizeException.class, () -> {
            service.createDeck(cards);
        });

        String expectedMessage = "Ratio of Suit and Label is not valid";
        String actualMessage = exception.getMessage();

        Assert.assertTrue(actualMessage.contains(expectedMessage));
    }

}
