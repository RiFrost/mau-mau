package htw.kbe.maumau.domain;

import java.util.List;

public interface IDeck {

    void shuffle();
    List<Card> drawCards(int amount);
    List<Card> getCards();
    long getTotalAmount();

}
