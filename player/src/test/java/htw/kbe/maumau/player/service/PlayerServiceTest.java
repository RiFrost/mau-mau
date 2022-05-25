package htw.kbe.maumau.player.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Label;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.card.service.CardService;
import htw.kbe.maumau.card.service.CardServiceImpl;
import htw.kbe.maumau.player.domain.Player;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class PlayerServiceTest {

    @InjectMocks
    private PlayerService service;
    private List<Card> cards;
    private Suit suit;
    private Card card;

    @Mock
    private CardService cardService;

    @BeforeEach
    public void setUp() {
        this.service = new PlayerServiceImpl();
        this.cardService = Mockito.mock(CardService.class);
        this.service.setPlayerService(this.cardService);
        cardService = new CardServiceImpl();
        }


    @Test
    public void testCreateNewPlayer() {
        Player player = new Player(1,"Tim");
        Assertions.assertEquals(1, player.getId());
        Assertions.assertEquals("Tim", player.getName());
    }

    @Test
    public void testSayMauMau(){
        Player player = new Player(1,"Tim");
        player.setHasSaidMauMau(true);
        Assertions.assertTrue(player.hasSaidMau());
        player.setHasSaidMauMau(false);
        Assertions.assertFalse(player.hasSaidMau());
    }

    @Test
    public void testPlayCard(){
//        Player player = new Player(1,"Tim");
//        service.playCard(player, card);
//        Card expectedCard = new Card(Suit.CLUBS, Label.ASS);
//        Assertions.assertEquals(expectedCard, card);
    }
}
