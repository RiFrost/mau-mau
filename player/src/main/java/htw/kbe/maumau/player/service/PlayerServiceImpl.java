package htw.kbe.maumau.player.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.player.domain.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PlayerServiceImpl implements PlayerService {

    private List<Card> cardList = new LinkedList<Card>();

    @Override
    public Player createNewPlayer(Long id, String name) {
        return new Player(id, name);
    }

    @Override
    public Player sayMauMau(Player player) {
        if(!player.isHasSaidMauMau()) {
            player.setMustDraw(true);
        }
        else
            player.setHasSaidMauMau(true);
        return player;
    }

    @Override
    public Card playCard(Player player, Card card) {
        cardList = player.getHandCards();
        cardList.remove(card);          // zum Ablagestapel hinzufügen
        player.setHandCards(cardList);
        return card;
    }

    @Override
    public Player drawCards(Player player, List<Card> cards) {
//        cardList = player.getHandCards();
//        cardList.add(card);             // Prüfung Listengröße
//        player.setHandCards(cards);
        return player;
    }
}
