package htw.kbe.maumau.game.dao;

import htw.kbe.maumau.game.exceptions.DaoException;
import htw.kbe.maumau.game.exceptions.GameNotFoundException;
import htw.kbe.maumau.game.export.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.Objects;


@Repository
public class GameDaoImpl implements GameDao {

    //Todo: Funktioniert nicht mit Autowired in Kombi mit Tests???
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("MauMau");
    private EntityManager entityManager = emf.createEntityManager();

//    @Autowired
//    private EntityManager entityManager;

    private static Logger logger = LogManager.getLogger(GameDaoImpl.class);

    @Override
    public Game findById(Long id) {
        try {
            Game game = entityManager.find(Game.class, id);
            if (Objects.isNull(game)) {
                logger.info("Game with ID %d is not found", id);
                throw new GameNotFoundException(String.format("Game with ID %d not found.", id));
            }
            logger.info("Game with ID %d is found", game.getId());
            return game;
        } catch (PersistenceException e) {
            logger.error("DaoException is thrown: %s", e.getMessage());
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public boolean findGame() {
        try {
            int number = ((Number) entityManager.createNamedQuery("Game.countAll").getSingleResult()).intValue();
            logger.info("%d saved Games are found", number);
            return number > 0;
        } catch (PersistenceException e) {
            logger.error("DaoException is thrown: %s", e.getMessage());
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void saveGame(Game game) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(game);
            entityManager.getTransaction().commit();
            logger.info("Game with ID %d is saved", game.getId());
        } catch (PersistenceException e) {
            entityManager.getTransaction().rollback();
            logger.error("DaoException is thrown: %s", e.getMessage());
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void deleteGame(Game game) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.merge(game));
            entityManager.getTransaction().commit();
            logger.info("Game with ID %d is deleted", game.getId());
        } catch (PersistenceException e) {
            entityManager.getTransaction().rollback();
            logger.error("DaoException is thrown: %s", e.getMessage());
            throw new DaoException(e.getMessage());
        }
    }
}
