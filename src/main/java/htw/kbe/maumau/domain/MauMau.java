package htw.kbe.maumau.domain;

import java.util.List;

public interface MauMau {

    /**
     * Draws the given amount of cards
     * @param amount amount of cards that will get drawn
     * @return list of cards that will be drawn
     */
    List<Card> initialCardDealing(int amount);

    /**
     * shuffles the deck
     */
    void shuffle();

    /**
     * saves the last played card when deck is empty
     * @return the card that was on top of the discardpile
     */
    Card saveLastCard();

    /**
     * starts a new game of mau mau
     */
    void startGame();

    /**
     * ends a game
     */
    void endGame();

    /**
     * checks if the card from hand can be played
     * @param card card from hand
     * @param discardedCard card from top of the discardpile
     * @return boolean
     */
    boolean canBePlayed(Card card, Card discardedCard);

    /**
     *
     * method checks if special rules need to be applied
     */
    void applyRules(Card card);

    /**
     * active player wishes a suit when the card was a jack
     * @return Suit
     */
    Suit wish();

    /**
     * next player is suspended for this round
     * @return suspended Player
     */
    Player suspend();

    /**
     * when card is a SEVEN next player must draw 2 cards or play another SEVEN
     * @return Cards
     */
    Card draw();

    /**
     * method checks if player "said" mau when he has one card left in his hand
     * @param mauState either true or false
     */
    void saidMau(boolean mauState);

    /**
     * makes next player in line the active player
     * @param activePlayer Player who took his turn
     * @return Player who his next in line
     */
    Player nextplayer(Player activePlayer);

}
