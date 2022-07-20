package htw.kbe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ComponentScan(basePackages = {"htw.kbe.maumau.deck", "htw.kbe.maumau.rule", "htw.kbe.maumau.player", "htw.kbe.maumau.card", "htw.kbe.maumau.controller", "htw.kbe.maumau.game", "htw.kbe.maumau.virtualPlayer"})
@Configuration
public class AppConfig {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("MauMau");

    @Bean
    public EntityManager entityManager() {
        return emf.createEntityManager();
    }

}
