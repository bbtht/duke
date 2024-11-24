package tasklist;

// Task.java is a generic task type

import java.util.*;

public abstract class Task {
    // fields to store the task description and its completion status
    protected String description;
    public boolean isDone;

    // constructor that initialises task with description and sets initial status to "not done"
    public Task(String desc) {
        this.description = desc;
        this.isDone = false;
    }

    public static Task parse(String line) {
        // System.out.println("Parsing line: " + line);

        if (line.startsWith("[T]")) {
            boolean isDone = line.charAt(4) == 'X';  // Check for 'X'
            String description = line.substring(7).trim();  // Adjust index to capture full description
            // System.out.println("Description: " + description);  // Debug print for description
            Todo todo = new Todo(description);
            if (isDone) {
                todo.markAsDone();
            }
            return todo;
        } else if (line.startsWith("[D]")) {
            boolean isDone = line.charAt(4) == 'X';
            String[] parts = line.substring(7).split(" \\(by: ");  // Adjust index to capture full description
            String description = parts[0].trim();
            String deadlineDate = parts[1].substring(0, parts[1].length() - 1).trim();  // Remove closing ')'
            // System.out.println("Description: " + description);  // Debug print for description
            // System.out.println("Deadline Date: " + deadlineDate);  // Debug print for deadline date
            Deadline deadline = new Deadline(description, deadlineDate);
            if (isDone) {
                deadline.markAsDone();
            }
            return deadline;
        } else if (line.startsWith("[E]")) {
            boolean isDone = line.charAt(4) == 'X';
            // Split the line into parts based on the "from" and "to" keywords
            String[] parts = line.substring(7).split(" \\(from: | to: ");

            // System.out.println("Raw parts: " + Arrays.toString(parts));

            // Extract the description, start, and end times
            String description = parts[0].trim();
            String start = parts[1].trim().replace(",", ""); // Remove any trailing commas
            String end = parts[2].substring(0, parts[2].length() - 1).trim().replace(",", ""); // Remove any trailing commas

            // Debug prints
            // System.out.println("Description: " + description);  // Debug print for description
            // System.out.println("Event Start: " + start);  // Debug print for event start
            // System.out.println("Event End: " + end);  // Debug print for event end

            Event event = new Event(description, start, end);
            if (isDone) {
                event.markAsDone();
            }
            return event;
        }
        throw new IllegalArgumentException("Unknown task format: " + line);
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isDone() {
        return this.isDone;
    }

    // returns status "X" if task is done, and a blank space if task not done
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    // marks the task as done by setting isDone to true
    public void markAsDone() {
        this.isDone = true;
    }

    // marks the task as not done by setting isDone to false
    public void markAsNotDone() {
        this.isDone = false;
    }

    public String getTaskStorageString(){
        return description;
    }

    public String getType() { // Abstract method to get the task type
        return null;
    }

    public abstract String getDetails(); // Abstract method to get task-specific detail

    @Override
    public String toString() {
        String doneMark = isDone ? "X" : " ";
        return String.format("[%s][%s] %s %s", getType(), doneMark, getDescription(), getDetails()).trim();
    }
}
