package htw.kbe.maumau.game.dao;


import htw.kbe.maumau.game.export.Game;

public interface GameDao {

    Game findById(Long id);

    boolean findGame();

    void create(Game game);

    void delete(Game game);

}
