package htw.kbe.maumau.virtualPlayer;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.CardService;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.player.export.Player;
import htw.kbe.maumau.virtualPlayer.export.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AIServiceImpl implements AIService {

    @Autowired
    private CardService cardService;

    @Override
    public Card playCard(Player AI) {
        int rndIdx = (int) (Math.random() * AI.getHandCards().size());
        return AI.getHandCards().get(rndIdx);
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