package htw.kbe.maumau.uicontroller.instructions;

import java.io.*;
import java.util.stream.Collectors;

public class GameInstructionsLoader {

    private static String fileName = "game_instructions.txt";

    public static String loadFromFile() {
        String instructions = "";
        InputStream inputStream = GameInstructionsLoader.class.getClassLoader().getResourceAsStream(fileName);
        try (InputStreamReader in = new InputStreamReader(inputStream);
             BufferedReader buffer = new BufferedReader(in)) {
             instructions = buffer.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            System.out.println("Game instructions could not loaded");
        }
        return instructions;
    }
}
