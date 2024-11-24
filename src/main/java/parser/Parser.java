package parser;

import tasklist.*;
import storage.Storage;
import ui.Ui;
import java.time.LocalDateTime;
import java.time.format.*;

/**
 * This Parser.java is the parser which will take the user input,
 * to read and execute the necessary commands
 * */

public class Parser {
    private final TaskList taskList;
    private final Ui ui;
    private final Storage storage;

    public Parser(TaskList taskList, Ui ui) {
        this.taskList = taskList;
        this.ui = ui;
        this.storage = new Storage("data/sunny.txt");
    }

    public void parseCommand(String input) {
        if (input.equalsIgnoreCase("list")) {
            listTasks();
        } else if (input.equalsIgnoreCase("help")) {
            ui.displayAvailableCommands();
        } else if (input.startsWith("mark")) {
            updateTaskStatus(input, true);
        } else if (input.startsWith("unmark")) {
            updateTaskStatus(input, false);
        } else if (input.startsWith("delete")) {
            deleteTask(input);
        } else {
            addTask(input);
        }
    }

    private void listTasks() {
        if (taskList.getTasks().isEmpty()) {
            ui.printMessage("Uh-oh, you don't have any tasks in your list yet. Add some tasks and get started!\uD83E\uDD14");
            return;
        }

        StringBuilder taskListString = new StringBuilder("Here's a list of your tasks, all set to go!\uD83D\uDCDD \n");
        for (int i = 0; i < taskList.getTasks().size(); i++) {
            taskListString.append(i + 1).append(". ").append(taskList.getTasks().get(i)).append("\n");
        }
        ui.printMessage(taskListString.toString());
    }

    private void addTask(String input) {
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
            ui.displayErrorMessage("I didn’t quite catch that. Make sure to use a valid task type (e.g., todo, deadline, or event).");
        }
        saveTasks();
    }

    private void addTodoTask(String[] taskParts) {
        if (taskParts.length < 2 || taskParts[1].trim().isEmpty()) {
            ui.displayErrorMessage("Hey, you need to give a description for your todo task. Can’t leave that blank! \uD83D\uDE05");
            return;
        }
        Todo todo = new Todo(taskParts[1].trim());
        boolean isAdded = taskList.addTask(todo);
        if (isAdded) {
            ui.displaySuccessMessage("added a new Todo: ✅\n Todo Task: " + todo + "\n" + ui.displayTaskCount(taskList.getTasks().size()));
        }
    }

    private void addDeadlineTask(String[] taskParts) {
        if (taskParts.length < 2 || taskParts[1].trim().isEmpty()) {
            ui.displayErrorMessage("Oops, you didn’t give a description for your deadline task. Please provide one.");
            return;
        }

        String[] deadlineParts = taskParts[1].split("/by", 2);
        if (deadlineParts.length < 2 || deadlineParts[1].trim().isEmpty()) {
            ui.displayErrorMessage("Looks like you forgot to mention the due date! Try something like '/by 2024-12-01 15:00'.");
            return;
        }

        String description = deadlineParts[0].trim();
        String dateTimeString = deadlineParts[1].trim();

        try {
            // define the expected date-time format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime deadlineDateTime = LocalDateTime.parse(dateTimeString, formatter);
            Deadline deadline = new Deadline(description, deadlineDateTime);
            boolean isAdded = taskList.addTask(deadline);
            if (isAdded) {
                ui.displaySuccessMessage("added a new Deadline: ✅\n Deadline Task: " + deadline + "\n" + ui.displayTaskCount(taskList.getTasks().size()));
            }
        } catch (DateTimeParseException e) {
            ui.displayErrorMessage("Invalid date and time format! \uD83D\uDCC5 \uD83D\uDD70\uFE0F Please use 'yyyy-MM-dd HH:mm', e.g., '/by 2024-12-01 15:00'.");
        }
    }

    private void addEventTask(String[] taskParts) {
        if (taskParts.length < 2 || taskParts[1].trim().isEmpty()) {
            ui.displayErrorMessage("Hmm... it looks like you missed the description for your event. Please provide one.");
            return;
        }

        String input = taskParts[1];
        String[] eventParts = input.split("/from|/to", 3);
        if (eventParts.length < 3 || eventParts[1].trim().isEmpty() || eventParts[2].trim().isEmpty()) {
            ui.displayErrorMessage("Don’t forget to provide both the start and end times! Try something like `/from Mon 2pm /to 4pm`.");
            return;
        }

        Event event = new Event(eventParts[0].trim(), eventParts[1].trim(), eventParts[2].trim());
        boolean isAdded = taskList.addTask(event);
        if (isAdded) {
            ui.displaySuccessMessage("added an Event: ✅\n Event Task: " + event + "\n" + ui.displayTaskCount(taskList.getTasks().size()));
        }
    }

    private void updateTaskStatus(String input, boolean markAsDone) {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            ui.displayErrorMessage("Hmm, I think you forgot to mention which task to mark. Try again with a valid task number. \uD83D\uDD22");
            return;
        }

        try {
            int taskIndex = Integer.parseInt(parts[1]) - 1;
            Task task = taskList.getTasks().get(taskIndex);
            if (markAsDone) {
                if (task.isDone()) {
                    ui.displayErrorMessage("This task is already done! You’ve crossed it off already! ✨\n Task: " + task);
                } else {
                    task.markAsDone();
                    ui.displaySuccessMessage("marked this task as done  ✅\n Task: " + task);
                }
            } else {
                if (!task.isDone()) {
                    ui.displayErrorMessage("This task is already marked as not done. Keep going! 💪\n Task: " + task);
                } else {
                    task.markAsNotDone();
                    ui.displaySuccessMessage("task marked as not done, let's complete it soon! 💪\n Task: " + task);
                }
            }
            saveTasks();
        } catch (Exception e) {
            ui.displayErrorMessage("Oops, something went wrong. Please check the task number and try again! \uD83D\uDD04");
        }
    }

    private void deleteTask(String input) {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            ui.displayErrorMessage("Hey! You need to specify the task number you want to delete. I can’t do it without that! \uD83D\uDE31");
            return;
        }

        try {
            int taskIndex = Integer.parseInt(parts[1]) - 1;
            Task removedTask = taskList.removeTask(taskIndex);
            ui.displaySuccessMessage("removed task: ❌\n Task: " + removedTask);
        } catch (Exception e) {
            ui.displayErrorMessage("Oops, I couldn’t find that task. Make sure you gave the correct task number. ❗");
        }
        saveTasks();
    }

    private void saveTasks() {
        try {
            storage.save("data/sunny.txt", taskList.getTasks());
        } catch (Exception e) {
            ui.displayErrorMessage("Uh-oh, I wasn’t able to save your tasks. Please try again later. \uD83D\uDD04" + e.getMessage());
        }
    }
}
