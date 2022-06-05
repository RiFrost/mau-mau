package htw.kbe.maumau.uicontroller.ascii;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Label;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.card.service.CardService;
import htw.kbe.maumau.card.service.CardServiceImpl;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AsciiCards {

    public static String getCardImage(String suit, String label) {
        return label.equals("10") ? String.format("______\n|%s  |\n| %s  |\n|__%s|", label, suit, label) : String.format("_____\n|%s  |\n| %s |\n|__%s|", label, suit, label);
    }

    public static Map<Card, String> cardImages;

    static {
        cardImages = new HashMap<>();
        cardImages.put(new Card(Suit.CLUBS, Label.SEVEN), getCardImage("\u2663", "7"));
        cardImages.put(new Card(Suit.CLUBS, Label.EIGHT), getCardImage("\u2663", "8"));
        cardImages.put(new Card(Suit.CLUBS, Label.NINE), getCardImage("\u2663", "9"));
        cardImages.put(new Card(Suit.CLUBS, Label.TEN), getCardImage("\u2663", "10"));
        cardImages.put(new Card(Suit.CLUBS, Label.JACK), getCardImage("\u2663", "J"));
        cardImages.put(new Card(Suit.CLUBS, Label.QUEEN), getCardImage("\u2663", "Q"));
        cardImages.put(new Card(Suit.CLUBS, Label.KING), getCardImage("\u2663", "K"));
        cardImages.put(new Card(Suit.CLUBS, Label.ASS), getCardImage("\u2663", "A"));

        cardImages.put(new Card(Suit.HEARTS, Label.SEVEN), getCardImage("\u2665", "7"));
        cardImages.put(new Card(Suit.HEARTS, Label.EIGHT), getCardImage("\u2665", "8"));
        cardImages.put(new Card(Suit.HEARTS, Label.NINE), getCardImage("\u2665", "9"));
        cardImages.put(new Card(Suit.HEARTS, Label.TEN), getCardImage("\u2665", "10"));
        cardImages.put(new Card(Suit.HEARTS, Label.JACK), getCardImage("\u2665", "J"));
        cardImages.put(new Card(Suit.HEARTS, Label.QUEEN), getCardImage("\u2665", "Q"));
        cardImages.put(new Card(Suit.HEARTS, Label.KING), getCardImage("\u2665", "K"));
        cardImages.put(new Card(Suit.HEARTS, Label.ASS), getCardImage("\u2665", "A"));

        cardImages.put(new Card(Suit.DIAMONDS, Label.SEVEN), getCardImage("\u2666", "7"));
        cardImages.put(new Card(Suit.DIAMONDS, Label.EIGHT), getCardImage("\u2666", "8"));
        cardImages.put(new Card(Suit.DIAMONDS, Label.NINE), getCardImage("\u2666", "9"));
        cardImages.put(new Card(Suit.DIAMONDS, Label.TEN), getCardImage("\u2666", "10"));
        cardImages.put(new Card(Suit.DIAMONDS, Label.JACK), getCardImage("\u2666", "J"));
        cardImages.put(new Card(Suit.DIAMONDS, Label.QUEEN), getCardImage("\u2666", "Q"));
        cardImages.put(new Card(Suit.DIAMONDS, Label.KING), getCardImage("\u2666", "K"));
        cardImages.put(new Card(Suit.DIAMONDS, Label.ASS), getCardImage("\u2666", "A"));

        cardImages.put(new Card(Suit.SPADES, Label.SEVEN), getCardImage("\u2660", "7"));
        cardImages.put(new Card(Suit.SPADES, Label.EIGHT), getCardImage("\u2660", "8"));
        cardImages.put(new Card(Suit.SPADES, Label.NINE), getCardImage("\u2660", "9"));
        cardImages.put(new Card(Suit.SPADES, Label.TEN), getCardImage("\u2660", "10"));
        cardImages.put(new Card(Suit.SPADES, Label.JACK), getCardImage("\u2660", "J"));
        cardImages.put(new Card(Suit.SPADES, Label.QUEEN), getCardImage("\u2660", "Q"));
        cardImages.put(new Card(Suit.SPADES, Label.KING), getCardImage("\u2660", "K"));
        cardImages.put(new Card(Suit.SPADES, Label.ASS), getCardImage("\u2660", "A"));
    }

}
