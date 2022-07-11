package htw.kbe.maumau.game.dao;

import htw.kbe.maumau.game.exceptions.DaoException;
import htw.kbe.maumau.game.export.Game;
import htw.kbe.maumau.game.export.GameDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@Component
public class GameDaoImpl implements GameDao {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Game> findGame() {
        try {
            Query query = entityManager.createQuery("SELECT g FROM Game g");
            return query.getResultList();
        } catch (PersistenceException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void create(Game game) {
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
            entityManager.remove(game);
            entityManager.getTransaction().commit();
        } catch (PersistenceException e) {
            entityManager.getTransaction().rollback();
            throw new DaoException(e.getMessage());
        }
    }
}
