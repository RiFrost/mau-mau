package htw.kbe.maumau.game.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.card.service.CardService;
import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;
import htw.kbe.maumau.deck.service.DeckService;
import htw.kbe.maumau.game.domain.Game;
import htw.kbe.maumau.game.exceptions.InvalidPlayerSizeException;
import htw.kbe.maumau.player.domain.Player;
import htw.kbe.maumau.player.service.PlayerService;
import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;
import htw.kbe.maumau.rule.service.RulesService;

import java.util.List;

public interface GameService {

    /**
     * initializes a game with the desired number of players and the needed card deck
     * @param players list of players participating in the game
     * @return new game
     * @throws IllegalDeckSizeException when deck has not the right size of cards
     * @throws InvalidPlayerSizeException when player list size is above four or below two
     */
    Game startNewGame(List<Player> players) throws IllegalDeckSizeException, InvalidPlayerSizeException;

    /**
     * return the player for the next round, noting whether the round is played clockwise or counterclockwise.
     * @param game
     * @return player whose turn it is in the next round
     */
    Player getNextPlayer(Game game);

    /**
     * at the start of the game, each player is dealt their hand cards
     * @param game
     */
    void initialCardDealing(Game game);

    /**
     * as many cards are drawn from the draw pile as are indicated and then the cards are dealt to the player
     * @param amount number of cards the player must draw
     * @param game
     */
    void drawCards(int amount, Game game);

    /**
     * Checks whether the player may play a card in this round. He may not if he has to draw cards
     * (e.g. because a seven is on top and the player has no seven in his hand)
     * @param game
     * @return true when player can play a card, false when player has to draw cards instead
     */
    boolean canPlayerPlayCards(Game game);

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
     * @param card card that wants to be played
     * @param game
     * @throws PlayedCardIsInvalidException when the played card is not valid for that round
     */
    void validateCard(Card card, Game game) throws PlayedCardIsInvalidException;

    /**
     * set a suit wish of a player into the game and set askForSuitWish state to false
     * @param userWish suit wish of the player
     * @param game
     */
    void setPlayersSuitWish(Suit userWish, Game game);

    void setCardService(CardService cardService);

    void setDeckService(DeckService deckService);

    void setRulesService(RulesService rulesService);

    void setPlayerService(PlayerService playerService);
}
