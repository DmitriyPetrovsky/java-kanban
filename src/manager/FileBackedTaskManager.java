package manager;

import enums.Status;
import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
        save();
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
        save();
    }

    @Override
    public void removeSubtasks() {
        super.removeSubtasks();
        save();
    }

    @Override
    public void removeByIdTask(int id) {
        super.removeByIdTask(id);
        save();
    }

    @Override
    public void removeByIdSubtask(int id) {
        super.removeByIdSubtask(id);
        save();
    }

    @Override
    public void removeByIdEpic(int id) {
        super.removeByIdEpic(id);
        save();
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

    public void save() {
        List<Task> tasks = new ArrayList<>();
        try {
            if (Files.exists(file.toPath())) {
                Files.delete(file.toPath());
            } else {
                throw new ManagerSaveException("Файл не существует!");
            }
            Files.createFile(file.toPath());
            tasks = super.getAllTasks();
            tasks.addAll(super.getAllEpics());
            tasks.addAll(super.getAllSubtasks());
        } catch (ManagerSaveException e) {
            e.getMessage();
        } catch (IOException e) {
            //throw new ManagerSaveException("Ошибка доступа к файлу: " + file.toPath());
        }
        try (FileWriter fw = new FileWriter(file, true)) {
            String header = "id/type/name/status/description/epic\n";
            fw.write(header);
            for (Task t : tasks) {
                fw.write(toString(t));
            }
        } catch (IOException e) {
            //throw new ManagerSaveException("Ошибка записи в файл");
        }
    }

    private String toString(Task task) {
        String[] array = {Integer.toString(task.getId()), getTaskType(task), task.getTaskName(), task.getInfo(),
                task.getStatus().toString(), subtasksEpicId(task)};
        return String.join("/", array) + "\n";
    }

    public void load(File file) {
        Map<Integer, Task> allTasks = super.getTaskMap();
        Map<Integer, Epic> allEpics = super.getEpicMap();
        Map<Integer, Subtask> allSubtasks = super.getSubtaskMap();
        try (BufferedReader bf = new BufferedReader(new FileReader(file))) {
            while (bf.ready()) {
                String line = bf.readLine();
                String[] parts = line.split("/");
                switch (parts[1]) {
                    case "TASK" -> {
                        Task task = new Task(parts[2], parts[3]);
                        task.setStatus(Status.valueOf(parts[4]));
                        task.setId(Integer.parseInt(parts[0]));
                        allTasks.put(task.getId(), task);
                    }
                    case "EPIC" -> {
                        Epic epic = new Epic(parts[2], parts[3]);
                        epic.setStatus(Status.valueOf(parts[4]));
                        epic.setId(Integer.parseInt(parts[0]));
                        allEpics.put(epic.getId(), epic);
                    }
                    case "SUBTASK" -> {
                        Subtask subtask = new Subtask(parts[2], parts[3], Integer.parseInt(parts[5]));
                        subtask.setStatus(Status.valueOf(parts[4]));
                        subtask.setId(Integer.parseInt(parts[0]));
                        allEpics.get(subtask.getEpicId()).getSubtaskIds().add(subtask.getId());
                        allSubtasks.put(subtask.getId(), subtask);
                    }
                }
            }
            super.setTaskMap(allTasks);
            super.setEpicMap(allEpics);
            super.setSubtaskMap(allSubtasks);

        } catch (IOException e) {
            //throw new ManagerSaveException("Ошибка чтения из файла");
        }
    }

    public static void main(String[] args) {
        File file = new File("D:\\JAVA\\sp7.txt");
        FileBackedTaskManager fbtm = Managers.loadFromFile(file);
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
        fbtm.addTask(new Task("Задача 1", "Инфо зад.1"));
//        fbtm.addEpic(new Epic("Задача эпик 1", "Эпик с подзадачами"));
//        fbtm.addSubtask(new Subtask("Подзадача 1", "Инфо подзадачи 1", 1001));
//        fbtm.addTask(new Task("Задача 2", "Инфо зад.2"));
//        fbtm.addSubtask(new Subtask("Подзадача 2", "Инфо подзадачи 2", 1001));
//        fbtm.addEpic(new Epic("Задача эпик 2", "Эпик без подзадач"));
    }
}
