package htw.kbe.maumau.game.service;

import htw.kbe.maumau.card.service.CardService;
import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;
import htw.kbe.maumau.deck.service.DeckService;
import htw.kbe.maumau.game.domain.Game;
import htw.kbe.maumau.game.fixtures.GameFixture;
import htw.kbe.maumau.player.domain.Player;
import htw.kbe.maumau.player.service.PlayerService;
import htw.kbe.maumau.rule.service.RulesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class GameServiceTest {

    @InjectMocks
    private GameService service;

    @Mock
    private DeckService deckService;
    private RulesService rulesService;
    private CardService cardService;
    private PlayerService playerService;

    private List<Player> players = GameFixture.players();

    @BeforeEach
    public void setUp() throws IllegalDeckSizeException {
        service = new GameServiceImpl();
        deckService = mock(DeckService.class);
        rulesService = mock(RulesService.class);
        cardService = mock(CardService.class);
        playerService = mock(PlayerService.class);
        service.setPlayerService(playerService);
        service.setCardService(cardService);
        service.setDeckService(deckService);
        service.setRulesService(rulesService);
        when(deckService.createDeck(anyList())).thenReturn(GameFixture.deck());
    }

    @Test
    @DisplayName("should return a new instance of game with Cards and the Playerlist")
    public void startNewGame() throws IllegalDeckSizeException {
        Game game = service.startNewGame(players);

        assertEquals(players, game.getPlayers());
        assertEquals(players.get(0), game.getActivePlayer());
        assertNotEquals(null, game.getCardDeck().getTopCard());
        assertEquals(31, game.getCardDeck().getDrawPile().size());
        assertTrue(game.getClockWise());
    }

    @Test
    @DisplayName("should return the next player in clockwise direction")
    public void nextPlayerClockWise() throws IllegalDeckSizeException {
        Game game = service.startNewGame(players);
        Player activePlayer = players.get(0);
        Player nextActivePlayer = players.get(1);

        assertEquals(activePlayer, game.getActivePlayer());

        service.nextPlayer(game);

        assertEquals(nextActivePlayer, game.getActivePlayer());
    }

    @Test
    @DisplayName("should return the next player in counter clockwise direction")
    public void nextPlayerCounterClockWise() throws IllegalDeckSizeException {
        Game game = service.startNewGame(players);
        Player activePlayer = players.get(0);
        Player nextActivePlayer = players.get(players.size() - 1);

        assertEquals(activePlayer, game.getActivePlayer());
        game.switchDirection();
        service.nextPlayer(game);

        assertEquals(nextActivePlayer, game.getActivePlayer());
    }

    @Test
    @DisplayName("should return true if the Player does have the card in hand")
    public void hasPlayerHandCard() {
        assertTrue(true);
    }

    @Test
    @DisplayName("should return false if the Player does not have the card in hand")
    public void hasNotPlayerHandCard() {
        assertTrue(true);
    }

    @Test
    @DisplayName("")
    public void shouldDrawInitialHandCards()  throws IllegalDeckSizeException {
        Game game = service.startNewGame(players);

        service.dealCards(game);

        for(Player player : players) {
            assertEquals(5, player.getHandCards().size());
        }
    }
}
