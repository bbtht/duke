package tasklist;

// Task.java is a generic task type

public class Task {
    // fields to store the task description and its completion status
    protected String description;
    public boolean isDone;

    // constructor that initialises task with description and sets initial status to "not done"
    public Task(String desc) {
        this.description = desc;
        this.isDone = false;
    }

    public static Task parse(String line) {
        if (line.startsWith("[T]")) {
            // Extract description and completion status
            boolean isDone = line.charAt(3) == 'X';
            String description = line.substring(6);
            Todo todo = new Todo(description);
            if (isDone) {
                todo.markAsDone();
            }
            return todo;
        } else if (line.startsWith("[D]")) {
            // Extract description, due date, and completion status
            boolean isDone = line.charAt(3) == 'X';
            String[] parts = line.substring(6).split(" \\(by: ");
            String description = parts[0];
            String by = parts[1].substring(0, parts[1].length() - 1);
            Deadline deadline = new Deadline(description, by);
            if (isDone) {
                deadline.markAsDone();
            }
            return deadline;
        } else if (line.startsWith("[E]")) {
            // Extract description, start time, end time, and completion status
            boolean isDone = line.charAt(3) == 'X';
            String[] parts = line.substring(6).split(" \\(from: | to: ");
            String description = parts[0];
            String from = parts[1];
            String to = parts[2].substring(0, parts[2].length() - 1);
            Event event = new Event(description, from, to);
            if (isDone) {
                event.markAsDone();
            }
            return event;
        }
        throw new IllegalArgumentException("Unknown task format: " + line);
    }

    // returns status "X" if task is done, and a blank space if task not done
    public String getStatusIcon() {
        if (isDone) {
            return "X";
        } else {
            return " ";
        }
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

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
