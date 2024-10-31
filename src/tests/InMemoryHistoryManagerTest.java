package tests;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    void HistoryTest() {
        TaskManager manager = Managers.getDefault();
        manager.addTask(new Task("Задача 1", "Инфо зад.1"));
        manager.addTask(new Task("Задача 2", "Инфо зад.2"));
        manager.addTask(new Task("Задача 3", "Инфо зад.3"));
        manager.addTask(new Task("Задача 4", "Инфо зад.4"));
        manager.addEpic(new Epic("Задача эпик 1", "Эпик с тремя подзадачами"));
        manager.addSubtask(new Subtask("Подзадача 1 эп.1004", "Инфо подзадачи 1", 1004));
        manager.addSubtask(new Subtask("Подзадача 2 эп.1004", "Инфо подзадачи 2", 1004));
        manager.addSubtask(new Subtask("Подзадача 3 эп.1004", "Инфо подзадачи 3", 1004));
        manager.addEpic(new Epic("Задача эпик 2", "Эпик с одной подзадачей"));
        manager.addSubtask(new Subtask("Подзадача 1 эп. 1008", "Инфо подзадачи 1", 1008));

        manager.getByKeyTask(1002);
        manager.getByKeySubtask(1009);
        manager.getByKeyTask(1001);
        manager.getByKeyEpic(1008);
        assertEquals(4, manager.getHistory().size(), "Количество задач в истории отличается от 4-х");
        manager.getByKeySubtask(1006);
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
        assertEquals("Задача 2", task.getTaskName(), "Неверное имя задачи");
        assertEquals("Инфо зад.2", task.getInfo(), "Неверное описание задачи");
        assertEquals(1001, task.getId(), "Неверный ID");

    }

}