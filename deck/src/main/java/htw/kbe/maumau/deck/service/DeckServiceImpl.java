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

    public DeckServiceImpl() {
        this.cardService = new CardServiceImpl();
    }

    @Override
    public List<Card> shuffleDiscardPile(Deck deck) {
        List<Card> cards = deck.getDiscardPile();
        Collections.shuffle(cards);
        return cards;
    }

    @Override
    public Deck createDeck(List<Card> cards) throws IllegalDeckSizeException {
        Deck deck = new Deck(new ArrayList<>());
        validateCardStack(cards, deck);

        return new Deck(cards);
    }

    @Override
    public Card getTopCard(Deck deck) {
        return null;
    }

    @Override
    public List<Card> initialCardDealing(int amount) {
        return null;
    }

    private void validateCardStack(List<Card> cards, Deck deck) throws IllegalDeckSizeException {
        if (cards.isEmpty()) {
            throw new IllegalDeckSizeException("The number of cards for the deck fall below the limit of " + deck.getLimitOfCardStack());
        }

        List<Label> labels = cardService.getLabels();
        List<Suit> suits = cardService.getSuits();
        Map<Suit, Integer> numberPerSuit = new HashMap<>();
        Map<Label, Integer> numberPerLabel = new HashMap<>();
        suits.stream().forEach(suit -> {
            numberPerSuit.put(suit, 0);
            labels.stream().forEach(label -> numberPerLabel.put(label, 0));
        });

        cards.stream().forEach(
                card -> {
                    numberPerSuit.computeIfPresent(card.getSuit(), (k, v) -> v + 1);
                    numberPerLabel.computeIfPresent(card.getLabel(), (k, v) -> v + 1);
                }
        );

        boolean isAmountOfSuitsValid = numberPerSuit.values().stream().allMatch(x -> x == 8);
        boolean isAmountOfLabelsValid = numberPerLabel.values().stream().allMatch(x -> x == 4);

        if (!(isAmountOfSuitsValid && isAmountOfLabelsValid)) {
            throw new IllegalDeckSizeException("Ratio of Suit and Label is not valid");
        }
    }

}
