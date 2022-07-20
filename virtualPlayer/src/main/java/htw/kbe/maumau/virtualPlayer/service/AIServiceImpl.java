package htw.kbe.maumau.virtualPlayer.service;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.player.export.Player;
import htw.kbe.maumau.virtualPlayer.export.AIService;
import org.springframework.stereotype.Service;

@Service
public class AIServiceImpl implements AIService {

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
    public Suit suitWish() {
        return Suit.values()[((int) Math.random() * 4)];
    }

}