public class Task {
    // fields to store the task description and its completion status
    protected String description;
    protected boolean isDone;

    // constructor that initialises task with description and sets initial status to "not done"
    public Task(String desc) {
        this.description = desc;
        this.isDone = false;
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

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
