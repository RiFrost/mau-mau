package htw.kbe.maumau.game.export;

import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.deck.export.Deck;
import htw.kbe.maumau.player.export.Player;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Game {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @OneToMany(mappedBy="player")
    private List<Player> players;

    @OneToOne
    private Player activePlayer;
    @OneToOne
    private Deck cardDeck;
    @Column(nullable = false)
    private boolean clockWise = true;
    @Column(nullable = false)
    private int drawCardsCounter = 0;
    @Column(nullable = false)
    private Suit suitWish = null;
    @Column(nullable = false)
    private boolean askForSuitWish = false;
    @Column(nullable = false)
    private int lapCounter = 1;

    public Game() {
    }

    public Game(List<Player> players, Deck cardDeck) {
        this.players = players;
        this.cardDeck = cardDeck;
        this.activePlayer = players.get(0);
    }


    public List<Player> getPlayers() {
        return players;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setCardDeck(Deck cardDeck) {
        this.cardDeck = cardDeck;
    }

    public void setClockWise(boolean clockWise) {
        this.clockWise = clockWise;
    }

    public void setLapCounter(int lapCounter) {
        this.lapCounter = lapCounter;
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
        return "players=" + players +
                ", activePlayer=" + activePlayer +
                ", cardDeck=" + cardDeck +
                ", clockWise=" + clockWise +
                ", drawCardsCounter=" + drawCardsCounter +
                ", suitWish=" + suitWish +
                ", askForSuitWish=" + askForSuitWish +
                ", lapCounter=" + lapCounter;
    }
}
