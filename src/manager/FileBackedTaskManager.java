package manager;

import enums.Status;
import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
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
            String header = "id/type/name/status/description/startTime/duration/epic\n";
            fw.write(header);
            for (Task t : tasks) {
                try {
                    fw.write(toString(t));
                } catch (ManagerSaveException e) {
                    System.out.println(e.getMessage());
                }
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
                task.getStatus().toString(), task.getStringStartTime(),
                task.getStringDuration(), subtasksEpicId(task)};
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
                        loadTime(task, parts[5], parts[6]);
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
                        loadTime(epic, parts[5], parts[6]);
                        if (taskCounter < epic.getId()) {
                            taskCounter = epic.getId();
                            super.setTaskCounter(taskCounter);
                        }
                        allEpics.put(epic.getId(), epic);
                    }
                    case "SUBTASK" -> {
                        Subtask subtask = new Subtask(parts[2], parts[3], Integer.parseInt(parts[7]));
                        subtask.setStatus(Status.valueOf(parts[4]));
                        subtask.setId(Integer.parseInt(parts[0]));
                        loadTime(subtask, parts[5], parts[6]);
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
            super.fillSortedSet();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadTime(Task task, String startTime, String duration) {
        if (!startTime.equals(task.getNullTimeConst()) && !duration.equals(task.getNullTimeConst())) {
            task.setStartTime(LocalDateTime.parse(startTime, Managers.getDateTimeFormatter()));
            task.setDuration(Duration.ofMinutes(Long.parseLong(duration)));
            task.setEndTime(task.getStartTime().plus(task.getDuration()));
        }
    }
}
