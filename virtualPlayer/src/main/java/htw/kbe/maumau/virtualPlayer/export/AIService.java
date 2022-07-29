package htw.kbe.maumau.virtualPlayer.export;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.player.export.Player;

public interface AIService {

    /**
     * returns a valid card to play or null when it's not available
     * @param AI AI player who is in turn
     * @param topCard card that is on top of discard pile
     * @param suitWish suit that may have been requested
     * @param drawCounter counter that gives the number of drawn cards
     * @return card if valid or null when invalid
     */
    Card getPlayedCard(Player AI, Card topCard, Suit suitWish, int drawCounter);

    /**
     * returns 'mau' state of AI player
     * @param AI AI player who is in turn
     * @return true when player has only one hand card left, false if not
     */
    boolean sayMau(Player AI);

    /**
     * returns a suit wish that is also available in the AI player's hand cards
     * @param AI AI player who is in turn
     * @return suit wish
     */
    Suit getSuitWish(Player AI);

    /**
     * removes card that was played from hand card of AI player
     * @param AI - AI player who played the given card
     * @param card - played card to be removed
     */
    void removePlayedCard(Player AI, Card card);
}
