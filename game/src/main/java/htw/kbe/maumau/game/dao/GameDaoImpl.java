package htw.kbe.maumau.game.dao;

import htw.kbe.maumau.game.exceptions.DaoException;
import htw.kbe.maumau.game.export.Game;
import htw.kbe.maumau.game.export.GameDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;


@Component
public class GameDaoImpl implements GameDao {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Game findById(Long id) {
        try {
            return entityManager.find(Game.class, id);
        } catch (PersistenceException exp) {
            throw new DaoException(exp.getMessage());
        }
    }

    @Override
    public boolean findGame() {
        try {
            //Query query = entityManager.createQuery("SELECT COUNT(g) FROM Game g");
            int number = ((Number)entityManager.createNamedQuery("Game.countAll").getSingleResult()).intValue();
            return number > 0;
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
