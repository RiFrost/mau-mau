package htw.kbe.maumau.deck.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.deck.domain.Deck;
import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;

import java.util.List;

public interface DeckService {

    Deck createDeck(List<Card> cards) throws IllegalDeckSizeException;

    List<Card> initialCardDealing(Deck deck);

    List<Card> getCardsFromDrawPile(Deck deck, int numberOfDrawCards);

    Card setCardToTopCard(Deck deck, Card discardedCard);
}
