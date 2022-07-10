package htw.kbe.maumau.controller.dao;

import htw.kbe.maumau.game.export.Game;

public interface GameDao {

    Game findById(Long id);

    void create(Game game);

    void delete(Game game);

}
