import java.util.Scanner;

public class Sunny {
    private static final int maxTask = 100;
    private static final Task[] tasks = new Task[maxTask];
    private static int taskCount = 0;

    // prints a separator line
    private static void printSeparator() {
        System.out.println("____________________________________________________________");
    }

    // prints messages with separators to allow cleaner code
    private static void printMessage(String... messages) {
        printSeparator();
        for (String message : messages) {
            System.out.println(message);
        }
        printSeparator();
    }

    public static void main(String[] args) {
        // display welcome message
        String welcomeMessage = "Hello! I'm Sunny\n" +
                "What can I do for you?\n" +
                "Available Commands:\n" +
                "[list]: list all the tasks\n" +
                "[todo <description>]: add a todo task\n" +
                "[deadline <description> /by <date>]: add a deadline task\n" +
                "[event <description> /from <time> /to <time>]: add an event task\n" +
                "[mark <taskNumber>]: mark task number as done\n" +
                "[unmark <taskNumber>]: mark task number as not done\n" +
                "[bye]: end the session";
        printMessage(welcomeMessage);

        // scanner for user input
        Scanner in = new Scanner(System.in);
        String input;

        // loop to process user input
        while (true) {
            input = in.nextLine();

            // end session if user inputs "bye"
            if (input.equalsIgnoreCase("bye")) {
                printMessage(" Bye. Hope to see you again soon!");
                break;
            } else if (input.equalsIgnoreCase("list")) {
                // display list of tasks when user input "list"
                listTasks();
            } else if (input.startsWith("mark")) {
                // mark task as done when user input starts with "mark" (e.g. mark 2)
                markTask(input);
            } else if (input.startsWith("unmark")) {
                // mark task as not done when user input starts with "unmark" (e.g. unmark 2)
                unmarkTask(input);
            } else {
                addTask(input);
            }
        }
        // end scanner session
        in.close();
    }

    // method to display all task
    private static void listTasks() {
        StringBuilder taskList = new StringBuilder();
        taskList.append("Here are the tasks in your list:\n");
        for (int i = 0; i < taskCount; i++) {
            taskList.append(" ").append(i + 1).append(". ").append(tasks[i]).append("\n");
        }
        printMessage(taskList.toString());
    }

    // method to add a new task based on user input
    private static void addTask(String input) {
        // split the input into maximum two parts: command and description
        String[] taskParts = input.split(" ", 2);
        // convert part 1 of the input into task type (all lowercase for consistency)
        String task = taskParts[0].toLowerCase();
        // check if there is space for more tasks (max 100)
        if (taskCount < maxTask) {
            switch (task) {
                // determine the type of task to add
                case "todo":
                    addTodoTask(taskParts); // call method to add to do task
                    break;
                case "deadline":
                    addDeadlineTask(taskParts); // call method to add a deadline task
                    break;
                case "event":
                    addEventTask(taskParts); // call method to add an event task
                    break;
                default:
                    printMessage(" Unknown task type. Use todo, deadline, or event.");
            }
        } else {
            // notify user if the maximum number of tasks has been reached
            printMessage(" Sorry, too many tasks for Sunny to handle! There are " + maxTask + " tasks.");
        }
    }

    // method to add to do task
    private static void addTodoTask(String[] taskParts) {
        // check if user provided description for the task
        if (taskParts.length < 2) {
            printMessage(" Please provide a description for the todo task.");
            return; // exit the method if no description is provided
        }
        // create a new to do task with the provided description and add it to the tasks array
        tasks[taskCount++] = new Todo(taskParts[1]);
        printMessage(" added: " + tasks[taskCount - 1]);
    }

    // method to add a deadline task
    private static void addDeadlineTask(String[] taskParts) {
        // check if the user provided a description and a due date
        if (taskParts.length < 2 || !taskParts[1].contains("/by")) {
            printMessage(" Please provide a description and a due date (e.g. /by Monday).");
            return;
        }
        // split the description and due date using "/by" as the delimiter
        String[] deadlineParts = taskParts[1].split("/by", 2);
        // create a new Deadline task with the description and due date, and add it to the tasks array
        tasks[taskCount++] = new Deadline(deadlineParts[0].trim(), deadlineParts[1].trim());
        printMessage(" added: " + tasks[taskCount - 1]);
    }

    // method to add an event task
    private static void addEventTask(String[] taskParts) {
        // check if the user provided a description and both start and end time of the event
        if (taskParts.length < 2 || !taskParts[1].contains("/from") || !taskParts[1].contains("/to")) {
            printMessage(" Please provide a description and the time range (e.g. /from Mon 2pm /to 4pm).");
            return;
        }
        // split the description into parts using "/from" and "/to" as delimiters
        String[] eventParts = taskParts[1].split("/from|/to", 3);
        // create a new Event task with the description, start time, and end time, and add it to the tasks array
        tasks[taskCount++] = new Event(eventParts[0].trim(), eventParts[1].trim(), eventParts[2].trim());
        printMessage(" added: " + tasks[taskCount - 1]);
    }

    // method to mark specified task as done
    private static void markTask(String input) {
        // split the input into command and task number
        String[] parts = input.split(" ");
        // check if input has 2 parts (command and task number)
        if (parts.length == 2) {
            try {
                int taskNumber = Integer.parseInt(parts[1]) - 1;
                // validate if taskNumber is within task list
                if (taskNumber >= 0 && taskNumber < taskCount) {
                    // check if task is already marked as done
                    if (tasks[taskNumber].isDone) {
                        printMessage(" Task " + (taskNumber + 1) + " is already marked as done!");
                    } else {
                        // mark task as done
                        tasks[taskNumber].markAsDone();
                        printMessage("Nice! I've marked this task as done:\n    " + tasks[taskNumber]);
                    }
                } else {
                    // if taskNumber is out of bound, print invalid task number
                    printMessage(" Invalid task number, please try again! (eg. mark 2)");
                }
            } catch (NumberFormatException e) {
                // catch if task number is not a valid integer
                printMessage(" Please provide a valid numeric task number.");
            }
        } else {
            // if input format is incorrect (eg. no number provided)
            printMessage(" Invalid input format. Use: mark <number>");
        }
    }

    // method to mark specified task as not done
    private static void unmarkTask(String input) {
        String[] partsU = input.split(" ");
        // check if input has 2 parts (command and task number)
        if (partsU.length == 2) {
            try {
                int taskNumber = Integer.parseInt(partsU[1]) - 1;
                // validate if taskNumber is within task list
                if (taskNumber >= 0 && taskNumber < taskCount) {
                    // check if task is already marked as done
                    if (!tasks[taskNumber].isDone) {
                        printMessage(" Task " + (taskNumber + 1) + " is already marked as not done!");
                    } else {
                        tasks[taskNumber].markAsNotDone();
                        printMessage(" Nice! I've marked this task as not done:\n    " + tasks[taskNumber]);
                    }
                } else {
                    printMessage(" Invalid task number, please try again! (eg. unmark 2)");
                }
            } catch (NumberFormatException e) {
                // catch if task number is not a valid integer
                printMessage(" Please provide a valid numeric task number.");
            }
        } else {
            // if input format is incorrect (eg. no number provided)
            printMessage(" Invalid input format. Use: unmark <number>");
        }
    }
}
