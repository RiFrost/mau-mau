package htw.kbe.maumau.rule.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Label;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.player.domain.Player;
import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RuleServiceTest {

    private RulesService rulesService;

    private final Card clubsSeven = new Card(Suit.CLUBS, Label.SEVEN);
    private final Card spadesSeven = new Card(Suit.SPADES, Label.SEVEN);
    private final Card clubsEight = new Card(Suit.CLUBS, Label.EIGHT);
    private final Card clubsJack = new Card(Suit.CLUBS, Label.JACK);
    private final Card spadesJack = new Card(Suit.SPADES, Label.JACK);
    private final Card clubsNine = new Card(Suit.CLUBS, Label.NINE);
    private final Card heartsAss = new Card(Suit.HEARTS, Label.ASS);
    private final Card heartsJack = new Card(Suit.HEARTS, Label.JACK);
    private final Player player = new Player("Uwe");
    private final Suit userWish = Suit.HEARTS;

    @BeforeEach
    public void setUp() {
        rulesService = new RulesServiceImpl();
    }

    @Test
    @DisplayName("checks if card can be played when suit is the same")
    public void checkIsSuitValid() throws PlayedCardIsInvalidException {
        rulesService.validateCard(clubsSeven, clubsEight, null);
    }

    @Test
    @DisplayName("checks if card can be played when label is the same")
    public void checkIsLabelValid() throws PlayedCardIsInvalidException {
        rulesService.validateCard(clubsSeven, spadesSeven, null);
    }

    @Test
    @DisplayName("checks if card can be played when players card does match suit wish")
    public void checkSuitWishIsValid() throws PlayedCardIsInvalidException {
        rulesService.validateCard(heartsAss, clubsJack, userWish);
    }

    @Test
    @DisplayName("false when players card does match suit wish but jack on jack wants to be played")
    public void checkSuitWishIsValid1() {
        Exception exception = assertThrows(PlayedCardIsInvalidException.class, () -> {
            rulesService.validateCard(heartsJack, clubsJack, userWish);
        });

        String expectedMessage = "Jack on Jack is not allowed.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("false when label and suit is not the same")
    public void checkIsLabelInvalid() {
        Exception exception = assertThrows(PlayedCardIsInvalidException.class, () -> {
            rulesService.validateCard(clubsSeven, heartsAss, null);
        });

        String expectedMessage = "The card must not be played. Label or suit does not match.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("false when jack is played on jack")
    public void checkJackOnJack() {
        Exception exception = assertThrows(PlayedCardIsInvalidException.class, () -> {
            rulesService.validateCard(clubsJack, spadesJack, null);
        });

        String expectedMessage = "Jack on Jack is not allowed.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("false when players card does not match suit wish")
    public void checkSuitWish() {
        Exception exception = assertThrows(PlayedCardIsInvalidException.class, () -> {
            rulesService.validateCard(clubsEight, clubsJack, userWish);
        });

        String expectedMessage = "The card must not be played. Suit does not match players wish.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("checks drawCounter when SEVEN is played")
    public void checkMustDrawCards() {

        assertTrue(rulesService.mustDrawTwoCards(clubsSeven));
    }

    @Test
    @DisplayName("checks drawCounter when no SEVEN is played")
    public void checkMustNotDrawCards() {

        assertFalse(rulesService.mustDrawTwoCards(heartsAss));
    }

    @Test
    @DisplayName("checks if card is ASS")
    public void checkAss() {

        assertTrue(rulesService.isSuspended(heartsAss));
    }

    @Test
    @DisplayName("checks if card is JACK")
    public void checkJack() {

        assertTrue(rulesService.isCardJack(clubsJack));
    }

    @Test
    @DisplayName("checks if card is NINE")
    public void checkNine() {

        assertTrue(rulesService.changeGameDirection(clubsNine));
    }

    @Test
    @DisplayName("checks if player said 'mau' and has one hand card left")
    public void checkMauIsInvalid() {
        player.setSaidMau(true);
        player.setHandCards(List.of(clubsNine));
        assertFalse(rulesService.isPlayersMauInvalid(player));
    }

    @Test
    @DisplayName("checks if player has more than one hand card and said not 'mau'")
    public void checkMauIsInvalid1() {
        player.setSaidMau(false);
        player.setHandCards(List.of(clubsNine, spadesJack));
        assertFalse(rulesService.isPlayersMauInvalid(player));
    }

    @Test
    @DisplayName("checks if player has one hand card and said not 'mau'")
    public void checkMauIsInvalid2() {
        player.setSaidMau(false);
        player.setHandCards(List.of(clubsNine));
        assertTrue(rulesService.isPlayersMauInvalid(player));
    }

    @Test
    @DisplayName("checks if player has more than one hand card and said 'mau'")
    public void checkMauIsInvalid3() {
        player.setSaidMau(true);
        player.setHandCards(List.of(clubsNine, clubsEight));
        assertTrue(rulesService.isPlayersMauInvalid(player));
    }

    @Test
    @DisplayName("return default number of drawn cards")
    public void checkNumberOfDrawnCards() {

        assertEquals(2, rulesService.getNumberOfDrawnCards());
    }
}
