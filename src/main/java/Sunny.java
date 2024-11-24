import parser.Parser;
import storage.Storage;
import tasklist.*;
import ui.Ui;

import java.util.List;
import java.util.Scanner;

/**
 * This Sunny.java is the main class for the Sunny Task Management System.
 * to help user manage different types of task such as their to do task,
 * deadline task and events that they may have.
 * Provides a friendly interface to manage tasks such as to-dos, deadlines, and events.
 * */

public class Sunny {
    private static TaskList taskList;
    private static final Storage storage = new Storage("data/sunny.txt");
    private static Ui ui;

    public static void main(String[] args) {
        ui = new ui.Ui();
        taskList = new TaskList();
        Parser parser = new Parser(taskList, ui);

        ui.displayWelcomeMessage();

        // this portion is used to load the saved task list on hard disk
        try {
            List<Task> loadedTasks = storage.loadTasks();
            taskList.getTasks().addAll(loadedTasks);
            // if there are any tasks saved, let the user know to use the "list" input to view available command
            if (!loadedTasks.isEmpty()) {
                ui.printMessage("Great! Sunny can see that you got some tasks already loaded. Type 'list' to check them out. \uD83D\uDC40");
            }
        } catch (Exception e) {
            ui.displayErrorMessage("Something went wrong while loading tasks from storage. Please try again later. \uD83D\uDE15");
        }

        Scanner in = new Scanner(System.in);
        while (true) {
            String input = in.nextLine();
            if (input.equalsIgnoreCase("bye")||input.equalsIgnoreCase("exit")||input.equalsIgnoreCase("end")||input.equalsIgnoreCase("quit")) {
                ui.displayGoodbyeMessage();
                break;
            }
            parser.parseCommand(input);
        }
        in.close();
    }
}