package htw.kbe.maumau.virtualPlayer.service;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.CardService;
import htw.kbe.maumau.card.export.Label;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.player.export.Player;
import htw.kbe.maumau.virtualPlayer.fixtures.CardsFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VirtualPlayerServiceImplTest {

    @InjectMocks
    private AIServiceImpl aiService;

    @Mock
    private CardService cardService;

    private Player aiPlayer;

    @BeforeEach
    public void setUp() {
        aiPlayer = new Player("Mr Robot");
    }

    @Test
    @DisplayName("should return card that matches label of top card")
    public void testPlayValidCardLabel() {
        List<Card> cards = Arrays.asList(new Card(Suit.CLUBS, Label.SEVEN), new Card(Suit.DIAMONDS, Label.NINE));
        aiPlayer.setHandCards(cards);
        Card topCard = new Card(Suit.HEARTS, Label.NINE);

        assertEquals(new Card(Suit.DIAMONDS, Label.NINE), aiService.getPlayedCard(aiPlayer, topCard, null));
    }

    @Test
    @DisplayName("should return card that matches suit of top card")
    public void testPlayValidCardSuit() {
        List<Card> cards = Arrays.asList(new Card(Suit.CLUBS, Label.SEVEN), new Card(Suit.DIAMONDS, Label.QUEEN));
        aiPlayer.setHandCards(cards);
        Card topCard = new Card(Suit.CLUBS, Label.NINE);

        assertEquals(new Card(Suit.CLUBS, Label.SEVEN), aiService.getPlayedCard(aiPlayer, topCard, null));
    }

    @Test
    @DisplayName("should return null when label or suit of top card does not match players hand cards")
    public void testPlayValidCardWithoutSuitWish() {
        List<Card> cards = Arrays.asList(new Card(Suit.CLUBS, Label.SEVEN), new Card(Suit.DIAMONDS, Label.QUEEN));
        aiPlayer.setHandCards(cards);
        Card topCard = new Card(Suit.HEARTS, Label.NINE);

        assertNull(aiService.getPlayedCard(aiPlayer, topCard, null));
    }

    @Test
    @DisplayName("should return card that matches suit wish")
    public void testPlayValidCardWithGivenSuitWish() {
        List<Card> cards = Arrays.asList(new Card(Suit.CLUBS, Label.SEVEN), new Card(Suit.SPADES, Label.ASS));
        aiPlayer.setHandCards(cards);
        Card topCard = new Card(Suit.HEARTS, Label.JACK);

        assertEquals(new Card(Suit.SPADES, Label.ASS), aiService.getPlayedCard(aiPlayer, topCard, Suit.SPADES));
    }

    @Test
    @DisplayName("should only return a suit that is also given in players hand cards")
    public void testSuitWish() {
        List<Card> cards = Arrays.asList(new Card(Suit.CLUBS, Label.SEVEN));
        aiPlayer.setHandCards(cards);
        when(cardService.getSuits()).thenReturn(CardsFixture.suits);

        assertEquals(Suit.CLUBS, aiService.getSuitWish(aiPlayer));
    }
}
