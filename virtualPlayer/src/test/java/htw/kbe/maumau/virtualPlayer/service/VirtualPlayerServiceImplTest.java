package htw.kbe.maumau.virtualPlayer.service;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.CardService;
import htw.kbe.maumau.card.export.Label;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.player.export.Player;
import htw.kbe.maumau.rule.export.RulesService;
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

    @Mock
    private RulesService rulesService;

    private Player aiPlayer;

    @BeforeEach
    public void setUp() {
        aiPlayer = new Player("Mr Robot");
    }

    @Test
    @DisplayName("should play card that matches label of top card")
    public void testPlayValidCardLabel() {
        List<Card> cards = Arrays.asList(new Card(Suit.CLUBS, Label.SEVEN), new Card(Suit.DIAMONDS, Label.NINE));
        aiPlayer.setHandCards(cards);
        Card topCard = new Card(Suit.HEARTS, Label.NINE);
        when(rulesService.matchLabelOrSuit(any(), any())).thenReturn(false, true);
        when(rulesService.isSuitWishValid(any(), any())).thenReturn(false, false);
        when(rulesService.canPlaySeven(any(), any(), anyInt())).thenReturn(false);
        when(rulesService.isJackOnJack(any(), any())).thenReturn(false);

        assertEquals(new Card(Suit.DIAMONDS, Label.NINE), aiService.getPlayedCard(aiPlayer, topCard, null, 0));
    }

    @Test
    @DisplayName("should play card that matches suit of top card")
    public void testPlayValidCardSuit() {
        List<Card> cards = Arrays.asList(new Card(Suit.CLUBS, Label.SEVEN), new Card(Suit.DIAMONDS, Label.QUEEN));
        aiPlayer.setHandCards(cards);
        Card topCard = new Card(Suit.CLUBS, Label.NINE);
        when(rulesService.matchLabelOrSuit(any(), any())).thenReturn(true);
        when(rulesService.canPlaySeven(any(), any(), anyInt())).thenReturn(false);
        when(rulesService.isJackOnJack(any(), any())).thenReturn(false);

        assertEquals(new Card(Suit.CLUBS, Label.SEVEN), aiService.getPlayedCard(aiPlayer, topCard, null, 0));
    }

    @Test
    @DisplayName("should return null when label or suit of top card does not match players hand cards")
    public void testPlayValidCardWithoutSuitWish() {
        List<Card> cards = Arrays.asList(new Card(Suit.CLUBS, Label.SEVEN), new Card(Suit.DIAMONDS, Label.QUEEN));
        aiPlayer.setHandCards(cards);
        Card topCard = new Card(Suit.HEARTS, Label.NINE);
        when(rulesService.matchLabelOrSuit(any(), any())).thenReturn(false, false);
        when(rulesService.isSuitWishValid(any(), any())).thenReturn(false, false);

        assertNull(aiService.getPlayedCard(aiPlayer, topCard, null, 0));
    }

    @Test
    @DisplayName("should play card that matches suit wish")
    public void testPlayValidCardWithGivenSuitWish() {
        List<Card> cards = Arrays.asList(new Card(Suit.CLUBS, Label.SEVEN), new Card(Suit.SPADES, Label.ASS));
        aiPlayer.setHandCards(cards);
        Card topCard = new Card(Suit.HEARTS, Label.JACK);
        when(rulesService.matchLabelOrSuit(any(), any())).thenReturn(false, true);
        when(rulesService.isSuitWishValid(any(), any())).thenReturn(false, true);
        when(rulesService.canPlaySeven(any(), any(), anyInt())).thenReturn(false);
        when(rulesService.isJackOnJack(any(), any())).thenReturn(false);

        assertEquals(new Card(Suit.SPADES, Label.ASS), aiService.getPlayedCard(aiPlayer, topCard, Suit.SPADES, 0));
    }

    @Test
    @DisplayName("should play card with label SEVEN when top card label is SEVEN too and draw counter is greater than 0")
    public void testMustPlaySeven() {
        List<Card> cards = Arrays.asList(new Card(Suit.SPADES, Label.ASS), new Card(Suit.CLUBS, Label.SEVEN));
        aiPlayer.setHandCards(cards);
        Card topCard = new Card(Suit.HEARTS, Label.SEVEN);
        when(rulesService.matchLabelOrSuit(any(), any())).thenReturn(false, true);
        when(rulesService.isSuitWishValid(any(), any())).thenReturn(false, false);
        when(rulesService.canPlaySeven(any(), any(), anyInt())).thenReturn(false,true);

        assertEquals(new Card(Suit.CLUBS, Label.SEVEN), aiService.getPlayedCard(aiPlayer, topCard, null, 2));
    }


    @Test
    @DisplayName("should return true when ai player has only one hand card left")
    public void testMauState() {
        List<Card> cards = Arrays.asList(new Card(Suit.CLUBS, Label.SEVEN));
        aiPlayer.setHandCards(cards);

        assertTrue(aiService.sayMau(aiPlayer));
    }

    @Test
    @DisplayName("should return false when ai player has more than one hand card left")
    public void testMauState1() {
        List<Card> cards = Arrays.asList(new Card(Suit.CLUBS, Label.SEVEN), new Card(Suit.SPADES, Label.ASS));
        aiPlayer.setHandCards(cards);

        assertFalse(aiService.sayMau(aiPlayer));
    }

    @Test
    @DisplayName("should only choose a suit that is also given in players hand cards")
    public void testSuitWish() {
        List<Card> cards = Arrays.asList(new Card(Suit.CLUBS, Label.SEVEN));
        aiPlayer.setHandCards(cards);
        when(cardService.getSuits()).thenReturn(CardsFixture.suits);

        assertEquals(Suit.CLUBS, aiService.getSuitWish(aiPlayer));
    }
}
