package tasklist;

public class Deadline extends Task {
    public String deadlineDate;

    public Deadline(String deadlineDescription, String date) {
        super(deadlineDescription);
        this.deadlineDate = date;
    }

    public String getDeadlineDate() {
        return deadlineDate;
    }

    @Override
    public String toString() {
        return "[D][" + (isDone ? "X" : " ") + "] " + description + " (by: " + deadlineDate + ")";
    }

    @Override
    protected String getType() {
        return "D";
    }
}
