package htw.kbe.maumau.game.service;

import htw.kbe.maumau.card.domain.Card;
import htw.kbe.maumau.card.domain.Suit;
import htw.kbe.maumau.card.service.CardService;
import htw.kbe.maumau.deck.exceptions.IllegalDeckSizeException;
import htw.kbe.maumau.deck.service.DeckService;
import htw.kbe.maumau.game.domain.Game;
import htw.kbe.maumau.game.exceptions.InvalidPlayerSizeException;
import htw.kbe.maumau.player.domain.Player;
import htw.kbe.maumau.rule.exceptions.PlayedCardIsInvalidException;
import htw.kbe.maumau.rule.service.RulesService;

import java.util.List;

public interface GameService {

    /**
     *
     * @param players
     * @return
     * @throws IllegalDeckSizeException
     * @throws InvalidPlayerSizeException
     */
    Game startNewGame(List<Player> players) throws IllegalDeckSizeException, InvalidPlayerSizeException;

    /**
     *
     * @param game
     * @return
     */
    Player getNextPlayer(Game game);

    /**
     *
     * @param game
     */
    void dealCards(Game game);

    /**
     *
     * @param amount
     * @param game
     */
    void drawCards(int amount, Game game);

    /**
     *
     * @param game
     * @return
     */
    boolean canPlayerPlayCards(Game game);

    /**
     *
     * @param game
     */
    void applyCardRule(Game game);

    /**
     *
     * @param card
     * @param game
     * @throws PlayedCardIsInvalidException
     */
    void validateCard(Card card, Game game) throws PlayedCardIsInvalidException;

    /**
     *
     * @param userWish
     * @param game
     */
    void setUserWish(Suit userWish, Game game);

    /**
     *
     * @param cardService
     */
    void setCardService(CardService cardService);

    /**
     *
     * @param deckService
     */
    void setDeckService(DeckService deckService);

    /**
     *
     * @param rulesService
     */
    void setRulesService(RulesService rulesService);
}
