package manager;

import enums.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> allTasks = new HashMap<>();
    private final Map<Integer, Epic> allEpics = new HashMap<>();
    private final Map<Integer, Subtask> allSubtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private int taskCounter = 999;

    public void increaseTaskCounter() {
        taskCounter++;
    }

    @Override
    public void addTask(Task task) {
        increaseTaskCounter();
        task.setId(taskCounter);
        allTasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        increaseTaskCounter();
        epic.setId(taskCounter);
        allEpics.put(epic.getId(), epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        increaseTaskCounter();
        subtask.setId(taskCounter);
        allSubtasks.put(subtask.getId(), subtask);
        allEpics.get(subtask.getEpicId()).getSubtaskIds().add(subtask.getId());
    }

    @Override
    public void updateTask(Task task) {
        if (allTasks.containsKey(task.getId())) {
            allTasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (allEpics.containsKey(epic.getId())) {
            allEpics.put(epic.getId(), epic);
            checkStatus();
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (allSubtasks.containsKey(subtask.getId())) {
            allSubtasks.put(subtask.getId(), subtask);
            checkStatus();
        }
    }

    @Override
    public List<Task> getAllTasks() { return new ArrayList<>(allTasks.values()); }

    @Override
    public List<Epic> getAllEpics() { return new ArrayList<>(allEpics.values()); }

    @Override
    public List<Subtask> getAllSubtasks() { return new ArrayList<>(allSubtasks.values()); }

    @Override
    public void removeTasks() {
        allTasks.clear();
    }

    @Override
    public void removeEpics() {
        allEpics.clear();
        allSubtasks.clear();
    }

    @Override
    public void removeSubtasks() {
        allSubtasks.clear();
        for (Map.Entry<Integer, Epic> entryEpic : allEpics.entrySet()) {
            entryEpic.getValue().getSubtaskIds().clear();
        }
        checkStatus();
    }

    @Override
    public Task getByKeyTask(int id) {
        Task task;
        task = allTasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getByKeyEpic(int id) {
        Epic epic;
        epic = allEpics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getByKeySubtask(int id) {
        Subtask subtask;
        subtask = allSubtasks.get(id);
        historyManager.add(subtask);
        return allSubtasks.get(id);
    }

    @Override
    public void removeByIdTask(int id) {
        allTasks.remove(id);
    }

    @Override
    public void removeByIdSubtask(int id) {
        if (allSubtasks.containsKey(id)) {
            Epic tempEpic = allEpics.get(allSubtasks.get(id).getEpicId());
            tempEpic.getSubtaskIds().remove((Integer) id);
            allEpics.put(tempEpic.getId(), tempEpic);
            allSubtasks.remove(id);
        }
        checkStatus();
    }

    @Override
    public void removeByIdEpic(int id) {
        if (allEpics.containsKey(id)) {
            Epic tempEpic = allEpics.get(id);
            for (int i : tempEpic.getSubtaskIds()) {
                allSubtasks.remove(i);
            }
            allEpics.remove(id);
        }
    }

    @Override
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

    public List<Task> getHistory() {
        return historyManager.getHistory();
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

