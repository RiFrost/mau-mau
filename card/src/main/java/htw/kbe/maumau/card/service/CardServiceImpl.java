package htw.kbe.maumau.card.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Label;
import htw.kbe.maumau.card.domain.Suit;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CardServiceImpl implements CardService {

    private List<Card> cardList = new LinkedList<Card>();

    public CardServiceImpl() {
        createCardList();
    }

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
        return cardList;
    }

    private void createCardList() {
        for(Suit suit : Suit.values()) {
            for(Label label : Label.values()) {
                cardList.add(new Card(suit, label));
            }
        }
    }
}
