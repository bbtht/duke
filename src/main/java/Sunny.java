import java.util.Scanner;

public class Sunny {
    public static void main(String[] args) {
        // display welcome message
        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm Sunny");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");

        // User input
        Scanner in = new Scanner(System.in);
        String input;

        // Loop to take user input until "bye" is entered
        while (true) {
            input = in.nextLine();

            if (input.equalsIgnoreCase("bye")) {
                System.out.println("____________________________________________________________");
                System.out.println(" Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            }

            // echo user's input
            System.out.println("____________________________________________________________");
            System.out.println(" " + input);
            System.out.println("____________________________________________________________");
        }
        // end scanner session
        in.close();
    }
}
