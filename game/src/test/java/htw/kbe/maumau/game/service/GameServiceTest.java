package htw.kbe.maumau.game.service;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.CardService;
import htw.kbe.maumau.card.export.Label;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;
import htw.kbe.maumau.deck.export.DeckService;
import htw.kbe.maumau.game.dao.GameDao;
import htw.kbe.maumau.game.export.Game;
import htw.kbe.maumau.game.exceptions.InvalidPlayerSizeException;
import htw.kbe.maumau.game.fixtures.GameFixture;
import htw.kbe.maumau.player.export.Player;
import htw.kbe.maumau.player.export.PlayerService;
import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;
import htw.kbe.maumau.rule.export.RulesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @InjectMocks
    private GameServiceImpl service;
    @Mock
    private DeckService deckService;
    @Mock
    private RulesService rulesService;
    @Mock
    private CardService cardService;
    @Mock
    private PlayerService playerService;
    @Mock
    private GameDao gameDao;

    private List<Player> players;
    private Game game;

    @BeforeEach
    public void setUp() {
        players = GameFixture.players();
        game = GameFixture.game();
    }

    @Test
    @DisplayName("should return a new instance of game with Cards and the player list")
    public void testCreateValidGame() throws IllegalDeckSizeException, InvalidPlayerSizeException {
        when(cardService.getCards()).thenReturn(GameFixture.cards());
        when(deckService.createDeck(anyList())).thenReturn(GameFixture.deck());
        doNothing().when(gameDao).saveGame(any());
        Game game = service.createGame(players);

        verify(gameDao).saveGame(argThat(g -> g.equals(game)));
        verify(cardService).getCards();
        verify(deckService).createDeck(anyList());
        assertEquals(players, game.getPlayers());
        assertEquals(players.get(0), game.getActivePlayer());
        assertNotEquals(null, game.getCardDeck().getTopCard());
        assertEquals(31, game.getCardDeck().getDrawPile().size());
        assertTrue(game.isClockWise());
    }

    @Test
    @DisplayName("should throw exception when player list size is smaller than two")
    public void throwExceptionPlayerSizeIsTooLow() {

       assertThrows(InvalidPlayerSizeException.class, () -> service.createGame(players.subList(0, 1)));
    }

    @Test
    @DisplayName("should throw exception when player list size is higher than four")
    public void throwExceptionPlayerSizeIsTooHigh() {
        players.add(players.get(0));

        assertThrows(InvalidPlayerSizeException.class, () -> service.createGame(players));
    }

    @Test
    @DisplayName("should set userWish and set askForSuit state to false")
    public void setUserWish() {
        Suit userWish = Suit.CLUBS;
        game.setAskForSuitWish(true);

        service.setPlayersSuitWish(userWish, game);

        assertFalse(game.hasAskedForSuitWish());
        assertEquals(userWish, game.getSuitWish());
    }

    @Test
    @DisplayName("should call rulesService, deckService and playerService when validate played card")
    public void validateCard() throws PlayedCardIsInvalidException {
        Card expectedTopCard = new Card(Suit.CLUBS, Label.KING);
        Card playedCard = new Card(Suit.CLUBS, Label.SEVEN);
        Suit userWish = Suit.CLUBS;
        game.setSuitWish(userWish);
        game.getCardDeck().setTopCard(expectedTopCard);

        service.validateCard(playedCard, game);

        assertNull(game.getSuitWish());
        verify(rulesService, times(1)).validateCard(
                argThat(card -> card.equals(playedCard)),
                argThat(topCard -> topCard.equals(expectedTopCard)),
                argThat(suit -> suit.equals(userWish)),
                intThat(drawCounter -> drawCounter == game.getDrawCardsCounter())
        );

        verify(deckService, times(1)).setCardToTopCard(
                argThat(deck -> deck.equals(game.getCardDeck())),
                argThat(card -> card.equals(playedCard))
        );
        verify(playerService, times(1)).removePlayedCard(
                argThat(player -> player.equals(game.getActivePlayer())),
                argThat(card -> card.equals(playedCard))
        );
    }

    @Test
    @DisplayName("should not allow that player can play a card")
    public void playerCannotPlayCards() {
        game.getActivePlayer().setHandCards(List.of(new Card(Suit.DIAMONDS, Label.ASS)));
        game.getCardDeck().setTopCard(new Card(Suit.DIAMONDS, Label.SEVEN));
        when(rulesService.mustDrawCards(any(), any(), anyInt())).thenReturn(true);

        assertTrue(service.mustPlayerDrawCards(game));
    }

    @Test
    @DisplayName("should allow that player can play a card")
    public void playerCanPlayCards() {
        game.getActivePlayer().setHandCards(List.of(new Card(Suit.CLUBS, Label.SEVEN)));
        game.getCardDeck().setTopCard(new Card(Suit.DIAMONDS, Label.SEVEN));
        when(rulesService.mustDrawCards(any(), any(), anyInt())).thenReturn(false);

        assertFalse(service.mustPlayerDrawCards(game));
    }

    @Test
    @DisplayName("should apply jack on jack rule")
    public void applyIsCardJackRule() {
        Card topCard = new Card(Suit.CLUBS, Label.JACK);
        game.getCardDeck().setTopCard(topCard);
        when(rulesService.isCardJack(any())).thenReturn(true);

        service.applyCardRule(game);

        assertTrue(game.hasAskedForSuitWish());
        verify(rulesService).isCardJack(argThat(card -> card.equals(topCard)));
    }

    @Test
    @DisplayName("should apply suspended rule and set suspend state to next player in turn when lap counter is greater then 1")
    public void applyPlayerMustSuspendRule() {
        Card topCard = new Card(Suit.CLUBS, Label.ASS);
        game.getCardDeck().setTopCard(topCard);
        game.addUpLapCounter();
        Player expectedSuspendedPlayer = players.get(1);
        expectedSuspendedPlayer.setMustSuspend(true);
        when(rulesService.mustSuspend(any())).thenReturn(true);

        service.applyCardRule(game);

        assertEquals(expectedSuspendedPlayer.mustSuspend(), game.getPlayers().get(1).mustSuspend());
        verify(rulesService).mustSuspend(argThat(card -> card.equals(topCard)));
    }

    @Test
    @DisplayName("should apply suspended rule and set suspend state to active player when lap counter is 1")
    public void applyPlayerMustSuspendRule2() {
        Card topCard = new Card(Suit.CLUBS, Label.ASS);
        game.getCardDeck().setTopCard(topCard);
        Player activePlayer = game.getActivePlayer();
        Player expectedPlayer = new Player(activePlayer.getName());
        expectedPlayer.setMustSuspend(true);
        when(rulesService.mustSuspend(any())).thenReturn(true);

        service.applyCardRule(game);

        assertEquals(expectedPlayer.mustSuspend(), game.getActivePlayer().mustSuspend());
        verify(rulesService).mustSuspend(argThat(card -> card.equals(topCard)));
    }

    @Test
    @DisplayName("should apply change game direction rule")
    public void applyChangeGameDirectionRule() {
        Card topCard = new Card(Suit.CLUBS, Label.JACK);
        game.getCardDeck().setTopCard(topCard);
        when(rulesService.changeGameDirection(any())).thenReturn(true);

        service.applyCardRule(game);

        assertFalse(game.isClockWise());
        verify(rulesService).changeGameDirection(argThat(card -> card.equals(topCard)));
    }

    @Test
    @DisplayName("should apply draw two cards rule")
    public void applyDrawTwoCardsRule() {
        Card topCard = new Card(Suit.CLUBS, Label.JACK);
        game.getCardDeck().setTopCard(topCard);
        when(rulesService.mustDrawCards(any())).thenReturn(true);

        service.applyCardRule(game);

        assertEquals(2, game.getDrawCardsCounter());
        verify(rulesService).mustDrawCards(argThat(card -> card.equals(topCard)));
    }

    @Test
    @DisplayName("should apply 'mau' is invalid rule and reset players 'mau")
    public void applyIsMauInvalidRule() {
        Player activePlayer = game.getActivePlayer();
        activePlayer.setHandCards(new ArrayList<>(List.of(new Card(Suit.CLUBS, Label.SEVEN))));
        activePlayer.setSaidMau(true);
        List<Card> drawnCards = List.of(new Card(Suit.DIAMONDS, Label.JACK), new Card(Suit.CLUBS, Label.EIGHT));
        when(rulesService.isPlayersMauInvalid(any())).thenReturn(true);
        when(rulesService.getDefaultNumberOfDrawnCards()).thenReturn(2);
        when(deckService.getCardsFromDrawPile(any(), anyInt())).thenReturn(drawnCards);
        doNothing().when(playerService).addDrawnCards(any(), anyList());

        service.applyCardRule(game);

        verify(rulesService).isPlayersMauInvalid(argThat(player -> player.equals(activePlayer)));
        verify(rulesService, times(1)).getDefaultNumberOfDrawnCards();
        verify(deckService).getCardsFromDrawPile(
                argThat(deck -> deck.equals(game.getCardDeck())),
                intThat(numberOfDrawnCards -> numberOfDrawnCards == 2));
        verify(playerService, times(1)).addDrawnCards(argThat(
                        player -> player.equals(activePlayer)),
                argThat(cards -> cards.equals(drawnCards))
        );
    }

    @Test
    @DisplayName("should return the next player in clockwise direction")
    public void nextPlayerClockWise() {
        Player activePlayer = players.get(0);
        Player nextActivePlayer = players.get(1);

        assertEquals(activePlayer, game.getActivePlayer());

        service.switchToNextPlayer(game);

        assertEquals(nextActivePlayer, game.getActivePlayer());
    }

    @Test
    @DisplayName("should return the first player of the list as next player in clockwise direction")
    public void nextPlayerClockWiseLastPlayer() {
        Player activePlayer = players.get(3);
        Player nextActivePlayer = players.get(0);
        game.setActivePlayer(activePlayer);

        assertEquals(activePlayer, game.getActivePlayer());

        service.switchToNextPlayer(game);

        assertEquals(nextActivePlayer, game.getActivePlayer());
    }

    @Test
    @DisplayName("should return the first player as next in counter clockwise direction")
    public void nextPlayerCounterClockWiseFirstPlayer() {
        Player activePlayer = players.get(0);
        Player nextActivePlayer = players.get(players.size() - 1);

        assertEquals(activePlayer, game.getActivePlayer());

        game.switchDirection();
        service.switchToNextPlayer(game);

        assertEquals(nextActivePlayer, game.getActivePlayer());
    }

    @Test
    @DisplayName("should return the player which comes before in the list in counter clockwise direction")
    public void nextPlayerCounterClockWise() {
        Player activePlayer = players.get(2);
        Player nextActivePlayer = players.get(1);
        game.setActivePlayer(activePlayer);

        assertEquals(activePlayer, game.getActivePlayer());

        game.switchDirection();
        service.switchToNextPlayer(game);

        assertEquals(nextActivePlayer, game.getActivePlayer());
    }

    @Test
    @DisplayName("should add drawn cards from discard pile to players hand cards")
    public void dealCardsToPlayer() {
        Player activePlayer = players.get(0);
        game.setActivePlayer(activePlayer);
        List<Card> drawnCards = List.of(new Card(Suit.SPADES, Label.SEVEN), new Card(Suit.CLUBS, Label.EIGHT));
        when(deckService.getCardsFromDrawPile(any(), anyInt())).thenReturn(drawnCards);
        doNothing().when(playerService).addDrawnCards(any(), anyList());

        service.giveDrawnCardsToPlayer(2, game);

        verify(deckService, times(1)).getCardsFromDrawPile(argThat(
                        deck -> deck.equals(game.getCardDeck())),
                intThat(numberOfDrawnCards -> numberOfDrawnCards == 2)
        );
        verify(playerService, times(1)).addDrawnCards(argThat(
                        player -> player.equals(activePlayer)),
                argThat(cards -> cards.equals(drawnCards))
        );
    }

    @Test
    @DisplayName("should call 'initialCardDealing' and call 'drawCards' for each player in game")
    public void shouldDrawInitialHandCards() {
        when(deckService.initialCardDealing(any())).thenReturn(GameFixture.cards().subList(0, 5));
        doNothing().when(playerService).addDrawnCards(any(), anyList());

        service.initialCardDealing(game);

        verify(deckService, times(4)).initialCardDealing(any());
        verify(playerService, times(4)).addDrawnCards(any(), anyList());
    }

    @Test
    @DisplayName("should return next player who is not suspended")
    public void getNextNotSuspendedPlayer() {
        Player suspendedPlayer = game.getPlayers().get(1);
        suspendedPlayer.setMustSuspend(true);

        service.switchToNextPlayer(game);

        assertEquals(players.get(2), game.getActivePlayer());
        assertFalse(suspendedPlayer.mustSuspend());
    }


    @Test
    @DisplayName("should reset 'mau' state from active player to false")
    public void resetPlayersMau() {
        game.getActivePlayer().setSaidMau(true);

        service.resetPlayersMau(game);

        assertFalse(game.getActivePlayer().saidMau());
    }

    @Test
    @DisplayName("should declare game over when active player has no hand cards")
    public void gameIsOver() {
        game.getActivePlayer().setHandCards(new ArrayList<>());

        assertTrue(service.isGameOver(game));
    }

    @Test
    @DisplayName("should not declare game over when active player has at least one hand card")
    public void gameIsNotOver() {
        game.getActivePlayer().setHandCards(List.of(new Card(Suit.CLUBS, Label.JACK)));

        assertFalse(service.isGameOver(game));
    }

    @Test
    @DisplayName("should return true if games are found in database or false when not")
    public void hasGame(){
        when(gameDao.findGame()).thenReturn(true, false);

        assertTrue(service.hasGame());
        assertFalse(service.hasGame());
    }

    @Test
    @DisplayName("should return true if games are found in database or false when not")
    public void hasGames(){
        when(gameDao.findGame()).thenReturn(true, false);

        assertTrue(service.hasGame());
        assertFalse(service.hasGame());
    }

    @Test
    @DisplayName("should call delete method from dao service for deleting a game object in database")
    public void deleteGame(){
        doNothing().when(gameDao).deleteGame(any());

        service.deleteGame(game);

        verify(gameDao).deleteGame(argThat(g -> g.equals(game)));
    }

    @Test
    @DisplayName("should return game that is found by game id from database")
    public void getGame(){
        when(gameDao.findById(anyLong())).thenReturn(game);

        Game actualGame = service.getGame(1L);

        verify(gameDao).findById(longThat(id -> id == game.getId()));
        assertEquals(game, actualGame);
    }

}
