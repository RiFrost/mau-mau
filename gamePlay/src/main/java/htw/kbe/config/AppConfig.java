package htw.kbe.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = {"htw.kbe.maumau.deck", "htw.kbe.maumau.rule", "htw.kbe.maumau.player", "htw.kbe.maumau.card", "htw.kbe.maumau.controller", "htw.kbe.maumau.game"})
@Configuration
public class AppConfig {}
