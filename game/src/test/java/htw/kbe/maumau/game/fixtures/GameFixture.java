package htw.kbe.maumau.game.fixtures;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Label;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.deck.domain.Deck;
import htw.kbe.maumau.game.domain.Game;
import htw.kbe.maumau.player.domain.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GameFixture {

    private static List<Player> players = new LinkedList<Player>();


    public static List<Player> players() {
        players.add(new Player( "Phil"));
        players.add(new Player("Maria"));
        players.add(new Player("Jasmin"));
        players.add(new Player("Richard"));

        return players;
    }

    public static Deck deck() {
        Deck deck = new Deck();
        deck.setDrawPile(cards());
        return deck;
    }

    public static Game game() {
        return new Game(players(), deck());
    }

    public static List<Suit> suits = Arrays.asList(
            Suit.CLUBS,
            Suit.DIAMONDS,
            Suit.HEARTS,
            Suit.SPADES
    );

    public static List<Label> labels = Arrays.asList(
            Label.ASS,
            Label.KING,
            Label.QUEEN,
            Label.JACK,
            Label.TEN,
            Label.NINE,
            Label.EIGHT,
            Label.SEVEN
    );

    public static List<Card> cards() {
        List<Card> cards = new ArrayList<>();
        for(Suit suit : suits) {
            for(Label label : labels) {
                cards.add(new Card(suit, label));
            }
        }

        return cards;
    }

}
