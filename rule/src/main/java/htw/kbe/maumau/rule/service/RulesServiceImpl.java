package htw.kbe.maumau.rule.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Label;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.player.domain.Player;

public class RulesServiceImpl implements RulesService {

    @Override
    public boolean isCardValid(Card userCard, Card topCard) {

        if (topCard.getLabel().equals(Label.JACK)) {
            if (topCard.getLabel().equals(userCard.getLabel())) {
                return false;
            } else {
                return true;
            }
        } else {
            if (topCard.getLabel().equals(userCard.getLabel())) {
                return true;

            } else return topCard.getSuit().equals(userCard.getSuit());
        }
    }

    @Override
    public boolean isCardValid(Card userCard, Suit userWish) {
        return userCard.getSuit().equals(userWish);
    }

    @Override
    public int isCardSeven(int drawCardCounter, Card topCard) {
        int drawCardamount = drawCardCounter;
        if (topCard.getLabel().equals(Label.SEVEN)) {
            drawCardamount = drawCardamount + 2;
            return drawCardamount;
        }
        return drawCardamount;
    }

    @Override
    public boolean isCardEight(Card topCard) {
        return topCard.getLabel().equals(Label.EIGHT);
    }

    @Override
    public boolean isCardJack(Card topCard) {
        return topCard.getLabel().equals(Label.JACK);
    }

    @Override
    public boolean isCardNine(Card topCard) {
        return topCard.getLabel().equals(Label.NINE);
    }

    @Override
    public boolean isPlayersMauMauValid(Player player) {
        return player.isHasSaidMauMau();
    }
}
