package htw.kbe.appcontroller.application;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.service.CardService;
import htw.kbe.maumau.card.service.CardServiceImpl;
import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;
import htw.kbe.maumau.game.domain.Game;
import htw.kbe.maumau.game.exceptions.InvalidPlayerSizeException;
import htw.kbe.maumau.game.service.GameService;
import htw.kbe.maumau.game.service.GameServiceImpl;
import htw.kbe.maumau.player.domain.Player;
import htw.kbe.maumau.player.exceptions.InvalidPlayerNameException;
import htw.kbe.maumau.player.service.PlayerService;
import htw.kbe.maumau.player.service.PlayerServiceImpl;
import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;
import htw.kbe.maumau.uicontroller.service.UI;
import htw.kbe.maumau.uicontroller.service.UIImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.Random;


@ComponentScan(basePackages = {"htw.kbe.maumau.deck", "htw.kbe.maumau.rule", "htw.kbe.maumau.player", "htw.kbe.maumau.card", "htw.kbe.maumau.uicontroller", "htw.kbe.maumau.game"})
@Configuration
public class App {

    public static void main(String[] args) {
        final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(App.class);

        applicationContext.scan(GameServiceImpl.class.getPackage().getName());
        applicationContext.scan(PlayerServiceImpl.class.getPackage().getName());
        applicationContext.scan(UIImpl.class.getPackage().getName());
        applicationContext.scan(CardServiceImpl.class.getPackage().getName());

        final PlayerService playerService = applicationContext.getBean(PlayerServiceImpl.class);
        final GameService gameService = applicationContext.getBean(GameServiceImpl.class);
        final UI uiImpl = applicationContext.getBean(UIImpl.class);
        final CardService cardService = applicationContext.getBean(CardServiceImpl.class);

        while (true) {
            try {
                Game game = initializeGameStart(playerService, gameService, uiImpl, cardService);
                runGame(gameService, uiImpl, cardService, game);
            } catch (IllegalDeckSizeException illegalDeckSizeException) {
                System.out.println(illegalDeckSizeException.getMessage());
            }
            if (!uiImpl.hasNextRound()) {
                break;
            }
        }


        applicationContext.close();
    }


    private static void runGame(GameService gameService, UI uiImpl, CardService cardService, Game game) {
        while (true) {
            Player activePlayer = game.getActivePlayer();

            if (activePlayer.getHandCards().size() > 1) {
                gameService.resetPlayersMau(game);
            }

            if (gameService.mustPlayerDrawCards(game)) {
                handleDrawingCards(gameService, uiImpl, game, activePlayer);
                continue;
            }

            if (game.getLapCounter() != 1) {  // when round is equal 1, top card was shown in handleFirstRound()
                uiImpl.showTopCard(game.getCardDeck().getTopCard());
            }

            uiImpl.showHandCards(activePlayer, game.getSuitWish());

            if (uiImpl.playerWantToDrawCards()) {
                handleDrawingCards(gameService, uiImpl, game, activePlayer);
            } else {
                handlePlayedCard(gameService, uiImpl, game, activePlayer);

                if (gameService.isGameOver(game)) {
                    uiImpl.showWinnerMessage(activePlayer);
                    break;
                }

                if (game.hasAskedForSuitWish()) {
                    gameService.setPlayersSuitWish(uiImpl.getChosenSuit(activePlayer, cardService.getSuits()), game);
                }
            }

            gameService.getNextPlayer(game);
            game.addUpLapCounter();
        }
    }

    private static void handlePlayedCard(GameService gameService, UI uiImpl, Game game, Player activePlayer) {
        while (true) {
            try {
                Map<Card, Boolean> playedCardAndMau = uiImpl.getPlayedCard(activePlayer);

                if (playedCardAndMau.values().stream().findFirst().get()) {
                    game.getActivePlayer().setSaidMau(true);
                }

                gameService.validateCard(playedCardAndMau.keySet().stream().findFirst().get(), game);
                gameService.applyCardRule(game);
                break;
            } catch (PlayedCardIsInvalidException e) {
                uiImpl.showValidationFailedMessage(e.getMessage());
                if (uiImpl.playerWantToDrawCards()) {
                    handleDrawingCards(gameService, uiImpl, game, activePlayer);
                    break;
                }
            }
        }
    }

    private static void handleDrawingCards(GameService gameService, UI uiImpl, Game game, Player activePlayer) {
        if (game.getDrawCardsCounter() < 2) {
            game.setDrawCardsCounter(1);
        }
        uiImpl.showDrawnCardMessage(activePlayer, game.getDrawCardsCounter());
        gameService.giveDrawnCardsToPlayer(game.getDrawCardsCounter(), game);
    }

    private static Game initializeGameStart(PlayerService playerService, GameService gameService, UI uiImpl, CardService cardService) throws IllegalDeckSizeException {
        Game game;
        while (true) {
            try {
                List<String> playerNames = uiImpl.getPlayerNames(uiImpl.getNumberOfPlayer());
                game = gameService.startNewGame(playerService.createPlayers(playerNames));
                break;
            } catch (InvalidPlayerNameException | InvalidPlayerSizeException playerServiceException) {
                uiImpl.showValidationFailedMessage(playerServiceException.getMessage());
            }
        }

        gameService.initialCardDealing(game);
        uiImpl.showStartGameMessage();
        uiImpl.showTopCard(game.getCardDeck().getTopCard());
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

