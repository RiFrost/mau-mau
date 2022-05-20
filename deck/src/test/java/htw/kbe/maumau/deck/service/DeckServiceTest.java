package htw.kbe.maumau.deck.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Label;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.deck.domain.Deck;
import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;
import htw.kbe.maumau.deck.fixtures.CardsFixture;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.*;

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
    @DisplayName("Should create the deck when validation has passed successfully.")
    public void testCreateDeckWhenValidCardStack() throws IllegalDeckSizeException {
        Deck deck = new Deck();
        deck.setDrawPile(cards);

        Deck actualDeck = service.createDeck(cards); // maybe have to mock cardservice because it is used from validateDeck (to test component isolated)

        assertAll(
                () -> assertEquals(31, actualDeck.getDrawPile().size()),
                () -> assertTrue(Objects.nonNull(actualDeck.getTopCard())),
                () -> assertTrue(actualDeck.getDiscardPile().isEmpty())
        );
    }

    @Test
    @DisplayName("Should throw exception when delivered card stack is empty.")
    public void testThrowExceptionWhenCardStackIsEmpty() throws IllegalDeckSizeException {
            Exception exception = assertThrows(IllegalDeckSizeException.class, () -> {
            service.createDeck(new ArrayList<>());
        });

        String expectedMessage = "The number of cards does not match with 32";
        String actualMessage = exception.getMessage();

        Assert.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Should throw exception when delivered card stack has an invalid suit label ratio.")
    public void testThrowExceptionWhenSuitLabelOfRatioIsInvalid() throws IllegalDeckSizeException {
        cards = CardsFixture.cards();
        cards.remove(0);
        cards.add(new Card(cards.get(0).getSuit(), cards.get(0).getLabel()));

        Exception exception = assertThrows(IllegalDeckSizeException.class, () -> {
            service.createDeck(cards);
        });

        String expectedMessage = "Ratio of Suit and Label is not valid";
        String actualMessage = exception.getMessage();

        Assert.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Should deal the required number of drawn cards at the beginning of the game.")
    public void testInitialCardDealing() {
        Deck deck = new Deck();
        Collections.shuffle(cards);
        Card lastCard = cards.get((cards.size()-1));
        deck.setDiscardPile(Arrays.asList(lastCard));
        deck.setTopCard(lastCard);
        cards.remove(lastCard);
        deck.setDrawPile(cards);
        ArrayList<Card> expectedInitialCardsForPlayer = new ArrayList<>(cards.subList(0, deck.getNumberOfInitialCardsPerPlayer()));

        List<Card> actualInitialCardsForPlayer = service.initialCardDealing(deck);

        Assert.assertEquals(deck.getNumberOfInitialCardsPerPlayer(), actualInitialCardsForPlayer.size());
        Assert.assertEquals(26, deck.getDrawPile().size());
        Assert.assertEquals(expectedInitialCardsForPlayer, actualInitialCardsForPlayer);
    }

    @Test
    @DisplayName("Should deal the number of drawn cards and when the draw pile does not have enough cards the discard pile is set to the draw pile.")
    public void testGetDrawnCardWhenDrawPileIsHigherThenDiscardPile(){
        Deck deck = new Deck();
        deck.setDrawPile(new ArrayList<>(Arrays.asList(new Card(Suit.HEARTS, Label.ASS))));
        deck.setDiscardPile(new ArrayList<>(Arrays.asList(new Card(Suit.DIAMONDS, Label.ASS), new Card(Suit.CLUBS, Label.ASS))));

        List<Card> actualDrawnCards = service.getCardsFromDrawPile(deck, 2);

        Assert.assertEquals(2, actualDrawnCards.size());
        Assert.assertEquals(1, deck.getDrawPile().size());
        Assert.assertTrue(deck.getDiscardPile().isEmpty());
    }

    @Test
    @DisplayName("Should deal the number of drawn cards.")
    public void testGetDrawnCardsWhenDrawPileIsLowerThenDiscardPile(){
        Deck deck = new Deck();
        deck.setDrawPile(new ArrayList<>(Arrays.asList(new Card(Suit.DIAMONDS, Label.ASS), new Card(Suit.CLUBS, Label.ASS))));
        deck.setDiscardPile(new ArrayList<>(Arrays.asList(new Card(Suit.HEARTS, Label.ASS))));

        List<Card> actualDrawnCards = service.getCardsFromDrawPile(deck, 1);

        Assert.assertEquals(new ArrayList<>(Arrays.asList(new Card(Suit.DIAMONDS, Label.ASS))), actualDrawnCards);
        Assert.assertEquals(Arrays.asList(new Card(Suit.CLUBS, Label.ASS)), deck.getDrawPile());
        Assert.assertEquals(Arrays.asList(new Card(Suit.HEARTS, Label.ASS)), deck.getDiscardPile());
    }

    @Test
    @DisplayName("Should set a new card to the top card and add previous card to discard pile.")
    public void testSetDiscardedCardToTopCard(){
        Deck deck = new Deck();
        deck.setDiscardPile(new ArrayList<>(Arrays.asList(new Card(Suit.DIAMONDS, Label.ASS))));
        deck.setTopCard(new Card(Suit.SPADES, Label.EIGHT));
        Card discardedCard = new Card(Suit.CLUBS, Label.SEVEN);

        Card actualTopCard = service.setCardToTopCard(deck, discardedCard);

        Assert.assertEquals(discardedCard, actualTopCard);
        Assert.assertEquals(new ArrayList<>(Arrays.asList(new Card(Suit.DIAMONDS, Label.ASS), new Card(Suit.SPADES, Label.EIGHT))), deck.getDiscardPile());
    }

}
