package htw.kbe.appcontroller.application;

import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;
import htw.kbe.maumau.game.exceptions.InvalidPlayerSizeException;
import htw.kbe.maumau.game.service.GameService;
import htw.kbe.maumau.game.service.GameServiceImpl;
import htw.kbe.maumau.player.domain.Player;
import htw.kbe.maumau.player.exceptions.InvalidPlayerNameException;
import htw.kbe.maumau.player.service.PlayerService;
import htw.kbe.maumau.player.service.PlayerServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@ComponentScan(basePackages = {"htw.kbe.maumau.deck", "htw.kbe.maumau.rule", "htw.kbe.maumau.player", "htw.kbe.maumau.card"})
@Configuration
public class App {

    public static void main(String[] args) throws IllegalDeckSizeException, InvalidPlayerSizeException, InvalidPlayerNameException {

        withComponentScan();
    }

    private static void withComponentScan() throws InvalidPlayerNameException, IllegalDeckSizeException, InvalidPlayerSizeException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(App.class);

        applicationContext.scan(GameServiceImpl.class.getPackage().getName());
        applicationContext.scan(PlayerServiceImpl.class.getPackage().getName());

        final PlayerService playerService = applicationContext.getBean(PlayerServiceImpl.class);
        final GameService gameService = applicationContext.getBean(GameServiceImpl.class);
        List<Player> players = playerService.createPlayers(new ArrayList(Arrays.asList( "Maria", "Phil", "Jasmin", "Richard")));
        gameService.startNewGame(players);

        applicationContext.close();
    }
}

