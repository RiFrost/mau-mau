package htw.kbe.maumau.game.dao;

import htw.kbe.maumau.game.export.Game;

import java.util.List;

public interface GameDao {

    List<Game> findGame();

    void create(Game game);

    void delete(Game game);

}
