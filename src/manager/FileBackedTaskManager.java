package manager;

import enums.Status;
import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.util.List;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }
//
//    public void increaseTaskCounter() {
//        super.increaseTaskCounter();
//    }

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

    private String getTaskType(Task task) {
        if (task instanceof Epic) {
            return "EPIC";
        } else if (task instanceof Subtask) {
            return "SUBTASK";
        } else {
            return "TASK";
        }
    }

    private String subtasksEpicId(Task task) {
        if (task instanceof Subtask) {
            return Integer.toString(((Subtask) task).getEpicId());
        }
        return "";
    }

    public void save(Task task) {
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(toString(task));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String toString(Task task) {
        String[] array = {Integer.toString(task.getId()), getTaskType(task), task.getTaskName(), task.getInfo(),
                task.getStatus().toString(), subtasksEpicId(task)};
        return String.join("/", array) + "\n";
    }

    private void load(File file) {
        try (BufferedReader bf = new BufferedReader(new FileReader(file))) {
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
            throw new ManagerSaveException("Ошибка чтения из файла");
        }
    }

    public static void main(String[] args) {
        File file = new File("D:\\JAVA\\sp7.txt");
        FileBackedTaskManager fbtm = new FileBackedTaskManager(file);
        fbtm.load(fbtm.file);
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
//
//        fbtm.addTask(new Task("Задача 1", "Инфо зад.1"));
//        fbtm.addEpic(new Epic("Задача эпик 1", "Эпик с подзадачами"));
//        fbtm.addSubtask(new Subtask("Подзадача 1", "Инфо подзадачи 1", 1001));
//        fbtm.addTask(new Task("Задача 2", "Инфо зад.2"));
//        fbtm.addSubtask(new Subtask("Подзадача 2", "Инфо подзадачи 2", 1001));
//        fbtm.addEpic(new Epic("Задача эпик 2", "Эпик без подзадач"));
    }
}
