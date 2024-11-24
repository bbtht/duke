package tasklist;

public class Todo extends Task {
    public Todo(String todoDescription) {
        super(todoDescription);
    }

    @Override
    public String toString() {
        return "[T][" + (isDone ? "X" : " ") + "] " + description;
    }

    @Override
    protected String getType() {
        return "T";
    }
}
