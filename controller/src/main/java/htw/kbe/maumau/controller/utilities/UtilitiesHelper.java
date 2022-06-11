package htw.kbe.maumau.controller.utilities;

import htw.kbe.maumau.card.export.Card;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class UtilitiesHelper {

    /**
     * loads the game instruction from a txt file
     * @return game instruction
     */
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

    /**
     * creates an ascii image from the required card
     * @param card - card to be shown
     * @return a card image in ascii style
     */
    public String getCardImage(Card card) {
        String suit = "";
        String label = "";
        switch (card.getSuit()) {
            case CLUBS -> suit = "\u2663";
            case HEARTS -> suit = "\u2665";
            case DIAMONDS -> suit = "\u2666";
            case SPADES -> suit = "\u2660";
        }
        switch (card.getLabel()) {
            case SEVEN -> label = "7";
            case EIGHT -> label = "8";
            case NINE -> label = "9";
            case TEN -> label = "10";
            case JACK -> label = "J";
            case QUEEN -> label = "Q";
            case KING -> label = "K";
            case ASS -> label = "A";
        }
        return label.equals("10") ? String.format("______\n|%s  |\n| %s  |\n|__%s|", label, suit, label) : String.format("_____\n|%s  |\n| %s |\n|__%s|", label, suit, label);
    }

    // Note: initialize scanner object in every function for better testing opportunities

    /**
     * asks the user how many want to participate in the game
     * @return desired number of players
     */
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

    /**
     * asks the player what he wants to do
     * @param min - minimum input number
     * @param max - maximum input number
     * @return chosen number from player
     */
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
