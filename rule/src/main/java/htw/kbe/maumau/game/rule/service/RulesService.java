package htw.kbe.maumau.game.rule.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.game.rule.domain.Rule;
import htw.kbe.maumau.player.domain.Player;

public interface RulesService {

    boolean iJoker(Card card, Rule rule);

    boolean isSeven(Card card, Rule rule);

    boolean isAss(Card card, Rule rule);

    boolean isCardValid(Card userCard, Card topCard, Rule rule);

    boolean isCardValid(Card userCard, Card topCard, Suit userWish, Rule rule);

    boolean isPlayersMauMauValid(Player player, Rule rule);
}
