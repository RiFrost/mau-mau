package htw.kbe.maumau.player.export;

import htw.kbe.maumau.card.export.Card;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessorOrder;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String name;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Card> handCards;
    @Column(nullable = false)
    private boolean mustSuspend;
    @Column(nullable = false)
    private boolean saidMau;
    @Column(nullable = false)
    private boolean isAI = false;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
        this.handCards = new ArrayList<>();
    }

    public Player(String name, boolean isAI) {
        this.name = name;
        this.isAI = isAI;
        this.handCards = new ArrayList<>();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isAI() {
        return isAI;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return player.getName() == this.getName();
    }

    @Override
    public String toString() {
        return name;
    }

}
