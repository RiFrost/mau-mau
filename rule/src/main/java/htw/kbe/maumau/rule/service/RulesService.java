package htw.kbe.maumau.rule.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.player.domain.Player;

public interface RulesService {
    /**
     * Checks if card can be played, also if card was JACK and there is a suit wish
     * @param userCard card user wants to play
     * @param topCard card on top of discardpile
     * @param userWish suit wish after JACK was played
     * @return true if userCard is valid, false if not
     */
    boolean isCardValid(Card userCard, Card topCard, Suit userWish);

    /**
     * Checks if next player needs two draw two, also counts the number of cards that have to be drawn
     * @param drawCardCounter number of cards that need to be drawn
     * @param topCard card on top of discardpile
     * @return number of cards that need to be drawn
     */
    int drawTwoCards(int drawCardCounter, Card topCard);

    /**
     * Checks if next player is suspended for one round
     * @param topCard card on top of discardpile
     * @return true if card is ASS, false if not
     */
    boolean isSuspended(Card topCard);

    /**
     * Checks if last card played was a JACK and player can make a wish
     * @param topCard card on top of discardpile
     * @return true if card was JACK, false if not
     */
    boolean isCardJack(Card topCard);

    /**
     * Checks if card was NINE and gamedirection has to change
     * @param topCard card on top of discardpile
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
