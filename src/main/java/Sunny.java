import java.util.Scanner;


public class Sunny {
    private static final int maxTask = 100;
    private static final Task[] tasks = new Task[maxTask];
    private static int taskCount = 0;


    // prints a separator line
    private static void printSeparator() {
        System.out.println("____________________________________________________________________________________________________________________________");
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
                    printMessage(" Error, Please try again. Please provide a valid task type (e.g. todo, deadline, or event)");
            }
        } else {
            // notify user if the maximum number of tasks has been reached
            printMessage(" Error, Please try again. There are too many tasks for Sunny to handle! Maximum is " + maxTask + " tasks.");
        }
    }


    // method to add to do task
    private static void addTodoTask(String[] taskParts) {
        // check if user provided description for the task
        if (taskParts.length < 2 || taskParts[1].trim().isEmpty()) {
            printMessage(" Error, Please try again. The description of a todo task cannot be empty.");
            return; // exit the method if no description is provided
        }
        // create a new to do task with the provided description and add it to the tasks array
        tasks[taskCount++] = new Todo(taskParts[1]);
        printMessage(" added: " + tasks[taskCount - 1]);
    }


    // method to add a deadline task
    private static void addDeadlineTask(String[] taskParts) {
        // check if the user provided a description and a due date
        if (taskParts.length < 2 || taskParts[1].trim().isEmpty()) {
            printMessage(" Error, Please try again. The description of a deadline task cannot be empty.");
            return;
        }


        // split the description and due date using "/by" as the delimiter
        String[] deadlineParts = taskParts[1].split("/by", 2);
        if (deadlineParts.length < 2 || deadlineParts[1].trim().isEmpty()) {
            printMessage(" Error, Please try again. Please provide a due date (e.g. /by Monday).");
            return;
        }


        // create a new Deadline task with the description and due date, and add it to the tasks array
        tasks[taskCount++] = new Deadline(deadlineParts[0].trim(), deadlineParts[1].trim());
        printMessage(" added: " + tasks[taskCount - 1]);
    }


    // method to add an event task
    private static void addEventTask(String[] taskParts) {
        // check if the user provided a description
        if (taskParts.length < 2 || taskParts[1].trim().isEmpty()) {
            printMessage(" Error, Please try again. The description of an event task cannot be empty.");
            return;
        }


        String input = taskParts[1].trim();


        // check for the presence of both /from and /to
        if (!input.contains("/from") || !input.contains("/to")) {
            printMessage(" Error, Please try again. Please provide the start time and end time of the event (e.g. /from Mon 2pm /to 4pm).");
            return;
        }


        // validate the order of /from and /to
        int fromIndex = input.indexOf("/from");
        int toIndex = input.indexOf("/to");


        if (fromIndex > toIndex) {
            printMessage(" Error, Please try again. The /from time must come before the /to time.");
            return;
        }


        // split the description into parts using "/from" and "/to" as delimiters
        String[] eventParts = input.split("/from|/to", 3);


        // check if the split produced the expected number of parts
        if (eventParts.length < 3 || eventParts[1].trim().isEmpty() || eventParts[2].trim().isEmpty()) {
            printMessage(" Error, Please try again. Please provide the end time of the event (e.g. /to 4pm).");
            return;
        }


        // create a new Event task with the description, start time, and end time, and add it to the tasks array
        tasks[taskCount++] = new Event(eventParts[0].trim(), eventParts[1].trim(), eventParts[2].trim());
        printMessage("added: " + tasks[taskCount - 1]);
    }


    // method to mark specified task as done
    private static void markTask(String input) {
        // split the input into command and task number
        String[] parts = input.split(" ");
        // check if input has 2 parts (command and task number)
        if (parts.length < 2) {
            printMessage(" Error, Please try again. Please provide a task number to mark as done (e.g. mark <number>).");
            return;
        }
        try {
            int taskNumber = Integer.parseInt(parts[1]) - 1;
            // validate if the task number is within the bounds of the task list
            if (taskNumber < 0 || taskNumber >= taskCount) {
                printMessage(" Error, Please try again. Please provide a valid task number within the task list.");
                return;
            }
            // check if the specified task is already marked as done
            if (tasks[taskNumber].isDone) {
                printMessage(" Task " + (taskNumber + 1) + " is already marked as done!");
            } else {
                // mark task as done
                tasks[taskNumber].markAsDone();
                printMessage("Nice! I've marked this task as done:\n    " + tasks[taskNumber]);
            }
        } catch (NumberFormatException e) {
            // handle the case where the task number is not a valid integer
            printMessage(" Error, Please try again. Please provide a valid numeric task number.");
        }
    }


    // method to mark specified task as not done
    private static void unmarkTask(String input) {
        String[] partsU = input.split(" ");


        // check if the input has less than 2 parts or if the task number is empty
        if (partsU.length < 2) {
            printMessage(" Error, Please try again. Please provide a task number to unmark as done (e.g. unmark <number>).");
            return;
        }


        try {
            int taskNumber = Integer.parseInt(partsU[1]) - 1;
            // validate if the task number is within the bounds of the task list
            if (taskNumber < 0 || taskNumber >= taskCount) {
                printMessage(" Error, Please try again. Please provide a valid task number within the task list.");
                return;
            }
            // check if the specified task is already marked as not done
            if (!tasks[taskNumber].isDone) {
                printMessage(" Task " + (taskNumber + 1) + " is already marked as not done!");
            } else {
                // mark task as not done
                tasks[taskNumber].markAsNotDone();
                printMessage(" Nice! I've marked this task as not done:\n    " + tasks[taskNumber]);
            }
        } catch (NumberFormatException e) {
            // handle the case where the task number is not a valid integer
            printMessage(" Error, Please try again. Please provide a valid numeric task number.");
        }
    }
}
