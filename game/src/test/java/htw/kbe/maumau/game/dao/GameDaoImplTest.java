package htw.kbe.maumau.game.dao;

import htw.kbe.maumau.game.exceptions.GameNotFoundException;
import htw.kbe.maumau.game.export.Game;
import htw.kbe.maumau.game.fixtures.GameFixture;
import htw.kbe.maumau.player.export.Player;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


public class GameDaoImplTest {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("MauMau");
    private EntityManager entityManager = emf.createEntityManager();

    private EntityTransaction tx = entityManager.getTransaction();
    private Game game;
    private GameDao gameDao = new GameDaoImpl();

    @BeforeEach
    public void init() {
        tx.begin();
        this.game = new Game(GameFixture.players(), GameFixture.deck());
        entityManager.persist(this.game);
        tx.commit();
    }

    @AfterEach
    public void tearDown() {
        deleteEntry(this.game);
    }

    private void deleteEntry(Game game) {
        if (Objects.nonNull(entityManager.find(Game.class, game.getId()))) {
            tx.begin();
            entityManager.remove(game);
            tx.commit();
        }
    }

    @Test
    public void testFindById() {
        Game actualGame = gameDao.findById(this.game.getId());
        assertEquals(GameFixture.players().toString(), actualGame.getPlayers().toString());
        assertEquals(GameFixture.deck().toString(), actualGame.getCardDeck().toString());
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
            gameDao.saveGame(game);
            assertNotNull(entityManager.find(Game.class, game.getId()));
        } finally {
            deleteEntry(game);
        }
    }

    @Test
    public void testDeleteGame() {
        gameDao.deleteGame(this.game);
        entityManager.clear();
        assertNull(entityManager.find(Game.class, this.game.getId()));
    }
}
