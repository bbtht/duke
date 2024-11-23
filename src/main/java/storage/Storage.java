package storage;

import tasklist.*;
import java.io.*;
import java.util.ArrayList;

public class Storage {
    private static File storageFile;

    // this is a constructor to initialise the file path, ensuring the file path provided is not null
    public Storage(String filePath) {
        assert filePath != null : " The filePath cannot be null!";
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
    public static void save(String filePath, ArrayList<Task> taskList) {
        try {
            File targetFile = new File(filePath);
            if (!targetFile.exists()) {
                targetFile.getParentFile().mkdirs();
            }

            try (FileOutputStream writer = new FileOutputStream(filePath)) {
                for (Task task : taskList) {
                    writer.write((task.getTaskStorageString() + "\n").getBytes());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while accessing the file.", e);
        }
    }

    // new saveTasks method to call the existing save method
    public void saveTasks(ArrayList<Task> tasks) {
        save(storageFile.getPath(), tasks);
    }

    // new loadTasks() method
    public ArrayList<Task> loadTasks() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();

        // Ensure the file exists before attempting to load
        ensureFileExists();

        try (BufferedReader reader = new BufferedReader(new FileReader(storageFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming Task has a static method to parse a task from a string
                Task task = Task.parse(line);
                tasks.add(task);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing tasks found, starting with an empty task list.");
        } catch (IOException e) {
            throw new IOException("Error reading tasks from file.", e);
        }

        return tasks;
    }
}


