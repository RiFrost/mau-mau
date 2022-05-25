package htw.kbe.maumau.game.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Label.*;
import htw.kbe.maumau.card.service.CardService;
import htw.kbe.maumau.card.service.CardServiceImpl;
import htw.kbe.maumau.deck.domain.Deck;
import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;
import htw.kbe.maumau.deck.service.DeckService;
import htw.kbe.maumau.deck.service.DeckServiceImpl;
import htw.kbe.maumau.game.domain.Game;
import htw.kbe.maumau.player.domain.Player;
import htw.kbe.maumau.player.service.PlayerService;
import htw.kbe.maumau.player.service.PlayerServiceImpl;
import htw.kbe.maumau.rule.service.RulesService;
import htw.kbe.maumau.rule.service.RulesServiceImpl;

import java.util.List;

public class GameServiceImpl implements GameService {

    private DeckService deckService;
    private PlayerService playerService;
    private CardService cardService;
    private RulesService rulesService;

    public GameServiceImpl() {
        deckService = new DeckServiceImpl();
        playerService = new PlayerServiceImpl();
        cardService = new CardServiceImpl();
        rulesService = new RulesServiceImpl();
    }

    @Override
    public Game startNewGame(List<Player> players) throws IllegalDeckSizeException {
        Deck deck = deckService.createDeck(cardService.getCards());
        return new Game(players, deck);
    }

    @Override
    public Player nextPlayer(Game game) {
        Player activePlayer = game.getActivePlayer();
        List<Player> players = game.getPlayers();
        int idxActivePlayer = players.indexOf(activePlayer);

        if(game.getClockWise()) {
            if(idxActivePlayer == players.size() - 1) {
                game.setActivePlayer(players.get(0));
            } else {
                game.setActivePlayer(players.get(++idxActivePlayer));
            }
        } else {
            if(idxActivePlayer == 0) {
                game.setActivePlayer(players.get(players.size() - 1));
            } else {
                game.setActivePlayer(players.get(--idxActivePlayer));
            }
        }

        return game.getActivePlayer();
    }

    @Override
    public boolean hasPlayerHandCard(Card card, Game game) {
        return game.getActivePlayer().getHandCards().contains(card);
    }

    @Override
    public boolean checkPlayedCard(Card playedCard, Deck deck) {
        Card topCard = deck.getTopCard();
        if(!rulesService.isCardValid(playedCard, topCard, null)) return false;

        return true;
    }

    @Override
    public void cancelGame(Game game) {
        game.setCanceled(true);
    }

}
