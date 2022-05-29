package htw.kbe.maumau.rule.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Label;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.player.domain.Player;
import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;

import java.util.Objects;

public class RulesServiceImpl implements RulesService {

    @Override
    public void validateCard(Card playedCard, Card topCard, Suit userWish) throws PlayedCardIsInvalidException {
        if(topCard.getLabel().equals(playedCard.getLabel()) && topCard.getLabel().equals(Label.JACK)) throw new PlayedCardIsInvalidException("Jack on Jack is not allowed.");
        if(!(topCard.getLabel().equals(playedCard.getLabel()) || topCard.getSuit().equals(playedCard.getSuit())) && Objects.isNull(userWish)) throw new PlayedCardIsInvalidException("The card must not be played. Label or suit does not match.");
        if(Objects.nonNull(userWish) && !playedCard.getSuit().equals(userWish)) throw new PlayedCardIsInvalidException("The card must not be played. Suit does not match players wish.");
    }

    @Override
    public boolean mustDrawTwoCards(Card topCard) {
        return topCard.getLabel().equals(Label.SEVEN);
    }

    @Override
    public int getNumberOfDrawnCards() {
        return 2;
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
    public boolean isPlayersMauInvalid(Player player) {
        if(player.getHandCards().size() == 1 && player.saidMau()) {
            return false;
        }
        // Wichtig: Wenn Spieler min. 2 Karten hat, muss er nicht Mau gesagt haben, daher false!
        if(player.getHandCards().size() > 1 && !player.saidMau()) {
            return false;
        }
        return true;
    }
}
