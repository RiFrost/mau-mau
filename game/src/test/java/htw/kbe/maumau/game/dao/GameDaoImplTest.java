package htw.kbe.maumau.game.dao;

import htw.kbe.maumau.game.exceptions.GameNotFoundException;
import htw.kbe.maumau.game.export.Game;
import htw.kbe.maumau.game.fixtures.GameFixture;
import htw.kbe.maumau.player.export.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;


public class GameDaoImplTest {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("gameDemo");
    private EntityManager em = emf.createEntityManager();
    private EntityTransaction tx = em.getTransaction();
    private Game game;
    private GameDao gameDao = new GameDaoImpl();

    @Before
    public void setUp() {
        tx.begin();
        this.game = new Game(GameFixture.players(), GameFixture.deck());
        em.persist(this.game);
        tx.commit();
    }


    @After
    public void close() {
        deleteEntry(this.game);
    }

    private void deleteEntry(Game game) {
        if (Objects.nonNull(em.find(Game.class, game.getId()))) {
            tx.begin();
            em.remove(game);
            tx.commit();
        }
    }

    @Test
    public void testFindById() {
        Game actualGame = gameDao.findById(this.game.getId());
        //ToDo compare methode für Namen schreiben
        assertEquals(GameFixture.players(), actualGame.getPlayers());
        assertEquals(GameFixture.deck(), actualGame.getCardDeck());
    }

    @Test
    public void cantFindById() {
        Exception exception = assertThrows(GameNotFoundException.class, () -> {
            gameDao.findById(0L);
        });

        String expectedMessage = "Game with ID 0 not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFindGame() {
        assertTrue(gameDao.findGame());
    }

    @Test
    public void testCreateGame() {
        try {
            List<Player> players = List.of(new Player("Horst"), new Player("Karl"));
            Game game = new Game(players, GameFixture.deck());
            gameDao.create(game);
            assertNotNull(em.find(Game.class, game.getId()));
        } finally {
            deleteEntry(game);
        }
    }

    @Test
    public void testDeleteGame() {
        gameDao.delete(this.game);
        em.clear();
        assertNull(em.find(Game.class, this.game.getId()));
    }
}
