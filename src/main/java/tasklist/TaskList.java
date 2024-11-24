package tasklist;

import java.io.*;
import java.time.format.*;
import java.util.ArrayList;
import ui.Ui;
import java.time.*;

/**
 * This Tasklist.java class manages a list of tasks. It allows you to add, remove, and mark tasks as done.
 * Tasks can be saved to and loaded from a file, making it easy to persist task data between program runs.
 */

public class TaskList {
    private ArrayList<Task> taskLists;
    private Ui ui;

    // this creates a new task list. (starts empty)
    public TaskList() {
        this.taskLists = new ArrayList<>();
    }

    // this is a constructor for loading tasks from a file at the specified path
    public TaskList(String filePath) {
        this.taskLists = new ArrayList<>();
        loadTasksFromFile(filePath);
    }

    public void addTask(Task task) {
        taskLists.add(task);
    }

    // removes a task at a specific index from the list
    public Task removeTask(int index) {
        return taskLists.remove(index);
    }

    public void markTaskAsDone(int index) {
        taskLists.get(index).markAsDone();
    }

    public ArrayList<Task> getTasks() {
        return taskLists;
    }

    // this saves all tasks to a file
    public void saveTasksToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Task task : taskLists) {
                writer.write(formatTaskForSaving(task));
                writer.newLine();
            }
        } catch (IOException e) {
            ui.displayErrorMessage("Oops, there was an error saving your tasks: " + e.getMessage());
        }
    }

    /**
     * Formats a task as a string to be saved to a file.
     *
     * @param task The task to format.
     * @return A string representing the task in a file-friendly format.
     */
    private String formatTaskForSaving(Task task) {
        if (task instanceof Todo) {
            return String.format("[T][%s] %s", task.isDone() ? "X" : " ", task.getDescription());
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return String.format("[D][%s] %s (by: %s)", task.isDone() ? "X" : " ", task.getDescription(), deadline.getDeadlineDate());
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return String.format("[E][%s] %s (from: %s, to: %s)", task.isDone() ? "X" : " ", task.getDescription(), event.getEventStart(), event.getEventEnd());
        }
        return "";
    }

    // it loads tasks from a file, if the file is empty or doesn't exist, it starts with a fresh task list.
    private void loadTasksFromFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.length() > 0) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Task task = parseTaskFromFile(line);
                    taskLists.add(task);
                }
                ui.displaySuccessMessage("loaded saved tasks from sunny.txt. ✅");
            } catch (IOException e) {
                ui.displayErrorMessage("Oops, there was an error loading your tasks: " + e.getMessage());
            }
        } else {
            // If file doesn't exist or is empty
            ui.printMessage("No previous tasks found. Starting fresh. \uD83C\uDD95");
        }
    }

    private String formatTaskForFile(Task task) {
        if (task instanceof Todo) {
            return String.format("[T][%s] %s", task.isDone() ? "X" : " ", task.getDescription());
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return String.format("[D][%s] %s (by: %s)", task.isDone() ? "X" : " ", task.getDescription(), deadline.getDeadlineDate());
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return String.format("[E][%s] %s (from: %s, to: %s)", task.isDone() ? "X" : " ", task.getDescription(), event.getEventStart(), event.getEventEnd());
        }
        return "";
    }

    // parses a task from a file line and returns the corresponding task object.
    private Task parseTaskFromFile(String line) {
        if (line.startsWith("[T]")) {
            return parseTodoTask(line);
        } else if (line.startsWith("[D]")) {
            return parseDeadlineTask(line);
        } else if (line.startsWith("[E]")) {
            return parseEventTask(line);
        }
        throw new IllegalArgumentException("Unknown task format, please try again ❌: " + line);
    }

    // parses a To do task from a file line
    private Task parseTodoTask(String line) {
        boolean isDone = line.charAt(4) == 'X';  // this is to check for 'X' for completed tasks
        String description = line.substring(8).trim();
        Todo todo = new Todo(description);
        if (isDone) {
            todo.markAsDone();
        }
        return todo;
    }

    // parses a Deadline task from a file line
    private Task parseDeadlineTask(String line) {
        boolean isDone = line.charAt(4) == 'X';  // Check for 'X' for completed tasks
        String[] parts = line.substring(8).split(" \\(by: ");
        String description = parts[0].trim();
        String deadlineDate = parts[1].substring(0, parts[1].length() - 1).trim();  // Remove closing ')'

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime deadline = LocalDateTime.parse(deadlineDate, formatter);
            Deadline task = new Deadline(description, deadline);
            if (isDone) {
                task.markAsDone();
            }
            return task;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: please try again ❌:" + deadlineDate);
        }
    }

    // Parses an Event task from a file line
    private Task parseEventTask(String line) {
        boolean isDone = line.charAt(4) == 'X';  // check for 'X' for completed tasks
        String[] parts = line.substring(8).split(" \\(from: | to: ");
        String description = parts[0].trim();
        String start = parts[1].trim();
        String end = parts[2].substring(0, parts[2].length() - 1).trim();  // -1 is used to remove closing ')'
        Event event = new Event(description, start, end);
        if (isDone) {
            event.markAsDone();
        }
        return event;
    }
}

