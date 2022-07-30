package htw.kbe.maumau.virtualPlayer.service;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Label;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.player.export.Player;
import htw.kbe.maumau.rule.export.RulesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VirtualPlayerServiceImplTest {

    @InjectMocks
    private AIServiceImpl aiService;

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

        assertEquals(new Card(Suit.DIAMONDS, Label.NINE), aiService.getPlayedCard(aiPlayer, topCard, null, 0));
    }

    @Test
    @DisplayName("should play card that matches suit of top card")
    public void testPlayValidCardSuit() {
        List<Card> cards = Arrays.asList(new Card(Suit.CLUBS, Label.SEVEN), new Card(Suit.DIAMONDS, Label.QUEEN));
        aiPlayer.setHandCards(cards);
        Card topCard = new Card(Suit.CLUBS, Label.NINE);
        when(rulesService.matchLabelOrSuit(any(), any())).thenReturn(true);

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

        assertEquals(new Card(Suit.SPADES, Label.ASS), aiService.getPlayedCard(aiPlayer, topCard, Suit.SPADES, 0));
    }

    @Test
    @DisplayName("should return null when no suits of hand cards matches suit wish")
    public void testPlayValidCardWithGivenSuitWish1() {
        List<Card> cards = Arrays.asList(new Card(Suit.SPADES, Label.JACK), new Card(Suit.HEARTS, Label.ASS));
        aiPlayer.setHandCards(cards);
        Card topCard = new Card(Suit.HEARTS, Label.JACK);
        when(rulesService.isSuitWishValid(any(), any())).thenReturn(false, false);
        when(rulesService.matchLabelOrSuit(any(), any())).thenReturn(false, false);

        assertEquals(null, aiService.getPlayedCard(aiPlayer, topCard, Suit.DIAMONDS, 0));
    }

    @Test
    @DisplayName("should play card with label SEVEN when top card label is SEVEN too and draw counter is greater than 0")
    public void testMustPlaySeven() {
        List<Card> cards = Arrays.asList(new Card(Suit.SPADES, Label.ASS), new Card(Suit.CLUBS, Label.SEVEN));
        aiPlayer.setHandCards(cards);
        Card topCard = new Card(Suit.HEARTS, Label.SEVEN);
        when(rulesService.matchLabelOrSuit(any(), any())).thenReturn(false, true);
        when(rulesService.isSuitWishValid(any(), any())).thenReturn(false, false);
        when(rulesService.canPlaySeven(any(), any(), anyInt())).thenReturn(true);

        assertEquals(new Card(Suit.CLUBS, Label.SEVEN), aiService.getPlayedCard(aiPlayer, topCard, null, 2));
    }

    @Test
    @DisplayName("should return null when ai player cannot play a SEVEN but top card and draw counter requires it")
    public void testMustPlaySevenButCannot() {
        List<Card> cards = Arrays.asList(new Card(Suit.SPADES, Label.ASS), new Card(Suit.HEARTS, Label.EIGHT));
        aiPlayer.setHandCards(cards);
        Card topCard = new Card(Suit.HEARTS, Label.SEVEN);
        when(rulesService.matchLabelOrSuit(any(), any())).thenReturn(false, true);
        when(rulesService.isSuitWishValid(any(), any())).thenReturn(false, false);
        when(rulesService.canPlaySeven(any(), any(), anyInt())).thenReturn(false,false);

        assertNull(aiService.getPlayedCard(aiPlayer, topCard, null, 2));
    }


    @Test
    @DisplayName("should return true when ai player has only two hand cards left") // Because ai has two say mau before the card leaves the 'hand' so in order to be correct it has to say mau with 2 cards eft in 'hand'
    public void testMauState() {
        List<Card> cards = Arrays.asList(new Card(Suit.CLUBS, Label.SEVEN), new Card(Suit.CLUBS, Label.ASS));
        aiPlayer.setHandCards(cards);

        assertTrue(aiService.saidMau(aiPlayer));
    }

    @Test
    @DisplayName("should return false when ai player has more than two hand card left")
    public void testMauState1() {
        List<Card> cards = Arrays.asList(new Card(Suit.CLUBS, Label.SEVEN), new Card(Suit.SPADES, Label.ASS), new Card(Suit.SPADES, Label.KING));
        aiPlayer.setHandCards(cards);

        assertFalse(aiService.saidMau(aiPlayer));
    }

    @Test
    @DisplayName("should only choose a suit that is also given in players hand cards")
    public void testSuitWish() {
        List<Card> cards = Arrays.asList(new Card(Suit.CLUBS, Label.SEVEN));
        aiPlayer.setHandCards(cards);

        assertEquals(Suit.CLUBS, aiService.getSuitWish(aiPlayer));
    }

    @Test
    @DisplayName("should remove played card from hand cards")
    public void testRemovePlayedCardFromHandCards(){
        List <Card> handCards = new ArrayList<>(Arrays.asList(new Card(Suit.HEARTS, Label.SEVEN), new Card(Suit.CLUBS, Label.ASS)));
        aiPlayer.setHandCards(handCards);

        aiService.removePlayedCard(aiPlayer, new Card(Suit.HEARTS, Label.SEVEN));

        assertEquals(1, aiPlayer.getHandCards().size());
        assertEquals(Arrays.asList(new Card(Suit.CLUBS, Label.ASS)), aiPlayer.getHandCards());
    }

    @Test
    @DisplayName("should add drawn cards to hand cards")
    public void testAddDrawnCardsToHandCards(){
        List <Card> handCards = new ArrayList<>(Arrays.asList(new Card(Suit.HEARTS, Label.SEVEN), new Card(Suit.CLUBS, Label.ASS)));
        aiPlayer.setHandCards(handCards);

        aiService.addDrawnCards(aiPlayer, Arrays.asList(new Card(Suit.SPADES, Label.EIGHT)));

        assertEquals(3, aiPlayer.getHandCards().size());
    }
}
