package htw.kbe.maumau.game.domain;

import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.deck.domain.Deck;
import htw.kbe.maumau.player.domain.Player;

import java.util.List;

public class Game {

    private List<Player> players;
    private Player activePlayer;
    private Deck cardDeck;
    private boolean clockWise = true;
    private int drawCardsCounter = 0;
    private Suit userWish = null;
    private boolean askForSuitWish = false;

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

    public boolean getClockWise() {
        return clockWise;
    }

    public int getDrawCardsCounter() {
        return drawCardsCounter;
    }

    public void setDrawCardsCounter(int amount) {
        drawCardsCounter = amount;
    }

    public Suit getUserWish() {
        return userWish;
    }

    public void setUserWish(Suit userWish) {
        this.userWish = userWish;
    }

    public void addUpDrawCounter() {
        drawCardsCounter += 2;
    }

    public boolean isAskForSuitWish() {
        return askForSuitWish;
    }

    public void setAskForSuitWish(boolean askForSuitWish) {
        this.askForSuitWish = askForSuitWish;
    }
}
