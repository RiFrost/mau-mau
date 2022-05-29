package htw.kbe.maumau.rule.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.player.domain.Player;
import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;

public interface RulesService {

    /**
     * Checks if card can be played, also if card was JACK and there is a suit wish
     * @param userCard card user wants to play
     * @param topCard card on top of discard pile
     * @param userWish suit wish after JACK was played
     * @throws PlayedCardIsInvalidException when card cannot be played
     */
    void validateCard(Card userCard, Card topCard, Suit userWish) throws PlayedCardIsInvalidException;

    /**
     * Checks if a player needs to draw cards
     * @param topCard card on top of discard pile
     * @return true if card has label SEVEN, false when not
     */
    boolean mustDrawCards(Card topCard);

    /**
     * checks if at least one hand card of the player is a SEVEN and top card is a SEVEN
     * decides if a player has to draw cards
     * @param player
     * @param topCard card on top of discard pile
     * @return true if player has to draw cards, false if player has not to draw
     */
    boolean mustDrawCards(Player player, Card topCard);

    /**
     * gets number of drawn cards
     * @return number of cards to be drawn
     */
    int getNumberOfDrawnCards();

    /**
     * Checks if next player is suspended for one round
     * @param topCard card on top of discard pile
     * @return true if card is ASS, false if not
     */
    boolean mustSuspend(Card topCard);

    /**
     * Checks if last card played was a JACK and player can make a wish
     * @param topCard card on top of discard pile
     * @return true if card was JACK, false if not
     */
    boolean isCardJack(Card topCard);

    /**
     * Checks if card was NINE and game direction has to change
     * @param topCard card on top of discard pile
     * @return true if card was NINE, false if not
     */
    boolean changeGameDirection(Card topCard);

    /**
     * Checks if player said 'mau' and if it's valid
     * @param player player in turn
     * @return false, if players 'mau' is valid, true if it's invalid
     */
    boolean isPlayersMauInvalid(Player player);
}
