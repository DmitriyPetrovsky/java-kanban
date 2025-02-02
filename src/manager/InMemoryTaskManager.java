package manager;

import enums.Status;
import enums.Type;
import exceptions.DateTimeOverlayException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> allTasks = new HashMap<>();
    private Map<Integer, Epic> allEpics = new HashMap<>();
    private Map<Integer, Subtask> allSubtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private Set<Task> sortedByDateTasks = new TreeSet<>(Task::compareByDate);

    private int taskCounter = 999;

    public void increaseTaskCounter() {
        taskCounter++;
    }

    public void setTaskCounter(int taskCounter) {
        this.taskCounter = taskCounter;
    }

    private void setEpicTime(Subtask subtask) {
        Epic epic = getByKeyEpic(subtask.getEpicId());
        Optional<Subtask> minTimeSubtask = getAllSubtasks().stream()
                .filter(sbt -> sbt.getEpicId() == epic.getId() && sbt.getStartTime() != null)
                .min(Subtask::minTimeCompare);
        Optional<Subtask> maxTimeSubtask = getAllSubtasks().stream()
                .filter(sbt -> sbt.getEpicId() == epic.getId() && sbt.getEndTime() != null)
                .min(Subtask::maxTimeCompare);
        if (minTimeSubtask.isPresent() && maxTimeSubtask.isPresent()) {
            epic.setStartTime(minTimeSubtask.get().getStartTime());
            epic.setEndTime(maxTimeSubtask.get().getEndTime());
            epic.setDuration(Duration.between(minTimeSubtask.get().getStartTime(), maxTimeSubtask.get().getEndTime()));
        } else {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(null);
        }
    }

    @Override
    public void addTask(Task task) throws DateTimeOverlayException {
        try {
            checkOverlay(task);
            increaseTaskCounter();
            task.setId(taskCounter);
            allTasks.put(task.getId(), task);
            fillSortedSet();
        } catch (DateTimeOverlayException e) {
            System.out.println(e.getMessage());
            throw new DateTimeOverlayException(e.getMessage());
        }
    }

    @Override
    public void addEpic(Epic epic) throws DateTimeOverlayException {
        try {
            checkOverlay(epic);
            increaseTaskCounter();
            epic.setId(taskCounter);
            allEpics.put(epic.getId(), epic);
            fillSortedSet();
        }  catch (DateTimeOverlayException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addSubtask(Subtask subtask) throws DateTimeOverlayException {
        try {
            checkOverlay(subtask);
            increaseTaskCounter();
            subtask.setId(taskCounter);
            allSubtasks.put(subtask.getId(), subtask);
            allEpics.get(subtask.getEpicId()).getSubtaskIds().add(subtask.getId());
            setEpicTime(subtask);
            sortedByDateTasks.add(subtask);
            fillSortedSet();
        } catch (DateTimeOverlayException e) {
            System.out.println(e.getMessage());
            throw new DateTimeOverlayException(e.getMessage());
        }
    }

    @Override
    public void updateTask(Task task) throws DateTimeOverlayException {
        try {
            checkOverlay(task);
            if (allTasks.containsKey(task.getId())) {
                allTasks.put(task.getId(), task);
                fillSortedSet();
            }
        } catch (DateTimeOverlayException e) {
            System.out.println(e.getMessage());
            throw new DateTimeOverlayException(e.getMessage());
        }
    }

    @Override
    public void updateEpic(Epic epic) throws DateTimeOverlayException {
        try {
            checkOverlay(epic);
            if (allEpics.containsKey(epic.getId())) {
                allEpics.put(epic.getId(), epic);
                checkStatus();
                fillSortedSet();
            }
        } catch (DateTimeOverlayException e) {
            System.out.println(e.getMessage());
            throw new DateTimeOverlayException(e.getMessage());
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) throws DateTimeOverlayException {
        try {
            checkOverlay(subtask);
            if (allSubtasks.containsKey(subtask.getId())) {
                allSubtasks.put(subtask.getId(), subtask);
                setEpicTime(subtask);
                checkStatus();
                fillSortedSet();
            }
        } catch (DateTimeOverlayException e) {
            System.out.println(e.getMessage());
            throw new DateTimeOverlayException(e.getMessage());
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(allTasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(allEpics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(allSubtasks.values());
    }

    @Override
    public void removeTasks() {
        allTasks.clear();
        fillSortedSet();
    }

    @Override
    public void removeEpics() {
        allEpics.clear();
        allSubtasks.clear();
        fillSortedSet();
    }

    @Override
    public void removeSubtasks() {
        allSubtasks.clear();
        for (Map.Entry<Integer, Epic> entryEpic : allEpics.entrySet()) {
            entryEpic.getValue().getSubtaskIds().clear();
        }
        getAllEpics().stream()
                        .peek(epic -> {
                            epic.setStartTime(null);
                            epic.setDuration(null);
                            epic.setEndTime(null);
                        })
                        .collect(Collectors.toList());
        checkStatus();
        fillSortedSet();
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
        historyManager.remove(id);
        fillSortedSet();
    }

    @Override
    public void removeByIdSubtask(int id) {
        if (allSubtasks.containsKey(id)) {
            Epic tempEpic = allEpics.get(allSubtasks.get(id).getEpicId());
            tempEpic.getSubtaskIds().remove((Integer) id);
            allEpics.put(tempEpic.getId(), tempEpic);
            allSubtasks.remove(id);
            historyManager.remove(id);
        }
        checkStatus();
        fillSortedSet();
    }

    @Override
    public void removeByIdEpic(int id) {
        if (allEpics.containsKey(id)) {
            Epic tempEpic = allEpics.get(id);
            for (int i : tempEpic.getSubtaskIds()) {
                allSubtasks.remove(i);
                historyManager.remove(i);
            }
            allEpics.remove(id);
            historyManager.remove(id);
            fillSortedSet();
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

    public Map<Integer, Task> getTaskMap() {
        return allTasks;
    }

    public Map<Integer, Subtask> getSubtaskMap() {
        return allSubtasks;
    }

    public Map<Integer, Epic> getEpicMap() {
        return allEpics;
    }

    public void setTaskMap(Map<Integer, Task> allTasks) {
        this.allTasks = allTasks;
    }

    public void setSubtaskMap(Map<Integer, Subtask> allSubtasks) {
        this.allSubtasks = allSubtasks;
    }

    public void setEpicMap(Map<Integer, Epic> allEpics) {
        this.allEpics = allEpics;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedByDateTasks);
    }

    public void fillSortedSet() {
        sortedByDateTasks.clear();
        List<Task> allTasksSet = Stream.of(getAllTasks(), getAllEpics(), getAllSubtasks())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        sortedByDateTasks.addAll(allTasksSet);
    }

    public boolean isTaskValid(Task task) {
        Subtask subtask;
        if (task.getType() == Type.SUBTASK) {
            subtask = (Subtask) task;
        } else {
            subtask = null;
        }
        if (task.getStartTime() == null || task.getEndTime() == null) return true;
        return !sortedByDateTasks.stream()
                .filter(t -> (!(t.getType() == Type.EPIC) && !(task.getType() == Type.SUBTASK)) || (subtask != null && subtask.getEpicId() != t.getId()))
                .filter(t -> t.getStartTime() != null && t.getEndTime() != null)
                .anyMatch(existingTask -> task.overlaps(existingTask) && task.getId() != existingTask.getId());
    }

    public void checkOverlay(Task task) {
        if (!isTaskValid(task)) {
            throw new DateTimeOverlayException("Задача '" + task.getTaskName()
                    + "' пересекается по времени с существующей и не будет добавлена.");
        }
    }
}

