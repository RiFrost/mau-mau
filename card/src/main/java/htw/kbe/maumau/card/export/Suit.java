package htw.kbe.maumau.card.export;

public enum Suit {
    CLUBS(4), SPADES(3), HEARTS(2), DIAMONDS(1);

    public final int value;

    private Suit(int value) {
        this.value = value;
    }
}
