package htw.kbe.maumau.controller.service;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.controller.utilities.UtilitiesHelper;
import htw.kbe.maumau.player.export.Player;
import htw.kbe.maumau.controller.export.ViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ViewServiceImpl implements ViewService {

    @Autowired
    private UtilitiesHelper utilitiesHelper;

    @Override
    public int getNumberOfPlayer() {
        System.out.println("Welcome to M(i)au M(i)au!\n\n");
        System.out.println(utilitiesHelper.loadFromFile());
        System.out.println("\n\nHow many players will take part? Please choose a number from 2 to 4.");
        int number = utilitiesHelper.getChosenNumber(2, 4);
        System.out.printf("The game will start with %d players!\n", number);
        return number;
    }

    @Override
    public List<String> getPlayerNames(int numberOfPlayer) {
        List<String> playerNames = new ArrayList<>();
        int playerNo = 1;
        for (int i = 0; i < numberOfPlayer; i++) {
            System.out.printf("Player %d, please type in your name:", playerNo++);
            String playerName = utilitiesHelper.getPlayerName();
            playerNames.add(playerName);
        }
        return playerNames;
    }

    @Override
    public void showStartGameMessage() {
        System.out.println("\nLet's begin!\n");
    }

    @Override
    public void showTopCard(Card topCard) {
        System.out.printf("The top card is %s\n", topCard);
        System.out.println(utilitiesHelper.getCardImage(topCard));
    }

    @Override
    public void showHandCards(Player player, Suit suit) {
        int numberOfCard = 1;

        if(Objects.nonNull(suit)) {
            System.out.printf("\nA suit wish is given. Please choose a card of %s\n", suit);
        }

        System.out.printf("\n%s, here are your hand cards:", player.getName());
        for (Card card: player.getHandCards()) {
            System.out.printf("\n%d: %s\n", numberOfCard++, card);
            System.out.println(utilitiesHelper.getCardImage(card));
        }
    }

    @Override
    public Map<Card, Boolean> getPlayedCard(Player player) {
        Map<Card, Boolean> cardAndMau = new HashMap<>();
        System.out.printf("%s, please choose a card:\n", player.getName());
        int number = utilitiesHelper.getChosenNumber(1, player.getHandCards().size());

        boolean saidMau = saidMau(player);
        cardAndMau.put(player.getHandCards().get(number - 1), saidMau);

        return cardAndMau;
    }

    @Override
    public Suit getChosenSuit(Player player, List<Suit> suits) {
        System.out.printf("%s, please choose a suit:\n", player.getName());
        int numberOfSuit = 1;
        for (Suit suit: suits){
            System.out.printf("%d: %s\n", numberOfSuit++, suit);
        }
        int number = utilitiesHelper.getChosenNumber(1,suits.size());
        return suits.get(number - 1);
    }

    @Override
    public boolean saidMau(Player player) {
        System.out.printf("%s do you want to say 'mau'?\n", player.getName());
        System.out.println("1: YES\n2: NO");
        int saidMau = utilitiesHelper.getChosenNumber(1, 2);
        return saidMau == 1;
    }

    @Override
    public void showDrawnCardMessage(Player player, int numberOfDrawnCards) {
        System.out.printf("\n%s got %d CARD from draw pile!\n", player.getName(), numberOfDrawnCards);
    }

    @Override
    public boolean playerWantToDrawCards() {
        System.out.println("What do you want to do?\n1: PLAY\n2: DRAW");
        int number = utilitiesHelper.getChosenNumber(1, 2);
        return number == 2;
    }

    @Override
    public void showValidationFailedMessage(String exceptionMessage) {
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
        int number = utilitiesHelper.getChosenNumber(1, 2);
        return number == 1;
    }
}
