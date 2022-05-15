package htw.kbe.maumau.game.service;

import htw.kbe.maumau.deck.domain.Deck;
import htw.kbe.maumau.game.domain.Game;
import htw.kbe.maumau.player.domain.Player;

import java.util.List;

public class GameServiceImpl implements GameService {

    @Override
    public Game startGame(List<Player> players, Deck deck) {
        return null;
    }

    @Override
    public Player switchToNewPlayer(List<Player> players) {
        return null;
    }

    @Override
    public Player callWinner(List<Player> players) {
        return null;
    }

    @Override
    public boolean isGameCancelled(List<Player> players) {
        return false;
    }
}
