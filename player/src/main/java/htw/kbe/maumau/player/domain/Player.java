package htw.kbe.maumau.player.domain;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Suit;

import java.util.List;

public class Player {

    private long id;
    private String name;
    private boolean npc;
    private List<Card> handCards;
    private boolean isActive;
    private boolean mustDraw;
    private long numOfDrawCards;
    private boolean mustSuspend;
    private boolean hasGivenUp;
    private Suit suitWish;
    private boolean hasSaidMauMau;

    public Player(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNpc() {
        return npc;
    }

    public void setNpc(boolean npc) {
        this.npc = npc;
    }

    public List<Card> getHandCards() {
        return handCards;
    }

    public void setHandCards(List<Card> handCards) {
        this.handCards = handCards;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isMustDraw() {
        return mustDraw;
    }

    public void setMustDraw(boolean mustDraw) {
        this.mustDraw = mustDraw;
    }

    public long getNumOfDrawCards() {
        return numOfDrawCards;
    }

    public void setNumOfDrawCards(long numOfDrawCards) {
        this.numOfDrawCards = numOfDrawCards;
    }

    public boolean isMustSuspend() {
        return mustSuspend;
    }

    public void setMustSuspend(boolean mustSuspend) {
        this.mustSuspend = mustSuspend;
    }

    public boolean isHasGivenUp() {
        return hasGivenUp;
    }

    public void setHasGivenUp(boolean hasGivenUp) {
        this.hasGivenUp = hasGivenUp;
    }

    public Suit getSuitWish() {
        return suitWish;
    }

    public void setSuitWish(Suit suitWish) {
        this.suitWish = suitWish;
    }

    public boolean hasSaidMau() {
        return hasSaidMauMau;
    }

    public void setHasSaidMauMau(boolean hasSaidMauMau) {
        this.hasSaidMauMau = hasSaidMauMau;
    }
}
