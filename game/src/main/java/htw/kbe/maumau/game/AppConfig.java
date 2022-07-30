package htw.kbe.maumau.game;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Configuration
public class AppConfig {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("MauMau");

    @Bean
    public EntityManager entityManager() {
        return emf.createEntityManager();
    }

}
