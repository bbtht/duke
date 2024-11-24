package ui;

import java.util.*;

/**
 * This Ui.java manages the user interface displayed.
 * This will include print statements and more.
 */

public class Ui {

    public void printMessage(String... messages) {
        printSeparator();
        for (String message : messages) {
            System.out.println(message);
        }
    }

    private void printSeparator() {
        System.out.println("____________________________________________________________________________________________________________________________");
    }

    public void displayWelcomeMessage() {
        String logo = "      \\   |   /      \n"
                + "        .-*-.\n"
                + "     --  | |  --\n"
                + "        `-*-'\n"
                + "      /   |   \\      \n";
        String welcomeMessage = "Hello! I am Sunny, your happy task manager!\n" +
                "What can I do for you today?";
        printMessage(logo + welcomeMessage);
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
                "[help]: display this help message";
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

    public void displayTaskCount(int count) {
        System.out.println("Sunny noted that you currently have " + count + " tasks in your list.");
    }

    public void displayGoodbyeMessage() {
        printMessage("Sunny's heart feels heavy... until we meet again! Goodbye, and take care!");
    }

}
