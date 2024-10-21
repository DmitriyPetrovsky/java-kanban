package manager;

import enums.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {
    private final Map<Integer, Task> allTasks = new HashMap<>();
    private final Map<Integer, Epic> allEpics = new HashMap<>();
    private final Map<Integer, Subtask> allSubtasks = new HashMap<>();
    private static int taskCounter = 0;

    public static int getTaskCounter() {
        return taskCounter;
    }

    public static void increaseTaskCounter() {
        taskCounter++;
    }

    public void addTask(Task task) {
        if (allTasks.containsKey(task.getId())) {
            updateTask(task);
        } else if (!allSubtasks.containsKey(task.getId()) && !allEpics.containsKey(task.getId())) {
            allTasks.put(task.getId(), task);
        }
    }

    public void addEpic(Epic epic) {
        if (allEpics.containsKey(epic.getId())) {
            updateEpic(epic);
        } else if (!allTasks.containsKey(epic.getId()) && !allSubtasks.containsKey(epic.getId())) {
            allEpics.put(epic.getId(), epic);
        }
    }

    public void addSubtask(Subtask subtask) {
        if (allEpics.containsKey(subtask.getEpicId()) && allSubtasks.containsKey(subtask.getId())) {
            updateSubtask(subtask);
        } else if (allEpics.containsKey(subtask.getEpicId()) && !allSubtasks.containsKey(subtask.getId())) {
            allSubtasks.put(subtask.getId(), subtask);
            allEpics.get(subtask.getEpicId()).getSubtasks().add(subtask.getId());
        }
    }

    private void updateTask(Task task) {
        allTasks.put(task.getId(), task);
    }

    private void updateEpic(Epic epic) {
        if (allEpics.get(epic.getId()).getStatus() == epic.getStatus()) {
            allEpics.put(epic.getId(), epic);
        }
        checkStatus();
    }

    private void updateSubtask(Subtask subtask) {
        if (allEpics.containsKey(subtask.getEpicId())) {
            allSubtasks.put(subtask.getId(), subtask);
        }
        checkStatus();
    }


    public Map<Integer, Task> getAllTasks() {
        return allTasks;
    }

    public Map<Integer, Epic> getAllEpics() {
        return allEpics;
    }

    public Map<Integer, Subtask> getAllSubtasks() {
        return allSubtasks;
    }

    public void removeAll() {
        allTasks.clear();
        allEpics.clear();
        allSubtasks.clear();
    }

    public Task getByKeyTask(int id) {
        return allTasks.getOrDefault(id, null);
    }

    public Epic getByKeyEpic(int id) {
        return allEpics.getOrDefault(id, null);
    }

    public Subtask getByKeySubtask(int id) {
        return allSubtasks.getOrDefault(id, null);
    }

    public void removeById(int id) {
        if (allTasks.containsKey(id)) {
            allTasks.remove(id);
        } else if (allSubtasks.containsKey(id)) {
            Epic tempEpic = allEpics.get(allSubtasks.get(id).getEpicId());
            tempEpic.getSubtasks().remove((Integer) id);
            allEpics.put(tempEpic.getId(), tempEpic);
            allSubtasks.remove(id);
        } else if (allEpics.containsKey(id)) {
            Epic tempEpic = allEpics.get(id);
            for (int i : tempEpic.getSubtasks()) {
                allSubtasks.remove(i);
            }
            allEpics.remove(id);

        }
        checkStatus();
    }

    public Map<Integer, Subtask> getSubtasks(int id) {
        Map<Integer, Subtask> result = new HashMap<>();
        if (allEpics.containsKey(id)) {
            List<Integer> tempList = allEpics.get(id).getSubtasks();
            for (Integer ids : tempList) {
                result.put(ids, allSubtasks.get(ids));
            }
        }
        return result;
    }

    public void printAll() {

        for (Map.Entry<Integer, Task> entryTask : allTasks.entrySet()) {
            System.out.println(entryTask.getValue().toString());
        }
        for (Map.Entry<Integer, Epic> entryEpic : allEpics.entrySet()) {
            Epic tempEpic = entryEpic.getValue();
            System.out.println(tempEpic.toString());
            if (getSubtasks(tempEpic.getId()) != null) {
                for (Map.Entry<Integer, Subtask> entrySub : getSubtasks(tempEpic.getId()).entrySet()) {
                    Subtask tempSubs = entrySub.getValue();
                    System.out.println(tempSubs.toString());
                }
            }
        }
    }

    private void checkStatus() {
        Epic tempEpic;
        for (Map.Entry<Integer, Epic> entry : allEpics.entrySet()) {
            tempEpic = entry.getValue();
            int newCount = 0;
            int inProgressCount = 0;
            int doneCount = 0;
            Map<Integer, Subtask> subTasks = getSubtasks(tempEpic.getId());
            if (!subTasks.isEmpty()) {
                for (Map.Entry<Integer, Subtask> entrySub : subTasks.entrySet()) {
                    if (entrySub.getValue().getStatus() == Status.NEW) newCount++;
                    if (entrySub.getValue().getStatus() == Status.IN_PROGRESS) inProgressCount++;
                    if (entrySub.getValue().getStatus() == Status.DONE) doneCount++;
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
            } else if (tempEpic.getSubtasks().isEmpty()) {
                tempEpic.setStatus(Status.NEW);
                allEpics.put(tempEpic.getId(), tempEpic);
            }
        }
    }

}

