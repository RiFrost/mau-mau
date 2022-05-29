package htw.kbe.maumau.game.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.card.service.CardService;
import htw.kbe.maumau.deck.domain.Deck;
import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;
import htw.kbe.maumau.deck.service.DeckService;
import htw.kbe.maumau.game.domain.Game;
import htw.kbe.maumau.game.exceptions.InvalidPlayerSizeException;
import htw.kbe.maumau.player.domain.Player;
import htw.kbe.maumau.player.service.PlayerService;
import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;
import htw.kbe.maumau.rule.service.RulesService;

import java.util.List;
import java.util.Objects;

public class GameServiceImpl implements GameService {

    private DeckService deckService;
    private CardService cardService;
    private RulesService rulesService;
    private PlayerService playerService;

    @Override
    public Game startNewGame(List<Player> players) throws IllegalDeckSizeException, InvalidPlayerSizeException {
        if(players.size() < 2 || players.size() > 4) throw new InvalidPlayerSizeException("Number of players is not valid");
        Deck deck = deckService.createDeck(cardService.getCards());
        return new Game(players, deck);
    }

    @Override
    public Player getNextPlayer(Game game) {
        game.setActivePlayer(getNextActivePlayer(game));

        if(game.getActivePlayer().mustSuspend()) {
            game.getActivePlayer().setMustSuspend(false);
            getNextPlayer(game);
        }

        return game.getActivePlayer();
    }

    private Player getNextActivePlayer(Game game) {
        Player activePlayer = game.getActivePlayer();
        List<Player> players = game.getPlayers();
        int idxActivePlayer = players.indexOf(activePlayer);

        if(game.getClockWise()) {
            if(idxActivePlayer == players.size() - 1) {
                activePlayer = players.get(0);
            } else {
                activePlayer = players.get(++idxActivePlayer);
            }
        } else {
            if(idxActivePlayer == 0) {
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
        for(Player player : players) {
            playerService.drawCards(player, deckService.initialCardDealing(deck));
        }
    }

    @Override
    public void drawCards(int amount, Game game) {  // Note: re-use this method for drawing penalty cards and cards because of a SEVEN
        Player activePlayer = game.getActivePlayer();
        List<Card> drawCards = deckService.getCardsFromDrawPile(game.getCardDeck(), amount);

        playerService.drawCards(activePlayer, drawCards);

        if(game.getDrawCardsCounter() > 1) {  // Maye that logic is not completely right. But for now we leave like that
            game.setDrawCardsCounter(0);
        }
    }

    @Override
    public boolean canPlayerPlayCards(Game game) {
        Player activePlayer = game.getActivePlayer();
        return !rulesService.mustDrawCards(activePlayer, game.getCardDeck().getTopCard());
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
        rulesService.validateCard(card, topCard, game.getSuitWish());
        deckService.setCardToTopCard(deck, card);

        if (Objects.nonNull(game.getSuitWish())) {
            game.setSuitWish(null);
        }
    }

    @Override
    public void applyCardRule(Game game) {
        Card topCard = game.getCardDeck().getTopCard();
        if(rulesService.isPlayersMauInvalid(game.getActivePlayer())) {
            drawCards(rulesService.getNumberOfDrawnCards(), game);
        }
        else if(rulesService.mustSuspend(topCard)) {
            Player nextPlayer = getNextActivePlayer(game);
            nextPlayer.setMustSuspend(true);
        }
        else if(rulesService.isCardJack(topCard)) {
            game.setAskForSuitWish(true);
            // Note: After Jack was played and applyRule is done, then ask for players wish in UI and set wish into game
        }
        else if(rulesService.changeGameDirection(topCard)) {
            game.switchDirection();
        }
        else if(rulesService.mustDrawCards(topCard)) {
            game.addUpDrawCounter();
        }
    }

    @Override
    public void setCardService(CardService cardService) {
        this.cardService = cardService;
    }

    @Override
    public void setRulesService(RulesService rulesService) {
        this.rulesService = rulesService;
    }

    @Override
    public void setDeckService(DeckService deckService) {
        this.deckService = deckService;
    }

    @Override
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }
}