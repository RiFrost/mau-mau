package htw.kbe.app;

import htw.kbe.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    public static void main(String[] args) {
            final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
            var app = applicationContext.getBean(AppConfig.class);

            app.appController().play();

            applicationContext.close();
        }
}

