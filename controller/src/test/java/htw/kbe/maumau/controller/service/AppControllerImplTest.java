package htw.kbe.maumau.controller.service;

import htw.kbe.maumau.card.export.CardService;
import htw.kbe.maumau.controller.export.AppController;
import htw.kbe.maumau.controller.export.ViewService;
import htw.kbe.maumau.game.export.GameService;
import htw.kbe.maumau.player.export.PlayerService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AppControllerImplTest {

    @InjectMocks
    private AppController appController;
    @Mock
    private GameService gameService;
    @Mock
    private ViewService viewService;
    @Mock
    private CardService cardService;
    @Mock
    private PlayerService playerService;


}
