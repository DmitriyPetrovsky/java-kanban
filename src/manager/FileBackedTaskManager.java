package manager;

import enums.Status;
import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.file.Files;
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

    private void saveToFile() throws ManagerSaveException {
        List<Task> tasks;
        try {
            if (Files.exists(file.toPath())) {
                Files.delete(file.toPath());
            }
            Files.createFile(file.toPath());
            tasks = super.getAllTasks();
            tasks.addAll(super.getAllEpics());
            tasks.addAll(super.getAllSubtasks());
        } catch (IOException e) {
            throw new ManagerSaveException("Не удается создать файл.");
        }
        try (FileWriter fw = new FileWriter(file, true)) {
            String header = "id/type/name/status/description/epic\n";
            fw.write(header);
            for (Task t : tasks) {
                fw.write(toString(t));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удается записать в файл.");
        }
    }

    private void save() {
        try {
            saveToFile();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
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
        int taskCounter = 999;
        try (BufferedReader bf = new BufferedReader(new FileReader(file))) {
            while (bf.ready()) {
                String line = bf.readLine();
                String[] parts = line.split("/");
                switch (parts[1]) {
                    case "TASK" -> {
                        Task task = new Task(parts[2], parts[3]);
                        task.setStatus(Status.valueOf(parts[4]));
                        task.setId(Integer.parseInt(parts[0]));
                        if (taskCounter < task.getId()) {
                            taskCounter = task.getId();
                            super.setTaskCounter(taskCounter);
                        }
                        allTasks.put(task.getId(), task);
                    }
                    case "EPIC" -> {
                        Epic epic = new Epic(parts[2], parts[3]);
                        epic.setStatus(Status.valueOf(parts[4]));
                        epic.setId(Integer.parseInt(parts[0]));
                        if (taskCounter < epic.getId()) {
                            taskCounter = epic.getId();
                            super.setTaskCounter(taskCounter);
                        }
                        allEpics.put(epic.getId(), epic);
                    }
                    case "SUBTASK" -> {
                        Subtask subtask = new Subtask(parts[2], parts[3], Integer.parseInt(parts[5]));
                        subtask.setStatus(Status.valueOf(parts[4]));
                        subtask.setId(Integer.parseInt(parts[0]));
                        allEpics.get(subtask.getEpicId()).getSubtaskIds().add(subtask.getId());
                        allSubtasks.put(subtask.getId(), subtask);
                        if (taskCounter < subtask.getId()) {
                            taskCounter = subtask.getId();
                            super.setTaskCounter(taskCounter);
                        }
                    }
                }
            }
            super.setTaskMap(allTasks);
            super.setEpicMap(allEpics);
            super.setSubtaskMap(allSubtasks);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
//---------------------Дополнительное задание--------------------------------

    public static void main(String[] args) {
        try {
            File tempFile = Files.createTempFile("temp", ".txt").toFile();
            TaskManager tm = Managers.loadFromFile(tempFile);
            tm.addTask(new Task("Задача 1", "Инфо зад.1"));
            tm.addTask(new Task("Задача 2", "Инфо зад.2"));
            tm.addEpic(new Epic("Задача эпик 1", "Эпик с подзадачей"));
            tm.addSubtask(new Subtask("Подзадача 1 эп.1004", "Инфо подзадачи 1", 1002));
            System.out.println("Выводим добавленные задачи из первого менеджера:");
            for (Task task : tm.getAllTasks()) {
                System.out.println(task.toString());
            }
            for (Epic epic : tm.getAllEpics()) {
                System.out.println(epic.toString());
            }
            for (Subtask subtask : tm.getAllSubtasks()) {
                System.out.println(subtask.toString());
            }
            System.out.println("\n---------------------------------------------------------");
            System.out.println("Создаем новый менеджер и передаем в него файл с задачами.");
            TaskManager newManager = Managers.loadFromFile(tempFile);
            System.out.println("Выводим задачи, загруженные из файла в новый менеджер:");
            for (Task task : newManager.getAllTasks()) {
                System.out.println(task.toString());
            }
            for (Epic epic : newManager.getAllEpics()) {
                System.out.println(epic.toString());
            }
            for (Subtask subtask : newManager.getAllSubtasks()) {
                System.out.println(subtask.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
