package htw.kbe.maumau.domain;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private long id;
    private String name;
    private long startNumOfCards;
    private boolean isActive;
    private boolean mustDraw;
    private long numOfDrawCards;
    private boolean mustSuspend;
    private Suit suitWish;
    private boolean hasSaidMauMau;
    private List<Card> handCards;

    public void drawCards(int amount, Deck deck) {
        while(amount > 0) {
            try {
                handCards.add(deck.draw());
            } catch(Exception e) {
                // TODO get cards from DiscardPile, put them back into the Deck, shuffle and draw the remaining cards
                System.out.println("error deck is empty");
            }

            amount--;
        }
    }

}
