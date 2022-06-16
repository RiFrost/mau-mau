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
        logger.error("SAMPLE ERROR MESSAGE FOR PLAYER-SERVICE");
        List<Player> players = new ArrayList();
        for (String name : names) {
           validateName(name, names);
           players.add(new Player(name));
        }
        return players;
    }

    @Override
    public void validateName(String name, List<String> names) throws InvalidPlayerNameException {
        if(Collections.frequency(names, name) >= 2) throw new InvalidPlayerNameException(String.format("The Player %s occurs more than one time", name));

        if(name.length() <= 0 || name.length() > 15 || name.isBlank()) {
            throw new InvalidPlayerNameException("The Player name is invalid!");
        }
    }

    @Override
    public void removePlayedCard(Player player, Card card) {
        player.getHandCards().remove(card);
    }

    @Override
    public void addDrawnCards(Player player, List<Card> cards) {
        player.getHandCards().addAll(cards);
    }

}
