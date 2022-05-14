package htw.kbe.maumau.game.domain;

import htw.kbe.maumau.game.player.domain.Player;
import htw.kbe.maumau.game.deck.domain.Deck;

import java.util.List;

public class Game {

    private List<Player> players;
    private Deck cardDeck;
    private boolean isCanceled;

    public Game(List<Player> players, Deck cardDeck) {
        this.players = players;
        this.cardDeck = cardDeck;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Deck getCardDeck() {
        return cardDeck;
    }

    public void setCardDeck(Deck cardDeck) {
        this.cardDeck = cardDeck;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }
}
