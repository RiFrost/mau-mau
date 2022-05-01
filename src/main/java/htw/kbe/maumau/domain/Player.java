package htw.kbe.maumau.domain;

import java.util.List;

public class Player {

    private long id;
    private String name;
    private List<Card> handCards;
    private boolean isActive;
    private boolean mustDraw;
    private long numOfDrawCards;
    private boolean mustSuspend;
    private Suit suitWish;
    private boolean hasSaidMauMau;

}
