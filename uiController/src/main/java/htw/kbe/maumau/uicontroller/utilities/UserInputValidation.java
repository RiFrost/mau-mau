package htw.kbe.maumau.uicontroller.utilities;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class UserInputValidation {

    // Note: initialize scanner object in every function for better testing opportunities

    public String getPlayerName() {
        String name;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            name = scanner.next();
            if (name.isBlank()) {
                System.out.println("Your name cannot be blank! Please try again!");
            } else if (name.matches(".*[0-9].*")) {
                System.out.println("Your name cannot contain numbers!");
            } else if (name.length() < 3) {
                System.out.println("Your name is too short! Please choose a longer name for you!");
            } else if (name.length() > 15) {
                System.out.println("Your name is too long! Please choose a shorter name for you!");
            } else {
                break;
            }
        }
        return name;
    }

    public int getChosenNumber(int min, int max) {
        Scanner scanner = new Scanner(System.in);
        int index;
        while (true) {
            try {
                index = Integer.parseInt(scanner.next());
                if (index < min || index > max) {
                    System.out.printf("Please choose a number between %d and %d:", min, max);
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("This is not a number. Please try again!");
            }
        }
        return index;
    }


}
