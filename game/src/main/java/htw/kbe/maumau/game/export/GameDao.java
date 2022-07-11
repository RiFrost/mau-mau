package htw.kbe.maumau.game.export;

import java.util.List;

public interface GameDao {

    List<Game> findGame();

    void create(Game game);

    void delete(Game game);

}
