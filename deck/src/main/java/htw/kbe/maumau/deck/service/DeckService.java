package htw.kbe.maumau.deck.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.deck.domain.Deck;
import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;

import java.util.List;

public interface DeckService {

    List<Card> shuffleDiscardPile(Deck deck);

    Deck createDeck(List<Card> cards) throws IllegalDeckSizeException;

    Card getTopCard(Deck deck);

    List<Card> initialCardDealing(int amount);
}
