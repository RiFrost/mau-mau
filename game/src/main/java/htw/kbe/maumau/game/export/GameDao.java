package htw.kbe.maumau.game.export;


public interface GameDao {

    Game findById(Long id);

    boolean findGame();

    void create(Game game);

    void delete(Game game);

}
