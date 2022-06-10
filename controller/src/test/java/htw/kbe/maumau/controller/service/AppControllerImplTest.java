package htw.kbe.maumau.controller.service;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Label;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.controller.export.AppController;
import htw.kbe.maumau.controller.export.ViewService;
import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;
import htw.kbe.maumau.game.exceptions.InvalidPlayerSizeException;
import htw.kbe.maumau.game.export.Game;
import htw.kbe.maumau.game.export.GameService;
import htw.kbe.maumau.player.exceptions.InvalidPlayerNameException;
import htw.kbe.maumau.player.export.Player;
import htw.kbe.maumau.player.export.PlayerService;
import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AppControllerImplTest {

    @InjectMocks
    private AppController appController = new AppControllerImpl();
    @Mock
    private GameService gameService;
    @Mock
    private ViewService viewService;

    @Mock
    private PlayerService playerService;

    private List<String> playerNames = List.of("Philipp", "Jasmin");
    private Game game = GameFixture.game();

    @Test
    @DisplayName("should play 2 rounds and game ends with a winner without exceptions")
    public void runGame() throws InvalidPlayerNameException, IllegalDeckSizeException, InvalidPlayerSizeException, PlayedCardIsInvalidException {
        game.getActivePlayer().setHandCards(List.of(new Card(Suit.SPADES, Label.ASS)));
        game.getPlayers().get(1).setHandCards(List.of(new Card(Suit.CLUBS, Label.KING)));
        game.getPlayers().get(1).setSaidMau(true);

        // initializeGameStart()
        when(viewService.getNumberOfPlayer()).thenReturn(2);
        when(viewService.getPlayerNames(anyInt())).thenReturn(playerNames);
        when(playerService.createPlayers(anyList())).thenReturn(game.getPlayers());
        doNothing().when(gameService).initialCardDealing(any());
        doNothing().when(viewService).showStartGameMessage();
        doNothing().when(viewService).showTopCard(any());
        when(gameService.createGame(any())).thenReturn(game);
        doNothing().when(gameService).applyCardRule(any());

        // runGame() starts here
        when(gameService.mustPlayerDrawCards(any())).thenReturn(false);

        // Player 1 wants to draw a card
        doNothing().when(viewService).showHandCards(any(), any());
        when(viewService.playerWantToDrawCards()).thenReturn(true, false);
        doNothing().when(viewService).showDrawnCardMessage(any(), anyInt());
        doNothing().when(gameService).giveDrawnCardsToPlayer(anyInt(), any());
        when(gameService.getNextPlayer(any())).thenReturn(game.getPlayers().get(1));
        game.setActivePlayer(game.getPlayers().get(1));

        // Player 2 plays his last card and wins the game
        when(gameService.mustPlayerDrawCards(any())).thenReturn(false);
        when(viewService.getPlayedCard(any())).thenReturn(Map.of(
                new Card(Suit.CLUBS, Label.KING), false));
        doNothing().when(gameService).validateCard(any(), any());
        when(gameService.isGameOver(any())).thenReturn(true);
        doNothing().when(viewService).showWinnerMessage(any());

        // a new round of play is not desired
        when(viewService.hasNextRound()).thenReturn(false);

        appController.playGame();

        verify(viewService, times(1)).getNumberOfPlayer();
        verify(viewService, times(1)).getPlayerNames(
                intThat(n -> n == 2)
        );
        verify(playerService, times(1)).createPlayers(
                argThat(names -> names.equals(playerNames))
        );
        verify(gameService, times(1)).initialCardDealing(
                argThat(g -> g.equals(game))
        );
        verify(viewService, times(1)).showStartGameMessage();
        verify(viewService, times(2)).showTopCard(any());
        verify(gameService, times(1)).createGame(
                argThat(playerList -> playerList.equals(game.getPlayers()))
        );
        verify(gameService, times(2)).applyCardRule(any());
        verify(gameService, times(2)).mustPlayerDrawCards(
                argThat(g -> g.equals(game))
        );
        verify(viewService, times(2)).showHandCards(any(), any());
        verify(viewService, times(2)).playerWantToDrawCards();
        verify(viewService, times(1)).showDrawnCardMessage(argThat(
                        player -> player.equals(game.getActivePlayer())),
                intThat(n -> n == 1)
        );
        verify(gameService, times(1)).giveDrawnCardsToPlayer(
                intThat(n -> n == 1),
                argThat(g -> g.equals(game))
        );
        verify(gameService, times(1)).getNextPlayer(
                argThat(g -> g.equals(game))
        );
        verify(viewService, times(1)).getPlayedCard(
                argThat(player -> player.equals(game.getPlayers().get(1)))
        );
        verify(gameService, times(1)).validateCard(
                argThat(card -> card.equals(game.getPlayers().get(1).getHandCards().get(0))),
                argThat(g -> g.equals(game))
        );
        verify(gameService, times(1)).isGameOver(
                argThat(g -> g.equals(game))
        );
        verify(viewService, times(1)).showWinnerMessage(
                argThat(player -> player.equals(game.getPlayers().get(1)))
        );
        verify(viewService, times(1)).hasNextRound();
    }

}
