package htw.kbe.maumau.game.service;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.card.export.CardService;
import htw.kbe.maumau.deck.export.Deck;
import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;
import htw.kbe.maumau.deck.export.DeckService;
import htw.kbe.maumau.game.export.Game;
import htw.kbe.maumau.game.exceptions.InvalidPlayerSizeException;
import htw.kbe.maumau.game.export.GameService;
import htw.kbe.maumau.player.export.Player;
import htw.kbe.maumau.player.export.PlayerService;
import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;
import htw.kbe.maumau.rule.export.RulesService;
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

    @Override
    public Game createGame(List<Player> players) throws IllegalDeckSizeException, InvalidPlayerSizeException {
        if (players.size() < 2 || players.size() > 4)
            throw new InvalidPlayerSizeException("Number of players is not valid");
        Deck deck = deckService.createDeck(cardService.getCards());
        return new Game(players, deck);
    }

    @Override
    public Player getNextPlayer(Game game) {
        game.setActivePlayer(getNextActivePlayer(game));

        if (game.getActivePlayer().mustSuspend()) {
            game.getActivePlayer().setMustSuspend(false);
            getNextPlayer(game);
        }

        return game.getActivePlayer();
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
    public void initialCardDealing(Game game) {
        Deck deck = game.getCardDeck();
        List<Player> players = game.getPlayers();
        for (Player player : players) {
            playerService.addDrawnCards(player, deckService.initialCardDealing(deck));
        }
    }

    @Override
    public void giveDrawnCardsToPlayer(int numberOfDrawnCards, Game game) {
        Player activePlayer = game.getActivePlayer();
        List<Card> drawCards = deckService.getCardsFromDrawPile(game.getCardDeck(), numberOfDrawnCards);
        playerService.addDrawnCards(activePlayer, drawCards);

        if (game.getDrawCardsCounter() > 0) {
            game.setDrawCardsCounter(0);
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
        game.setAskForSuitWish(false);
    }

    @Override
    public void validateCard(Card card, Game game) throws PlayedCardIsInvalidException {
        Deck deck = game.getCardDeck();
        Card topCard = deck.getTopCard();
        rulesService.validateCard(card, topCard, game.getSuitWish(), game.getDrawCardsCounter());
        deckService.setCardToTopCard(deck, card);
        playerService.removePlayedCard(game.getActivePlayer(), card);

        if (Objects.nonNull(game.getSuitWish())) {
            game.setSuitWish(null);
        }
    }

    @Override
    public void applyCardRule(Game game) {
        Card topCard = game.getCardDeck().getTopCard();
        if (rulesService.isPlayersMauInvalid(game.getActivePlayer())) {
            System.out.println("Mau invalid");
            giveDrawnCardsToPlayer(rulesService.getDefaultNumberOfDrawnCards(), game);
            resetPlayersMau(game);
        } else if (rulesService.mustSuspend(topCard)) {
            if (game.getLapCounter() == 1) { // only used for the first round when LABEL ASS is top card
                game.getActivePlayer().setMustSuspend(true);
            } else {
                Player nextPlayer = getNextActivePlayer(game);
                nextPlayer.setMustSuspend(true);
                System.out.println("next player suspend");
            }
        } else if (rulesService.isCardJack(topCard)) {
            game.setAskForSuitWish(true);
            System.out.println("ask for suit wish true");
        } else if (rulesService.changeGameDirection(topCard)) {
            game.switchDirection();
            System.out.println("Switch direction");
        } else if (rulesService.mustDrawCards(topCard)) {
            game.addUpDrawCounter();
            System.out.println("seven counter");
            System.out.println(game.getDrawCardsCounter());
        }
    }

    @Override
    public boolean isGameOver(Game game) {
        return game.getActivePlayer().getHandCards().size() == 0;
    }

    @Override
    public void resetPlayersMau(Game game) {
        game.getActivePlayer().setSaidMau(false);
    }


}