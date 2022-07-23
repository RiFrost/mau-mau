package htw.kbe.maumau.virtualPlayer.service;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.CardService;
import htw.kbe.maumau.card.export.Label;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.player.export.Player;
import htw.kbe.maumau.rule.export.RulesService;
import htw.kbe.maumau.virtualPlayer.export.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AIServiceImpl implements AIService {

    @Autowired
    private CardService cardService;

    @Autowired
    private RulesService rulesService;

    @Override
    public Card getPlayedCard(Player AI, Card topCard, Suit suitWish, int drawCounter) {
        for (Card card: AI.getHandCards()) {
            if (rulesService.matchLabelOrSuit(card, topCard) || rulesService.isSuitWishValid(suitWish, card.getSuit())) {
                if (topCard.getLabel().equals(Label.SEVEN) && drawCounter >= rulesService.getDefaultNumberOfDrawnCards()) {
                    if (rulesService.canPlaySeven(card.getLabel(), topCard.getLabel(), drawCounter)) {
                        return card;
                    }
                    continue;
                }
                if (!rulesService.isJackOnJack(card.getLabel(), topCard.getLabel()) ) {
                    return card;
                }
            }
        }
        return null;
    }

    @Override
    public boolean sayMau(Player AI) {
        return AI.getHandCards().size() == 1;
    }

    @Override
    public Suit getSuitWish(Player AI) {
        List<Suit> validSuits = AI.getHandCards().stream().map(Card::getSuit).filter(item -> cardService.getSuits().contains(item)).collect(Collectors.toList());
        return validSuits.get((int) (Math.random()*validSuits.size()));
    }

}