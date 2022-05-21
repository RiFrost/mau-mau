package htw.kbe.maumau.rule.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Label;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.player.domain.Player;

public class RulesServiceImpl implements RulesService {

    @Override
    public boolean isCardValid(Card userCard, Card topCard, Suit userWish) {

        if (topCard.getLabel().equals(Label.JACK)) {
            if (topCard.getLabel().equals(userCard.getLabel())) {
                return false;
            } else {
                return userCard.getSuit().equals(userWish);
            }
        } else {
            if (topCard.getLabel().equals(userCard.getLabel())) {
                return true;

            } else return topCard.getSuit().equals(userCard.getSuit());
        }
    }


    @Override
    public int drawTwo(int drawCardCounter, Card topCard) {
        int numberOfDrawCards = drawCardCounter;
        return topCard.getLabel().equals(Label.SEVEN) ? numberOfDrawCards + 2 : numberOfDrawCards;
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
        return player.isHasSaidMau();
    }
}
