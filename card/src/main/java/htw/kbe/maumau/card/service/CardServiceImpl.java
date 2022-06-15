package htw.kbe.maumau.card.service;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.CardService;
import htw.kbe.maumau.card.export.Label;
import htw.kbe.maumau.card.export.Suit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private static Logger logger = LogManager.getLogger(CardServiceImpl.class);

    @Override
    public List<Suit> getSuits() {
        return Arrays.asList(Suit.values());
    }

    @Override
    public List<Label> getLabels() {
        return Arrays.asList(Label.values());
    }

    @Override
    public List<Card> getCards() {
        logger.error("SAMPLE ERROR MESSAGE FOR CARD-SERVICE");
        List<Card> cardList = new ArrayList<>();
        for(Suit suit : Suit.values()) {
            for(Label label : Label.values()) {
                cardList.add(new Card(suit, label));
            }
        }
        return cardList;
    }
}
