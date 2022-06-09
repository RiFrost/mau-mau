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
    public void runGame() {
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

    private static void runGame(GameService gameService, ViewService viewServiceImpl, CardService cardService, Game game) {
        while (true) {
            Player activePlayer = game.getActivePlayer();

            if (activePlayer.getHandCards().size() > 1) {
                gameService.resetPlayersMau(game);
            }

            if (gameService.mustPlayerDrawCards(game)) {
                handleDrawingCards(gameService, viewServiceImpl, game, activePlayer);
                continue;
            }

            if (game.getLapCounter() != 1) {  // when round is equal 1, top card was shown in handleFirstRound()
                viewServiceImpl.showTopCard(game.getCardDeck().getTopCard());
            }

            viewServiceImpl.showHandCards(activePlayer, game.getSuitWish());

            if (viewServiceImpl.playerWantToDrawCards()) {
                handleDrawingCards(gameService, viewServiceImpl, game, activePlayer);
            } else {
                handlePlayedCard(gameService, viewServiceImpl, game, activePlayer);

                if (gameService.isGameOver(game)) {
                    viewServiceImpl.showWinnerMessage(activePlayer);
                    break;
                }

                if (game.hasAskedForSuitWish()) {
                    gameService.setPlayersSuitWish(viewServiceImpl.getChosenSuit(activePlayer, cardService.getSuits()), game);
                }
            }

            gameService.getNextPlayer(game);
            game.addUpLapCounter();
        }
    }

    private static void handlePlayedCard(GameService gameService, ViewService viewServiceImpl, Game game, Player activePlayer) {
        while (true) {
            try {
                Map<Card, Boolean> playedCardAndMau = viewServiceImpl.getPlayedCard(activePlayer);

                if (playedCardAndMau.values().stream().findFirst().get()) {
                    game.getActivePlayer().setSaidMau(true);
                }

                gameService.validateCard(playedCardAndMau.keySet().stream().findFirst().get(), game);
                gameService.applyCardRule(game);
                break;
            } catch (PlayedCardIsInvalidException e) {
                viewServiceImpl.showValidationFailedMessage(e.getMessage());
                if (viewServiceImpl.playerWantToDrawCards()) {
                    handleDrawingCards(gameService, viewServiceImpl, game, activePlayer);
                    break;
                }
            }
        }
    }

    private static void handleDrawingCards(GameService gameService, ViewService viewServiceImpl, Game game, Player activePlayer) {
        if (game.getDrawCardsCounter() < 2) {
            game.setDrawCardsCounter(1);
        }
        viewServiceImpl.showDrawnCardMessage(activePlayer, game.getDrawCardsCounter());
        gameService.giveDrawnCardsToPlayer(game.getDrawCardsCounter(), game);
    }

    private static Game initializeGameStart(PlayerService playerService, GameService gameService, ViewService viewServiceImpl, CardService cardService) throws IllegalDeckSizeException {
        Game game;
        while (true) {
            try {
                List<String> playerNames = viewServiceImpl.getPlayerNames(viewServiceImpl.getNumberOfPlayer());
                game = gameService.startNewGame(playerService.createPlayers(playerNames));
                break;
            } catch (InvalidPlayerNameException | InvalidPlayerSizeException playerServiceException) {
                viewServiceImpl.showValidationFailedMessage(playerServiceException.getMessage());
            }
        }

        gameService.initialCardDealing(game);
        viewServiceImpl.showStartGameMessage();
        viewServiceImpl.showTopCard(game.getCardDeck().getTopCard());
        handleFirstRound(gameService, cardService, game);
        return game;
    }

    private static void handleFirstRound(GameService gameService, CardService cardService, Game game) {
        gameService.applyCardRule(game);   // Important when a SEVEN, JACK, ASS etc. is a top card at the beginning

        if (game.hasAskedForSuitWish()) {
            int rdmNumber = new Random().nextInt(cardService.getSuits().size());
            gameService.setPlayersSuitWish(cardService.getSuits().get(rdmNumber), game);
        } else if (!game.isClockWise()) {
            gameService.getNextPlayer(game);
        } else if (game.getActivePlayer().mustSuspend()) {
            game.getActivePlayer().setMustSuspend(false);
            gameService.getNextPlayer(game);
        }
    }


}
