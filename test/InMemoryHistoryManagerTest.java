

import exceptions.DateTimeOverlayException;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static TaskManager manager;

    @BeforeEach
    void presets() {
        manager = Managers.getDefault();
    }

    void fillManager() {
        try {
            manager.addTask(new Task("Задача 1", "Инфо зад.1", "18.12.2024 12:00", 90L));
            manager.addTask(new Task("Задача 2", "Инфо зад.2"));
            manager.addEpic(new Epic("Задача эпик 1", "Эпик с двумя подзадачами"));
            manager.addSubtask(new Subtask("Подзадача 1 эп.1004", "Инфо подзадачи 1", "18.12.2024 18:00", 30L, 1002));
            manager.addSubtask(new Subtask("Подзадача 2 эп.1004", "Инфо подзадачи 2", "18.12.2024 20:00", 60L, 1002));
            manager.addSubtask(new Subtask("Подзадача 3 эп.1004", "Инфо подзадачи 3", "18.12.2024 22:00", 15L, 1002));
            manager.addTask(new Task("Задача 3", "Пересечение по времени", "18.12.2024 18:20", 45L));
            manager.addTask(new Task("Задача 4", "Инфо зад.4", "16.12.2024 10:00", 45L));
        } catch (DateTimeOverlayException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void historyTest() {
        fillManager();
        manager.getByKeyTask(1000);
        manager.getByKeyEpic(1002);
        manager.getByKeySubtask(1003);
        manager.getByKeyTask(1001);
        assertEquals(4, manager.getHistory().size(), "Количество задач в истории отличается от 4-х");
        manager.getByKeySubtask(1005);
        assertEquals(5, manager.getHistory().size(), "Количество задач в истории отличается от 5-и");
        List<Task> history = manager.getHistory();
        Task task = new Task("Новая задача", "Новое описание");
        task.setId(1001);
        manager.updateTask(task);
        assertEquals("Новая задача", manager.getByKeyTask(1001).getTaskName(),
                "Имя задачи не было изменено в карте");
        assertEquals("Новое описание", manager.getByKeyTask(1001).getInfo(),
                "Описание задачи не было изменено в карте");
        assertEquals(1001, manager.getByKeyTask(1001).getId(), "Неверный ID");
        manager.getByKeyTask(1001);
        task = history.get(2);
        assertEquals("Подзадача 1 эп.1004", task.getTaskName(), "Неверное имя задачи");
        assertEquals("Инфо подзадачи 1", task.getInfo(), "Неверное описание задачи");
        assertEquals(1003, task.getId(), "Неверный ID");
    }

    @Test
    void removeHeadTaskTest() {
        fillManager();
        manager.getByKeyTask(1000);
        manager.getByKeyEpic(1002);
        manager.getByKeyTask(1001);
        manager.getByKeySubtask(1003);
        manager.removeByIdTask(1000);
        List<Task> history = manager.getHistory();
        assertEquals(3, history.size());
        assertEquals("Задача эпик 1", history.getFirst().getTaskName());
    }

    @Test
    void removeFromCenterSubtaskTest() {
        fillManager();
        manager.getByKeyTask(1000);
        manager.getByKeyEpic(1002);
        manager.getByKeySubtask(1003);
        manager.getByKeyTask(1001);
        manager.removeByIdTask(1003);
        List<Task> history = manager.getHistory();
        assertEquals(3, history.size(), "Количество задач в истории не " +
                "совпадает");
    }

    @Test
    void removeTailSubtaskTest() {
        fillManager();
        manager.getByKeyTask(1000);
        manager.getByKeyEpic(1002);
        manager.getByKeyTask(1001);
        manager.getByKeySubtask(1003);
        manager.removeByIdTask(1003);
        List<Task> history = manager.getHistory();
        assertEquals(3, history.size());
        assertEquals("Задача 2", history.get(2).getTaskName(), "Названия задач не " +
                "совпадают");
    }

    @Test
    void removeTailEpicWithHisSubtaskTest() {
        fillManager();
        manager.getByKeyTask(1000);
        manager.getByKeyTask(1001);
        manager.getByKeySubtask(1004);
        manager.getByKeySubtask(1003);
        manager.getByKeyEpic(1002);
        manager.removeByIdEpic(1002);
        List<Task> history = manager.getHistory();
        assertEquals(2, history.size());
    }

    @Test
    void checkIfDouble() {
        fillManager();
        manager.getByKeyTask(1000);
        manager.getByKeyTask(1001);
        manager.getByKeySubtask(1004);
        manager.getByKeySubtask(1003);
        manager.getByKeyEpic(1002);
        manager.getByKeyTask(1000);
        List<Task> history = manager.getHistory();
        assertEquals(5, history.size());
        assertEquals("Задача 1", history.getLast().getTaskName(), "Последняя задача не является " +
                "последней просмотренной");
    }

}