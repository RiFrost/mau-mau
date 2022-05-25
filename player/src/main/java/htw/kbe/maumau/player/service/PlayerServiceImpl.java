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
        if(!player.hasSaidMau()) {
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
    public Player drawCards(Player player, Card card) {     // Paramter card sinvoll?
        cardList = player.getHandCards();
        long numberOfDrawnCards = player.getNumOfDrawCards();
        for (int i = 0; i == numberOfDrawnCards; i++) {
            cardList.add(card);
        }
        player.setHandCards(cardList);
        return player;
    }
}
