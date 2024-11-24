package tasklist;

import java.io.*;
import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> taskLists;

    // Default constructor
    public TaskList() {
        this.taskLists = new ArrayList<>();
    }

    // Constructor for loading tasks from file
    public TaskList(String filePath) {
        this.taskLists = new ArrayList<>();
        loadTasksFromFile(filePath);
    }

    // Add a task to the list
    public void addTask(Task task) {
        taskLists.add(task);
    }

    // Remove a task by index
    public Task removeTask(int index) {
        return taskLists.remove(index);
    }

    // Mark a task as done
    public void markTaskAsDone(int index) {
        taskLists.get(index).markAsDone();
    }

    // Get the list of tasks
    public ArrayList<Task> getTasks() {
        return taskLists;
    }

    // Save tasks to the file
    public void saveTasksToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Task task : taskLists) {
                writer.write(formatTaskForSaving(task));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    private String formatTaskForSaving(Task task) {
        if (task instanceof Todo) {
            return String.format("[T][%s] %s", task.isDone() ? "X" : " ", task.getDescription());
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return String.format("[D][%s] %s (by: %s)", task.isDone() ? "X" : " ", task.getDescription(), deadline.getDeadlineDate());
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return String.format("[E][%s] %s (from: %s, to: %s)", task.isDone() ? "X" : " ", task.getDescription(), event.getEventStart(), event.getEventEnd());
        }
        return "";
    }

    // Load tasks from the file
    private void loadTasksFromFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.length() > 0) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Task task = parseTaskFromFile(line);
                    taskLists.add(task);
                }
                System.out.println("Loaded saved tasks from sunny.txt.");
            } catch (IOException e) {
                System.out.println("Error reading tasks from file: " + e.getMessage());
            }
        } else {
            // If file doesn't exist or is empty
            System.out.println("No previous tasks found. Starting fresh.");
        }
    }

    // Format a task for saving to file
    private String formatTaskForFile(Task task) {
        if (task instanceof Todo) {
            return String.format("[T][%s] %s", task.isDone() ? "X" : " ", task.getDescription());
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return String.format("[D][%s] %s (by: %s)", task.isDone() ? "X" : " ", task.getDescription(), deadline.getDeadlineDate());
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return String.format("[E][%s] %s (from: %s, to: %s)", task.isDone() ? "X" : " ", task.getDescription(), event.getEventStart(), event.getEventEnd());
        }
        return "";
    }

    private Task parseTaskFromFile(String line) {
        if (line.startsWith("[T]")) {
            boolean isDone = line.charAt(4) == 'X';  // Check for 'X' for completed tasks
            String description = line.substring(8).trim();  // Adjust to account for format
            Todo todo = new Todo(description);
            if (isDone) {
                todo.markAsDone();  // Mark task as done
            }
            return todo;
        } else if (line.startsWith("[D]")) {
            boolean isDone = line.charAt(4) == 'X';  // Check for 'X' for completed tasks
            String[] parts = line.substring(8).split(" \\(by: ");
            String description = parts[0].trim();
            String deadlineDate = parts[1].substring(0, parts[1].length() - 1).trim();  // Remove closing ')'
            Deadline deadline = new Deadline(description, deadlineDate);
            if (isDone) {
                deadline.markAsDone();  // Mark task as done
            }
            return deadline;
        } else if (line.startsWith("[E]")) {
            boolean isDone = line.charAt(4) == 'X';  // Check for 'X' for completed tasks
            String[] parts = line.substring(8).split(" \\(from: | to: ");
            String description = parts[0].trim();
            String start = parts[1].trim();
            String end = parts[2].substring(0, parts[2].length() - 1).trim();  // Remove closing ')'
            Event event = new Event(description, start, end);
            if (isDone) {
                event.markAsDone();  // Mark task as done
            }
            return event;
        }
        throw new IllegalArgumentException("Unknown task format: " + line);
    }
}

