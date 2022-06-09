package htw.kbe.maumau.controller.service;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Label;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.controller.utilities.UtilitiesHelper;
import htw.kbe.maumau.player.export.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ViewServiceImplTest {

    @InjectMocks
    private ViewServiceImpl ui;

    @Mock
    private UtilitiesHelper mockedUtilitiesHelper;

//    @BeforeEach
//    void setUp() {
//        mockedUserInputValidation = mock(UserInputValidation.class);
//        ui = new UIImpl(mockedUserInputValidation);
//    }

    @Test
    @DisplayName("should return given number from user input")
    public void getNumberOfPlayer() {
        when(mockedUtilitiesHelper.getChosenNumber(anyInt(), anyInt())).thenReturn(2);

        assertEquals(2, ui.getNumberOfPlayer());
        verify(mockedUtilitiesHelper, times(1)).getChosenNumber(anyInt(), anyInt());
    }

    @Test
    @DisplayName("should create a new list of given player names")
    public void getPlayerNames() {
        List<String> expectedPlayerNames = List.of("Philipp", "Jasmin","Richard", "Maria");

        when(mockedUtilitiesHelper.getPlayerName()).thenReturn(expectedPlayerNames.get(0),
                expectedPlayerNames.get(1),
                expectedPlayerNames.get(2),
                expectedPlayerNames.get(3));

        List<String> actualPlayerNames = ui.getPlayerNames(4);

        verify(mockedUtilitiesHelper, times(4)).getPlayerName();
        assertEquals(4, actualPlayerNames.size());
        assertEquals(expectedPlayerNames, actualPlayerNames);
    }

    @Test
    @DisplayName("should create a Map with chosen Card to play and chosen 'mau' state")
    public void getChosenCardAndMauState() {
        Player player = new Player("Jasmin");
        player.setHandCards(List.of(new Card(Suit.CLUBS, Label.JACK), new Card(Suit.SPADES, Label.ASS)));
        when(mockedUtilitiesHelper.getChosenNumber(anyInt(),anyInt())).thenReturn(2, 1);

        Map<Card, Boolean> actualMap = ui.getPlayedCard(player);

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

        assertEquals(Suit.DIAMONDS, ui.getChosenSuit(player, suits));
        verify(mockedUtilitiesHelper, times(1)).getChosenNumber(anyInt(), anyInt());
    }

    @Test
    @DisplayName("should return chosen 'mau' state from the player")
    public void getSaidMau() {
        Player player = new Player("Jasmin");
        when(mockedUtilitiesHelper.getChosenNumber(anyInt(), anyInt())).thenReturn(1, 2);

        assertTrue(ui.saidMau(player));
        assertFalse(ui.saidMau(player));
        verify(mockedUtilitiesHelper, times(2)).getChosenNumber(anyInt(), anyInt());
    }

    @Test
    @DisplayName("should return if player wants to draw a card or not")
    public void getIfAPlayerWantsToDrawCards() {
        when(mockedUtilitiesHelper.getChosenNumber(anyInt(), anyInt())).thenReturn(2, 1);

        assertTrue(ui.playerWantToDrawCards());
        assertFalse(ui.playerWantToDrawCards());
        verify(mockedUtilitiesHelper, times(2)).getChosenNumber(anyInt(), anyInt());
    }
}
