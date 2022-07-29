package htw.kbe.maumau.controller.service;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.player.export.Player;
import htw.kbe.maumau.controller.export.ViewService;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ViewServiceImpl implements ViewService {

    @Override
    public void showWelcomeMessage() {
        System.out.println("Welcome to M(i)au M(i)au!\n");
    }

    @Override
    public int getNumberOfPlayer() {
        System.out.println(loadFromFile());
        System.out.println("\n\nHow many players will take part? Please choose a number from 2 to 4.");
        int number = (int) getChosenNumber(2, 4);
        return number;
    }

    @Override
    public int getNumberOfAI(int totalNumPlayers) {
        System.out.println("\nHow many AI players you would like to have?");
        int number = (int) getChosenNumber(0, totalNumPlayers - 1);
        System.out.printf("The game will start with %d players!\n", totalNumPlayers - number);
        return number;
    }

    @Override
    public List<String> getPlayerNames(int numberOfPlayer) {
        List<String> playerNames = new ArrayList<>();
        int playerNo = 1;
        for (int i = 0; i < numberOfPlayer; i++) {
            System.out.printf("Player %d, please type in your name:", playerNo++);
            String playerName = getPlayerName();
            playerNames.add(playerName);
        }
        return playerNames;
    }

    @Override
    public void showStartGameMessage(long id) {
        System.out.println("\nLet's begin!\n");
        System.out.printf("Your game ID is %d. Please write it down to load your game later.\n", id);
        System.out.printf("After each round your game is saved automatically.\n\n");
    }

    @Override
    public void showTopCard(Card topCard) {
        System.out.printf("The top card is %s\n", topCard);
        System.out.println(getCardImage(topCard));
    }

    @Override
    public void showHandCards(Player player, Suit suit) {
        int numberOfCard = 0;

        if (Objects.nonNull(suit)) {
            System.out.printf("\nA suit wish is given. Please choose a card of %s\n", suit);
        }

        System.out.printf("\n%s, here are your hand cards:\n", player.getName());
        System.out.printf("\n%d: DRAW A CARD\n", numberOfCard);
        for (Card card : player.getHandCards()) {
            System.out.printf("\n%d: %s\n", ++numberOfCard, card);
            System.out.println(getCardImage(card));
        }
    }

    @Override
    public Card getPlayedCard(Player player) {
        System.out.printf("\n%s, please choose a card to play or draw a card:\n", player.getName());
        int number = (int) getChosenNumber(0, player.getHandCards().size());
        return number == 0 ? null : player.getHandCards().get(number - 1);
    }

    @Override
    public Suit getChosenSuit(Player player, List<Suit> suits) {
        System.out.printf("\n%s, please choose a suit:\n", player.getName());
        int numberOfSuit = 1;
        for (Suit suit : suits) {
            System.out.printf("%d: %s\n", numberOfSuit++, suit);
        }
        int number = (int) getChosenNumber(1, suits.size());
        return suits.get(number - 1);
    }

    @Override
    public boolean saidMau(Player player) {
        System.out.printf("\n%s do you want to say 'mau'?\n", player.getName());
        System.out.println("1: YES\n2: NO");
        return getChosenNumber(1, 2) == 1;
    }

    @Override
    public void showDirectionOfRotation(boolean isClockwise) {
        System.out.printf("\nGame direction is now %s!\n", isClockwise ? "CLOCKWISE": "COUNTERCLOCKWISE");
    }

    @Override
    public void showDrawnCardMessage(Player player, int numberOfDrawnCards) {
        System.out.printf("\n%s got %d CARD from draw pile!\n\n", player.getName(), numberOfDrawnCards);
    }

    @Override
    public void showAiPlayedCardMessage(Player player, Card card) {
        System.out.printf("\n%s has played %s!\n", player.getName(), card.toString());
    }

    @Override
    public void showAiPlayedSaidMau(Player player) {
        System.out.printf("\n%s has said 'MAU'!\n", player.getName());
    }

    @Override
    public void showErrorMessage(String exceptionMessage) {
        System.out.printf("%s\nPlease try again!\n", exceptionMessage);
    }

    @Override
    public void showWinnerMessage(Player player) {
        System.out.printf("CONGRATULATIONS %s, you won!\n", player.getName());
    }

    @Override
    public boolean hasNextRound() {
        System.out.print("\nWould you like to start a new round?\n");
        System.out.println("1: YES\n2: NO");
        return getChosenNumber(1, 2) == 1;
    }

    @Override
    public boolean playerWantsToLoadGame() {
        System.out.println("Would you like to load a previous game?");
        System.out.println("1: YES\n2: NO");
        return getChosenNumber(1, 2) == 1;
    }

    @Override
    public long getGameId() {
        System.out.println("Please enter the game ID to load this game.");
        return getChosenNumber(1, Long.MAX_VALUE);
    }

    private String loadFromFile() {
        String instructions = "";
        InputStream inputStream = this.getClass().getResourceAsStream("/game_instructions.txt");
        try (InputStreamReader in = new InputStreamReader(inputStream);
             BufferedReader buffer = new BufferedReader(in)) {
            instructions = buffer.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            System.out.println("Game instructions could not be loaded.");
        }
        return instructions;
    }

    private String getCardImage(Card card) {
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

    private String getPlayerName() {
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

    private long getChosenNumber(int min, long max) {
        Scanner scanner = new Scanner(System.in);
        long index;
        while (true) {
            try {
                index = Long.parseLong(scanner.next());
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
