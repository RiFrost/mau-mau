package htw.kbe.maumau.uicontroller.utilities;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class UserInputValidationTest {

    private UserInputValidation userInputValidation;

    @BeforeEach
    public void setUp() {
       userInputValidation = new UserInputValidation();
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
            String actual = userInputValidation.getPlayerName();
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
            int actual = userInputValidation.getChosenNumber(1, 2);
            assertEquals(actual, expected);
        } finally {
            System.setIn(stdin);
        }
    }
}