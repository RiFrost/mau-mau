package htw.kbe.maumau.rule.service;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Label;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.player.export.Player;
import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;
import htw.kbe.maumau.rule.export.RulesService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RulesServiceImpl implements RulesService {

    private static Logger logger = LogManager.getLogger(RulesServiceImpl.class);

    @Override
    public void validateCard(Card playedCard, Card topCard, Suit userWish, int drawCounter) throws PlayedCardIsInvalidException {
        Suit playedSuit = playedCard.getSuit();
        Label playedLabel = playedCard.getLabel();
        Label topLabel = topCard.getLabel();
        if (hasToPlaySeven(drawCounter, playedLabel, topLabel)) {
            logger.info("Card {} is not valid, because a SEVEN must be played", playedCard);
            throw new PlayedCardIsInvalidException("You have to play a SEVEN.");
        }
        if (isJackOnJack(playedLabel, topLabel)) {
            logger.info("Card {} is not valid, because JACK on Jack is not allowed", playedCard);
            throw new PlayedCardIsInvalidException("JACK on JACK is not allowed.");
        }
        if (!matchLabelOrSuit(playedCard, topCard) && !playedLabel.equals(Label.JACK) && Objects.isNull(userWish)) {
            logger.info("Card {} is not valid, because Label or suit does not match", playedCard);
            throw new PlayedCardIsInvalidException("The card cannot be played. Label or suit does not match.");
        }
        if (!isSuitWishValid(userWish, playedSuit)) {
            logger.info("Card {} is not valid, because Suit does not match players wish", playedCard);
            throw new PlayedCardIsInvalidException("The card cannot be played. Suit does not match players wish.");
        }
    }

    @Override
    public boolean matchLabelOrSuit(Card playedCard, Card topCard) {
        return playedCard.getLabel().equals(topCard.getLabel()) || playedCard.getSuit().equals(topCard.getSuit());
    }

    @Override
    public boolean hasToPlaySeven(int drawCounter, Label playedLabel, Label topLabel) {
        return drawCounter >= getDefaultNumberOfDrawnCards() && topLabel.equals(Label.SEVEN) && !playedLabel.equals(Label.SEVEN);
    }

    @Override
    public boolean canPlaySeven(Label playedLabel, Label topCardLabel, int drawCounter) {
        return topCardLabel.equals(Label.SEVEN) && playedLabel.equals(topCardLabel) && drawCounter >= getDefaultNumberOfDrawnCards();
    }

    @Override
    public boolean isSuitWishValid(Suit userWish, Suit playedSuit) {
        return Objects.nonNull(userWish) ? playedSuit.equals(userWish) : true;
    }

    @Override
    public boolean isJackOnJack(Label playedLabel, Label topLabel) {
        return playedLabel.equals(topLabel) && topLabel.equals(Label.JACK);
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
        //  Note: If the player has at least 2 cards, then he must not have said 'mau', therefore false!
        if (player.getHandCards().size() > 1 && !player.saidMau()) {
            return false;
        }
        return true;
    }
}
