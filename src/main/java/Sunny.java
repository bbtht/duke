import tasklist.*;
import storage.Storage;

import java.util.Scanner;

public class Sunny {
    private static TaskList taskList;
    private static final Storage storage = new Storage("data/sunny.txt");

    // Prints a separator line
    private static void printSeparator() {
        System.out.println("____________________________________________________________________________________________________________________________");
    }

    // Prints messages with separators
    private static void printMessage(String... messages) {
        printSeparator();
        for (String message : messages) {
            System.out.println(message);
        }
        printSeparator();
    }

    public enum TaskType {
        TODO,
        DEADLINE,
        EVENT;
    }


    public static void main(String[] args) {
        // Initialize TaskList and load tasks from storage
        taskList = new TaskList();
        try {
            taskList.getTasks().addAll(storage.loadTasks());
            printMessage("Tasks successfully loaded from storage!");
        } catch (Exception e) {
            printMessage("Error loading tasks from storage: " + e.getMessage(), "Starting with an empty task list.");
        }

        // Display welcome message
        String welcomeMessage = "Hello! I'm Sunny\n" +
                "What can I do for you?\n" +
                "Available Commands:\n" +
                "[list]: list all the tasks\n" +
                "[todo <description>]: add a todo task\n" +
                "[deadline <description> /by <date>]: add a deadline task\n" +
                "[event <description> /from <time> /to <time>]: add an event task\n" +
                "[mark <taskNumber>]: mark task number as done\n" +
                "[unmark <taskNumber>]: mark task number as not done\n" +
                "[delete <taskNumber>]: delete a task\n" +
                "[bye]: end the session";
        printMessage(welcomeMessage);

        // Scanner for user input
        Scanner in = new Scanner(System.in);
        String input;

        // Main input loop
        while (true) {
            input = in.nextLine();

            // Handle "bye" command
            if (input.equalsIgnoreCase("bye")) {
                printMessage("Bye. Hope to see you again soon!");
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
            printMessage("You have no tasks in your list.");
            return;
        }

        StringBuilder taskListString = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < taskList.getTasks().size(); i++) {
            taskListString.append(i + 1).append(". ").append(taskList.getTasks().get(i)).append("\n");
        }
        printMessage(taskListString.toString());
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
            printMessage("Error: Please provide a valid task type (e.g., todo, deadline, or event).");
        }
    }

    private static void addTodoTask(String[] taskParts) {
        if (taskParts.length < 2 || taskParts[1].trim().isEmpty()) {
            printMessage("Error: The description of a todo task cannot be empty.");
            return;
        }
        Todo todo = new Todo(taskParts[1].trim());
        taskList.addTask(todo);
        printMessage("Added: " + todo);
        saveTasks();
    }

    private static void addDeadlineTask(String[] taskParts) {
        if (taskParts.length < 2 || taskParts[1].trim().isEmpty()) {
            printMessage("Error: The description of a deadline task cannot be empty.");
            return;
        }

        String[] deadlineParts = taskParts[1].split("/by", 2);
        if (deadlineParts.length < 2 || deadlineParts[1].trim().isEmpty()) {
            printMessage("Error: Please provide a due date (e.g., /by Monday).");
            return;
        }

        Deadline deadline = new Deadline(deadlineParts[0].trim(), deadlineParts[1].trim());
        taskList.addTask(deadline);
        printMessage("Added: " + deadline);
        saveTasks();
    }

    private static void addEventTask(String[] taskParts) {
        if (taskParts.length < 2 || taskParts[1].trim().isEmpty()) {
            printMessage("Error: The description of an event task cannot be empty.");
            return;
        }

        String input = taskParts[1];
        String[] eventParts = input.split("/from|/to", 3);
        if (eventParts.length < 3 || eventParts[1].trim().isEmpty() || eventParts[2].trim().isEmpty()) {
            printMessage("Error: Please provide both start and end times (e.g., /from Mon 2pm /to 4pm).");
            return;
        }

        Event event = new Event(eventParts[0].trim(), eventParts[1].trim(), eventParts[2].trim());
        taskList.addTask(event);
        printMessage("Added: " + event);
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
            printMessage("Error: Please provide a task number.");
            return;
        }

        try {
            int taskIndex = Integer.parseInt(parts[1]) - 1;
            Task task = taskList.getTasks().get(taskIndex);
            if (markAsDone) {
                task.markAsDone();
                printMessage("Marked as done: " + task);
            } else {
                task.markAsNotDone();
                printMessage("Marked as not done: " + task);
            }
            saveTasks();
        } catch (Exception e) {
            printMessage("Error: Invalid task number.");
        }
    }

    private static void deleteTask(String input) {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            printMessage("Error: Please provide a task number to delete.");
            return;
        }

        try {
            int taskIndex = Integer.parseInt(parts[1]) - 1;
            Task removedTask = taskList.removeTask(taskIndex);
            printMessage("Removed: " + removedTask, "Now you have " + taskList.getTasks().size() + " tasks in the list.");
            saveTasks();
        } catch (Exception e) {
            printMessage("Error: Invalid task number.");
        }
    }

    private static void saveTasks() {
        try {
            printMessage("Starting to save tasks...");
            if (taskList.getTasks().isEmpty()) {
                printMessage("No tasks to save.");
            } else {
                printMessage("Saving " + taskList.getTasks().size() + " tasks...");
            }
            taskList.saveTasksToFile("data/sunny.txt");  // Ensure the path is correct
            printMessage("Tasks successfully saved to file.");
        } catch (Exception e) {
            printMessage("Error saving tasks: " + e.getMessage());
        }
    }
}
