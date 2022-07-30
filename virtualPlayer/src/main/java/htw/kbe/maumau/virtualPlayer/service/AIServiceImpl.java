package htw.kbe.maumau.virtualPlayer.service;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Label;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.player.export.Player;
import htw.kbe.maumau.rule.export.RulesService;
import htw.kbe.maumau.virtualPlayer.export.AIService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class AIServiceImpl implements AIService {

    @Autowired
    private RulesService rulesService;

    private static Logger logger = LogManager.getLogger(AIServiceImpl.class);

    @Override
    public Card getPlayedCard(Player AI, Card topCard, Suit suitWish, int drawCounter) {
        for (Card card : AI.getHandCards()) {
            if (rulesService.isSuitWishValid(suitWish, card.getSuit()) && !rulesService.isJackOnJack(card.getLabel(), topCard.getLabel())) {
                return card;
            }
            if (rulesService.matchLabelOrSuit(card, topCard) && Objects.isNull(suitWish)) {
                if (topCard.getLabel().equals(Label.SEVEN) && drawCounter >= rulesService.getDefaultNumberOfDrawnCards()) {
                    if (rulesService.canPlaySeven(card.getLabel(), topCard.getLabel(), drawCounter)) {
                        logger.info("{} is removed from deck of player {}", card, AI.getName());
                        return card;
                    }
                    continue;
                }
                return card;
            }
        }
        return null;
    }

    @Override
    public boolean saidMau(Player AI) {
        logger.info("{} said 'mau': ", AI.getName(), AI.getHandCards().size() == 2);
        return AI.getHandCards().size() == 2;
    }

    @Override
    public Suit getSuitWish(Player AI) {
        List<Suit> validSuits = AI.getHandCards().stream().map(Card::getSuit).distinct().toList();
        return validSuits.get((int) (Math.random() * validSuits.size()));
    }

    @Override
    public void removePlayedCard(Player AI, Card card) {
        AI.getHandCards().remove(card);
        logger.info("{} is removed from deck of player {}", card, AI.getName());
    }

    @Override
    public void addDrawnCards(Player AI, List<Card> cards) {
        AI.getHandCards().addAll(cards);
        Collections.sort(AI.getHandCards());
        logger.info("{} is added to deck of player {}", cards, AI.getName());
    }

}