package htw.kbe.maumau.card.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Label {
    ASS,
    KING,
    QUEEN,
    JACK,
    TEN,
    NINE,
    EIGHT,
    SEVEN;

    public static List getLabels() {
        return new ArrayList<>(Arrays. asList(ASS,
                KING,
                QUEEN,
                JACK,
                TEN,
                NINE,
                EIGHT,
                SEVEN));
    }
}
