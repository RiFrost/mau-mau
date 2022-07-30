package htw.kbe.maumau.game.dao;

import htw.kbe.maumau.game.exceptions.GameNotFoundException;
import htw.kbe.maumau.game.export.Game;
import htw.kbe.maumau.game.fixtures.GameFixture;
import htw.kbe.maumau.player.export.Player;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


public class GameDaoImplTest {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("MauMau");
    private EntityManager entityManager = emf.createEntityManager();

    private EntityTransaction tx = entityManager.getTransaction();
    private Game game;
    private GameDaoImpl gameDao;

    @BeforeEach
    public void init() {
        this.gameDao = new GameDaoImpl();
        this.gameDao.setEntityManager(entityManager);
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
            entityManager.remove(entityManager.contains(game) ? game : entityManager.merge(game));
            tx.commit();
        }
    }

    @Test
    @DisplayName("should find game by ID")
    public void testFindById() {
        Game actualGame = gameDao.findById(this.game.getId());
        List<Player> expectedPlayers = GameFixture.players();
        List<Player> actualPlayers = actualGame.getPlayers();
        Collections.sort(expectedPlayers);
        Collections.sort(actualPlayers);
        assertEquals(expectedPlayers.toString(), actualPlayers.toString());
        assertEquals(GameFixture.deck().toString(), actualGame.getCardDeck().toString());
    }

    @Test
    @DisplayName("should not find game when ID ist not in database")
    public void cantFindById() {
        Exception exception = assertThrows(GameNotFoundException.class, () -> {
            gameDao.findById(0L);
        });

        String expectedMessage = "Game with ID 0 not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("should return true if game are already saved in database")
    public void testFindGame() {
        assertTrue(gameDao.findGame());
    }

    @Test
    @DisplayName("should save a new game in database")
    public void testCreateGame() {
        List<Player> players = List.of(new Player("Horst"), new Player("Karl"));
        Game newGame = new Game(players, GameFixture.deck());
        try {
            gameDao.saveGame(newGame);
            assertNotNull(entityManager.find(Game.class, newGame.getId()));
        } finally {
            deleteEntry(newGame);
        }
    }

    @Test
    @DisplayName("should remove game from database")
    public void testDeleteGame() {
        gameDao.deleteGame(this.game);
        entityManager.clear();
        assertNull(entityManager.find(Game.class, this.game.getId()));
    }
}
