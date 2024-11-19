public class Deadline extends Task {
    public String deadlineDate;

    public Deadline(String deadlineDescription, String date) {
        super(deadlineDescription);
        this.deadlineDate = date;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + deadlineDate + ")";
    }
}
