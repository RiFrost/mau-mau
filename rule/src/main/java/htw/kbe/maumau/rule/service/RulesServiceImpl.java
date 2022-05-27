package htw.kbe.maumau.rule.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Label;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.player.domain.Player;
import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;

import java.util.Objects;

public class RulesServiceImpl implements RulesService {


    @Override
    public void validateCard(Card userCard, Card topCard, Suit userWish) throws PlayedCardIsInvalidException {
        if(Objects.nonNull(userWish) && !(userCard.getSuit().equals(userWish))) throw new PlayedCardIsInvalidException("No card with the correct suit can be played, because of the wish of the user before.");
        if(topCard.getLabel().equals(userCard.getLabel()) && topCard.getLabel().equals(Label.JACK)) throw new PlayedCardIsInvalidException("Jack on Jack is not allowed.");
        if(!(topCard.getLabel().equals(userCard.getLabel()) || topCard.getSuit().equals(userCard.getSuit()))) throw new PlayedCardIsInvalidException("No card with the correct suit or colour can be played.");
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
        return player.getHandCards().size() == 1 && player.saidMau();
    }
}
