package tasklist;

import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * This class represents a generic task. Specific task types like Event, Todo, and Deadline
 * inherit from this class to share common features and behaviors.
 * A task can either be completed or still pending.
 */

public abstract class Task {
    protected String description;
    public boolean isDone;

    /**
     * Constructor to create a task with a given description.
     * By default, a new task is marked as not done.
     */
    public Task(String desc) {
        this.description = desc;
        this.isDone = false;
    }

    /**
     * Parses a task string from file storage and creates a corresponding task object.
     * It identifies whether it's a to do, Deadline, or Event based on the string format.
     *
     * @param line the string containing task data
     * @return the corresponding Task object
     */
    public static Task parse(String line) {
        // commenting out the debugging print statements in the parse function below
        // System.out.println("Parsing line: " + line);
        if (line.startsWith("[T]")) {
            return parseTodo(line);
        } else if (line.startsWith("[D]")) {
            return parseDeadline(line);
        } else if (line.startsWith("[E]")) {
            return parseEvent(line);
        }
        throw new IllegalArgumentException("Unknown task format, please try again ❌: " + line);
    }

    // parses a To do task from a string
    private static Todo parseTodo(String line) {
        boolean isDone = line.charAt(4) == 'X';
        String description = line.substring(7).trim();
        // System.out.println("Description: " + description);
        Todo todo = new Todo(description);
        if (isDone) {
            todo.markAsDone();
        }
        return todo;
    }

    // parses a Deadline task from a string
    private static Deadline parseDeadline(String line) {
        boolean isDone = line.charAt(4) == 'X';
        String[] parts = line.substring(7).split(" \\(by: ");
        String description = parts[0].trim();
        String deadlineDateString = parts[1].substring(0, parts[1].length() - 1).trim();
        // System.out.println("Description: " + description);
        // System.out.println("Deadline Date: " + deadlineDate);
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime parsedDeadlineDate = LocalDateTime.parse(deadlineDateString, formatter);
            Deadline deadline = new Deadline(description, parsedDeadlineDate);
            if (isDone) {
                deadline.markAsDone();
            }
            return deadline;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format in task file: " + deadlineDateString, e);
        }
    }

    // parses an Event task from a string
    private static Event parseEvent(String line) {
        boolean isDone = line.charAt(4) == 'X';
        // this is used to split the line into parts based on the "from" and "to" keywords
        String[] parts = line.substring(7).split(" \\(from: | to: ");

        // System.out.println("Raw parts: " + Arrays.toString(parts));

        // used to extract the description, start, and end time for events
        String description = parts[0].trim();
        String start = parts[1].trim().replace(",", "");
        // remove any extra commas, faced error for displaying task list when ending the program and starting again
        String end = parts[2].substring(0, parts[2].length() - 1).trim().replace(",", "");

        // System.out.println("Description: " + description);
        // System.out.println("Event Start: " + start);
        // System.out.println("Event End: " + end);

        Event event = new Event(description, start, end);
        if (isDone) {
            event.markAsDone();
        }
        return event;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isDone() {
        return this.isDone;
    }

    // returns "X" if task is done, or a blank space if it is not done
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    // these method can be overridden by subclasses to return specific task details
    public String getTaskStorageString(){
        // returns only the description when saving to file
        return description;
    }

    public abstract String getDetails();

    @Override
    public String toString() {
        // format task output with type, status, description, and details
        String doneMark = isDone ? "X" : " ";
        return String.format("[%s][%s] %s %s", getType(), doneMark, getDescription(), getDetails()).trim();
    }

    // returns the type of the task, to be defined in subclasses
    public abstract String getType();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Task task = (Task) obj;
        return description.equals(task.description);
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }
}
