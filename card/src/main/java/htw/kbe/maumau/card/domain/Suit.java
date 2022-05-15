package htw.kbe.maumau.card.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Suit {
    CLUBS, SPADES, HEARTS, DIAMONDS;

    public static List getSuits() {
        return new ArrayList<>(Arrays. asList(CLUBS, SPADES, HEARTS, DIAMONDS));
    }
}
