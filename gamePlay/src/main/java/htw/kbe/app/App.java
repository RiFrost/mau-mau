package htw.kbe.app;

import htw.kbe.config.AppConfig;
import htw.kbe.maumau.controller.export.AppController;
import htw.kbe.maumau.controller.service.AppControllerImpl;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    public static void main(String[] args) {
            ConfigurableApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
            final AppController controller  = applicationContext.getBean(AppControllerImpl.class);

            controller.play();

            applicationContext.close();
        }
}

