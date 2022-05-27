package htw.kbe.maumau.rule.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.player.domain.Player;
import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;

public interface RulesService {
    int penaltyCards = 2;

    /**
     * Checks if card can be played, also if card was JACK and there is a suit wish
     * @param userCard card user wants to play
     * @param topCard card on top of discard pile
     * @param userWish suit wish after JACK was played
     */
    void validateCard(Card userCard, Card topCard, Suit userWish) throws PlayedCardIsInvalidException;

    /**
     * Checks if next player needs two draw two, also counts the number of cards that have to be drawn
     * @param topCard card on top of discard pile
     * @return true if
     */
    boolean drawTwoCards(Card topCard);

    /**
     * Checks if next player is suspended for one round
     * @param topCard card on top of discard pile
     * @return true if card is ASS, false if not
     */
    boolean isSuspended(Card topCard);

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
     * Checks if player said Mau
     * @param player player in turn
     * @return true if player.isHasSaidMau() true, false if not
     */
    boolean isPlayersMauValid(Player player);

}
