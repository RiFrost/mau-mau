package htw.kbe.maumau.game.export;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.card.export.Suit;
import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;
import htw.kbe.maumau.game.exceptions.DaoException;
import htw.kbe.maumau.game.exceptions.InvalidPlayerSizeException;
import htw.kbe.maumau.player.export.Player;
import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;

import java.util.List;

public interface GameService {

    /**
     * initializes a game with the desired number of players and the needed card deck
     * @param players - list of players participating in the game
     * @return new game
     * @throws IllegalDeckSizeException when deck has not the right size of cards
     * @throws InvalidPlayerSizeException when player list size is above four or below two
     */
    Game createGame(List<Player> players) throws IllegalDeckSizeException, InvalidPlayerSizeException;

    /**
     * sets the player for the next round, noting whether the round is played clockwise or counterclockwise.
     * @param game
     */
    void switchToNextPlayer(Game game);

    /**
     * at the start of the game, each player is dealt their hand cards
     * @param game
     */
    void initialCardDealing(Game game);

    /**
     * as many cards are drawn from the draw pile as are indicated and then the cards are dealt to the player
     * @param numberOfDrawnCards - number of cards the player must draw
     * @param game
     */
    void giveDrawnCardsToPlayer(int numberOfDrawnCards, Game game);

    /**
     * Checks whether the player may play a card in this round. He may not if he has to draw cards
     * (e.g. because a seven is on top and the player has no seven in his hand)
     * @param game
     * @return true when player can play a card, false when player has to draw cards instead
     */
    boolean mustPlayerDrawCards(Game game);

    /**
     * checks if the played card match a card rule
     * belonging to the card rule further methods will be executed
     * @param game
     */
    void applyCardRule(Game game);

    /**
     * validates the card to be played
     * if the card is valid, it is added to the discard pile
     * if a suit wish was played and valid then removes suit wish from game
     * @param card - card that wants to be played
     * @param game
     * @throws PlayedCardIsInvalidException when the played card is not valid for that round
     */
    void validateCard(Card card, Game game) throws PlayedCardIsInvalidException;

    /**
     * set a suit wish of a player into the game and set askForSuitWish state to false
     * @param userWish - suit wish of the player
     * @param game
     */
    void setPlayersSuitWish(Suit userWish, Game game);

    /**
     * calls the game as won when the active player has no more hand cards
     * @param game
     * @return true, if active player has no hand cards, false if not
     */
    boolean isGameOver(Game game);

    /**
     * set 'mau' state to false of active player
     * @param game
     */
    void resetPlayersMau(Game game);

    void saveGame(Game game) throws DaoException;

    void deleteGame(Game game) throws DaoException;

    boolean hasGame() throws DaoException;

    Game getGame(long id) throws DaoException;
}
