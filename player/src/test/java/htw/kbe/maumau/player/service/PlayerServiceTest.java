package htw.kbe.maumau.player.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Label;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.card.service.CardService;
import htw.kbe.maumau.card.service.CardServiceImpl;
import htw.kbe.maumau.player.domain.Player;
import htw.kbe.maumau.player.exceptions.IllegalPlayerSizeException;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PlayerServiceTest {

    @InjectMocks
    private PlayerService service;
    private List<Card> cards;
    private Suit suit;
    private Card card;

    @Mock
    private CardService cardService;
    private PlayerService playerService;

    @BeforeEach
    public void setUp() {
        this.service = new PlayerServiceImpl();
        this.cardService = Mockito.mock(CardService.class);
        this.service.setPlayerService(this.playerService);
        cardService = new CardServiceImpl();
        }


    @Test
    @DisplayName("should return a player with its id and name")
    public void testCreateNewPlayer() throws IllegalPlayerSizeException {
        Assertions.assertEquals(1L, service.createNewPlayer(1L,"Tim").getId());
        Assertions.assertEquals("Tim", service.createNewPlayer(1L,"Tim").getName());
    }

    @Test
    public void testValidatePlayerSize() {
//        List<Player> playerList = new LinkedList<>();
//        playerList.add(new Player(1,"Tim"));          // IndexOutOfBoundsException
//        playerList.add(new Player(2,"Tia"));
//        service.validatePlayerSize(playerList);
//        verify(playerList,times(1));
    }

    @Test
    @DisplayName("test if player says mau and the draw consequence if this is false")
    public void testSayMauMau(){
        Player player = new Player(1,"Tim");
        player.setHasSaidMauMau(true);
        service.sayMauMau(player);
        Assertions.assertTrue(player.hasSaidMau());

        player.setHasSaidMauMau(false);
        service.sayMauMau(player);
        Assertions.assertFalse(player.hasSaidMau());
        Assertions.assertTrue(player.isMustDraw());
    }

    @Test
    public void testPlayCard(){
//        Player player = new Player(1,"Tim");
//        service.playCard(player, card);
//        Card expectedCard = new Card(Suit.CLUBS, Label.ASS);
//        Assertions.assertEquals(expectedCard, card);
    }
}
