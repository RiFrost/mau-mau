package htw.kbe.maumau.player.domain;

import htw.kbe.maumau.card.domain.Card;

import java.util.ArrayList;
import java.util.List;

public class Player {

//    private boolean npc;   Idea
//    private boolean hasGivenUp;   Idea
//    private UUID id; Idea
    private String name;
    private List<Card> handCards;
    private boolean mustSuspend;
    private boolean saidMau;

    public Player(String name) {
        this.name = name;
        this.handCards = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Card> getHandCards() {
        return handCards;
    }

    public void setHandCards(List<Card> handCards) {
        this.handCards = handCards;
    }

    public boolean mustSuspend() {
        return mustSuspend;
    }

    public void setMustSuspend(boolean mustSuspend) {
        this.mustSuspend = mustSuspend;
    }

    public boolean saidMau() {
        return saidMau;
    }

    public void setSaidMau(boolean hasSaidMauMau) {
        this.saidMau = hasSaidMauMau;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return player.getName() == this.getName();
    }
}
