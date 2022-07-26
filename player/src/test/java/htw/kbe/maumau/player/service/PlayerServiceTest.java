package htw.kbe.maumau.player.service;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Label;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.player.export.Player;
import htw.kbe.maumau.player.exceptions.InvalidPlayerNameException;
import htw.kbe.maumau.player.export.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerServiceTest {

    private PlayerService service;
    private Player player;
    private List<Card> handCards;

    @BeforeEach
    public void setUp() {
        this.service = new PlayerServiceImpl();
        this.player = new Player("Jasmin");
        this.handCards = new ArrayList<>(Arrays.asList(new Card(Suit.HEARTS, Label.SEVEN), new Card(Suit.CLUBS, Label.ASS))); // ADD FIXTURES
    }

    @Test
    @DisplayName("should return a list of players")
    public void testCreateValidPlayers() throws InvalidPlayerNameException {
        List<String> validNames = new ArrayList<>(Arrays.asList("Jasmin", "Richard", "Philipp", "Maria"));

        List<Player> expectedPlayers = service.createPlayers(validNames,0);

        assertEquals(4, expectedPlayers.size());
        assertEquals("Jasmin", expectedPlayers.get(0).getName());
        assertEquals("Richard", expectedPlayers.get(1).getName());
        assertEquals("Philipp", expectedPlayers.get(2).getName());
        assertEquals("Maria", expectedPlayers.get(3).getName());
    }

    @Test
    @DisplayName("should return a list of players and ai players")
    public void testCreateValidPlayersAndAiPlayers() throws InvalidPlayerNameException {
        List<String> validNames = new ArrayList<>(Arrays.asList("Jasmin", "Maria"));

        List<Player> expectedPlayers = service.createPlayers(validNames,2);

        assertEquals(4, expectedPlayers.size());
        assertEquals("Jasmin", expectedPlayers.get(0).getName());
        assertEquals("Maria", expectedPlayers.get(1).getName());
        assertTrue(Pattern.matches(".* \\[AI\\]", expectedPlayers.get(2).getName()));
        assertTrue(Pattern.matches(".* \\[AI\\]", expectedPlayers.get(3).getName()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "This name is way to long", "    "})
    public void testValidatePlayerName(String name) {
        List<String> validNames = new ArrayList<>(Arrays.asList("Jasmin, Richard", "Philipp", "Maria"));

        Exception e = assertThrows(InvalidPlayerNameException.class, () -> {
            service.validateName(name, validNames);
        });
    }

    @Test
    @DisplayName("should throw InvalidPlayerName when name is duplicated")
    public void testDuplicatePlayerName(){
        String duplicatedName = "Maria";
        List<String> validNames = new ArrayList<>(Arrays.asList("Jasmin, Richard", "Maria", "Maria"));

        Exception e = assertThrows(InvalidPlayerNameException.class, () -> {
            service.validateName(duplicatedName, validNames);
        });
    }

    @Test
    @DisplayName("should remove played card from hand cards")
    public void testRemovePlayedCardFromHandCards(){
        player.setHandCards(handCards);

        service.removePlayedCard(player, new Card(Suit.HEARTS, Label.SEVEN));

        assertEquals(1, player.getHandCards().size());
        assertEquals(Arrays.asList(new Card(Suit.CLUBS, Label.ASS)), player.getHandCards());
    }

    @Test
    @DisplayName("should add drawn cards to hand cards")
    public void testAddDrawnCardsToHandCards(){
        player.setHandCards(handCards);

        service.addDrawnCards(player, Arrays.asList(new Card(Suit.SPADES, Label.EIGHT)));

        assertEquals(3, player.getHandCards().size());
    }

    @Test
    @DisplayName("should sort player hand cards")
    public void testSortHandCards() {
        this.handCards = new ArrayList<>(Arrays.asList(
                new Card(Suit.HEARTS, Label.SEVEN),
                new Card(Suit.CLUBS, Label.ASS),
                new Card(Suit.SPADES, Label.EIGHT),
                new Card(Suit.DIAMONDS, Label.SEVEN),
                new Card(Suit.CLUBS, Label.KING)
        ));
        List<Card> expectedHandCards =  new ArrayList<>(Arrays.asList(
                new Card(Suit.CLUBS, Label.ASS),
                new Card(Suit.CLUBS, Label.KING),
                new Card(Suit.SPADES, Label.EIGHT),
                new Card(Suit.HEARTS, Label.SEVEN),
                new Card(Suit.DIAMONDS, Label.SEVEN)
        ));

        service.addDrawnCards(player, handCards);

        assertEquals(expectedHandCards, player.getHandCards());
    }

    @Test
    @DisplayName("should sort more complex player hand cards")
    public void testSortHandCards02() {
        this.handCards = new ArrayList<>(Arrays.asList(
                new Card(Suit.HEARTS, Label.SEVEN),
                new Card(Suit.CLUBS, Label.ASS),
                new Card(Suit.SPADES, Label.EIGHT),
                new Card(Suit.DIAMONDS, Label.SEVEN),
                new Card(Suit.CLUBS, Label.KING),
                new Card(Suit.DIAMONDS, Label.JACK),
                new Card(Suit.DIAMONDS, Label.ASS),
                new Card(Suit.HEARTS, Label.EIGHT),
                new Card(Suit.SPADES, Label.QUEEN),
                new Card(Suit.SPADES, Label.NINE)
        ));
        List<Card> expectedHandCards =  new ArrayList<>(Arrays.asList(
                new Card(Suit.CLUBS, Label.ASS),
                new Card(Suit.CLUBS, Label.KING),
                new Card(Suit.SPADES, Label.QUEEN),
                new Card(Suit.SPADES, Label.NINE),
                new Card(Suit.SPADES, Label.EIGHT),
                new Card(Suit.HEARTS, Label.EIGHT),
                new Card(Suit.HEARTS, Label.SEVEN),
                new Card(Suit.DIAMONDS, Label.ASS),
                new Card(Suit.DIAMONDS, Label.JACK),
                new Card(Suit.DIAMONDS, Label.SEVEN)
        ));

        service.addDrawnCards(player, handCards);

        assertEquals(expectedHandCards, player.getHandCards());
    }
}
