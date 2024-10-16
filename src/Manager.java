import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {
    private static HashMap<Integer, Task> allTasks  = new HashMap<>();


    public static void addTask(Task task) {
        if (!allTasks.containsKey(task.getId())) {
            int id = task.getId();
            allTasks.put(id, task);
        }
        if (task instanceof Subtask && getByKey(((Subtask) task).getEpicId()) instanceof EpicTask) {
            ((EpicTask) getByKey(((Subtask) task).getEpicId()))
                    .getSubtasks()
                    .add(task.getId());
        }
    }

    public static void updateTask(int oldId, Task task) {
        if (allTasks.containsKey(oldId) && !allTasks.containsKey(task.getId())) {
            allTasks.remove(oldId);
            addTask(task);
        }
    }

    public static HashMap<Integer, Task> getAllTasks() {
        return allTasks;
    }

    public static void removeAll() {
        allTasks.clear();
    }

    public static Task getByKey(int id) {
        if (allTasks.containsKey(id)) {
            return allTasks.get(id);
        } else {
            return null;
        }
    }
    public static void removeTaskById(int id){
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

    public static HashMap<Integer, Task> getSubtasks(int id){
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
                List<Integer> tempList = new ArrayList<>();
                tempList = ((EpicTask) tempTask).getSubtasks();

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
            } else if (!(tempTask instanceof Subtask) && tempTask instanceof Task) {
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
}
