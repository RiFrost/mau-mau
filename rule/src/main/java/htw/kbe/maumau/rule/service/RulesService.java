package htw.kbe.maumau.rule.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.player.domain.Player;

public interface RulesService {

    boolean isCardValid(Card userCard, Card topCard);

    boolean isCardValid(Card userCard, Suit userWish);

    int isCardSeven(int drawCardCounter, Card topCard);

    boolean isCardEight(Card topCard);

    boolean isCardJack(Card topCard);

    boolean isCardNine(Card topCard);

    boolean isPlayersMauMauValid(Player player);

}
