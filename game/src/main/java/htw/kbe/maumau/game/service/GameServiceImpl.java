package htw.kbe.maumau.game.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.card.service.CardService;
import htw.kbe.maumau.deck.domain.Deck;
import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;
import htw.kbe.maumau.deck.service.DeckService;
import htw.kbe.maumau.game.domain.Game;
import htw.kbe.maumau.player.domain.Player;
import htw.kbe.maumau.player.service.PlayerService;
import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;
import htw.kbe.maumau.rule.service.RulesService;

import java.util.List;

public class GameServiceImpl implements GameService {

    private DeckService deckService;
    private PlayerService playerService;
    private CardService cardService;
    private RulesService rulesService;

    @Override
    public Game startNewGame(List<Player> players) throws IllegalDeckSizeException {
        Deck deck = deckService.createDeck(cardService.getCards());
        return new Game(players, deck);
    }

    @Override
    public Player nextPlayer(Game game) {
        game.setActivePlayer(getNextPlayer(game));

        if(game.getActivePlayer().mustSuspend()) {
            game.getActivePlayer().setMustSuspend(false);
            nextPlayer(game);
        }

        return game.getActivePlayer();
    }

    private Player getNextPlayer(Game game) {
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
    public void dealCards(Game game) {
        Deck deck = game.getCardDeck();
        List<Player> players = game.getPlayers();
        for(Player player : players) {
            player.setHandCards(deckService.initialCardDealing(deck));
        }
    }

    @Override
    public void drawCards(int amount, Game game) {
        Player activePlayer = game.getActivePlayer();
        List<Card> handCards = activePlayer.getHandCards();
        List<Card> drawCards = deckService.getCardsFromDrawPile(game.getCardDeck(), amount);
        handCards.addAll(drawCards);
        activePlayer.setHandCards(handCards);
    }

    @Override
    public boolean canPlayerPlayCards(Game game) {
        return true;
    }

    @Override
    public void setUserWish(Suit userWish, Game game) { // UI kann direkt anpassen?
        game.setUserWish(userWish);
        game.setAskForSuitWish(false);
    }

    @Override
    public void validateCard(Card card, Game game) throws PlayedCardIsInvalidException {
        Deck deck = game.getCardDeck();
        Card topCard = deck.getTopCard();
        rulesService.validateCard(card, topCard, game.getUserWish());
        deckService.setCardToTopCard(deck, card);
    }

    @Override
    public void applyCardRule(Game game) {
        Card topCard = game.getCardDeck().getTopCard();
        if(!rulesService.isPlayersMauValid(game.getActivePlayer())) {
            drawCards(RulesService.penaltyCards, game);
        }
        else if(rulesService.isSuspended(topCard)) {
            Player nextPlayer = getNextPlayer(game);
            nextPlayer.setMustSuspend(true);
        }
        else if(rulesService.isCardJack(topCard)) {
            game.setAskForSuitWish(true);
        }
        else if(rulesService.changeGameDirection(topCard)) {
            game.switchDirection();
        }
        else if(rulesService.drawTwoCards(topCard)) {
            game.addUpDrawCounter();
        }
    }

    // SERVICE SETTER METHODS

    public void setCardService(CardService cardService) {
        this.cardService = cardService;
    }

    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public void setRulesService(RulesService rulesService) {
        this.rulesService = rulesService;
    }

    public void setDeckService(DeckService deckService) {
        this.deckService = deckService;
    }
}


/*
UI
for (runde) {
TopKarte = 7
Spieler A ist dran

if Spieler A hat Handkarte 7 -> er darf spielen -> validateCard
else Spieler A muss ziehen -> drawCard


}


 */