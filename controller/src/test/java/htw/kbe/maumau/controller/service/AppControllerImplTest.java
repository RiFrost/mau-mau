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
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
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

    private List<String> playerNames = List.of("Philipp", "Jasmin", "Richard");
    private Game game;

    @BeforeEach
    public void setUp() {
        game = GameFixture.game();
        game.getActivePlayer().setHandCards(List.of(new Card(Suit.SPADES, Label.ASS)));
        game.getPlayers().get(1).setHandCards(List.of(new Card(Suit.HEARTS, Label.KING)));
        game.getPlayers().get(2).setHandCards(List.of(new Card(Suit.CLUBS, Label.KING)));
        game.getPlayers().get(2).setSaidMau(true);
        game.setDrawCardsCounter(2);
    }

    public Player getActivePlayer(int number) {
        game.setActivePlayer(game.getPlayers().get(number));
        return game.getActivePlayer();
    }

    @Test
    @DisplayName("should play 3 rounds and game ends with a winner without exceptions")
    public void runGame() throws InvalidPlayerNameException, IllegalDeckSizeException, InvalidPlayerSizeException, PlayedCardIsInvalidException {
        // initializeGameStart()
        when(viewService.getNumberOfPlayer()).thenReturn(3);
        when(viewService.getPlayerNames(anyInt())).thenReturn(playerNames);
        when(playerService.createPlayers(anyList())).thenReturn(game.getPlayers());
        doNothing().when(gameService).initialCardDealing(any());
        doNothing().when(viewService).showStartGameMessage();
        doNothing().when(viewService).showTopCard(any());
        when(gameService.createGame(any())).thenReturn(game);
        doNothing().when(gameService).applyCardRule(any());

        // gaming loop starts here
        when(gameService.mustPlayerDrawCards(any())).thenReturn(true, false, false);

        // Player 1 has to draw cards because of a SEVEN
        when(gameService.switchToNextPlayer(any())).thenAnswer(p -> getActivePlayer(1));

        // Player 2 wants to draw a card
        doNothing().when(viewService).showHandCards(any(), any());
        when(viewService.playerWantToDrawCards()).thenReturn(true, false);
        doNothing().when(viewService).showDrawnCardMessage(any(), anyInt());
        doNothing().when(gameService).giveDrawnCardsToPlayer(anyInt(), any());
        when(gameService.switchToNextPlayer(any())).thenAnswer(p -> (getActivePlayer(2)));

        // Player 3 plays his last card and wins the game
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
                intThat(n -> n == 3)
        );
        verify(playerService, times(1)).createPlayers(
                argThat(names -> names.equals(playerNames))
        );
        verify(gameService, times(1)).initialCardDealing(
                argThat(g -> g.equals(game))
        );
        verify(viewService, times(1)).showStartGameMessage();
        verify(viewService, times(3)).showTopCard(any());
        verify(gameService, times(1)).createGame(
                argThat(playerList -> playerList.equals(game.getPlayers()))
        );
        verify(gameService, times(2)).applyCardRule(any());
        verify(gameService, times(3)).mustPlayerDrawCards(
                argThat(g -> g.equals(game))
        );
        verify(viewService, times(2)).showHandCards(any(), any());
        verify(viewService, times(2)).playerWantToDrawCards();
        verify(viewService, times(2)).showDrawnCardMessage(any(), anyInt());
        verify(gameService, times(2)).giveDrawnCardsToPlayer(anyInt(), any());
        verify(gameService, times(2)).switchToNextPlayer(
                argThat(g -> g.equals(game))
        );
        verify(viewService, times(1)).getPlayedCard(
                argThat(player -> player.equals(game.getPlayers().get(2)))
        );
        verify(gameService, times(1)).validateCard(
                argThat(card -> card.equals(game.getPlayers().get(2).getHandCards().get(0))),
                argThat(g -> g.equals(game))
        );
        verify(gameService, times(1)).isGameOver(
                argThat(g -> g.equals(game))
        );
        verify(viewService, times(1)).showWinnerMessage(
                argThat(player -> player.equals(game.getPlayers().get(2)))
        );
        verify(viewService, times(1)).hasNextRound();
    }

}
