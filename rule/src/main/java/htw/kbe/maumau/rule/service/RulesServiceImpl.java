package htw.kbe.maumau.rule.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Label;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.player.domain.Player;

import java.util.Objects;

public class RulesServiceImpl implements RulesService {

    @Override
    public boolean isCardValid(Card userCard, Card topCard, Suit userWish) {
        if (Objects.nonNull(userWish)) {
            return userCard.getSuit().equals(userWish);
        }
        if (topCard.getLabel().equals(userCard.getLabel()) && topCard.getLabel().equals(Label.JACK)) {
            return false;
        }
        return topCard.getLabel().equals(userCard.getLabel()) || topCard.getSuit().equals(userCard.getSuit());
    }


    @Override
    public boolean drawTwoCards(Card topCard) {
        return topCard.getLabel().equals(Label.SEVEN);
    }

    @Override
    public boolean isSuspended(Card topCard) {
        return topCard.getLabel().equals(Label.ASS);
    }

    @Override
    public boolean isCardJack(Card topCard) {
        return topCard.getLabel().equals(Label.JACK);
    }

    @Override
    public boolean changeGameDirection(Card topCard) {
        return topCard.getLabel().equals(Label.NINE);
    }

    @Override
    public boolean isPlayersMauValid(Player player) {
        return player.hasSaidMau();
    }
}
