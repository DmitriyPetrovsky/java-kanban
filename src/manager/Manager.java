package manager;

import enums.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {
    private final Map<Integer, Task> allTasks = new HashMap<>();
    private final Map<Integer, Epic> allEpics = new HashMap<>();
    private final Map<Integer, Subtask> allSubtasks = new HashMap<>();
    private int taskCounter = 999;

    public void increaseTaskCounter() {
        taskCounter++;
    }

    public void addTask(Task task) {
        increaseTaskCounter();
        task.setId(taskCounter);
        allTasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        increaseTaskCounter();
        epic.setId(taskCounter);
        allEpics.put(epic.getId(), epic);
    }

    public void addSubtask(Subtask subtask) {
        increaseTaskCounter();
        subtask.setId(taskCounter);
        allSubtasks.put(subtask.getId(), subtask);
        allEpics.get(subtask.getEpicId()).getSubtaskIds().add(subtask.getId());
    }

    public void updateTask(Task task) {
        if (allTasks.containsKey(task.getId())) {
            allTasks.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (allEpics.containsKey(epic.getId())) {
            allEpics.put(epic.getId(), epic);
            checkStatus();
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (allSubtasks.containsKey(subtask.getId())) {
            allSubtasks.put(subtask.getId(), subtask);
            checkStatus();
        }
    }


    public List<Task> getAllTasks() { return new ArrayList<>(allTasks.values()); }

    public List<Epic> getAllEpics() { return new ArrayList<>(allEpics.values()); }

    public List<Subtask> getAllSubtasks() { return new ArrayList<>(allSubtasks.values()); }

    public void removeTasks() {
        allTasks.clear();
    }

    public void removeEpics() {
        allEpics.clear();
        allSubtasks.clear();
    }

    public void removeSubtasks() {
        allSubtasks.clear();
        for (Map.Entry<Integer, Epic> entryEpic : allEpics.entrySet()) {
            entryEpic.getValue().getSubtaskIds().clear();
        }
        checkStatus();
    }

    public Task getByKeyTask(int id) {
        return allTasks.get(id);
    }

    public Epic getByKeyEpic(int id) {
        return allEpics.get(id);
    }

    public Subtask getByKeySubtask(int id) {
        return allSubtasks.get(id);
    }

    public void removeByIdTask(int id) {
        allTasks.remove(id);
    }

    public void removeByIdSubtask(int id) {
        if (allSubtasks.containsKey(id)) {
            Epic tempEpic = allEpics.get(allSubtasks.get(id).getEpicId());
            tempEpic.getSubtaskIds().remove((Integer) id);
            allEpics.put(tempEpic.getId(), tempEpic);
            allSubtasks.remove(id);
        }
        checkStatus();
    }

    public void removeByIdEpic(int id) {
        if (allEpics.containsKey(id)) {
            Epic tempEpic = allEpics.get(id);
            for (int i : tempEpic.getSubtaskIds()) {
                allSubtasks.remove(i);
            }
            allEpics.remove(id);
        }
    }

    public List<Subtask> getSubtasksByEpicId(int id) {
        List<Subtask> result = new ArrayList<>();
        if (allEpics.containsKey(id)) {
            List<Integer> tempList = allEpics.get(id).getSubtaskIds();
            for (Integer ids : tempList) {
                result.add(allSubtasks.get(ids));
            }
        }
        return result;
    }

    private void checkStatus() {
        Epic tempEpic;
        for (Map.Entry<Integer, Epic> entry : allEpics.entrySet()) {
            tempEpic = entry.getValue();
            int newCount = 0;
            int inProgressCount = 0;
            int doneCount = 0;
            List<Subtask> subTasks = getSubtasksByEpicId(tempEpic.getId());
            if (!subTasks.isEmpty()) {
                for (Subtask subtask : subTasks) {
                    if (subtask.getStatus() == Status.NEW) newCount++;
                    if (subtask.getStatus() == Status.IN_PROGRESS) inProgressCount++;
                    if (subtask.getStatus() == Status.DONE) doneCount++;
                }
                if (newCount == 0 && inProgressCount == 0 && doneCount > 0) {
                    if (tempEpic.getStatus() != Status.DONE) {
                        tempEpic.setStatus(Status.DONE);
                        allEpics.put(tempEpic.getId(), tempEpic);
                    }
                }
                if (inProgressCount > 0 || (doneCount > 0 && (inProgressCount != 0 || newCount != 0))) {
                    if (tempEpic.getStatus() != Status.IN_PROGRESS) {
                        tempEpic.setStatus(Status.IN_PROGRESS);
                        allEpics.put(tempEpic.getId(), tempEpic);
                    }
                }
                if (newCount > 0 && inProgressCount == 0 && doneCount == 0) {
                    if (tempEpic.getStatus() != Status.NEW) {
                        tempEpic.setStatus(Status.NEW);
                        allEpics.put(tempEpic.getId(), tempEpic);
                    }
                }
            } else if (tempEpic.getSubtaskIds().isEmpty()) {
                tempEpic.setStatus(Status.NEW);
                allEpics.put(tempEpic.getId(), tempEpic);
            }
        }
    }
}

