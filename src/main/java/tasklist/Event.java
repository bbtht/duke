package tasklist;

public class Event extends Task {
    private final String eventStart;
    private final String eventEnd;

    public Event(String eventDescription, String from, String to) {
        super(eventDescription);
        this.eventStart = from;
        this.eventEnd = to;
    }

    public String getEventStart() {
        return eventStart;
    }

    public String getEventEnd() {
        return eventEnd;
    }

    @Override
    public String toString() {
        return "[E][" + (isDone ? "X" : " ") + "] " + description + " (from: " + eventStart + ", to: " + eventEnd + ")";
    }

    @Override
    protected String getType() {
        return "E";
    }
}
