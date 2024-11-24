package storage;

import tasklist.*;
import java.io.*;
import java.util.ArrayList;

/**
 * This Storage.java represents the storage, which is used to store our task list in the hard disk
 * It will load the user input list into data/sunny.txt everytime the user add or remove command
 */

public class Storage {
    private static File storageFile;

    // this is a constructor to initialise the file path, ensuring the file path provided is not null
    public Storage(String filePath) {
        assert filePath != null : "The filePath cannot be null!";
        storageFile = new File(filePath);
    }

    public File load() {
        return storageFile;
    }

    // this method is to ensure that the files and the necessary directories exists, if not, create it
    private static void ensureFileExists() {
        if (!storageFile.exists()) {
            File parentDirectory = storageFile.getParentFile();
            if (parentDirectory != null && !parentDirectory.exists()) {
                parentDirectory.mkdirs();
            }
        }
    }

    public void writeToFile(ArrayList<Task> tasks) throws IOException {
        ensureFileExists();
        try (FileWriter writer = new FileWriter(storageFile, true)) {
            for (Task task : tasks) {
                writer.write(task.getTaskStorageString() + "\n");
            }
        }
    }

    // save the task list to the specified file path, creating the file if necessary
    public void save(String filePath, ArrayList<Task> taskList) {
        File targetFile = new File(filePath);
        try {
            ensureFileExists();

            try (FileOutputStream writer = new FileOutputStream(targetFile)) {
                for (Task task : taskList) {
                    writer.write((task.getTaskStorageString() + "\n").getBytes());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong while accessing the file. ❗", e);
        }
    }

    // load tasks from the file and return them as a list
    public ArrayList<Task> loadTasks() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();

        // ensure the file exists before attempting to load
        ensureFileExists();

        try (BufferedReader reader = new BufferedReader(new FileReader(storageFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = Task.parse(line);
                tasks.add(task);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Looks like you're starting fresh! Let's add some tasks to get started. \uD83D\uDDD2\uFE0F ✍\uFE0F \nYou can type 'help' to see all available commands. \uD83D\uDC81\u200D♀\uFE0F");
        } catch (IOException e) {
            throw new IOException("Error reading tasks from file. ❌", e);
        }
        return tasks;
    }
}



