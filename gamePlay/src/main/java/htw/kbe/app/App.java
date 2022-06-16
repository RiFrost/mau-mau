package htw.kbe.app;

import htw.kbe.config.AppConfig;
import htw.kbe.maumau.game.service.GameServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    private static Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) {
            final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
            var app = applicationContext.getBean(AppConfig.class);

            logger.error("SAMPLE ERROR MESSAGE FOR APP");

            app.appController().playGame();

            applicationContext.close();
        }
}

