package htw.kbe.maumau.game.service;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.card.export.CardService;
import htw.kbe.maumau.deck.export.Deck;
import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;
import htw.kbe.maumau.deck.export.DeckService;
import htw.kbe.maumau.game.dao.GameDao;
import htw.kbe.maumau.game.exceptions.DaoException;
import htw.kbe.maumau.game.export.Game;
import htw.kbe.maumau.game.exceptions.InvalidPlayerSizeException;
import htw.kbe.maumau.game.export.GameService;
import htw.kbe.maumau.player.export.Player;
import htw.kbe.maumau.player.export.PlayerService;
import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;
import htw.kbe.maumau.rule.export.RulesService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private DeckService deckService;
    @Autowired
    private CardService cardService;
    @Autowired
    private RulesService rulesService;
    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameDao gameDao;

    private static Logger logger = LogManager.getLogger(GameServiceImpl.class);

    @Override
    public Game createGame(List<Player> players) throws IllegalDeckSizeException, InvalidPlayerSizeException {
        if (players.size() < 2 || players.size() > 4) {
            logger.error("Number of players is not equal or grater than 2 or equal or less than 4");
            throw new InvalidPlayerSizeException("Number of players is not valid");
        }
        Deck deck = deckService.createDeck(cardService.getCards());
        Game game = new Game(players, deck);
        logger.info("Game is created: {}", game);
        gameDao.saveGame(game);
        return game;
    }

    @Override
    public void initialCardDealing(Game game) {
        Deck deck = game.getCardDeck();
        List<Player> players = game.getPlayers();
        for (Player player : players) {
            playerService.addDrawnCards(player, deckService.initialCardDealing(deck));
            logger.info("Player {} got 5 initial hand cards: {}", player.getName(), player.getHandCards());
        }
    }

    @Override
    public void switchToNextPlayer(Game game) {
        game.setActivePlayer(getNextActivePlayer(game));
        if (game.getActivePlayer().mustSuspend()) {
            game.getActivePlayer().setMustSuspend(false);
            switchToNextPlayer(game);
        }
        logger.info("Active player is: {}", game.getActivePlayer());
    }

    private Player getNextActivePlayer(Game game) {
        Player activePlayer = game.getActivePlayer();
        List<Player> players = game.getPlayers();
        int idxActivePlayer = players.indexOf(activePlayer);

        if (game.isClockWise()) {
            if (idxActivePlayer == players.size() - 1) {
                activePlayer = players.get(0);
            } else {
                activePlayer = players.get(++idxActivePlayer);
            }
        } else {
            if (idxActivePlayer == 0) {
                activePlayer = players.get(players.size() - 1);
            } else {
                activePlayer = players.get(--idxActivePlayer);
            }
        }
        return activePlayer;
    }

    @Override
    public void giveDrawnCardsToPlayer(int numberOfDrawnCards, Game game) {
        Player activePlayer = game.getActivePlayer();
        logger.info("Player {} has to draw {} cards", activePlayer.getName(), numberOfDrawnCards);
        List<Card> drawCards = deckService.getCardsFromDrawPile(game.getCardDeck(), numberOfDrawnCards);
        playerService.addDrawnCards(activePlayer, drawCards);

        if (game.getDrawCardsCounter() > 0) {
            game.setDrawCardsCounter(0);
            logger.info("draw counter is set to 0");
        }
    }

    @Override
    public boolean mustPlayerDrawCards(Game game) {
        Player activePlayer = game.getActivePlayer();
        return rulesService.mustDrawCards(activePlayer, game.getCardDeck().getTopCard(), game.getDrawCardsCounter());
    }

    @Override
    public void setPlayersSuitWish(Suit userWish, Game game) {
        game.setSuitWish(userWish);
        logger.info("Player's wish {} is set", userWish);
        game.setAskForSuitWish(false);
    }

    @Override
    public void validateCard(Card card, Game game) throws PlayedCardIsInvalidException {
        Deck deck = game.getCardDeck();
        Card topCard = deck.getTopCard();
        rulesService.validateCard(card, topCard, game.getSuitWish(), game.getDrawCardsCounter());
        deckService.setCardToTopCard(deck, card);
        playerService.removePlayedCard(game.getActivePlayer(), card);
        logger.info("Card {} passed the validation", card);
        if (Objects.nonNull(game.getSuitWish())) {
            game.setSuitWish(null);
        }
    }

    @Override
    public void applyCardRule(Game game) {
        Card topCard = game.getCardDeck().getTopCard();
        if (rulesService.mustDrawCards(topCard)) {
            game.addUpDrawCounter();
            logger.info("Draw counter is increased by {}", game.getDrawCardsCounter());
        }
        if (rulesService.isPlayersMauInvalid(game.getActivePlayer())) {
            giveDrawnCardsToPlayer(rulesService.getDefaultNumberOfDrawnCards(), game);
            logger.info("'Mau' of Player {} is invalid", game.getActivePlayer().getName());
        }
        if (rulesService.mustSuspend(topCard)) {
            setSuspendStatusToPlayer(game);
        }
        if (rulesService.isCardJack(topCard)) {
            game.setAskForSuitWish(true);
            logger.info("Player {} gets to make a suit wish", game.getActivePlayer().getName());
        }
        if (rulesService.changeGameDirection(topCard)) {
            game.switchDirection();
            logger.info("Direction is switched");
        }
    }

    private void setSuspendStatusToPlayer(Game game) {
        if (game.getLapCounter() == 1) {  // only used for the first round when LABEL ASS is top card
            game.getActivePlayer().setMustSuspend(true);
            logger.info("Player {} must suspend in current round", game.getActivePlayer().getName());
        } else {
            Player nextPlayer = getNextActivePlayer(game);
            nextPlayer.setMustSuspend(true);
            logger.info("Player {} must suspend in the next round", nextPlayer.getName());
        }
    }

    @Override
    public boolean isGameOver(Game game) {
        return game.getActivePlayer().getHandCards().size() == 0;
    }

    @Override
    public void resetPlayersMau(Game game) {
        game.getActivePlayer().setSaidMau(false);
        logger.info("'Mau' state of active Player {} is reset", game.getActivePlayer().getName());
    }

    @Override
    public void saveGame(Game game) throws DaoException {
        try {
            gameDao.saveGame(game);
        } catch(DaoException e) {
            logger.warn("Something went wrong when trying to save the Game."); // WIP
        }
    }

    @Override
    public void deleteGame(Game game) throws DaoException {
        try {
            gameDao.delete(game);
        } catch(DaoException e) {
            logger.warn("Something went wrong when trying to delete the Game.");
        }
    }

    @Override
    public boolean hasGame() throws DaoException {
            return gameDao.findGame();
    }

    @Override
    public Game getGame(long id) throws DaoException {
        return gameDao.findById(id);
    }

}