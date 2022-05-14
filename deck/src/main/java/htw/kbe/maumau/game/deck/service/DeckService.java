package htw.kbe.maumau.game.deck.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.game.deck.domain.Deck;

import java.util.List;

public interface DeckService {

    List<Card> shuffle(Deck deck);

    Deck createDeck(List<Card> cards);

    Card getTopCard(Deck deck);

    List<Card> initialCardDealing(int amount);
}
