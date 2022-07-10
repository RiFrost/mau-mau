package htw.kbe.maumau.controller.service;


import htw.kbe.maumau.controller.dao.GameDao;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Controller
public class AppControllerImpl implements AppController {

    @Autowired
    private GameService gameService;

    @Autowired
    private CardService cardService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    public ViewService viewService;

    @Autowired
    private GameDao gameDao;

    private static Logger logger = LogManager.getLogger(AppControllerImpl.class);

    @Override
    public void play() {
        while (true) {
            try {
                Game game = initializeGameStart(playerService, gameService, viewService, cardService);
                runGame(gameService, viewService, cardService, game);
            } catch (Exception e) {
                viewService.showErrorMessage(e.getMessage());
                logger.error("Exception was thrown with the following message: {}", e.getMessage());
                break;
            }
            if (!viewService.hasNextRound()) {
                break;
            }
            logger.info("A new game round has started");
        }
        logger.info("Game ended");
    }

    private Game initializeGameStart(PlayerService playerService, GameService gameService, ViewService viewService, CardService cardService) throws IllegalDeckSizeException, InvalidPlayerNameException, InvalidPlayerSizeException {
        List<String> playerNames = viewService.getPlayerNames(viewService.getNumberOfPlayer());
        Game game = gameService.createGame(playerService.createPlayers(playerNames));
        gameService.initialCardDealing(game);
        viewService.showStartGameMessage();
        viewService.showTopCard(game.getCardDeck().getTopCard());
        handleFirstRound(gameService, cardService, game);
        return game;
    }

    private void runGame(GameService gameService, ViewService viewService, CardService cardService, Game game) {
        while (true) {
            logger.info("Current round: {}", game.getLapCounter());
            Player activePlayer = game.getActivePlayer();

            if (activePlayer.getHandCards().size() > 1) {
                gameService.resetPlayersMau(game);
            }

            if (gameService.mustPlayerDrawCards(game)) {
                logger.info("Active player {} must draw cards", activePlayer.getName());
                handleDrawingCards(gameService, viewService, game, activePlayer);
                gameService.switchToNextPlayer(game);
                game.addUpLapCounter();
                continue;
            }

            if (game.getLapCounter() != 1) {  // when round is equal 1, top card was shown in handleFirstRound()
                viewService.showTopCard(game.getCardDeck().getTopCard());
            }

            viewService.showHandCards(activePlayer, game.getSuitWish());
            handlePlayersTurn(gameService, viewService, game, activePlayer);

            if (gameService.isGameOver(game)) {
                viewService.showWinnerMessage(activePlayer);
                logger.info("Game is over. Player {} won", activePlayer.getName());
                break;
            }

            if (game.hasAskedForSuitWish()) {
                gameService.setPlayersSuitWish(viewService.getChosenSuit(activePlayer, cardService.getSuits()), game);
            }

            gameDao.create(game);

            gameService.switchToNextPlayer(game);
            game.addUpLapCounter();
        }
    }

    private void handlePlayersTurn(GameService gameService, ViewService viewService, Game game, Player activePlayer) {
        while (true) {
            try {
                Card playedCard = viewService.getPlayedCard(activePlayer);
                if (Objects.isNull(playedCard)) {
                    logger.info("Active player {} wants to draw a card", activePlayer.getName());
                    handleDrawingCards(gameService, viewService, game, activePlayer);
                    break;
                }
                if (viewService.saidMau(activePlayer)) {
                    activePlayer.setSaidMau(true);
                }
                gameService.validateCard(playedCard, game);
                gameService.applyCardRule(game);
                break;

            } catch (PlayedCardIsInvalidException e) {
                viewService.showErrorMessage(e.getMessage());
                logger.info("Played card is not valid to play. Player has to choose another card or draw a card");
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
        } else if (game.getActivePlayer().mustSuspend()) {  // Here the active player must suspend not the next player!
            game.getActivePlayer().setMustSuspend(false);
            gameService.switchToNextPlayer(game);
        }
    }

}
