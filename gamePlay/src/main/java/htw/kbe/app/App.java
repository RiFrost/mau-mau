package htw.kbe.app;

import htw.kbe.config.AppConfig;
import htw.kbe.maumau.controller.export.AppController;
import htw.kbe.maumau.controller.service.AppControllerImpl;
import htw.kbe.maumau.deck.export.Deck;
import htw.kbe.maumau.game.export.Game;
import htw.kbe.maumau.player.export.Player;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {
            ConfigurableApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
            final AppController controller  = applicationContext.getBean(AppControllerImpl.class);
//            Game game = new Game(List.of(new Player("Detlef"), new Player("Ute")), new Deck());

//            EntityManagerFactory emf = Persistence.createEntityManagerFactory("gameDemo");
//            EntityManager em = emf.createEntityManager();
//
//            EntityTransaction tx = em.getTransaction();
//
//            tx.begin();
//
//            em.persist(game);
//            tx.commit();

            controller.play();

            applicationContext.close();
        }
}

