package htw.kbe.maumau.player.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.player.domain.Player;
import htw.kbe.maumau.player.exceptions.InvalidPlayerNameException;

import java.util.List;

public interface PlayerService {

    /**
     * initialise player
     * @param names - List of the playername
     * @return player who is ready to play
     * @throws InvalidPlayerNameException when the name is empty, blanks only or longer then 15 symbols
     */
    List<Player> createPlayers(List<String> names) throws InvalidPlayerNameException;

    /**
     * player who has to draw cards
     * @param player active player
     * @param card card that has to drawn
     * @return player with his drawn cards
     */
    void drawCards(Player player, List<Card> card);

    /**
     * card that is played by a player and will be discarded
     * @param player active player
     * @param card card which will be discarded
     * @return card that is played
     */
    void playCard(Player player, Card card);

    /**
     * validate name
     * @param name that has to be validated
     * @throws InvalidPlayerNameException when String name is empty, has whitespaces or is too long
     */
    void validateName(String name, List<String> names) throws InvalidPlayerNameException;

}
