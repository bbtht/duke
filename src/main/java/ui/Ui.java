package ui;

import java.util.*;

/**
 * This Ui.java manages the user interface displayed.
 * This will include print statements and more.
 */

public class Ui {

    public void printMessage(String... messages) {
        for (String message : messages) {
            System.out.println(message);
        }
        printSeparator();
    }

    private void printSeparator() {
        System.out.println("____________________________________________________________________________________________________________________________");
    }

    private static final String[] GREETINGS = {
            "Hey there! Ready to tackle your tasks with Sunny today? 😊",
            "Hello, Sunny here to help! Let's get started with managing your tasks! 💪",
            "Hi! Time to get things done with Sunny! 🚀"
    };

    public void displayWelcomeMessage() {
        printSeparator();
        String logo = "      \\   |   /      \n"
                + "        .-*-.\n"
                + "     --  | |  --\n"
                + "        `-*-'\n"
                + "      /   |   \\      \n";
        Random random = new Random();
        int index = random.nextInt(GREETINGS.length);
        printMessage(logo + GREETINGS[index]);
    }

    public void displayAvailableCommands() {
        String availableCommands = "Available Commands:\n" +
                "[list]: list all the tasks\n" +
                "[todo <description>]: add a todo task\n" +
                "[deadline <description> /by <date>]: add a deadline task\n" +
                "[event <description> /from <time> /to <time>]: add an event task\n" +
                "[mark <taskNumber>]: mark task number as done\n" +
                "[unmark <taskNumber>]: mark task number as not done\n" +
                "[delete <taskNumber>]: delete a task\n" +
                "[bye/exit/end/quit]: end the session\n" +
                "[help]: display this help message\n" +
                "[find <keyword>]: find keyword related to task ";
        printMessage(availableCommands);
    }

    public void displayErrorMessage(String errorMessage) {
        printMessage("Sunny detects error: " + errorMessage);
    }

    public void displaySuccessMessage(String successMessage) {
        printMessage("Yay! Sunny successfully " + successMessage);
    }

    public void promptForInput(String prompt) {
        System.out.print(prompt);
    }

    public String displayTaskCount(int count) {
        return "Now you have " + count + (count == 1 ? " task" : " tasks") + " in the list.";
    }

    // List of randomized goodbye messages
    private static final String[] GOODBYE_MESSAGES = {
            "Goodbye! Sunny says: Stay productive and keep shining! 🌟",
            "Until next time! Remember: Every day is a chance to be amazing! 💪",
            "Take care! Sunny will be here when you're ready to conquer more tasks! 🌞",
            "Bye for now! Keep up the great work! 💼💡",
            "See you later! Keep chasing your goals, you’re doing great! 🏃‍♂️🏅"
    };

    public void displayGoodbyeMessage() {
        Random random = new Random();
        int index = random.nextInt(GOODBYE_MESSAGES.length);
        printMessage(GOODBYE_MESSAGES[index]);
    }

}
