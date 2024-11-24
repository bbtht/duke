import tasklist.*;
import storage.Storage;
import ui.Ui;

import java.util.*;

public class Sunny {
    private static TaskList taskList;
    private static final Storage storage = new Storage("data/sunny.txt");
    private static Ui ui;

    public enum TaskType {
        TODO,
        DEADLINE,
        EVENT;
    }

    public static void main(String[] args) {
        ui = new ui.Ui();

        taskList = new TaskList(); // initialize TaskList and load tasks from storage

        // displaying welcome message
        ui.displayWelcomeMessage();

        // print
        try {
            List<Task> loadedTasks = storage.loadTasks();
            taskList.getTasks().addAll(loadedTasks);
            if (!loadedTasks.isEmpty()) {
                ui.printMessage("Additional Note: You have existing tasks loaded in storage from the previous session. Input 'list' to view them\n" +
                        "or you can input 'help' to view the full list of available commands!");
            }
        } catch (Exception e) {
            ui.displayErrorMessage("loading tasks from storage " + e.getMessage());
        }

        // Scanner for user input
        Scanner in = new Scanner(System.in);
        String input;

        // Main input loop
        while (true) {
            input = in.nextLine();

            // Handle "bye" command
            if (input.equalsIgnoreCase("bye")||input.equalsIgnoreCase("exit")||input.equalsIgnoreCase("end")||input.equalsIgnoreCase("quit")) {
                ui.displayGoodbyeMessage();
                break;
            }

            // Process other commands
            processInput(input);
        }
        in.close();
    }

    private static void processInput(String input) {
        if (input.equalsIgnoreCase("list")) {
            listTasks();
        } else if (input.equalsIgnoreCase("help")) {
            ui.displayAvailableCommands();
        } else if (input.startsWith("mark")) {
            markTask(input);
        } else if (input.startsWith("unmark")) {
            unmarkTask(input);
        } else if (input.startsWith("delete")) {
            deleteTask(input);
        } else {
            addTask(input);
        }
    }

    private static void listTasks() {
        if (taskList.getTasks().isEmpty()) {
            ui.printMessage("You have no tasks in your list.");
            return;
        }

        StringBuilder taskListString = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < taskList.getTasks().size(); i++) {
            taskListString.append(i + 1).append(". ").append(taskList.getTasks().get(i)).append("\n");
        }
        ui.printMessage(taskListString.toString());
    }


    private static void addTask(String input) {
        String[] taskParts = input.split(" ", 2);
        String command = taskParts[0].toLowerCase();

        try {
            TaskType taskType = TaskType.valueOf(command.toUpperCase());
            switch (taskType) {
                case TODO:
                    addTodoTask(taskParts);
                    break;
                case DEADLINE:
                    addDeadlineTask(taskParts);
                    break;
                case EVENT:
                    addEventTask(taskParts);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            ui.displayErrorMessage("Please provide a valid task type (e.g., todo, deadline, or event).");
        }
    }

    private static void addTodoTask(String[] taskParts) {
        if (taskParts.length < 2 || taskParts[1].trim().isEmpty()) {
            ui.displayErrorMessage("The description of a todo task cannot be empty.");
            return;
        }
        Todo todo = new Todo(taskParts[1].trim());
        taskList.addTask(todo);
        ui.displaySuccessMessage("added " + todo);
        ui.displayTaskCount(taskList.getTasks().size());
        saveTasks();
    }

    private static void addDeadlineTask(String[] taskParts) {
        if (taskParts.length < 2 || taskParts[1].trim().isEmpty()) {
            ui.displayErrorMessage("The description of a deadline task cannot be empty.");
            return;
        }

        String[] deadlineParts = taskParts[1].split("/by", 2);
        if (deadlineParts.length < 2 || deadlineParts[1].trim().isEmpty()) {
            ui.displayErrorMessage("Please provide a due date (e.g., /by Monday).");
            return;
        }

        Deadline deadline = new Deadline(deadlineParts[0].trim(), deadlineParts[1].trim());
        taskList.addTask(deadline);
        ui.displaySuccessMessage("added " + deadline);
        saveTasks();
    }

    private static void addEventTask(String[] taskParts) {
        if (taskParts.length < 2 || taskParts[1].trim().isEmpty()) {
            ui.displayErrorMessage("The description of an event task cannot be empty.");
            return;
        }

        String input = taskParts[1];
        String[] eventParts = input.split("/from|/to", 3);
        if (eventParts.length < 3 || eventParts[1].trim().isEmpty() || eventParts[2].trim().isEmpty()) {
            ui.displayErrorMessage("Please provide both start and end times (e.g., /from Mon 2pm /to 4pm).");
            return;
        }

        Event event = new Event(eventParts[0].trim(), eventParts[1].trim(), eventParts[2].trim());
        taskList.addTask(event);
        ui.displaySuccessMessage("added " + event);
        ui.displayTaskCount(taskList.getTasks().size());
        saveTasks();
    }

    private static void markTask(String input) {
        updateTaskStatus(input, true);
    }

    private static void unmarkTask(String input) {
        updateTaskStatus(input, false);
    }

    private static void updateTaskStatus(String input, boolean markAsDone) {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            ui.displayErrorMessage("Please provide a task number.");
            return;
        }

        try {
            int taskIndex = Integer.parseInt(parts[1]) - 1;
            Task task = taskList.getTasks().get(taskIndex);
            if (markAsDone) {
                if (task.isDone()) {
                    ui.displayErrorMessage("This task is already marked as done - " + task);
                } else {
                    task.markAsDone();
                    ui.displaySuccessMessage("marked this task as done: " + task);
                }
            } else {
                if (!task.isDone()) {
                    ui.displayErrorMessage("This task is already marked as not done - " + task);
                } else {
                    task.markAsNotDone();
                    ui.displaySuccessMessage("marked this task as not done: " + task);
                }
            }
            saveTasks();
        } catch (Exception e) {
            ui.displayErrorMessage("Invalid task number.");
        }
    }

    private static void deleteTask(String input) {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            ui.displayErrorMessage("Please provide a task number to delete.");
            return;
        }

        try {
            int taskIndex = Integer.parseInt(parts[1]) - 1;
            Task removedTask = taskList.removeTask(taskIndex);
            ui.displaySuccessMessage("removed " + removedTask);
            ui.displayTaskCount(taskList.getTasks().size());
            saveTasks();
        } catch (Exception e) {
            ui.displayErrorMessage("Invalid task number.");
        }
    }

    private static void saveTasks() {
        try {
            // ui.printMessage("Starting to save tasks...");
            if (taskList.getTasks().isEmpty()) {
                ui.printMessage("No tasks to save.");
            } else {
                // ui.printMessage("Saving " + taskList.getTasks().size() + " tasks...");
            }
            taskList.saveTasksToFile("data/sunny.txt");  // Ensure the path is correct
            // ui.printMessage("Tasks successfully saved to file.");
        } catch (Exception e) {
            ui.displayErrorMessage(" " + e.getMessage());
        }
    }
}
