package htw.kbe.maumau.rule.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.player.domain.Player;
import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;

public interface RulesService {

    /**
     * Checks if card can be played, also if card was JACK, SEVEN and if there is a suit wish
     * @param player- player who is in turn
     * @param playedCard - card user wants to play
     * @param topCard - card on top of discard pile
     * @param userWish - suit wish after JACK was played
     * @param drawCounter - number of drawn cards
     * @throws PlayedCardIsInvalidException
     */
    void validateCard(Player player, Card playedCard, Card topCard, Suit userWish, int drawCounter) throws PlayedCardIsInvalidException;

    /**
     * Checks if a player needs to draw cards
     * @param topCard - card on top of discard pile
     * @return true if card has label SEVEN, false when not
     */
    boolean mustDrawCards(Card topCard);

    /**
     * gets default number of drawn cards
     * @return number of cards to be drawn
     */
    int getDefaultNumberOfDrawnCards();

    /**
     * checks if next player is suspended for one round
     * @param topCard - card on top of discard pile
     * @return true if card is ASS, false if not
     */
    boolean mustSuspend(Card topCard);

    /**
     * checks if at least one hand card of the player is a SEVEN and top card is a SEVEN and draw counter is greater
     * or equal default number of drawn cards,
     * decides if a player has to draw cards
     * @param player - player who is in turn
     * @param topCard - card on top of discard pile
     * @param drawCounter - number of drawn cards
     * @return true if player has to draw cards, false if player has not to draw
     */
    boolean mustDrawCards(Player player, Card topCard, int drawCounter);

    /**
     * Checks if last card played was a JACK and player can make a wish
     * @param topCard - card on top of discard pile
     * @return true if card was JACK, false if not
     */
    boolean isCardJack(Card topCard);

    /**
     * Checks if card was NINE and game direction has to change
     * @param topCard - card on top of discard pile
     * @return true if card was NINE, false if not
     */
    boolean changeGameDirection(Card topCard);

    /**
     * Checks if player said 'mau' and if it's valid
     * @param player - player in turn
     * @return false, if players 'mau' is valid, true if it's invalid
     */
    boolean isPlayersMauInvalid(Player player);
}
