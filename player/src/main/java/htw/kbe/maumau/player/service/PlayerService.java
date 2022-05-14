package htw.kbe.maumau.player.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.player.domain.Player;
import java.util.List;

public interface PlayerService {

    Player createNewPlayer(String id, String name);

    Player drawCards(Player player, List<Card> cards);

    Card playCard(Player player);

    Player sayMauMau(Player player);
}
