package htw.kbe.maumau.card.service;

import htw.kbe.maumau.card.domain.Label;
import htw.kbe.maumau.card.domain.Suit;
import java.util.Arrays;
import java.util.List;

public class CardServiceImpl implements CardService {
    @Override
    public List<Suit> getSuits() {
        return Arrays.asList(Suit.values());
    }

    @Override
    public List<Label> getLabels() {
        return Arrays.asList(Label.values());
    }
}
