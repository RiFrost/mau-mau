package htw.kbe.maumau.rule.service;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Label;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.player.export.Player;
import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;
import htw.kbe.maumau.rule.export.RulesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    private int drawCounter = 0;

    @BeforeEach
    public void setUp() {
        rulesService = new RulesServiceImpl();
    }

    @Test
    @DisplayName("should not throw exception when suit of played card matches suit of top card")
    public void testPlayedSuitMatchesTopSuit() throws PlayedCardIsInvalidException {
        rulesService.validateCard(clubsSeven, clubsEight, null, drawCounter);
    }

    @Test
    @DisplayName("should not throw exception when label of played card matches label of top card")
    public void testPlayedLabelMatchesTopLabel() throws PlayedCardIsInvalidException {
        rulesService.validateCard(clubsSeven, spadesSeven, null, drawCounter);
    }

    @Test
    @DisplayName("should not throw exception when played card matches players suit wish")
    public void testSuitWishMatchesPlayedCard() throws PlayedCardIsInvalidException {
        rulesService.validateCard(heartsAss, clubsJack, userWish, drawCounter);
    }

    @Test
    @DisplayName("should not throw exception when jack is played and suit or label of the top card does not match")
    public void testPlayedJackNotOnJack() throws PlayedCardIsInvalidException {
        rulesService.validateCard(clubsJack, heartsAss, null, drawCounter);
    }

    @Test
    @DisplayName("should not throw exception when label seven is not played and draw counter is 0 but label of top card is seven")
    public void testPlayedLabelMustNotEqualsTopLabel() throws PlayedCardIsInvalidException {
        rulesService.validateCard(clubsNine, clubsSeven, null, drawCounter);
    }

    @Test
    @DisplayName("should throw exception when players card does match suit wish but jack on jack wants to be played")
    public void checkSuitWishIsValid1() {
        Exception exception = assertThrows(PlayedCardIsInvalidException.class, () -> {
            rulesService.validateCard(heartsJack, clubsJack, userWish, drawCounter);
        });

        String expectedMessage = "JACK on JACK is not allowed.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("should throw exception when label and suit is not the same")
    public void checkIsLabelInvalid() {
        Exception exception = assertThrows(PlayedCardIsInvalidException.class, () -> {
            rulesService.validateCard(clubsSeven, heartsAss, null, drawCounter);
        });

        String expectedMessage = "The card cannot be played. Label or suit does not match.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("should throw exception when jack on jack is played")
    public void checkJackOnJack() {
        Exception exception = assertThrows(PlayedCardIsInvalidException.class, () -> {
            rulesService.validateCard(clubsJack, spadesJack, null, drawCounter);
        });

        String expectedMessage = "JACK on JACK is not allowed.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("should throw exception when players card does not match suit wish")
    public void checkSuitWish() {
        Exception exception = assertThrows(PlayedCardIsInvalidException.class, () -> {
            rulesService.validateCard(clubsEight, clubsJack, userWish, drawCounter);
        });

        String expectedMessage = "The card cannot be played. Suit does not match players wish.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("should throw exception when player has SEVEN and top card is SEVEN but player doesn't want to play SEVEN")
    public void checkLabelSevenOnSeven() {
        player.setHandCards(List.of(spadesSeven));
        drawCounter = 2;
        Exception exception = assertThrows(PlayedCardIsInvalidException.class, () -> {
            rulesService.validateCard(clubsEight, clubsSeven, null, drawCounter);
        });

        String expectedMessage = "You have to play a SEVEN.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("checks if SEVEN is played")
    public void checkMustDrawCards() {

        assertTrue(rulesService.mustDrawCards(clubsSeven));
    }

    @Test
    @DisplayName("player must draw cards when hand card is not a SEVEN but top card is a SEVEN and draw counter is greater or equal 2")
    public void checkMustDrawCards1() {
        int drawCounter = 2;
        player.setHandCards(List.of(clubsNine));

        assertTrue(rulesService.mustDrawCards(player, clubsSeven, drawCounter));
    }

    @Test
    @DisplayName("player must not draw when hand card and top card is a SEVEN and draw counter is greater or equal 2")
    public void checkMustDrawCards2() {
        int drawCounter = 2;
        player.setHandCards(List.of(spadesSeven));

        assertFalse(rulesService.mustDrawCards(player, clubsSeven, drawCounter));
    }

    @Test
    @DisplayName("player must not draw when top card is a SEVEN but draw counter is less than 2")
    public void checkMustDrawCards3() {
        int drawCounter = 0;
        player.setHandCards(List.of(clubsNine));

        assertFalse(rulesService.mustDrawCards(player, clubsSeven, drawCounter));
    }

    @Test
    @DisplayName("checks if card is ASS")
    public void checkAss() {

        assertTrue(rulesService.mustSuspend(heartsAss));
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

        assertEquals(2, rulesService.getDefaultNumberOfDrawnCards());
    }
}
