package htw.kbe.app;

import htw.kbe.maumau.controller.export.AppController;
import htw.kbe.maumau.controller.service.AppControllerImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = {"htw.kbe.maumau.deck", "htw.kbe.maumau.rule", "htw.kbe.maumau.player", "htw.kbe.maumau.card", "htw.kbe.maumau.controller", "htw.kbe.maumau.game"})
@Configuration
public class App {

     public static void main(String[] args) {
            final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(App.class);

            applicationContext.scan(AppControllerImpl.class.getPackage().getName());

            final AppController appController = applicationContext.getBean(AppControllerImpl.class);

            appController.playGame();

            applicationContext.close();
        }
}

