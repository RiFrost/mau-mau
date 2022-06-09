package htw.kbe.maumau.game.export;

import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.deck.export.Deck;
import htw.kbe.maumau.player.export.Player;

import java.util.List;

public class Game {

    private List<Player> players;
    private Player activePlayer;
    private Deck cardDeck;
    private boolean clockWise = true;
    private int drawCardsCounter = 0;
    private Suit suitWish = null;
    private boolean askForSuitWish = false;
    private int lapCounter = 1;

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

    public Player getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(Player player) {
        activePlayer = player;
    }

    public void switchDirection() {
        this.clockWise = !this.clockWise;
    }

    public int getDrawCardsCounter() {
        return drawCardsCounter;
    }

    public void setDrawCardsCounter(int amount) {
        drawCardsCounter = amount;
    }

    public Suit getSuitWish() {
        return suitWish;
    }

    public void setSuitWish(Suit suitWish) {
        this.suitWish = suitWish;
    }

    public void addUpDrawCounter() {
        drawCardsCounter += 2;
    }

    public boolean hasAskedForSuitWish() {
        return askForSuitWish;
    }

    public void setAskForSuitWish(boolean askForSuitWish) {
        this.askForSuitWish = askForSuitWish;
    }

    public boolean isClockWise() {
        return clockWise;
    }

    public int getLapCounter() {
        return lapCounter;
    }

    public void addUpLapCounter() {
        this.lapCounter += 1;
    }

    @Override
    public String toString() {
        return "Player " + players;
    }
}
