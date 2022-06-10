package htw.kbe.maumau.controller.service;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Label;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.controller.utilities.UtilitiesHelper;
import htw.kbe.maumau.player.export.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ViewServiceImplTest {

    @InjectMocks
    private ViewServiceImpl service;

    @Mock
    private UtilitiesHelper mockedUtilitiesHelper;

    @Captor
    private ArgumentCaptor<Card> cardArgumentCaptor;

    @Test
    @DisplayName("should return given number from user input")
    public void getNumberOfPlayer() {
        when(mockedUtilitiesHelper.loadFromFile()).thenReturn("game instructions");
        when(mockedUtilitiesHelper.getChosenNumber(anyInt(), anyInt())).thenReturn(2);

        assertEquals(2, service.getNumberOfPlayer());
        verify(mockedUtilitiesHelper, times(1)).getChosenNumber(anyInt(), anyInt());
        verify(mockedUtilitiesHelper, times(1)).loadFromFile();
    }

    @Test
    @DisplayName("should call getCardImage for showing top card")
    public void showTopCard() {
        Card topCard = new Card(Suit.SPADES, Label.SEVEN);
        when(mockedUtilitiesHelper.getCardImage(any())).thenReturn("image");

        service.showTopCard(topCard);

        verify(mockedUtilitiesHelper, times(1)).getCardImage(argThat(
                card -> card.equals(topCard))
        );
    }

    @Test
    @DisplayName("should call getCardImage for showing all hand cards from player")
    public void showHandCards() {
        Player player = new Player("Phil");
        List<Card> handCards = List.of(new Card(Suit.SPADES, Label.SEVEN), new Card(Suit.CLUBS, Label.ASS));
        player.setHandCards(handCards);
        when(mockedUtilitiesHelper.getCardImage(any())).thenReturn("image");

        service.showHandCards(player, null);

        verify(mockedUtilitiesHelper, times(2)).getCardImage(cardArgumentCaptor.capture());
        List<Card> allValues = cardArgumentCaptor.getAllValues();
        assertEquals(handCards.get(0), allValues.get(0));
        assertEquals(handCards.get(1), allValues.get(1));
    }

    @Test
    @DisplayName("should create a new list of given player names")
    public void getPlayerNames() {
        List<String> expectedPlayerNames = List.of("Philipp", "Jasmin", "Richard", "Maria");

        when(mockedUtilitiesHelper.getPlayerName()).thenReturn(expectedPlayerNames.get(0),
                expectedPlayerNames.get(1),
                expectedPlayerNames.get(2),
                expectedPlayerNames.get(3));

        List<String> actualPlayerNames = service.getPlayerNames(4);

        verify(mockedUtilitiesHelper, times(4)).getPlayerName();
        assertEquals(4, actualPlayerNames.size());
        assertEquals(expectedPlayerNames, actualPlayerNames);
    }

    @Test
    @DisplayName("should create a Map with chosen Card to play and chosen 'mau' state")
    public void getChosenCardAndMauState() {
        Player player = new Player("Jasmin");
        player.setHandCards(List.of(new Card(Suit.CLUBS, Label.JACK), new Card(Suit.SPADES, Label.ASS)));
        when(mockedUtilitiesHelper.getChosenNumber(anyInt(), anyInt())).thenReturn(2, 1);

        Map<Card, Boolean> actualMap = service.getPlayedCard(player);

        assertTrue(actualMap.containsKey(new Card(Suit.SPADES, Label.ASS)));
        assertEquals(true, actualMap.get(new Card(Suit.SPADES, Label.ASS)));
        verify(mockedUtilitiesHelper, times(2)).getChosenNumber(anyInt(), anyInt());
    }

    @Test
    @DisplayName("should return the chosen suit wish from player")
    public void getChosenSuitWish() {
        List<Suit> suits = List.of(Suit.SPADES, Suit.CLUBS, Suit.DIAMONDS, Suit.HEARTS);
        Player player = new Player("Jasmin");
        when(mockedUtilitiesHelper.getChosenNumber(anyInt(), anyInt())).thenReturn(3);

        assertEquals(Suit.DIAMONDS, service.getChosenSuit(player, suits));
        verify(mockedUtilitiesHelper, times(1)).getChosenNumber(anyInt(), anyInt());
    }

    @Test
    @DisplayName("should return chosen 'mau' state from the player")
    public void getSaidMau() {
        Player player = new Player("Jasmin");
        when(mockedUtilitiesHelper.getChosenNumber(anyInt(), anyInt())).thenReturn(1, 2);

        assertTrue(service.saidMau(player));
        assertFalse(service.saidMau(player));
        verify(mockedUtilitiesHelper, times(2)).getChosenNumber(anyInt(), anyInt());
    }

    @Test
    @DisplayName("should return if player wants to draw a card or not")
    public void checkIfAPlayerWantsToDrawCards() {
        when(mockedUtilitiesHelper.getChosenNumber(anyInt(), anyInt())).thenReturn(2, 1);

        assertTrue(service.playerWantToDrawCards());
        assertFalse(service.playerWantToDrawCards());
        verify(mockedUtilitiesHelper, times(2)).getChosenNumber(anyInt(), anyInt());
    }

    @Test
    @DisplayName("should return if player wants to start a new round")
    public void checkIfPlayerWantNewRound() {
        when(mockedUtilitiesHelper.getChosenNumber(anyInt(), anyInt())).thenReturn(1, 2);

        assertTrue(service.hasNextRound());
        assertFalse(service.hasNextRound());
        verify(mockedUtilitiesHelper, times(2)).getChosenNumber(anyInt(), anyInt());
    }
}
