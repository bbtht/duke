package tasklist;

public class Todo extends Task {
    public Todo(String todoDescription) {
        super(todoDescription);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
