package htw.kbe.maumau.player.service;

import htw.kbe.maumau.card.export.Card;
import htw.kbe.maumau.player.export.Player;
import htw.kbe.maumau.player.exceptions.InvalidPlayerNameException;
import htw.kbe.maumau.player.export.PlayerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static Logger logger = LogManager.getLogger(PlayerServiceImpl.class);

    @Override
    public List<Player> createPlayers(List<String> names) throws InvalidPlayerNameException {
        List<Player> players = new ArrayList();
        for (String name : names) {
            validateName(name, names);
            players.add(new Player(name));
        }
        logger.info("The following players were created: {}", players);
        return players;
    }

    @Override
    public void validateName(String name, List<String> names) throws InvalidPlayerNameException {
        if(Collections.frequency(names, name) >= 2) {
            logger.error("Player's name {} occurs more than one time", name);
            throw new InvalidPlayerNameException(String.format("The Player %s occurs more than one time", name));
        }

        if(name.length() <= 0 || name.length() > 15 || name.isBlank()) {
            logger.error("Player's name is either too long, too short or blank");
            throw new InvalidPlayerNameException("The Player name is invalid!");
        }
    }

    @Override
    public void removePlayedCard(Player player, Card card) {
        player.getHandCards().remove(card);
        logger.info("{} is removed from deck of player {}", card, player.getName());
    }

    @Override
    public void addDrawnCards(Player player, List<Card> cards) {
        player.getHandCards().addAll(cards);
        Collections.sort(player.getHandCards());
        logger.info("{} is added to deck of player {}", cards, player.getName());
    }
}
