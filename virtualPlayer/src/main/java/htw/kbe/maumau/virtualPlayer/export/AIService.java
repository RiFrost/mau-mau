package htw.kbe.maumau.virtualPlayer.export;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.player.export.Player;

public interface AIService {

    Card playCard(Player AI);

    boolean sayMau(Player AI);

    /**
     * returns a suit wish that is also available in the AI player's hand cards
     * @param AI AI player who is in turn
     * @return suit wish
     */
    Suit getSuitWish(Player AI);

}
