//package htw.kbe.maumau.rule.service;
//
//import htw.kbe.maumau.card.domain.Card;
//import htw.kbe.maumau.card.domain.Label;
//import htw.kbe.maumau.card.domain.Suit;
//import htw.kbe.maumau.player.domain.Player;
//import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class RuleServiceTest {
//
//    private RulesService rulesService;
//
//    private final Card clubsSeven = new Card(Suit.CLUBS, Label.SEVEN);
//    private final Card spadesSeven = new Card(Suit.SPADES, Label.SEVEN);
//    private final Card clubsEight = new Card(Suit.CLUBS, Label.EIGHT);
//    private final Card clubsJack = new Card(Suit.CLUBS, Label.JACK);
//    private final Card spadesJack = new Card(Suit.SPADES, Label.JACK);
//    private final Card clubsNine = new Card(Suit.CLUBS, Label.NINE);
//    private final Card heartsAss = new Card(Suit.HEARTS, Label.ASS);
//    private final Player player = new Player("Uwe");
//    private final Suit userWish = Suit.HEARTS;
//    private final Suit userWish2 = null;
//    int drawCardCounter = 0;
//
//    @BeforeEach
//    public void setUp() {
//        rulesService = new RulesServiceImpl();
//    }
//
//
//    @Test
//    @DisplayName("checks if card can be played when suit is the same")
//    void checkSuitValid() throws PlayedCardIsInvalidException {
//        assertTrue(rulesService.validateCard(clubsSeven, clubsEight, userWish2));
//    }
//
//    @Test
//    @DisplayName("checks if card can be played when label is the same")
//    void checkLabelValid() throws PlayedCardIsInvalidException {
//        assertTrue(rulesService.validateCard(clubsSeven, spadesSeven, userWish2));
//    }
//
//    @Test
//    @DisplayName("false when label and suit is not the same")
//    void checkLabelinvalid() throws PlayedCardIsInvalidException {
//        assertFalse(rulesService.validateCard(clubsSeven, heartsAss, userWish2));
//    }
//
//    @Test
//    @DisplayName("false when jack is played on jack")
//    void checkJackOnJack() throws PlayedCardIsInvalidException {
//        assertFalse(rulesService.validateCard(clubsJack, spadesJack, userWish2));
//    }
//
//    @Test
//    @DisplayName("checks if wished suit is played")
//    void checkSuitWish() throws PlayedCardIsInvalidException {
//        assertTrue(rulesService.validateCard(heartsAss, clubsJack, userWish));
//    }
//
//    @Test
//    @DisplayName("checks drawCounter when SEVEN is played")
//    void checkCounterWithSeven() {
//
//        assertTrue(rulesService.drawTwoCards(clubsSeven));
//    }
//
//    @Test
//    @DisplayName("checks drawCounter when no SEVEN is played")
//    void checkCounterWithoutSeven() {
//
//        assertFalse(rulesService.drawTwoCards(heartsAss));
//    }
//
//    @Test
//    @DisplayName("checks if card is ASS")
//    void checkAss() {
//
//        assertTrue(rulesService.isSuspended(heartsAss));
//    }
//
//    @Test
//    @DisplayName("checks if card is JACK")
//    void checkJack() {
//
//        assertTrue(rulesService.isCardJack(clubsJack));
//    }
//
//    @Test
//    @DisplayName("checks if card is NINE")
//    void checkNine() {
//
//        assertTrue(rulesService.changeGameDirection(clubsNine));
//    }
//
//    @Test
//    @DisplayName("checks if player said Mau")
//    void checkMau() {
//        player.setSaidMau(true);
//        assertTrue(rulesService.isPlayersMauValid(player));
//    }
//}
