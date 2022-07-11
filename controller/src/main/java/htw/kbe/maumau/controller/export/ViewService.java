package htw.kbe.maumau.controller.export;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.game.export.Game;
import htw.kbe.maumau.player.export.Player;

import java.util.List;

public interface ViewService {

        /**
         * shows gaming instructions
         * asks the player for the desired number of players
         * @return number of players
         */
        int getNumberOfPlayer();

        /**
         * asks for player names depending on the number of players
         * @param numberOfPlayer - number of players in the game
         * @return list of player names
         */
        List<String> getPlayerNames(int numberOfPlayer);

        /**
         * lets the player know that the game has started
         */
        void showStartGameMessage();

        /**
         * lets the player know which card is on top of the pile
         * @param topCard - card that is in top of the discard pile
         */
        void showTopCard(Card topCard);

        /**
         * shows the player his hand cards and if there is a suit wish, it is also shown
         * @param player - player who is in turn
         * @param suit   - suit that was requested (optional)
         */
        void showHandCards(Player player, Suit suit);

        /**
         * asks the player which card he wants to discard or if he wants to draw card(s)
         * @param player - player who is in turn
         * @return card  - chosen card that the player wants to play
         */
        Card getPlayedCard(Player player);

        /**
         * asks the player for the suit wish
         * @param player - player who is in turn
         * @param suits  - list of suits to choose from
         * @return desired suit wish
         */
        Suit getChosenSuit(Player player, List<Suit> suits);

        /**
         * asks the player if he wants say 'mau'
         * @param player - player who is in turn
         * @return true if player said 'mau', false if not
         */
        boolean saidMau(Player player);

        /**
         * lets the player know how many cards he has drawn
         * @param player             - player who is in turn
         * @param numberOfDrawnCards - number of cards to be drawn
         */
        void showDrawnCardMessage(Player player, int numberOfDrawnCards);

        /**
         * lets the player know want went wrong in the game
         * @param exceptionMessage - message that was thrown by exception
         */
        void showErrorMessage(String exceptionMessage);

        /**
         * lets players know who has won
         * @param player - player who won
         */
        void showWinnerMessage(Player player);

        /**
         * aks for next round to play
         * @return true if player wants to play again, false if game should quit
         */
        boolean hasNextRound();

        boolean loadGame(Game game);

}
