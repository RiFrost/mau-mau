package htw.kbe.maumau.player.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Label;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.player.domain.Player;
import htw.kbe.maumau.player.exceptions.InvalidPlayerNameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        List<Player> expectedPlayers = service.createPlayers(validNames);

        assertEquals(4, expectedPlayers.size());
        assertEquals("Jasmin", expectedPlayers.get(0).getName());
        assertEquals("Richard", expectedPlayers.get(1).getName());
        assertEquals("Philipp", expectedPlayers.get(2).getName());
        assertEquals("Maria", expectedPlayers.get(3).getName());
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
    @DisplayName("should set mau state to true")
    public void testSayMau(){
        assertFalse(player.saidMau());
        service.sayMau(player);
        assertTrue(player.saidMau());
    }

    @Test
    @DisplayName("should set suspend state to true")
    public void testMustSuspend(){
        assertFalse(player.mustSuspend());
        service.mustSuspend(player);
        assertTrue(player.mustSuspend());
    }

    @Test
    @DisplayName("should remove played card from hand cards")
    public void testRemovePlayedCardFromHandCards(){
        player.setHandCards(handCards);

        service.playCard(player, new Card(Suit.HEARTS, Label.SEVEN));

        assertEquals(1, player.getHandCards().size());
        assertEquals(Arrays.asList(new Card(Suit.CLUBS, Label.ASS)), player.getHandCards());
    }

    @Test
    @DisplayName("should add drawn cards to hand cards")
    public void testAddDrawnCardsToHandCards(){
        player.setHandCards(handCards);

        service.drawCards(player, Arrays.asList(new Card(Suit.SPADES, Label.EIGHT)));

        assertEquals(3, player.getHandCards().size());
    }
}
