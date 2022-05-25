package htw.kbe.maumau.game.domain;


import htw.kbe.maumau.deck.domain.Deck;
import htw.kbe.maumau.player.domain.Player;

import java.util.List;

public class Game {

    private List<Player> players;
    private Deck cardDeck;
    private Player activePlayer;
    private boolean isCanceled;
    private boolean clockWise = true;

    public Game(List<Player> players, Deck cardDeck) {
        this.players = players;
        this.cardDeck = cardDeck;
        this.activePlayer = players.get(0);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Deck getCardDeck() {
        return cardDeck;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(Player player) {
        activePlayer = player;
    }

    public void switchDirection() {
        this.clockWise = !this.clockWise;
    }

    public boolean getClockWise() {
        return clockWise;
    }

}
