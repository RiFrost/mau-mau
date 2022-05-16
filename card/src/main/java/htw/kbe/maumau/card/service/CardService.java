package htw.kbe.maumau.card.service;

import htw.kbe.maumau.card.domain.Label;
import htw.kbe.maumau.card.domain.Suit;
import java.util.List;

public interface CardService {

    List<Suit> getSuits();
    List<Label> getLabels();

}
