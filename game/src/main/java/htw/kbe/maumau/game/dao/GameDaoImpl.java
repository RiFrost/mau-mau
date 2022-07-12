package htw.kbe.maumau.game.dao;

import htw.kbe.maumau.game.exceptions.DaoException;
import htw.kbe.maumau.game.exceptions.GameNotFoundException;
import htw.kbe.maumau.game.export.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Objects;


@Component
public class GameDaoImpl implements GameDao {

    //Todo: Funktioniert nicht mit Autowired in Kombi mit Tests???
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("gameDemo");
    private EntityManager entityManager = emf.createEntityManager();

    @Override
    public Game findById(Long id) {
        try {
            Game game = entityManager.find(Game.class, id);
            if (Objects.isNull(game)) throw new GameNotFoundException(String.format("Game with ID %d not found.", id));
            return game;
        } catch (PersistenceException exp) {
            throw new DaoException(exp.getMessage());
        }

    }

    @Override
    public boolean findGame() {
        try {
            int number = ((Number) entityManager.createNamedQuery("Game.countAll").getSingleResult()).intValue();
            return number > 0;
        } catch (PersistenceException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void saveGame(Game game) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(game);
            entityManager.getTransaction().commit();
        } catch (PersistenceException e) {
            entityManager.getTransaction().rollback();
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void delete(Game game) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.merge(game));
            entityManager.getTransaction().commit();
        } catch (PersistenceException e) {
            entityManager.getTransaction().rollback();
            throw new DaoException(e.getMessage());
        }
    }
}
