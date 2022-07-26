package htw.kbe.maumau.rule.export;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Label;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.player.export.Player;
import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;

public interface RulesService {

    /**
     * validates if a card can be played, also if card was JACK, SEVEN and if there is a suit wish
     * @param playedCard - card user wants to play
     * @param topCard - card on top of discard pile
     * @param userWish - suit wish after JACK was played
     * @param drawCounter - number of drawn cards
     * @throws PlayedCardIsInvalidException throw exception when card cannot be played
     */
    void validateCard(Card playedCard, Card topCard, Suit userWish, int drawCounter) throws PlayedCardIsInvalidException;

    /**
     * checks if suit or label of played card matches suit or label of top card
     * @param playedCard card to be played
     * @param topCard card on top of discard pile
     * @return true when label or suit matches, false if not
     */
    boolean matchLabelOrSuit(Card playedCard, Card topCard);

    /**
     * checks if player must play a SEVEN (only for normal player)
     * @param drawCounter number of drawn cards
     * @param playedLabel label of the card to be played
     * @param topLabel label of card on top of discard pile
     * @return true if player must play SEVEN, but he didn't, false if not
     */
    boolean hasToPlaySeven(int drawCounter, Label playedLabel, Label topLabel);

    /**
     * checks if player can play a SEVEN (only for AI player)
     * @param playedLabel label of the card to be played
     * @param topCardLabel label of card on top of discard pile
     * @param drawCounter number of drawn cards
     * @return true if player can play SEVEN, false if not
     */
    boolean canPlaySeven(Label playedLabel, Label topCardLabel, int drawCounter);

    /**
     * checks if suit wish matches suit of played card (if suit wish is given)
     * @param userWish suit wish to be played
     * @param playedSuit suit to be played
     * @return true if suit of played card matches suit wish or suit wish is not given, false if not
     */
    boolean isSuitWishValid(Suit userWish, Suit playedSuit);

    /**
     * checks if label of played Card is JACK and label of top card too
     * @param playedLabel label of the card to be played
     * @param topLabel label of card on top of discard pile
     * @return true if label JACK is of played Card matches label of top card, false if not
     */
    boolean isJackOnJack(Label playedLabel, Label topLabel);

    /**
     * Checks if a player needs to draw cards
     * @param topCard card on top of discard pile
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
     * @return true if card was EIGHT, false if not
     */
    boolean changeGameDirection(Card topCard);

    /**
     * Checks if player said 'mau' and if it's valid
     * @param player - player in turn
     * @return false, if players 'mau' is valid, true if it's invalid
     */
    boolean isPlayersMauInvalid(Player player);
}
