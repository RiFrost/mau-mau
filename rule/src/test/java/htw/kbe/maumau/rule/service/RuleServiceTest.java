package htw.kbe.maumau.rule.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Label;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.player.domain.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RuleServiceTest {

    private RulesService rulesService;

    private final Card clubsSeven = new Card(Suit.CLUBS, Label.SEVEN);
    private final Card spadesSeven = new Card(Suit.SPADES, Label.SEVEN);
    private final Card clubsEight = new Card(Suit.CLUBS, Label.EIGHT);
    private final Card clubsJack = new Card(Suit.CLUBS, Label.JACK);
    private final Card spadesJack = new Card(Suit.SPADES, Label.JACK);
    private final Card clubsNine = new Card(Suit.CLUBS, Label.NINE);
    private final Card heartsAss = new Card(Suit.HEARTS, Label.ASS);
    private final Player player = new Player(2, "Uwe");
    private final Suit userWish = Suit.CLUBS;
    int drawCardCounter = 0;

    @BeforeEach
    public void setUp() {
        rulesService = new RulesServiceImpl();
    }


    @Test
    @DisplayName("checks if card can be played when suit is the same")
    void checkSuitValid() {
        assertTrue(rulesService.isSuitWishValid(clubsSeven, clubsEight));
    }

    @Test
    @DisplayName("checks if card can be played when label is the same")
    void checkLabelValid() {
        assertTrue(rulesService.isSuitWishValid(clubsSeven, spadesSeven));
    }

    @Test
    @DisplayName("false when label and suit is not the same")
    void checkLabelinvalid() {
        assertFalse(rulesService.isSuitWishValid(clubsSeven, heartsAss));
    }

    @Test
    @DisplayName("false when jack is played on jack")
    void checkJackOnJack() {
        assertFalse(rulesService.isSuitWishValid(clubsJack, spadesJack));
    }

    @Test
    @DisplayName("checks if wished suit is played")
    void checkSuitWish() {
        assertTrue(rulesService.isSuitWishValid(clubsSeven, userWish));
    }

    @Test
    @DisplayName("checks drawCounter when SEVEN is played")
    void checkCounterWithSeven() {

        assertEquals(2, rulesService.isCardSeven(drawCardCounter, clubsSeven));
    }

    @Test
    @DisplayName("checks drawCounter when one card SEVEN is played")
    void checkCounterWithoutSeven() {

        assertEquals(0, rulesService.isCardSeven(drawCardCounter, heartsAss));
    }

    @Test
    @DisplayName("checks if card is ASS")
    void checkAss() {

        assertTrue(rulesService.isSuspended(heartsAss));
    }

    @Test
    @DisplayName("checks if card is JACK")
    void checkJack() {

        assertTrue(rulesService.isCardJack(clubsJack));
    }

    @Test
    @DisplayName("checks if card is NINE")
    void checkNine() {

        assertTrue(rulesService.changeGameDirection(clubsNine));
    }

    @Test
    @DisplayName("checks if player said Mau")
    void checkMau() {
        player.setHasSaidMauMau(true);
        assertTrue(rulesService.isPlayersMauValid(player));
    }
}
