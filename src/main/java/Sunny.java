import java.util.Scanner;

public class Sunny {
    private static final int maxTask = 100;
    private static final Task[] tasks = new Task[maxTask];
    private static int taskCount = 0;

    public static void main(String[] args) {
        // display welcome message
        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm Sunny");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");

        // scanner for user input
        Scanner in = new Scanner(System.in);
        String input;

        // loop to process user input
        while (true) {
            input = in.nextLine();

            // end session if user inputs "bye"
            if (input.equalsIgnoreCase("bye")) {
                System.out.println("____________________________________________________________");
                System.out.println(" Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            } else if (input.equalsIgnoreCase("list")) {
                // display list of tasks when user input "list"
                listTasks();
            } else if (input.startsWith("mark")) {
                // mark task as done when user input starts with "mark" (eg. mark 2)
                markTask(input);
            } else if (input.startsWith("unmark")) {
                // mark task as not done when user input starts with "unmark" (eg. unmark 2)
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
        System.out.println("____________________________________________________________");
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println(" " + (i + 1) + ". " + tasks[i]);
        }
        System.out.println("____________________________________________________________");
    }

    // method when user adds new task
    private static void addTask(String description) {
        if (taskCount < maxTask) {
            tasks[taskCount] = new Task(description);
            taskCount++;
            System.out.println("____________________________________________________________");
            System.out.println(" added: " + description);
            System.out.println("____________________________________________________________");
        } else {
            // display message when the limit of 100 task is hit
            System.out.println("____________________________________________________________");
            System.out.println(" Sorry, too many tasks for Sunny to handle! There are " + maxTask + " tasks.");
            System.out.println("____________________________________________________________");
        }
    }

    // method to mark specified task as done
    private static void markTask(String input) {
        String[] parts = input.split(" ");
        // check if input has 2 parts (command and task number)
        if (parts.length == 2) {
            try {
                int taskNumber = Integer.parseInt(parts[1]) - 1;
                // validate if taskNumber is within task list
                if (taskNumber >= 0 && taskNumber < taskCount) {
                    // check if task is already marked as done
                    if (tasks[taskNumber].isDone) {
                        System.out.println("____________________________________________________________");
                        System.out.println(" Task " + (taskNumber + 1) + " is already marked as done!");
                        System.out.println("____________________________________________________________");
                    } else {
                        // mark task as done
                        tasks[taskNumber].markAsDone();
                        System.out.println("____________________________________________________________");
                        System.out.println(" Nice! I've marked this task as done:");
                        System.out.println("    " + tasks[taskNumber]);
                        System.out.println("____________________________________________________________");
                    }
                } else {
                    // if taskNumber is out of bound, print invalid task number
                    System.out.println("____________________________________________________________");
                    System.out.println(" Invalid task number, please try again! (eg. mark 2)");
                    System.out.println("____________________________________________________________");
                }
            } catch (NumberFormatException e) {
                // catch if task number is not a valid integer
                System.out.println("__________________________________________________________");
                System.out.println(" Please provide a valid numeric task number.");
                System.out.println("____________________________________________________________");
            }
        } else {
            // if input format is incorrect (eg. no number provided)
            System.out.println("____________________________________________________________");
            System.out.println(" Invalid input format. Use: mark <number>");
            System.out.println("____________________________________________________________");
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
                        System.out.println("____________________________________________________________");
                        System.out.println(" Task " + (taskNumber + 1) + " is already marked as not done!");
                        System.out.println("____________________________________________________________");
                    } else {
                        tasks[taskNumber].markAsNotDone();
                        System.out.println("____________________________________________________________");
                        System.out.println(" Nice! I've marked this task as not done:");
                        System.out.println("    " + tasks[taskNumber]);
                        System.out.println("____________________________________________________________");
                    }
                } else {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Invalid task number, please try again! (eg. unmark 2)");
                    System.out.println("____________________________________________________________");
                }
            } catch (NumberFormatException e) {
                // catch if task number is not a valid integer
                System.out.println("__________________________________________________________");
                System.out.println(" Please provide a valid numeric task number.");
                System.out.println("____________________________________________________________");
            }
        } else {
            // if input format is incorrect (eg. no number provided)
            System.out.println("____________________________________________________________");
            System.out.println(" Invalid input format. Use: unmark <number>");
            System.out.println("____________________________________________________________");
        }
    }
}
