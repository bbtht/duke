package tasklist;

public class Event extends Task {
    private final String eventStart;
    private final String eventEnd;

    public Event(String eventDescription, String from, String to) {
        super(eventDescription);
        this.eventStart = from;
        this.eventEnd = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + eventStart + ", to: " + eventEnd + ")";
    }
}
