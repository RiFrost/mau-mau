package htw.kbe.maumau.rule.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.rule.domain.Rule;
import htw.kbe.maumau.player.domain.Player;

public class RulesServiceImpl implements RulesService {
    public boolean isJoker(Card card, Rule rule) {
        return false;
    }

    public boolean isSeven(Card card, Rule rule) {
        return false;
    }

    public boolean isAss(Card card, Rule rule) {
        return false;
    }

    public boolean isCardValid(Card userCard, Card topCard, Rule rule) {
        return false;
    }

    public boolean isCardValid(Card userCard, Card topCard, Suit userWish, Rule rule) {
        return false;
    }

    public boolean isPlayersMauMauValid(Player player, Rule rule) {
        return false;
    }
}
