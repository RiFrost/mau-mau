package htw.kbe.maumau.player;

import java.util.Arrays;
import java.util.List;

public class Name {

    private static List<String> aiNames = Arrays.asList(
            "Klara",
            "Anne",
            "Claire",
            "Volker",
            "Iris",
            "Thaddäus"
    );

    public static String getRandomName() {
        return aiNames.get(((int) Math.random() * aiNames.size()));
    }

}
