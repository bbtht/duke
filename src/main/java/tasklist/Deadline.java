package tasklist;

/**
 * This Deadline.java represents a type of task (Deadline)
 * Deadline includes the endDate / endTime of a task that requires timing urgency
 */

public class Deadline extends Task {
    public String deadlineDate;

    public Deadline(String deadlineDescription, String date) {
        super(deadlineDescription);
        this.deadlineDate = date;
    }

    public String getDeadlineDate() {
        return this.deadlineDate;
    }

    @Override
    public String getType() {
        return "D";  // Type for Deadline task
    }

    @Override
    public String getDetails() {
        return "(by: " + deadlineDate + ")";  // Format the deadline details
    }

    @Override
    public String getTaskStorageString() {
        String doneMark = isDone() ? "X" : " ";
        return String.format("[D][%s] %s (by: %s)", doneMark, description, deadlineDate);
    }
}
