import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Manager {
    private final static HashMap<Integer, Task> allTasks = new HashMap<>();
    private static int taskCounter = 0;

    public static int getTaskCounter() {
        return taskCounter;
    }

    public static void increaseTaskCounter() {
        taskCounter++;
    }

    public static void addTask(Task task) {
        if (allTasks.containsKey(task.getId())) {
            updateTask(task);
        } else if (!allTasks.containsKey(task.getId()) && !(task instanceof Subtask)) {
            allTasks.put(task.getId(), task);
        } else if (task instanceof Subtask && getByKey(((Subtask) task).getEpicId()) instanceof EpicTask) {
            (Objects.requireNonNull(getByKey(((Subtask) task).getEpicId())))
                    .getSubtasks()
                    .add(task.getId());
            allTasks.put(task.getId(), task);
        }
    }

    public static HashMap<Integer, Task> getAllTasks() {
        return allTasks;
    }

    public static void removeAll() {
        allTasks.clear();
    }

    public static Task getByKey(int id) {
        return allTasks.getOrDefault(id, null);
    }

    public static void removeTaskById(int id) {
        if (allTasks.containsKey(id)) {
            Task tempTask = allTasks.get(id);
            if (tempTask instanceof Subtask) {
                Task tempEpicTask = allTasks.get(((Subtask) tempTask).getEpicId());
                Integer idAsValue = id;
                tempEpicTask.getSubtasks().remove(idAsValue);
            } else if (tempTask instanceof EpicTask) {
                Task tempEpicTask = allTasks.get(id);
                List<Integer> tempList = tempEpicTask.getSubtasks();
                for (Integer ids : tempList) {
                    allTasks.remove(ids);
                }
            }
            allTasks.remove(id);
        }
    }

    public static HashMap<Integer, Task> getSubtasks(int id) {
        HashMap<Integer, Task> result = new HashMap<>();
        if (allTasks.get(id) instanceof EpicTask) {
            Task tempEpicTask = allTasks.get(id);
            List<Integer> tempList = tempEpicTask.getSubtasks();
            for (Integer ids : tempList) {
                result.put(ids, allTasks.get(ids));
            }
        }
        return result;
    }

    public static void printAll() {
        Task tempTask;
        int counter = 1;
        String result = "";

        for (Map.Entry<Integer, Task> entry : allTasks.entrySet()) {
            tempTask = entry.getValue();
            if (tempTask instanceof EpicTask) {
                List<Integer> tempList;
                tempList = tempTask.getSubtasks();

                result = result + "Задача " + counter +
                        ": Type = 'EpicTask', " +
                        "ID = '" + tempTask.getId() +
                        "', TaskName = '" + tempTask.getTaskName() +
                        "', Info = '" + tempTask.getInfo() +
                        "' Status = '" + tempTask.getStatus() + "' \n";
                counter++;


                for (Integer subtaskId : tempList) {
                    tempTask = allTasks.get(subtaskId);
                    result = result + "Задача " + counter +
                            ": -> Type = 'SubTask', " +
                            "ID = '" + tempTask.getId() +
                            "', TaskName = '" + tempTask.getTaskName() +
                            "', Info = '" + tempTask.getInfo() +
                            "' Status = '" + tempTask.getStatus() + "' \n";
                    counter++;

                }
            } else if (!(tempTask instanceof Subtask)) {
                result = result + "Задача " + counter +
                        ": Type = 'Task', " +
                        "ID = '" + tempTask.getId() +
                        "', TaskName = '" + tempTask.getTaskName() +
                        "', Info = '" + tempTask.getInfo() +
                        "' Status = '" + tempTask.getStatus() + "' \n";
                counter++;
            }

        }
        System.out.println(result);
    }

    public static void updateTask(Task task) {
        int id = task.getId();
        if (allTasks.containsKey(id) && allTasks.get(id).getClass() == task.getClass()) {
            if (task instanceof EpicTask && allTasks.get(task.getId()).getStatus() != task.getStatus()) {
                return;
            }
            allTasks.put(task.getId(), task);
            checkStatus();
        }
    }

    public static void checkStatus() {
        Task tempTask;
        for (Map.Entry<Integer, Task> entry : allTasks.entrySet()) {
            tempTask = entry.getValue();
            if (tempTask instanceof EpicTask) {
                int newCount = 0;
                int inProgressCount = 0;
                int doneCount = 0;
                HashMap<Integer, Task> subTasks = getSubtasks(tempTask.getId());
                if (!subTasks.isEmpty()) {
                    for (Map.Entry<Integer, Task> entrySub : subTasks.entrySet()) {
                        if (entrySub.getValue().getStatus() == Status.NEW) newCount++;
                        if (entrySub.getValue().getStatus() == Status.IN_PROGRESS) inProgressCount++;
                        if (entrySub.getValue().getStatus() == Status.DONE) doneCount++;
                    }
                    if (newCount == 0 && inProgressCount == 0 && doneCount > 0) {
                        if (tempTask.getStatus() != Status.DONE) {
                            tempTask.setStatus(Status.DONE);
                            allTasks.put(tempTask.getId(), tempTask);
                        }
                    }
                    if (inProgressCount > 0 || (doneCount > 0 && (inProgressCount != 0 || newCount != 0))) {
                        if (tempTask.getStatus() != Status.IN_PROGRESS) {
                            tempTask.setStatus(Status.IN_PROGRESS);
                            allTasks.put(tempTask.getId(), tempTask);
                        }
                    }
                    if (newCount > 0 && inProgressCount == 0 && doneCount == 0) {
                        if (tempTask.getStatus() != Status.NEW) {
                            tempTask.setStatus(Status.NEW);
                            allTasks.put(tempTask.getId(), tempTask);
                        }
                    }
                }
            }
        }

    }
}
