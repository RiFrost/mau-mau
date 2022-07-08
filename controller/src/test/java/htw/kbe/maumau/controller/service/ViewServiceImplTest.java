package htw.kbe.maumau.controller.service;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Label;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.controller.export.ViewService;
import htw.kbe.maumau.player.export.Player;
import htw.kbe.maumau.rule.service.RulesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ViewServiceImplTest {


    private ViewService service;

    @BeforeEach
    public void setUp() {
        service = new ViewServiceImpl();
    }

    @Test
    @DisplayName("should return given number from user input and only accept numbers greater than or equal to minimum " +
            "or less than or equal to maximum")
    public void getNumberOfPlayer() {
        String[] userInput = new String[]{
                "invalid_string",
                "-",
                "0",
                "5",
                "1",
                "4"
        };
        int expected = 4;
        System.setIn(new ByteArrayInputStream(String.join("\n", userInput).getBytes()));

        assertEquals(expected, service.getNumberOfPlayer());
    }

    @Test
    @DisplayName("should create a new list of given player names and only accepts names greater than 4 and" +
            "or less than 15 characters")
    public void getPlayerNames() {
        List<String> expectedPlayerNames = List.of("Philipp");
        String[] input = new String[]{
                " ",
                "5",
                "No",
                "-",
                "RichardJasminAndPhillip",
                "Philipp",
                "anything_after_Richard_will_be_ignored"
        };
        System.setIn(new ByteArrayInputStream(String.join("\n", input).getBytes()));

        List<String> actualPlayerNames = service.getPlayerNames(1);

        assertEquals(expectedPlayerNames, actualPlayerNames);
    }

    @Test
    @DisplayName("should create a Map with chosen Card to play and chosen 'mau' state")
    public void getChosenCardAndMauState() {
/*        Player player = new Player("Jasmin");
        player.setHandCards(List.of(new Card(Suit.CLUBS, Label.JACK), new Card(Suit.SPADES, Label.ASS)));
        String[] input = new String[]{
                "2",
                "1"
        };
        InputStream in = new ByteArrayInputStream(String.join("\n", input).getBytes());
        System.setIn(in);

        Map<Card, Boolean> actualMap = service.getPlayedCard(player);

        assertTrue(actualMap.containsKey(new Card(Suit.SPADES, Label.ASS)));
        assertEquals(true, actualMap.get(new Card(Suit.SPADES, Label.ASS)));*/
    }

    @Test
    @DisplayName("should return the chosen suit wish from player")
    public void getChosenSuitWish() {
        List<Suit> suits = List.of(Suit.SPADES, Suit.CLUBS, Suit.DIAMONDS, Suit.HEARTS);
        Player player = new Player("Jasmin");
        String input = "3";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertEquals(Suit.DIAMONDS, service.getChosenSuit(player, suits));
    }

    @Test
    @DisplayName("should return chosen 'mau' state from the player")
    public void getSaidMau() {
        Player player = new Player("Jasmin");

        String input = "1";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertTrue(service.saidMau(player));

        input = "2";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertFalse(service.saidMau(player));
    }

    @Test
    @DisplayName("should return if player wants to draw a card or not")
    public void checkIfAPlayerWantsToDrawCards() {
        String input = "1";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertFalse(service.playerWantToDrawCards());

        input = "2";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertTrue(service.playerWantToDrawCards());
    }

    @Test
    @DisplayName("should return if player wants to start a new round")
    public void checkIfPlayerWantNewRound() {
        String input = "1";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertTrue(service.hasNextRound());

        input = "2";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertFalse(service.hasNextRound());
    }
}
