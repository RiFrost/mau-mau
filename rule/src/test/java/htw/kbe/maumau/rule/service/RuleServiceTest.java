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

    @BeforeEach
    public void setUp() {
        rulesService = new RulesServiceImpl();
    }

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

    @Test
    @DisplayName("checks if card can be played when suit is the same")
    void checkSuitValid() {
        assertTrue(rulesService.isCardValid(clubsSeven, clubsEight));
    }

    @Test
    @DisplayName("checks if card can be played when label is the same")
    void checkLabelValid() {
        assertTrue(rulesService.isCardValid(clubsSeven, spadesSeven));
    }

    @Test
    @DisplayName("false when label and suit is not the same")
    void checkLabelinvalid() {
        assertFalse(rulesService.isCardValid(clubsSeven, heartsAss));
    }

    @Test
    @DisplayName("false when jack is played on jack")
    void checkJackOnJack() {
        assertFalse(rulesService.isCardValid(clubsJack, spadesJack));
    }

    @Test
    @DisplayName("checks if wished suit is played")
    void checkSuitWish() {
        assertTrue(rulesService.isCardValid(clubsSeven, userWish));
    }

    @Test
    @DisplayName("checks drawCounter when one card SEVEN is played")
    void checkSevenCounter() {

        assertEquals(2, rulesService.isCardSeven(drawCardCounter, clubsSeven));
    }

    @Test
    @DisplayName("checks if card is EIGHT")
    void checkEight() {

        assertTrue(rulesService.isCardEight(clubsEight));
    }

    @Test
    @DisplayName("checks if card is JACK")
    void checkJack() {

        assertTrue(rulesService.isCardJack(clubsJack));
    }

    @Test
    @DisplayName("checks if card is NINE")
    void checkNine() {

        assertTrue(rulesService.isCardNine(clubsNine));
    }

    @Test
    @DisplayName("checks if player said Mau")
    void checkMau() {
        player.setHasSaidMauMau(true);
        assertTrue(rulesService.isPlayersMauMauValid(player));
    }
}
