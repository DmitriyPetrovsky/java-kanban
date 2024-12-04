package manager;

import enums.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

//import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.HashMap;
//import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {
    //private final Map<Integer, Task> allTasks = new HashMap<>();
    //private final Map<Integer, Epic> allEpics = new HashMap<>();
    //private final Map<Integer, Subtask> allSubtasks = new HashMap<>();
    //private final HistoryManager historyManager = Managers.getDefaultHistory();
    private String fileName = "";

    private int maxTaskCounter = 999;

    public FileBackedTaskManager(String fileName) {
        super();
        this.fileName = fileName;
    }

    public void increaseTaskCounter() {
        super.increaseTaskCounter();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save(task);
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save(epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save(subtask);
    }

    public void save(Task task) {
        try (FileWriter fw = new FileWriter(fileName, true)) {
            fw.write(toString(task));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String toString(Task task) {
        String result;
        if (task instanceof Subtask) {
            result = task.getId() + "/SUBTASK/" + task.getTaskName() + "/" + task.getInfo() + "/" + task.getStatus() +
                    "/" + ((Subtask) task).getEpicId() + "\n";
        } else if (task instanceof Epic) {
            result = task.getId() + "/EPIC/" + task.getTaskName() + "/" + task.getInfo() + "/" + task.getStatus() + "\n";
        } else {
            result = task.getId() + "/TASK/" + task.getTaskName() + "/" + task.getInfo() + "/" + task.getStatus() + "\n";
        }
        return result;
    }

    private void load(String fileName) {
        try (BufferedReader bf = new BufferedReader(new FileReader(fileName))) {
            while (bf.ready()) {
                String line = bf.readLine();
                String[] parts = line.split("/");
                    if (parts[1].equals("TASK")) {
                        Task task = new Task(parts[2], parts[3]);
                        task.setStatus(Status.valueOf(parts[4]));
                        super.addTask(task);
                    } else if (parts[1].equals("EPIC")) {
                        Epic epic = new Epic(parts[2], parts[3]);
                        epic.setStatus(Status.valueOf(parts[4]));
                        super.addEpic(epic);
                    } else if (parts[1].equals("SUBTASK")) {
                        Subtask subtask = new Subtask(parts[2], parts[3], Integer.valueOf(parts[5]));
                        subtask.setStatus(Status.valueOf(parts[4]));
                        super.addSubtask(subtask);
                    }
            }
        } catch (IOException e) {
        }
    }

    public static void main(String[] args) {
        FileBackedTaskManager fbtm = new FileBackedTaskManager("D:\\JAVA\\sp7.txt");
        fbtm.load(fbtm.fileName);
        List<Epic> allEpics = fbtm.getAllEpics();
        List<Subtask> subtasks;
        List<Task> allTasks = fbtm.getAllTasks();
        for (Task task : allTasks) {
            System.out.println(task);
        }
        for (Epic epic : allEpics) {
            System.out.println(epic);
            subtasks = fbtm.getSubtasksByEpicId(epic.getId());
            for (Subtask subtask : subtasks) {
                System.out.println(subtask);
            }
        }
//        System.out.println(fbtm.getAllTasks());
//        fbtm.addTask(new Task("Задача новая", "Инфо зад.новой"));
//        System.out.println(fbtm.getAllTasks());
//        fbtm.addTask(new Task("Задача 1", "Инфо зад.1"));
//        fbtm.addEpic(new Epic("Задача эпик 1", "Эпик с подзадачами"));
//        fbtm.addSubtask(new Subtask("Подзадача 1", "Инфо подзадачи 1", 1001));
//        fbtm.addTask(new Task("Задача 2", "Инфо зад.2"));
//        fbtm.addSubtask(new Subtask("Подзадача 2", "Инфо подзадачи 2", 1001));
//        fbtm.addEpic(new Epic("Задача эпик 2", "Эпик без подзадач"));
    }
}
