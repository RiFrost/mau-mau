package htw.kbe.maumau.player.export;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.player.export.Player;
import htw.kbe.maumau.player.exceptions.InvalidPlayerNameException;

import java.util.List;

public interface PlayerService {

    /**
     * initializes new players
     * @param names - list of player names
     * @param aiPlayers - number of virtual players
     * @return list of players
     * @throws InvalidPlayerNameException when the name is empty, blanks only or longer then 15 symbols
     */
    List<Player> createPlayers(List<String> names, int aiPlayers) throws InvalidPlayerNameException;

    /**
     * adds drawn card(s) to hand cards to given player and hand cards are sorted by suit (from black to red)
     * and label (from ASS to SEVEN).
     * @param player - who gets drawn cards
     * @param card - card that was drawn
     */
    void addDrawnCards(Player player, List<Card> card);

    /**
     * removes card that was played from hand card of player
     * @param player - player who played the given card
     * @param card - played card to be removed
     */
    void removePlayedCard(Player player, Card card);

    /**
     * validates name
     * @param name - that has to be validated
     * @param names - list of names that is needed to check if names duplicate
     * @throws InvalidPlayerNameException when String name is empty, has whitespaces, is too long or names duplicate
     */
    void validateName(String name, List<String> names) throws InvalidPlayerNameException;
}
