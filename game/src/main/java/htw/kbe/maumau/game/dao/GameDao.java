package htw.kbe.maumau.game.dao;


import htw.kbe.maumau.game.export.Game;

public interface GameDao {

    /**
     * finds and loads game by game id
     * @param id id of game
     * @return game that was found
     */
    Game findById(Long id);

    /**
     * counts the number of found games
     * @return true if a games are found in database, false if not
     */
    boolean findGame();

    /**
     * stores the game in the database
     * @param game game to be saved
     */
    void saveGame(Game game);

    /**
     * deletes the game from the database
     * @param game game to be deleted
     */
    void deleteGame(Game game);

}
