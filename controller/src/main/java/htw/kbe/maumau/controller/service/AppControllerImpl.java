package htw.kbe.maumau.controller.service;


import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.CardService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class AppControllerImpl implements AppController {

    @Autowired
    private GameService gameService;

    @Autowired
    private CardService cardService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    public ViewService viewService;

    @Override
    public void playGame() {
        while (true) {
            try {
                Game game = initializeGameStart(playerService, gameService, viewService, cardService);
                runGame(gameService, viewService, cardService, game);
            } catch (IllegalDeckSizeException illegalDeckSizeException) {
                System.out.println(illegalDeckSizeException.getMessage());
            }
            if (!viewService.hasNextRound()) {
                break;
            }
        }
    }

    private Game initializeGameStart(PlayerService playerService, GameService gameService, ViewService viewService, CardService cardService) throws IllegalDeckSizeException {
        Game game;
        while (true) {
            try {
                List<String> playerNames = viewService.getPlayerNames(viewService.getNumberOfPlayer());
                game = gameService.createGame(playerService.createPlayers(playerNames));
                break;
            } catch (InvalidPlayerNameException | InvalidPlayerSizeException playerServiceException) {
                viewService.showValidationFailedMessage(playerServiceException.getMessage());
            }
        }

        gameService.initialCardDealing(game);
        viewService.showStartGameMessage();
        viewService.showTopCard(game.getCardDeck().getTopCard());
        handleFirstRound(gameService, cardService, game);
        for (Player p : game.getPlayers()) {
           // System.out.println(p.getName());
        }
        return game;
    }

    private void runGame(GameService gameService, ViewService viewService, CardService cardService, Game game) {
        while (true) {
            Player activePlayer = game.getActivePlayer();

            if (activePlayer.getHandCards().size() > 1) {
                gameService.resetPlayersMau(game);
            }

            if (gameService.mustPlayerDrawCards(game)) {
                handleDrawingCards(gameService, viewService, game, activePlayer);
                gameService.switchToNextPlayer(game);
                game.addUpLapCounter();
                continue;
            }

            if (game.getLapCounter() != 1) {  // when round is equal 1, top card was shown in handleFirstRound()
                viewService.showTopCard(game.getCardDeck().getTopCard());
            }

            viewService.showHandCards(activePlayer, game.getSuitWish());

            if (viewService.playerWantToDrawCards()) {
                handleDrawingCards(gameService, viewService, game, activePlayer);
            } else {
                handlePlayedCard(gameService, viewService, game, activePlayer);

                if (gameService.isGameOver(game)) {
                    viewService.showWinnerMessage(activePlayer);
                    break;
                }

                if (game.hasAskedForSuitWish()) {
                    gameService.setPlayersSuitWish(viewService.getChosenSuit(activePlayer, cardService.getSuits()), game);
                }
            }

            gameService.switchToNextPlayer(game);
            game.addUpLapCounter();
        }
    }

    private void handlePlayedCard(GameService gameService, ViewService viewService, Game game, Player activePlayer) {
        while (true) {
            try {
                Map<Card, Boolean> playedCardAndMau = viewService.getPlayedCard(activePlayer);

                if (playedCardAndMau.values().stream().findFirst().get()) {
                    game.getActivePlayer().setSaidMau(true);
                }
                gameService.validateCard(playedCardAndMau.keySet().stream().findFirst().get(), game);
                gameService.applyCardRule(game);
                break;

            } catch (PlayedCardIsInvalidException e) {
                viewService.showValidationFailedMessage(e.getMessage());
                if (viewService.playerWantToDrawCards()) {
                    handleDrawingCards(gameService, viewService, game, activePlayer);
                    break;
                }
            }
        }
    }

    private void handleDrawingCards(GameService gameService, ViewService viewService, Game game, Player activePlayer) {
        if (game.getDrawCardsCounter() < 2) {
            game.setDrawCardsCounter(1);
        }
        viewService.showDrawnCardMessage(activePlayer, game.getDrawCardsCounter());
        gameService.giveDrawnCardsToPlayer(game.getDrawCardsCounter(), game);
    }

    private void handleFirstRound(GameService gameService, CardService cardService, Game game) {
        gameService.applyCardRule(game);   // Important when a SEVEN, JACK, ASS etc. is a top card at the beginning

        if (game.hasAskedForSuitWish()) {
            int rdmNumber = new Random().nextInt(cardService.getSuits().size());
            gameService.setPlayersSuitWish(cardService.getSuits().get(rdmNumber), game);
        } else if (!game.isClockWise()) {
            gameService.switchToNextPlayer(game);
        } else if (game.getActivePlayer().mustSuspend()) {
            game.getActivePlayer().setMustSuspend(false);
            gameService.switchToNextPlayer(game);
        }
    }

}
