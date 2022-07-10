package htw.kbe.maumau.card.export;

import javax.persistence.Entity;

public enum Label {
    ASS(14),
    KING(13),
    QUEEN(12),
    JACK(11),
    TEN(10),
    NINE(9),
    EIGHT(8),
    SEVEN(7);

    public final int value;

    private Label(int value) {
        this.value = value;
    }
}
