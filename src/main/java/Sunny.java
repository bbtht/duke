import java.util.Scanner;

public class Sunny {
    private static final int maxTask = 100;
    private static final String[] tasks = new String[maxTask];
    private static int taskCount = 0;

    public static void main(String[] args) {
        // display welcome message
        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm Sunny");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");

        // User input
        Scanner in = new Scanner(System.in);
        String input;

        // Loop to process user input
        while (true) {
            input = in.nextLine();

            // End session if user inputs "bye"
            if (input.equalsIgnoreCase("bye")) {
                System.out.println("____________________________________________________________");
                System.out.println(" Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            }

            // User input add into List
            if (input.equalsIgnoreCase("list")) {
                System.out.println("____________________________________________________________");
                if (taskCount == 0) {
                    System.out.println("No task added yet, please enter a task:");
                } else {
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(" " + (i+1) + ". " + tasks[i]);
                    }
                }
                System.out.println("____________________________________________________________");
            }

            // User add new task
            else if (taskCount < maxTask) {
                    tasks[taskCount] = input;
                    taskCount++;
                    System.out.println("____________________________________________________________");
                    System.out.println(" added: " + input);
                    System.out.println("____________________________________________________________");
            }

            // If more than 100 task
            else {
                System.out.println("____________________________________________________________");
                System.out.println(" Sorry, too many tasks for Sunny to handle! There are " + maxTask + " tasks.");
                System.out.println("____________________________________________________________");
            }
        }
        // end scanner session
        in.close();
    }
}
