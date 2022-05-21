package htw.kbe.maumau.rule.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.player.domain.Player;

public interface RulesService {

    boolean isSuitWishValid(Card userCard, Card topCard);

    boolean isSuitWishValid(Card userCard, Suit userWish);

    int isCardSeven(int drawCardCounter, Card topCard);

    boolean isSuspended(Card topCard);

    boolean isCardJack(Card topCard);

    boolean changeGameDirection(Card topCard);

    boolean isPlayersMauValid(Player player);

}
