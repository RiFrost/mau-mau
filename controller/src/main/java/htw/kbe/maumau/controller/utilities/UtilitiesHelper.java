package htw.kbe.maumau.controller.utilities;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Label;
import htw.kbe.maumau.card.export.Suit;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class UtilitiesHelper {

    private Map<Card, String> cardImages = Map.ofEntries(
            Map.entry(new Card(Suit.CLUBS, Label.SEVEN), getCardImage("\u2663", "7")),
            Map.entry(new Card(Suit.CLUBS, Label.EIGHT), getCardImage("\u2663", "8")),
            Map.entry(new Card(Suit.CLUBS, Label.TEN), getCardImage("\u2663", "10")),
            Map.entry(new Card(Suit.CLUBS, Label.JACK), getCardImage("\u2663", "J")),
            Map.entry(new Card(Suit.CLUBS, Label.NINE), getCardImage("\u2663", "9")),
            Map.entry(new Card(Suit.CLUBS, Label.QUEEN), getCardImage("\u2663", "Q")),
            Map.entry(new Card(Suit.CLUBS, Label.KING), getCardImage("\u2663", "K")),
            Map.entry(new Card(Suit.CLUBS, Label.ASS), getCardImage("\u2663", "A")),
            Map.entry(new Card(Suit.HEARTS, Label.SEVEN), getCardImage("\u2665", "7")),
            Map.entry(new Card(Suit.HEARTS, Label.EIGHT), getCardImage("\u2665", "8")),
            Map.entry(new Card(Suit.HEARTS, Label.NINE), getCardImage("\u2665", "9")),
            Map.entry(new Card(Suit.HEARTS, Label.TEN), getCardImage("\u2665", "10")),
            Map.entry(new Card(Suit.HEARTS, Label.JACK), getCardImage("\u2665", "J")),
            Map.entry(new Card(Suit.HEARTS, Label.QUEEN), getCardImage("\u2665", "Q")),
            Map.entry(new Card(Suit.HEARTS, Label.ASS), getCardImage("\u2665", "A")),
            Map.entry(new Card(Suit.HEARTS, Label.KING), getCardImage("\u2665", "K")),

            Map.entry(new Card(Suit.DIAMONDS, Label.SEVEN), getCardImage("\u2666", "7")),
            Map.entry(new Card(Suit.DIAMONDS, Label.EIGHT), getCardImage("\u2666", "8")),
            Map.entry(new Card(Suit.DIAMONDS, Label.NINE), getCardImage("\u2666", "9")),
            Map.entry(new Card(Suit.DIAMONDS, Label.TEN), getCardImage("\u2666", "10")),
            Map.entry(new Card(Suit.DIAMONDS, Label.JACK), getCardImage("\u2666", "J")),
            Map.entry(new Card(Suit.DIAMONDS, Label.QUEEN), getCardImage("\u2666", "Q")),
            Map.entry(new Card(Suit.DIAMONDS, Label.KING), getCardImage("\u2666", "K")),
            Map.entry(new Card(Suit.DIAMONDS, Label.ASS), getCardImage("\u2666", "A")),
            Map.entry(new Card(Suit.SPADES, Label.SEVEN), getCardImage("\u2660", "7")),
            Map.entry(new Card(Suit.SPADES, Label.EIGHT), getCardImage("\u2660", "8")),
            Map.entry(new Card(Suit.SPADES, Label.NINE), getCardImage("\u2660", "9")),
            Map.entry(new Card(Suit.SPADES, Label.TEN), getCardImage("\u2660", "10")),
            Map.entry(new Card(Suit.SPADES, Label.JACK), getCardImage("\u2660", "J")),
            Map.entry(new Card(Suit.SPADES, Label.QUEEN), getCardImage("\u2660", "Q")),
            Map.entry(new Card(Suit.SPADES, Label.KING), getCardImage("\u2660", "K")),
            Map.entry(new Card(Suit.SPADES, Label.ASS), getCardImage("\u2660", "A"))
    );

    public String loadFromFile() {
        String instructions = "";
        InputStream inputStream = this.getClass().getResourceAsStream("/game_instructions.txt");
        try (InputStreamReader in = new InputStreamReader(inputStream);
             BufferedReader buffer = new BufferedReader(in)) {
            instructions = buffer.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            System.out.println("Game instructions could not loaded");
        }
        return instructions;
    }

    private String getCardImage(String suit, String label) {
        return label.equals("10") ? String.format("______\n|%s  |\n| %s  |\n|__%s|", label, suit, label) : String.format("_____\n|%s  |\n| %s |\n|__%s|", label, suit, label);
    }

    public String getCardImage(Card card) {
        return this.cardImages.get(card);
    }

    // Note: initialize scanner object in every function for better testing opportunities
    public String getPlayerName() {
        String name;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            name = scanner.next();
            if (name.isBlank()) {
                System.out.println("Your name cannot be blank! Please try again!");
            } else if (name.matches(".*[0-9].*")) {
                System.out.println("Your name cannot contain numbers!");
            } else if (name.length() < 3) {
                System.out.println("Your name is too short! Please choose a longer name for you!");
            } else if (name.length() > 15) {
                System.out.println("Your name is too long! Please choose a shorter name for you!");
            } else {
                break;
            }
        }
        return name;
    }

    public int getChosenNumber(int min, int max) {
        Scanner scanner = new Scanner(System.in);
        int index;
        while (true) {
            try {
                index = Integer.parseInt(scanner.next());
                if (index < min || index > max) {
                    System.out.printf("Please choose a number between %d and %d:", min, max);
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("This is not a number. Please try again!");
            }
        }
        return index;
    }


}
