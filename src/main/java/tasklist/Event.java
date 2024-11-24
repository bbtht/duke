package tasklist;

/**
 * This Event.java represents a type of task (Event)
 * Event includes start time and end time of an Event
 */

public class Event extends Task {
    private final String eventStart;
    private final String eventEnd;

    public Event(String eventDescription, String from, String to) {
        super(eventDescription);
        this.eventStart = from;
        this.eventEnd = to;
    }

    public String getEventStart() {
        return this.eventStart;
    }

    public String getEventEnd() {
        return this.eventEnd;
    }

    @Override
    public String getType() {
        return "E";
    }

    @Override
    public String getDetails() {
        return String.format("(from: %s, to: %s)", eventStart, eventEnd);
    }

    @Override
    public String getTaskStorageString() {
        String doneMark = isDone() ? "X" : " ";
        return String.format("[E][%s] %s (from: %s, to: %s)", doneMark, description, eventStart, eventEnd);
    }

}
