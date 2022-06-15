package htw.kbe.maumau.controller.utilities;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Label;
import htw.kbe.maumau.card.export.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UtilitiesHelperTest {

    private UtilitiesHelper utilitiesHelper;

    @BeforeEach
    public void setUp() {
       utilitiesHelper = new UtilitiesHelper();
    }

    @Test
    @DisplayName("should give a card image belonging to it's card")
    public void testGetCardImage() {
        String expectedASS = String.format("_____\n|%s  |\n| %s |\n|__%s|", "A", "\u2663", "A");
        String expectedTEN = String.format("______\n|%s  |\n| %s  |\n|__%s|", "10", "\u2663", "10");

        String actualASS = utilitiesHelper.getCardImage(new Card(Suit.CLUBS, Label.ASS));
        String actualTEN = utilitiesHelper.getCardImage(new Card(Suit.CLUBS, Label.TEN));

        assertEquals(expectedASS, actualASS);
        assertEquals(expectedTEN, actualTEN);
    }

    @Test
    @DisplayName("should load game instructions")
    public void testFileLoading() {
        String instruction = utilitiesHelper.loadFromFile();

        assertNotNull(instruction);
    }

    @Test
    @DisplayName("should catch exception when file not found")
    public void testFileLoadingFail() {
        BufferedReader mockedBufferedReader = mock(BufferedReader.class);
        when(mockedBufferedReader.lines()).thenThrow(NullPointerException.class);

        utilitiesHelper.loadFromFile();

        assertDoesNotThrow(() -> {});
    }

    @Test
    @DisplayName("should only accept")
    public void testUserInputByStrings() {
        String[] userInput = new String[]{
                " ",
                "5",
                "No",
                "-",
                "RichardJasminAndPhillip",
                "Richard",
                "anything_after_Richard_will_be_ignored"
        };

        String expected = "Richard";
        InputStream stdin = System.in;

        try {
            System.setIn(new ByteArrayInputStream(String.join("\n", userInput).getBytes()));
            String actual = utilitiesHelper.getPlayerName();
            assertEquals(actual, expected);
        } finally {
            System.setIn(stdin);
        }
    }

    @Test
    @DisplayName("should only accept numbers greater than or equal to minimum or less than or equal to maximum")
    public void testUserInputByNumbers() {
        String[] userInput = new String[]{
                "invalid_string",
                "-",
                "0",
                "3",
                "2",
                "anything_after_2_will_be_ignored"
        };

        int expected = 2;
        InputStream stdin = System.in;

        try {
            System.setIn(new ByteArrayInputStream(String.join("\n", userInput).getBytes()));
            int actual = utilitiesHelper.getChosenNumber(1, 2);
            assertEquals(actual, expected);
        } finally {
            System.setIn(stdin);
        }
    }
}