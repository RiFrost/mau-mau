package htw.kbe.maumau.virtualPlayer.export;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.player.export.Player;

public interface AIService {

    Card playCard(Player AI);

    boolean sayMau(Player AI);

    Suit suitWish();

}
