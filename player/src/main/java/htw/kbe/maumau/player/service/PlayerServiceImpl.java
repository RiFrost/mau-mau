package htw.kbe.maumau.player.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.service.CardService;
import htw.kbe.maumau.player.domain.Player;
import htw.kbe.maumau.player.exceptions.IllegalPlayerSizeException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PlayerServiceImpl implements PlayerService {

    private List<Card> cardList = new LinkedList<>();
    private List<Player> playerList = new LinkedList<>();

    @Override
    public Player createNewPlayer(Long id, String name) {
        Player player = new Player(id, name);
        playerList.add(player);
        validatePlayerSize(playerList);          // Müsste eher ins Game oder?
        return player;
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

    @Override
    public void validatePlayerSize(List<Player> playerList){
        long limitOfPlayers = 5;
        if(!(playerList.size() <= limitOfPlayers)){
            try {
                throw new IllegalPlayerSizeException("The max number of players is " + limitOfPlayers);
            } catch (IllegalPlayerSizeException e) {
                throw new RuntimeException(e);
            }
        }
        if (playerList.size() == 1) {
            Player npcPlayer = new Player(100,"Kevin");
            npcPlayer.setNpc(true);
        }
    }

    @Override
    public void setPlayerService(CardService cardService) {
    }
}
