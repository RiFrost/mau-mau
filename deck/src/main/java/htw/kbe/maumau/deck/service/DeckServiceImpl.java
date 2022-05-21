package htw.kbe.maumau.deck.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Label;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.card.service.CardService;
import htw.kbe.maumau.card.service.CardServiceImpl;
import htw.kbe.maumau.deck.domain.Deck;
import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;
import java.util.*;

public class DeckServiceImpl implements DeckService {

    private CardService cardService;    // can be annotated with @Autowired if we use Spring

    @Override
    public Deck createDeck(List<Card> cards) throws IllegalDeckSizeException {
        Deck deck = new Deck();
        validateCardStack(cards, deck.getLimitOfCardStack());
        Collections.shuffle(cards);
        setTopCard(cards, deck);
        deck.setDrawPile(cards);
        return deck;
    }

    private void setTopCard(List<Card> cards, Deck deck) {
        Card lastCard = cards.get((cards.size()-1));
        deck.setTopCard(lastCard);
        cards.remove(lastCard);
    }

    @Override
    public List<Card> initialCardDealing(Deck deck) {
        List <Card> drawPile = deck.getDrawPile();
        ArrayList<Card> initialCardsForPlayer = new ArrayList<>(drawPile.subList(0, deck.getNumberOfInitialCardsPerPlayer()));
        drawPile.removeAll(initialCardsForPlayer);
        return initialCardsForPlayer;
    }

    @Override
    public List<Card> getCardsFromDrawPile(Deck deck, int numberOfDrawCards) {
        List<Card> drawPile = deck.getDrawPile();
        List<Card> drawnCards = new ArrayList<>();
        if(drawPile.size() <= numberOfDrawCards) {
            drawPile.addAll(deck.getDiscardPile());
            deck.getDiscardPile().removeAll(deck.getDiscardPile());
            Collections.shuffle(drawPile);
        }
        drawnCards.addAll(drawPile.subList(0, numberOfDrawCards));
        drawPile.removeAll(drawnCards);
        return drawnCards;
    }

    @Override
    public Card setCardToTopCard(Deck deck, Card discardedCard) {
        deck.getDiscardPile().add(deck.getTopCard());
        deck.setTopCard(discardedCard);
        return deck.getTopCard();
    }

    private void validateCardStack(List<Card> cards, long limitOfCardStack) throws IllegalDeckSizeException {
        if (cards.isEmpty() || cards.size() != limitOfCardStack) {
            throw new IllegalDeckSizeException("The number of cards does not match with " + limitOfCardStack);
        }

        Map<Suit, Integer> numberPerSuit = new HashMap<>();
        Map<Label, Integer> numberPerLabel = new HashMap<>();

        cardService.getSuits().stream().forEach(suit -> {
            numberPerSuit.put(suit, 0);
            cardService.getLabels().stream().forEach(label -> numberPerLabel.put(label, 0));
        });

         cards.stream().forEach(
                card -> {
                    numberPerSuit.computeIfPresent(card.getSuit(), (k, v) -> v + 1);
                    numberPerLabel.computeIfPresent(card.getLabel(), (k, v) -> v + 1);
                }
        );

        boolean isAmountOfSuitsValid = numberPerSuit.values().stream().allMatch(x -> x == cardService.getLabels().size());
        boolean isAmountOfLabelsValid = numberPerLabel.values().stream().allMatch(x -> x == cardService.getSuits().size());

        if (!(isAmountOfSuitsValid && isAmountOfLabelsValid)) {
            throw new IllegalDeckSizeException("Ratio of Suit and Label is not valid");
        }
    }

    public void setCardService(CardService cardService) {
        this.cardService = cardService;
    }
}
