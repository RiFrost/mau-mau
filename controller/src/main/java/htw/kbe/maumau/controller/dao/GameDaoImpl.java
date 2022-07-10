package htw.kbe.maumau.controller.dao;

import htw.kbe.maumau.game.export.Game;
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
    public void create(Game game) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(game);
            entityManager.getTransaction().commit();
        } catch (PersistenceException exp) {
            //entityManager.getTransaction().rollback();
            throw new DaoException(exp.getMessage());
        }
    }

    @Override
    public void delete(Game game) {
        try {
            entityManager.remove(game);
        } catch (PersistenceException exp) {
            throw new DaoException(exp.getMessage());
        }
    }
}
