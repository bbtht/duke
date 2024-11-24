package tasklist;

public class Todo extends Task {
    public Todo(String todoDescription) {
        super(todoDescription);
    }

    @Override
    public String getType() {
        return "T";  // Type for Todo task
    }

    @Override
    public String getDetails() {
        return "";  // No extra details for a basic Todo task
    }

    @Override
    public String getTaskStorageString() {
        String doneMark = isDone() ? "X" : " ";
        return String.format("[T][%s] %s", doneMark, description);
    }
}
