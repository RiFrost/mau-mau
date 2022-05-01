package htw.kbe.maumau.domain;

import java.util.List;

public interface MauMau {

    /**
     * draws the given amount of cards at the beginning of a game
     * @param amount amount of cards that will get drawn
     * @return list of cards that will be drawn
     */
    List<Card> initialCardDealing(int amount);

    /**
     * shuffles the deck
     */
    void shuffle();

    /**
     * saves the last played card that was on top of the discard pile when deck is empty
     */
    void bookmarkTopCard();

    /**
     * initialise a new deck
     * @return Deck for a new game
     */
    Deck startGame();

    /**
     * calls the winner of the game when a player has no more cards and said correctly 'mau mau'
     * @return Player who won the game
     */
    Player callWinner();

    /**
     * when game is canceled by a player then the game status is reset
     */
    void cancelGame();

    /**
     * player selects a card to discard
     * @return Card that the player wants to discard
     */
    Card playCard();

    /**
     * checks if the card is allowed to be discarded by the player
     * @param card from player's hand
     * @param discardedCard card from top of the discard pile
     * @return boolean either true if the card is valid or false if not
     */
    boolean canBePlayed(Card card, Card discardedCard);

    /**
     * checks whether a rule must be applied and calls the corresponding rule
     * @param card that is played
     */
    void applyRule(Card card);

    /**
     * active player wishes a suit when the card was a jack
     * @return Suit that the player wishes
     */
    Suit wish();

    /**
     * next player is suspended for this round
     * @return Player who must suspend
     */
    Player suspend();

    /**
     * when the top card on the discard pile is a SEVEN, the player must draw at least two cards
     * @param cards List of cards that has to be drawn
     */
    void drawCards(List<Card> cards);

    /**
     * player draws one card
     * @param card that has to be drawn
     */
    void drawCard(Card card);

    /**
     * player can say 'mau mau'
     */
    void sayMauMau();

    /**
     * checks if player said 'mau mau' when player has one card left in his hand
     * @param mauState either true if the 'mau mau' from the player is valid or false if not
     */
    void saidMauMau(boolean mauState);

    /**
     * makes next player in line the active player
     * @param activePlayer Player who took his turn
     * @return Player who is next in line
     */
    Player getNextPlayer(Player activePlayer);

}
