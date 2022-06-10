package htw.kbe.maumau.deck.service;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Label;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.card.service.CardServiceImpl;
import htw.kbe.maumau.deck.export.Deck;
import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;
import htw.kbe.maumau.deck.fixtures.CardsFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeckServiceTest {

    @InjectMocks
    private DeckServiceImpl service;
    @Mock
    private CardServiceImpl cardService;
    private List<Card> cards;

    @BeforeEach
    public void setUp() {
        this.cards = CardsFixture.cards();
    }

    @Test
    @DisplayName("Should create the deck when validation has passed successfully.")
    public void testCreateDeckWhenValidCardStack() throws IllegalDeckSizeException {
        Deck deck = new Deck();
        deck.setDrawPile(cards);

        Deck actualDeck = service.createDeck(cards);

        assertAll(
                () -> assertEquals(31, actualDeck.getDrawPile().size()),
                () -> assertTrue(Objects.nonNull(actualDeck.getTopCard())),
                () -> assertTrue(actualDeck.getDiscardPile().isEmpty())
        );
    }

    @Test
    @DisplayName("Should throw exception when delivered card stack is empty.")
    public void testThrowExceptionWhenCardStackIsEmpty() {
        Exception exception = assertThrows(IllegalDeckSizeException.class, () -> {
            service.createDeck(new ArrayList<>());
        });

        String expectedMessage = "The number of cards does not match with 32";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Should throw exception when delivered card stack has an invalid suit label ratio.")
    public void testThrowExceptionWhenSuitLabelOfRatioIsInvalid() {
        cards = CardsFixture.cards();
        cards.remove(0);
        cards.add(new Card(cards.get(0).getSuit(), cards.get(0).getLabel()));
        Mockito.when(cardService.getSuits()).thenReturn(CardsFixture.suits);
        Mockito.when(cardService.getLabels()).thenReturn(CardsFixture.labels);

        Exception exception = assertThrows(IllegalDeckSizeException.class, () -> {
            service.createDeck(cards);
        });

        String expectedMessage = "Ratio of Suit and Label is not valid";
        String actualMessage = exception.getMessage();

        verify(cardService, times(4)).getSuits();
        verify(cardService, times(8)).getLabels();
        assertTrue(actualMessage.contains(expectedMessage));
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

        assertEquals(deck.getNumberOfInitialCardsPerPlayer(), actualInitialCardsForPlayer.size());
        assertEquals(26, deck.getDrawPile().size());
        assertEquals(expectedInitialCardsForPlayer, actualInitialCardsForPlayer);
    }

    @Test
    @DisplayName("Should deal the number of drawn cards and when the draw pile does not have enough cards the discard pile is set to the draw pile.")
    public void testGetDrawnCardWhenDrawPileIsHigherThenDiscardPile(){
        Deck deck = new Deck();
        deck.setDrawPile(new ArrayList<>(Arrays.asList(new Card(Suit.HEARTS, Label.ASS))));
        deck.setDiscardPile(new ArrayList<>(Arrays.asList(new Card(Suit.DIAMONDS, Label.ASS), new Card(Suit.CLUBS, Label.ASS))));

        List<Card> actualDrawnCards = service.getCardsFromDrawPile(deck, 2);

        assertEquals(2, actualDrawnCards.size());
        assertEquals(1, deck.getDrawPile().size());
        assertTrue(deck.getDiscardPile().isEmpty());
    }

    @Test
    @DisplayName("Should deal the number of drawn cards.")
    public void testGetDrawnCardsWhenDrawPileIsLowerThenDiscardPile(){
        Deck deck = new Deck();
        deck.setDrawPile(new ArrayList<>(Arrays.asList(new Card(Suit.DIAMONDS, Label.ASS), new Card(Suit.CLUBS, Label.ASS))));
        deck.setDiscardPile(new ArrayList<>(Arrays.asList(new Card(Suit.HEARTS, Label.ASS))));

        List<Card> actualDrawnCards = service.getCardsFromDrawPile(deck, 1);

        assertEquals(new ArrayList<>(Arrays.asList(new Card(Suit.DIAMONDS, Label.ASS))), actualDrawnCards);
        assertEquals(Arrays.asList(new Card(Suit.CLUBS, Label.ASS)), deck.getDrawPile());
        assertEquals(Arrays.asList(new Card(Suit.HEARTS, Label.ASS)), deck.getDiscardPile());
    }

    @Test
    @DisplayName("Should set a new card to the top card and add previous card to discard pile.")
    public void testSetDiscardedCardToTopCard(){
        Deck deck = new Deck();
        deck.setDiscardPile(new ArrayList<>(Arrays.asList(new Card(Suit.DIAMONDS, Label.ASS))));
        deck.setTopCard(new Card(Suit.SPADES, Label.EIGHT));
        Card discardedCard = new Card(Suit.CLUBS, Label.SEVEN);

        Card actualTopCard = service.setCardToTopCard(deck, discardedCard);

        assertEquals(discardedCard, actualTopCard);
        assertEquals(new ArrayList<>(Arrays.asList(new Card(Suit.DIAMONDS, Label.ASS), new Card(Suit.SPADES, Label.EIGHT))), deck.getDiscardPile());
    }

}
