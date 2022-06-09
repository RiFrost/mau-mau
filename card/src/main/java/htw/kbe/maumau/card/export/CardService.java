package htw.kbe.maumau.card.export;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Label;
import htw.kbe.maumau.card.export.Suit;
import java.util.List;

public interface CardService {

    /**
     * all available Suits are put into a list
     * @return List of Suits
     */
    List<Suit> getSuits();

    /**
     * all available Labels are put into a list
     * @return List of Labels
     */
    List<Label> getLabels();

    /**
     * 32 cards are initialized. The card stack consists of 4 suits with 8 labels each.
     * @return List of Cards
     */
    List<Card> getCards();
}
