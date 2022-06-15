package htw.kbe.config;

import htw.kbe.maumau.controller.export.AppController;
import htw.kbe.maumau.controller.service.AppControllerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = {"htw.kbe.maumau.deck", "htw.kbe.maumau.rule", "htw.kbe.maumau.player", "htw.kbe.maumau.card", "htw.kbe.maumau.controller", "htw.kbe.maumau.game"})
@Configuration
public class AppConfig {

    @Bean
    public AppController appController() {
        return new AppControllerImpl();
    }

}
