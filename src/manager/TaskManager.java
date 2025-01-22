package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;
import java.util.Map;

public interface TaskManager {

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    Map<Integer, Task> getTaskMap();

    Map<Integer, Subtask> getSubtaskMap();

    Map<Integer, Epic> getEpicMap();

    void removeTasks();

    void removeEpics();

    void removeSubtasks();

    Task getByKeyTask(int id);

    Epic getByKeyEpic(int id);

    Subtask getByKeySubtask(int id);

    void removeByIdTask(int id);

    void removeByIdEpic(int id);

    void removeByIdSubtask(int id);

    List<Subtask> getSubtasksByEpicId(int id);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

}
