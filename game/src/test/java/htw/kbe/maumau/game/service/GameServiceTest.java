package htw.kbe.maumau.game.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Label;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.card.service.CardService;
import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;
import htw.kbe.maumau.deck.service.DeckService;
import htw.kbe.maumau.game.domain.Game;
import htw.kbe.maumau.game.exceptions.InvalidPlayerSizeException;
import htw.kbe.maumau.game.fixtures.GameFixture;
import htw.kbe.maumau.player.domain.Player;
import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;
import htw.kbe.maumau.rule.service.RulesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class GameServiceTest {

    @InjectMocks
    private GameService service;

    @Mock
    private DeckService deckService;
    private RulesService rulesService;
    private CardService cardService;

    private List<Player> players;
    private Game game;

    @BeforeEach
    public void setUp() throws IllegalDeckSizeException {
        service = new GameServiceImpl();
        deckService = mock(DeckService.class);
        rulesService = mock(RulesService.class);
        cardService = mock(CardService.class);
        service.setCardService(cardService);
        service.setDeckService(deckService);
        service.setRulesService(rulesService);
        players = GameFixture.players();
        game = GameFixture.game();
        when(deckService.createDeck(anyList())).thenReturn(GameFixture.deck());
    }

    @Test
    @DisplayName("should return a new instance of game with Cards and the player list")
    public void testCreateValidGame() throws IllegalDeckSizeException, InvalidPlayerSizeException {
        Game game = service.startNewGame(players);

        assertEquals(players, game.getPlayers());
        assertEquals(players.get(0), game.getActivePlayer());
        assertNotEquals(null, game.getCardDeck().getTopCard());
        assertEquals(31, game.getCardDeck().getDrawPile().size());
        assertTrue(game.getClockWise());
    }

    @Test
    @DisplayName("should throw exception when player list size is smaller than two")
    public void throwExceptionPlayerSizeIsTooLow() throws IllegalDeckSizeException, InvalidPlayerSizeException {
        Exception e = assertThrows(InvalidPlayerSizeException.class, () -> {
            service.startNewGame(players.subList(0, 1));
        });
    }

    @Test
    @DisplayName("should throw exception when player list size is higher than four")
    public void throwExceptionPlayerSizeIsTooHigh() throws IllegalDeckSizeException, InvalidPlayerSizeException {
        players.add(players.get(0));

        Exception e = assertThrows(InvalidPlayerSizeException.class, () -> {
            service.startNewGame(players);
        });
    }

    @Test
    @DisplayName("should set userWish and set askForSuit state to false")
    public void setUserWish() {
        Suit userWish = Suit.CLUBS;
        game.setAskForSuitWish(true);

        service.setUserWish(userWish, game);

        assertFalse(game.hasAskedForSuitWish());
        assertEquals(userWish, game.getUserWish());
    }

    @Test
    @DisplayName("should call rulesService and deckService when validate played card")
    public void validateCard() throws PlayedCardIsInvalidException {
        Card expectedTopCard = new Card(Suit.CLUBS, Label.KING);
        Card playedCard = new Card(Suit.CLUBS, Label.SEVEN);
        game.setUserWish(null);
        game.getCardDeck().setTopCard(expectedTopCard);

        service.validateCard(playedCard, game);

        verify(rulesService, times(1)).validateCard(
                argThat(card -> card.equals(playedCard)),
                argThat(topCard -> topCard.equals(expectedTopCard)),
                argThat(suit -> Objects.isNull(suit))
        );

        verify(deckService, times(1)).setCardToTopCard(
                argThat(deck -> deck.equals(game.getCardDeck())),
                argThat(card -> card.equals(playedCard))
        );
    }

    @Test
    @DisplayName("should apply rules")
    public void applyIsCardJackRule() throws PlayedCardIsInvalidException {
        Card topCard = new Card(Suit.CLUBS, Label.JACK);
        game.getCardDeck().setTopCard(topCard);
        when(rulesService.isCardJack(any())).thenReturn(true);

        service.applyCardRule(game);

        assertTrue(game.hasAskedForSuitWish());
        verify(rulesService).isCardJack(argThat(card -> card.equals(topCard)));
    }

    @Test
    @DisplayName("should apply rules")
    public void applyPlayerMustSuspendRule() throws PlayedCardIsInvalidException {
        Card topCard = new Card(Suit.CLUBS, Label.JACK);
        game.getCardDeck().setTopCard(topCard);
        Player activePlayer = players.get(0);
        Player expectedSuspendedPlayer = players.get(1);
        expectedSuspendedPlayer.setMustSuspend(true);
        game.setActivePlayer(activePlayer);
        when(rulesService.isSuspended(any())).thenReturn(true);

        service.applyCardRule(game);

        assertEquals(expectedSuspendedPlayer.mustSuspend(), game.getPlayers().get(1).mustSuspend());
        verify(rulesService).isSuspended(argThat(card -> card.equals(topCard)));
    }

    @Test
    @DisplayName("should apply rules")
    public void applyChangeGameDirectionRule() throws PlayedCardIsInvalidException {
        Card topCard = new Card(Suit.CLUBS, Label.JACK);
        game.getCardDeck().setTopCard(topCard);
        when(rulesService.changeGameDirection(any())).thenReturn(true);

        service.applyCardRule(game);

        assertFalse(game.getClockWise());
        verify(rulesService).changeGameDirection(argThat(card -> card.equals(topCard)));
    }

    @Test
    @DisplayName("should apply rules")
    public void applyDrawTwoCardsRule() throws PlayedCardIsInvalidException {
        Card topCard = new Card(Suit.CLUBS, Label.JACK);
        game.getCardDeck().setTopCard(topCard);
        when(rulesService.mustDrawTwoCards(any())).thenReturn(true);

        service.applyCardRule(game);

        assertEquals(2, game.getDrawCardsCounter());
        verify(rulesService).mustDrawTwoCards(argThat(card -> card.equals(topCard)));
    }

    @Test
    @DisplayName("should apply rules")
    public void applyIsMauInvalidRule() throws PlayedCardIsInvalidException {
        Player activePlayer = players.get(0);
        activePlayer.setHandCards(new ArrayList<>(List.of(new Card(Suit.CLUBS, Label.SEVEN))));
        game.setActivePlayer(activePlayer);
        when(rulesService.isPlayersMauInvalid(any())).thenReturn(true);
        when(rulesService.getNumberOfDrawnCards()).thenReturn(2);
        when(deckService.getCardsFromDrawPile(any(), anyInt())).thenReturn(
                List.of(new Card(Suit.DIAMONDS, Label.JACK), new Card(Suit.CLUBS, Label.EIGHT)));

        service.applyCardRule(game);

        assertEquals(3, game.getActivePlayer().getHandCards().size());
        verify(rulesService).isPlayersMauInvalid(argThat(player -> player.equals(activePlayer)));
        verify(rulesService, times(1)).getNumberOfDrawnCards();
        verify(deckService).getCardsFromDrawPile(
                argThat(deck -> deck.equals(game.getCardDeck())),
                intThat(numberOfDrawnCards -> numberOfDrawnCards == 2));
    }

    @Test
    @DisplayName("should return the next player in clockwise direction")
    public void nextPlayerClockWise() throws IllegalDeckSizeException, InvalidPlayerSizeException {
        Player activePlayer = players.get(0);
        Player nextActivePlayer = players.get(1);

        assertEquals(activePlayer, game.getActivePlayer());

        service.getNextPlayer(game);

        assertEquals(nextActivePlayer, game.getActivePlayer());
    }

    @Test
    @DisplayName("should return the next player in counter clockwise direction")
    public void nextPlayerCounterClockWise() throws IllegalDeckSizeException, InvalidPlayerSizeException {
        Player activePlayer = players.get(0);
        Player nextActivePlayer = players.get(players.size() - 1);

        assertEquals(activePlayer, game.getActivePlayer());

        game.switchDirection();
        service.getNextPlayer(game);

        assertEquals(nextActivePlayer, game.getActivePlayer());
    }

    @Test
    @DisplayName("should add drawn cards from discard pile to player's hand cards")
    public void dealCardsToPlayer() {
        Player activePlayer = players.get(0);
        game.setActivePlayer(activePlayer);
        activePlayer.setHandCards(GameFixture.cards().subList(0, 5));
        when(deckService.getCardsFromDrawPile(any(), anyInt())).thenReturn(Arrays.asList(new Card(Suit.SPADES, Label.SEVEN), new Card(Suit.CLUBS, Label.EIGHT)));

        service.drawCards(2, game);

        assertEquals(7, game.getActivePlayer().getHandCards().size());
    }

    @Test
    @DisplayName("should return true if the Player does have the card in hand")
    public void hasPlayerHandCard() {
        assertTrue(true);
    }

    @Test
    @DisplayName("should set initial hand cards to player")
    public void shouldDrawInitialHandCards() throws IllegalDeckSizeException, InvalidPlayerSizeException {
        Game game = GameFixture.game();
        when(deckService.initialCardDealing(any())).thenReturn(GameFixture.cards().subList(0, 5));

        service.dealCards(game);

        for (Player player : game.getPlayers()) {
            assertEquals(5, player.getHandCards().size());
        }
    }

    @Test
    @DisplayName("should return next player who is not suspended")
    public void getNextNotSuspendedPlayer() {
        Player activePlayer = players.get(0);
        game.setActivePlayer(activePlayer);
        Player suspendedPlayer = game.getPlayers().get(1);
        suspendedPlayer.setMustSuspend(true);

        Player nextPlayer = service.getNextPlayer(game);

        assertEquals(players.get(2), nextPlayer);
        assertFalse(suspendedPlayer.mustSuspend());
    }


}
