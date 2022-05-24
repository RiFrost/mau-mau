package htw.kbe.maumau.player.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.player.domain.Player;
import htw.kbe.maumau.player.exceptions.IllegalPlayerSizeException;

import java.util.List;

public interface PlayerService {

    /**
     * initialise player
     * @param id - Player who wants to join the game
     * @param name - Name of the player
     * @return player who is ready to play
     * @throws IllegalPlayerSizeException when the team is already full (size of max. 4 Players)
     */
    Player createNewPlayer(Long id, String name) throws IllegalPlayerSizeException;

    /**
     * cards that are drawn by a player
     * @param player active player
     * @param cards cards that are drawn
     * @return list of cards
     */
    Player drawCards(Player player, List<Card> cards);

    /**
     * card that is played by a player and will be discarded
     * @param player active player
     * @param card card which will be discarded
     * @return card that is played
     */
    Card playCard(Player player, Card card);

    /**
     * a message by a player, who only has one card left in hand
     * @param player active player
     * @return message "Maumau"
     */
    Player sayMauMau(Player player);
}
