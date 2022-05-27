package htw.kbe.maumau.player.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.player.domain.Player;
import htw.kbe.maumau.player.exceptions.InvalidPlayerNameException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerServiceImpl implements PlayerService {

    @Override
    public List<Player> createPlayers(List<String> names) throws InvalidPlayerNameException {
        List<Player> players = new ArrayList();
        for (String name : names) {
           validateName(name, names);
           players.add(new Player(name));
        }
        return players;
    }

    @Override
    public void validateName(String name, List<String> names) throws InvalidPlayerNameException {
        if(Collections.frequency(names, name) >= 2) throw new InvalidPlayerNameException("The Player s% occurs more than one time".format(name));

        if(name.length() <= 0 || name.length() > 15 || name.isBlank()) {
            throw new InvalidPlayerNameException("The Player name is invalid!");
        }
    }

    @Override
    public void sayMau(Player player) {
        player.setSaidMau(true);
    }

    public void mustSuspend(Player player) {
        player.setMustSuspend(true);
    }

    @Override
    public void playCard(Player player, Card card) {
        player.getHandCards().remove(card);
    }

    @Override
    public void drawCards(Player player, List<Card> cards) {
        player.getHandCards().addAll(cards);
    }

}
