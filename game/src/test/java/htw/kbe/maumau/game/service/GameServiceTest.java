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
import htw.kbe.maumau.player.service.PlayerService;
import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;
import htw.kbe.maumau.rule.service.RulesService;
import org.junit.Ignore;
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
    private PlayerService playerService;

    private List<Player> players;
    private Game game;

    @BeforeEach
    public void setUp() {
        deckService = mock(DeckService.class);
        rulesService = mock(RulesService.class);
        cardService = mock(CardService.class);
        playerService = mock(PlayerService.class);
        service = new GameServiceImpl(deckService, cardService, rulesService, playerService);
        players = GameFixture.players();
        game = GameFixture.game();
    }

    @Test
    @DisplayName("should return a new instance of game with Cards and the player list")
    public void testCreateValidGame() throws IllegalDeckSizeException, InvalidPlayerSizeException {
        when(deckService.createDeck(anyList())).thenReturn(GameFixture.deck());

        Game game = service.startNewGame(players);

        assertEquals(players, game.getPlayers());
        assertEquals(players.get(0), game.getActivePlayer());
        assertNotEquals(null, game.getCardDeck().getTopCard());
        assertEquals(31, game.getCardDeck().getDrawPile().size());
        assertTrue(game.getClockWise());
    }

    @Test
    @DisplayName("should throw exception when player list size is smaller than two")
    public void throwExceptionPlayerSizeIsTooLow() throws IllegalDeckSizeException {
        when(deckService.createDeck(anyList())).thenReturn(GameFixture.deck());

        Exception e = assertThrows(InvalidPlayerSizeException.class, () -> {
            service.startNewGame(players.subList(0, 1));
        });
    }

    @Test
    @DisplayName("should throw exception when player list size is higher than four")
    public void throwExceptionPlayerSizeIsTooHigh() throws IllegalDeckSizeException {
        when(deckService.createDeck(anyList())).thenReturn(GameFixture.deck());
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

        service.setPlayersSuitWish(userWish, game);

        assertFalse(game.hasAskedForSuitWish());
        assertEquals(userWish, game.getSuitWish());
    }

    @Test
    @DisplayName("should call rulesService and deckService when validate played card")
    public void validateCard() throws PlayedCardIsInvalidException {
        Card expectedTopCard = new Card(Suit.CLUBS, Label.KING);
        Card playedCard = new Card(Suit.CLUBS, Label.SEVEN);
        Suit userWish = Suit.CLUBS;
        game.setSuitWish(userWish);
        game.getCardDeck().setTopCard(expectedTopCard);

        service.validateCard(playedCard, game);

        assertEquals(null, game.getSuitWish());
        verify(rulesService, times(1)).validateCard(
                argThat(card -> card.equals(playedCard)),
                argThat(topCard -> topCard.equals(expectedTopCard)),
                argThat(suit -> suit.equals(userWish))
        );

        verify(deckService, times(1)).setCardToTopCard(
                argThat(deck -> deck.equals(game.getCardDeck())),
                argThat(card -> card.equals(playedCard))
        );
    }

    @Test
    @DisplayName("should not allow that player can play a card")
    public void playerCannotPlayCards() {
        Player activePlayer = players.get(0);
        activePlayer.setHandCards(List.of(new Card(Suit.DIAMONDS, Label.ASS)));
        game.setActivePlayer(activePlayer);
        game.getCardDeck().setTopCard(new Card(Suit.DIAMONDS, Label.SEVEN));
        when(rulesService.mustDrawCards(any(), any())).thenReturn(true);

        assertFalse(service.canPlayerPlayCards(game));
    }

    @Test
    @DisplayName("should allow that player can play a card")
    public void playerCanPlayCards() {
        Player activePlayer = players.get(0);
        activePlayer.setHandCards(List.of(new Card(Suit.CLUBS, Label.SEVEN)));
        game.setActivePlayer(activePlayer);
        game.getCardDeck().setTopCard(new Card(Suit.DIAMONDS, Label.SEVEN));
        when(rulesService.mustDrawCards(any(), any())).thenReturn(false);

        assertTrue(service.canPlayerPlayCards(game));
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
    @DisplayName("should apply suspended rule")
    public void applyPlayerMustSuspendRule() {
        Card topCard = new Card(Suit.CLUBS, Label.JACK);
        game.getCardDeck().setTopCard(topCard);
        Player activePlayer = players.get(0);
        Player expectedSuspendedPlayer = players.get(1);
        expectedSuspendedPlayer.setMustSuspend(true);
        game.setActivePlayer(activePlayer);
        when(rulesService.mustSuspend(any())).thenReturn(true);

        service.applyCardRule(game);

        assertEquals(expectedSuspendedPlayer.mustSuspend(), game.getPlayers().get(1).mustSuspend());
        verify(rulesService).mustSuspend(argThat(card -> card.equals(topCard)));
    }

    @Test
    @DisplayName("should apply change game direction rule")
    public void applyChangeGameDirectionRule() {
        Card topCard = new Card(Suit.CLUBS, Label.JACK);
        game.getCardDeck().setTopCard(topCard);
        when(rulesService.changeGameDirection(any())).thenReturn(true);

        service.applyCardRule(game);

        assertFalse(game.getClockWise());
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
    @DisplayName("should apply 'mau' is invalid rule")
    public void applyIsMauInvalidRule() {
        Player activePlayer = players.get(0);
        activePlayer.setHandCards(new ArrayList<>(List.of(new Card(Suit.CLUBS, Label.SEVEN))));
        game.setActivePlayer(activePlayer);
        List<Card> drawnCards = List.of(new Card(Suit.DIAMONDS, Label.JACK), new Card(Suit.CLUBS, Label.EIGHT));
        when(rulesService.isPlayersMauInvalid(any())).thenReturn(true);
        when(rulesService.getNumberOfDrawnCards()).thenReturn(2);
        when(deckService.getCardsFromDrawPile(any(), anyInt())).thenReturn(drawnCards);
        doNothing().when(playerService).drawCards(any(), anyList());

        service.applyCardRule(game);

        verify(rulesService).isPlayersMauInvalid(argThat(player -> player.equals(activePlayer)));
        verify(rulesService, times(1)).getNumberOfDrawnCards();
        verify(deckService).getCardsFromDrawPile(
                argThat(deck -> deck.equals(game.getCardDeck())),
                intThat(numberOfDrawnCards -> numberOfDrawnCards == 2));
        verify(playerService, times(1)).drawCards(argThat(
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

        service.getNextPlayer(game);

        assertEquals(nextActivePlayer, game.getActivePlayer());
    }

    @Test
    @DisplayName("should return the first player of the list as next player in clockwise direction")
    public void nextPlayerClockWiseLastPlayer() {
        Player activePlayer = players.get(3);
        Player nextActivePlayer = players.get(0);
        game.setActivePlayer(activePlayer);

        assertEquals(activePlayer, game.getActivePlayer());

        service.getNextPlayer(game);

        assertEquals(nextActivePlayer, game.getActivePlayer());
    }

    @Test
    @DisplayName("should return the first player as next in counter clockwise direction")
    public void nextPlayerCounterClockWiseFirstPlayer() {
        Player activePlayer = players.get(0);
        Player nextActivePlayer = players.get(players.size() - 1);

        assertEquals(activePlayer, game.getActivePlayer());

        game.switchDirection();
        service.getNextPlayer(game);

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
        service.getNextPlayer(game);

        assertEquals(nextActivePlayer, game.getActivePlayer());
    }

    @Test
    @DisplayName("should add drawn cards from discard pile to players hand cards")
    public void dealCardsToPlayer() {
        Player activePlayer = players.get(0);
        game.setActivePlayer(activePlayer);
        List<Card> drawnCards = List.of(new Card(Suit.SPADES, Label.SEVEN), new Card(Suit.CLUBS, Label.EIGHT));
        when(deckService.getCardsFromDrawPile(any(), anyInt())).thenReturn(drawnCards);
        doNothing().when(playerService).drawCards(any(), anyList());

        service.drawCards(2, game);

        verify(deckService, times(1)).getCardsFromDrawPile(argThat(
                        deck -> deck.equals(game.getCardDeck())),
                intThat(numberOfDrawnCards -> numberOfDrawnCards == 2)
        );
        verify(playerService, times(1)).drawCards(argThat(
                        player -> player.equals(activePlayer)),
                argThat(cards -> cards.equals(drawnCards))
        );
    }

    @Test
    @DisplayName("should return true if the player does have the card in hand")
    public void hasPlayerHandCard() {
        assertTrue(true);
    }

    @Test
    @DisplayName("should call 'initialCardDealing' and call 'drawCards' for each player in game")
    public void shouldDrawInitialHandCards() {
        when(deckService.initialCardDealing(any())).thenReturn(GameFixture.cards().subList(0, 5));
        doNothing().when(playerService).drawCards(any(), anyList());

        service.initialCardDealing(game);

        verify(deckService, times(4)).initialCardDealing(any());
        verify(playerService, times(4)).drawCards(any(), anyList());
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
