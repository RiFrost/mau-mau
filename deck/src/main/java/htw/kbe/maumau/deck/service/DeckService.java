package htw.kbe.maumau.deck.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.service.CardService;
import htw.kbe.maumau.deck.domain.Deck;
import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;

import java.util.List;

public interface DeckService {

    /**
     * initialise card deck
     * @param cards - Card stack that has to add to deck
     * @return deck that include the required card stack
     * @throws IllegalDeckSizeException when card stack is empty or has an invalid Suit Label ratio
     */
    Deck createDeck(List<Card> cards) throws IllegalDeckSizeException;

    /**
     * at the beginning of a game: depending on the initial number of drawn cards, cards are dealt from the draw pile
     * @param deck - card deck
     * @return initial cards for player
     */
    List<Card> initialCardDealing(Deck deck);

    /**
     * depending on the number of cards to be drawn, the cards are dealt from the draw pile
     * @param deck - card deck
     * @param numberOfDrawCards - number of cards to be drawn
     * @return card list of drawn cards
     */
    List<Card> getCardsFromDrawPile(Deck deck, int numberOfDrawCards);

    /**
     * discarded card is set to the new top card and previous top card is set to discard pile
     * @param deck - card deck
     * @param discardedCard - card that has to set to the new top card
     * @return new top card
     */
    Card setCardToTopCard(Deck deck, Card discardedCard);

    void setCardService(CardService cardService);
}
