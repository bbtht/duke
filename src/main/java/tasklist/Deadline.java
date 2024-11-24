package tasklist;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * This Deadline.java represents a type of task (Deadline)
 * Deadline includes the endDate / endTime of a task that requires timing urgency
 */

public class Deadline extends Task {
    private final LocalDateTime deadlineDate;

    public Deadline(String deadlineDescription, LocalDateTime date) {
        super(deadlineDescription);
        this.deadlineDate = date;
    }

    public LocalDateTime getDeadlineDate() {
        return deadlineDate;
    }

    @Override
    public String getType() {
        return "D";  // Type for Deadline task
    }

    @Override
    public String getDetails() {
        // format the LocalDateTime into a user-friendly string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");
        return String.format("(by: %s)", deadlineDate.format(formatter));
    }

    @Override
    public String getTaskStorageString() {
        // format the LocalDateTime into a savable string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("[D][%s] %s (by: %s)", getStatusIcon(), description, deadlineDate.format(formatter));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;

        Deadline deadline = (Deadline) obj;
        return deadlineDate.equals(deadline.deadlineDate);
    }

    @Override
    public int hashCode() {
        return description.hashCode() + deadlineDate.hashCode();
    }
}
