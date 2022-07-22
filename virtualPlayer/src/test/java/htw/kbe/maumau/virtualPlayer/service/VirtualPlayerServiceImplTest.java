package htw.kbe.maumau.virtualPlayer.service;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.CardService;
import htw.kbe.maumau.card.export.Label;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.player.export.Player;
import htw.kbe.maumau.virtualPlayer.AIServiceImpl;
import htw.kbe.maumau.virtualPlayer.fixtures.CardsFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VirtualPlayerServiceImplTest {

    @InjectMocks
    private AIServiceImpl aiService;

    @Mock
    private CardService cardService;

    private Player aiPlayer;

    @BeforeEach
    public void setUp() {

    }

    @Test
    @DisplayName("should only return a suit that is also given in players hand cards")
    public void testSuitWish() {
        aiPlayer = new Player("Mr Robot");
        List<Card> cards = Arrays.asList(new Card(Suit.CLUBS, Label.SEVEN));
        aiPlayer.setHandCards(cards);
        when(cardService.getSuits()).thenReturn(CardsFixture.suits);

        assertEquals(Suit.CLUBS, aiService.getSuitWish(aiPlayer));

    }
}
