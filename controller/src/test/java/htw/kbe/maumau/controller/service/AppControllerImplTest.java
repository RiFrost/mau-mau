package htw.kbe.maumau.controller.service;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.CardService;
import htw.kbe.maumau.card.export.Label;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.controller.export.ViewService;
import htw.kbe.maumau.controller.fixtures.GameFixture;
import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;
import htw.kbe.maumau.game.exceptions.GameNotFoundException;
import htw.kbe.maumau.game.exceptions.InvalidPlayerSizeException;
import htw.kbe.maumau.game.export.Game;
import htw.kbe.maumau.game.export.GameService;
import htw.kbe.maumau.player.exceptions.InvalidPlayerNameException;
import htw.kbe.maumau.player.export.Player;
import htw.kbe.maumau.player.export.PlayerService;
import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class AppControllerImplTest {

    @InjectMocks
    private AppControllerImpl appController;
    @Mock
    private GameService gameService;
    @Mock
    private ViewService viewService;
    @Mock
    private PlayerService playerService;
    @Mock
    private CardService cardService;

    private List<String> playerNames = Arrays.asList("Philipp", "Jasmin", "Richard");
    private Game game;

    // Helper function
    private Player setActivePlayer(int number) {
        game.setActivePlayer(game.getPlayers().get(number));
        return null;
    }

    // Helper function
    private boolean setAskForUserWish() {
        game.setAskForSuitWish(true);
        return true;
    }

    @BeforeEach
    public void setUp() {
        game = GameFixture.game();
        game.getActivePlayer().setHandCards(List.of(new Card(Suit.SPADES, Label.ASS)));
        game.getPlayers().get(1).setHandCards(List.of(new Card(Suit.HEARTS, Label.KING), new Card(Suit.CLUBS, Label.JACK)));
        game.getPlayers().get(2).setHandCards(List.of(new Card(Suit.CLUBS, Label.KING)));
        game.getPlayers().get(2).setSaidMau(true);
        game.setDrawCardsCounter(2);
    }

    @Test
    @DisplayName("should play 3 rounds and game ends with a winner without exceptions")
    public void runGame() throws InvalidPlayerNameException, IllegalDeckSizeException, InvalidPlayerSizeException, PlayedCardIsInvalidException {
        // initializeGameStart()
        doNothing().when(viewService).showWelcomeMessage();
        when(gameService.hasGame()).thenReturn(false);
        when(viewService.getNumberOfPlayer()).thenReturn(3);
        when(viewService.getPlayerNames(anyInt())).thenReturn(playerNames);
        when(playerService.createPlayers(anyList(), anyInt())).thenReturn(game.getPlayers());
        doNothing().when(gameService).initialCardDealing(any());
        doNothing().when(viewService).showStartGameMessage(game.getId());
        doNothing().when(viewService).showTopCard(any());
        when(gameService.createGame(any())).thenReturn(game);
        doNothing().when(gameService).applyCardRule(any());
        doNothing().when(viewService).showPlayedCard(any(), any());
        doNothing().when(gameService).saveGame(any());

        // gaming loop starts here
        doNothing().when(viewService).showActivePlayer(any());
        when(gameService.mustPlayerDrawCards(any())).thenReturn(true, false, false);

        // Player 1 has to draw cards because of a SEVEN
        doAnswer(a -> setActivePlayer(1)).doAnswer(a -> setActivePlayer(2)).when(gameService).switchToNextPlayer(any());

        // Player 2 wants to draw a card voluntary
        doNothing().when(viewService).showHandCards(any(), any());
        doNothing().when(viewService).showDrawnCardMessage(any(), anyInt());
        doNothing().when(gameService).giveDrawnCardsToPlayer(anyInt(), any());

        // Player 3 plays his last card and wins the game
        when(viewService.getPlayedCard(any())).thenReturn(null, new Card(Suit.CLUBS, Label.KING));
        when(viewService.saidMau(any())).thenReturn(false);
        doNothing().when(gameService).validateCard(any(), any());
        doNothing().when(viewService).showPlayersMau(any());
        when(gameService.isGameOver(any())).thenReturn(false, true);
        doNothing().when(viewService).showWinnerMessage(any());
        doNothing().when(gameService).deleteGame(any());

        // a new round of play is not desired
        when(viewService.hasNextRound()).thenReturn(false);

        assertDoesNotThrow(() -> appController.play());

        verify(viewService).showWelcomeMessage();
        verify(gameService).hasGame();
        verify(viewService).getNumberOfPlayer();
        verify(viewService).getPlayerNames(intThat(n -> n == 3));
        verify(playerService).createPlayers(argThat(names -> names.equals(playerNames)), anyInt());
        verify(gameService).initialCardDealing(argThat(g -> g.equals(game)));
        verify(viewService).showStartGameMessage(game.getId());
        verify(viewService, times(3)).showActivePlayer(any());
        verify(viewService, times(3)).showTopCard(any());
        verify(gameService).createGame(argThat(playerList -> playerList.equals(game.getPlayers())));
        verify(viewService).showPlayersMau(argThat(player -> player.getName().equals("Richard")));
        verify(gameService).validateCard(argThat(card -> card.equals(new Card(Suit.CLUBS, Label.KING))), argThat(g -> g.equals(game)));
        verify(gameService, times(2)).applyCardRule(any());
        verify(viewService).showPlayedCard(argThat(player -> player.getName().equals("Richard")), argThat(card -> card.equals(new Card(Suit.CLUBS, Label.KING))));
        verify(gameService, times(3)).mustPlayerDrawCards(argThat(g -> g.equals(game)));
        verify(viewService, times(2)).showHandCards(any(), any());
        verify(viewService, times(2)).showDrawnCardMessage(any(), anyInt());
        verify(gameService, times(2)).giveDrawnCardsToPlayer(anyInt(), any());
        verify(gameService, times(2)).switchToNextPlayer(argThat(g -> g.equals(game)));
        verify(gameService, times(2)).saveGame(argThat(g -> g.equals(game)));
        verify(viewService, times(2)).getPlayedCard(any());
        verify(viewService).saidMau(argThat(player -> player.getName().equals("Richard")));
        verify(gameService, times(2)).isGameOver(any());
        verify(viewService).showWinnerMessage(argThat(winner -> winner.getName().equals("Richard")));
        verify(gameService).deleteGame(argThat(g -> g.equals(game)));
        verify(viewService).hasNextRound();
    }

    @Test
    @DisplayName("should catch any Exception (here InvalidPlayerSizeException) that is thrown during the game")
    public void testCatchException() throws InvalidPlayerNameException, IllegalDeckSizeException, InvalidPlayerSizeException {
        String exceptionMessage = "Number of players is not valid";
        doNothing().when(viewService).showWelcomeMessage();
        when(gameService.hasGame()).thenReturn(false);

        // initializeGameStart()
        when(viewService.getNumberOfPlayer()).thenReturn(3);
        when(viewService.getPlayerNames(anyInt())).thenReturn(playerNames);
        when(gameService.createGame(any())).thenThrow(new InvalidPlayerSizeException(exceptionMessage));
        doNothing().when(viewService).showErrorMessage(anyString());

        assertDoesNotThrow(() -> appController.play());

        verify(viewService).showWelcomeMessage();
        verify(gameService).hasGame();
        verify(viewService).getNumberOfPlayer();
        verify(viewService).getPlayerNames(intThat(n -> n == 3));
        verify(playerService).createPlayers(argThat(names -> names.equals(playerNames)), anyInt());
        verify(viewService).showErrorMessage(argThat(msg -> msg.equals(exceptionMessage)));
    }

    @Test
    @DisplayName("should only allow valid cards and should set player's suit wish when JACK has been played")
    public void testCardValidationAndChoseSuit() throws PlayedCardIsInvalidException, GameNotFoundException {
        String exceptionMessage = "The card cannot be played. Label or suit does not match.";
        game.getPlayers().remove(0);
        game.setActivePlayer(game.getPlayers().get(0));
        playerNames.remove("Phil");

        // load game
        doNothing().when(viewService).showWelcomeMessage();
        when(gameService.hasGame()).thenReturn(true);
        when(viewService.playerWantsToLoadGame()).thenReturn(true);
        when(viewService.getGameId()).thenReturn(1L);
        when(gameService.getSavedGame(anyLong())).thenReturn(game);

        // gaming loop starts here
        doNothing().when(viewService).showActivePlayer(any());
        doNothing().when(gameService).resetPlayersMau(any());
        when(gameService.mustPlayerDrawCards(any())).thenReturn(false, false);
        doNothing().when(viewService).showHandCards(any(), any());

        // handlePlayedCard()
        // Player 1 play at first an invalid card then a valid card with label JACK and chose a suit
        // Player 2 play his last card and win the game
        when(viewService.getPlayedCard(any())).thenReturn(new Card(Suit.HEARTS, Label.KING), new Card(Suit.CLUBS, Label.JACK), new Card(Suit.CLUBS, Label.KING));
        when(viewService.saidMau(any())).thenReturn(false, true, false);
        doThrow(new PlayedCardIsInvalidException(exceptionMessage)).doNothing().doNothing().when(gameService).validateCard(any(), any());
        doNothing().when(viewService).showErrorMessage(exceptionMessage);
        when(gameService.isGameOver(any())).thenReturn(false, true);
        when(cardService.getSuits()).thenReturn(GameFixture.suits);
        doAnswer(a -> setAskForUserWish()).doNothing().when(gameService).applyCardRule(any());
        doNothing().when(viewService).showPlayedCard(any(), any());
        when(viewService.getChosenSuit(any(), anyList())).thenReturn(Suit.CLUBS);
        doNothing().when(gameService).setPlayersSuitWish(any(), any());
        doAnswer(p -> setActivePlayer(1)).when(gameService).switchToNextPlayer(any());
        doNothing().when(gameService).saveGame(any());
        doNothing().when(viewService).showWinnerMessage(any());
        doNothing().when(gameService).deleteGame(any());
        when(viewService.hasNextRound()).thenReturn(false);

        assertDoesNotThrow(() -> appController.play());

        verify(viewService).showWelcomeMessage();
        verify(gameService).hasGame();
        verify(viewService).playerWantsToLoadGame();
        verify(viewService).getGameId();
        verify(viewService, times(2)).showActivePlayer(any());
        verify(gameService).getSavedGame(longThat(id -> id == game.getId()));
        verify(viewService).showTopCard(any());
        verify(gameService, times(2)).applyCardRule(argThat(g -> g.equals(game)));
        verify(viewService, times(2)).showPlayedCard(any(), any());
        verify(gameService).resetPlayersMau(argThat(g -> g.equals(game)));
        verify(gameService, times(2)).mustPlayerDrawCards(any());
        verify(viewService, times(2)).showHandCards(any(), any());
        verify(viewService, times(3)).getPlayedCard(any());
        verify(viewService, times(3)).saidMau(any());
        verify(gameService, times(3)).validateCard(any(), any());
        verify(viewService).showErrorMessage(argThat(msg -> msg.equals(exceptionMessage)));
        verify(gameService, times(2)).isGameOver(argThat(g -> g.equals(game)));
        verify(cardService).getSuits();
        verify(viewService).getChosenSuit(
                argThat(player -> player.getName().equals("Jasmin")),
                argThat(suits -> suits.equals(GameFixture.suits)));
        verify(gameService).setPlayersSuitWish(
                argThat(suit -> suit.equals(Suit.CLUBS)),
                argThat(g -> g.equals(game)));
        verify(gameService).switchToNextPlayer(argThat(g -> g.equals(game)));
        verify(gameService, times(1)).saveGame(any());
        verify(viewService).showWinnerMessage(argThat(winner -> winner.getName().equals("Richard")));
        verify(gameService).deleteGame(argThat(g -> g.equals(game)));
        verify(viewService).hasNextRound();
    }

}
