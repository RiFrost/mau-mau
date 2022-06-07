package htw.kbe.maumau.rule.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Label;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.player.domain.Player;
import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RulesServiceImpl implements RulesService {

    @Override
    public void validateCard(Card playedCard, Card topCard, Suit userWish, int drawCounter) throws PlayedCardIsInvalidException {
        Suit playedSuit = playedCard.getSuit();
        Suit topSuit = topCard.getSuit();
        Label playedLabel = playedCard.getLabel();
        Label topLabel = topCard.getLabel();
        if (drawCounter >= getDefaultNumberOfDrawnCards() && topLabel.equals(Label.SEVEN) && !playedLabel.equals(Label.SEVEN))
            throw new PlayedCardIsInvalidException("You have to play a SEVEN.");
        if (playedLabel.equals(topLabel) && topLabel.equals(Label.JACK))
            throw new PlayedCardIsInvalidException("JACK on JACK is not allowed.");
        if (!(playedLabel.equals(topLabel) || playedSuit.equals(topSuit)) && !playedLabel.equals(Label.JACK) && Objects.isNull(userWish))
            throw new PlayedCardIsInvalidException("The card cannot be played. Label or suit does not match.");
        if (Objects.nonNull(userWish) && !playedSuit.equals(userWish))
            throw new PlayedCardIsInvalidException("The card cannot be played. Suit does not match players wish.");
    }

    @Override
    public boolean mustDrawCards(Card topCard) {
        return topCard.getLabel().equals(Label.SEVEN);
    }

    @Override
    public int getDefaultNumberOfDrawnCards() {
        return 2;
    }

    @Override
    public boolean mustSuspend(Card topCard) {
        return topCard.getLabel().equals(Label.ASS);
    }

    @Override
    public boolean mustDrawCards(Player player, Card topCard, int drawCounter) {
        boolean hasSeven = player.getHandCards().stream().filter(c -> c.getLabel().equals(Label.SEVEN)).findFirst().isPresent();
        boolean isTopCardSeven = mustDrawCards(topCard);
        boolean isCounterGreaterDefault = drawCounter >= getDefaultNumberOfDrawnCards();
        return !hasSeven && isTopCardSeven && isCounterGreaterDefault;
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
        if (player.getHandCards().size() <= 1 && player.saidMau()) {
            return false;
        }
        //  Note: If the player has at least 2 cards, he does not have to have said 'mau', therefore false!
        if (player.getHandCards().size() > 1 && !player.saidMau()) {
            return false;
        }
        return true;
    }
}
