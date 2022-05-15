package htw.kbe.maumau.deck.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.deck.domain.Deck;

import java.util.Collections;
import java.util.List;

import static htw.kbe.maumau.card.domain.Label.ASS;
import static htw.kbe.maumau.card.domain.Suit.HEARTS;

public class DeckServiceImpl implements DeckService {

    @Override
    public List<Card> shuffleDiscardPile(Deck deck) {
        List<Card> cards = deck.getDrawPile();
        Collections.shuffle(cards);
        return cards;
    }

    @Override
    public Deck createDeck(List<Card> cards) {
        Card card = new Card(HEARTS, ASS);

        return new Deck(cards, 32);
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
