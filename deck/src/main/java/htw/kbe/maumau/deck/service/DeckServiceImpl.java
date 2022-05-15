package htw.kbe.maumau.deck.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.deck.domain.Deck;
import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeckServiceImpl implements DeckService {

    @Override
    public List<Card> shuffleDiscardPile(Deck deck) {
        List<Card> cards = deck.getDiscardPile();
        Collections.shuffle(cards);
        return cards;
    }

    @Override
    public Deck createDeck(List<Card> cards) throws IllegalDeckSizeException {
        Deck deck = new Deck(new ArrayList<>());
        if(cards.size() == deck.getTotalAmount()) {
            deck.setDrawPile(cards);
            return new Deck(cards);
        } else throw new IllegalDeckSizeException("The number of cards for the deck exceeds the limit of" + deck.getTotalAmount());
    }

    @Override
    public Card getTopCard(Deck deck) {
        return null;
    }

    @Override
    public List<Card> initialCardDealing(int amount) {
        return null;
    }
}
