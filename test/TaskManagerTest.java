import enums.Status;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends Task> {

    public static TaskManager manager;

    public void fillManager() {
        manager.addTask(new Task("Задача 1", "Инфо зад.1", "18.12.2024 12:00", 90L));
        manager.addTask(new Task("Задача 2", "Инфо зад.2"));
        manager.addEpic(new Epic("Задача эпик 1", "Эпик с тремя подзадачами"));
        manager.addSubtask(new Subtask("Подзадача 1 эп.1002", "Инфо подзадачи 1", "18.12.2024 18:00", 30L, 1002));
        manager.addSubtask(new Subtask("Подзадача 2 эп.1002", "Инфо подзадачи 2", "18.12.2024 20:00", 60L, 1002));
        manager.addSubtask(new Subtask("Подзадача 3 эп.1002", "Инфо подзадачи 3", "18.12.2024 22:00", 15L, 1002));
        manager.addTask(new Task("Задача 3", "Инфо зад.3", "16.12.2024 10:00", 45L));
    }

    public void fillEpicAndSubtasks() {
        manager.addEpic(new Epic("Эпик", "Эпик с четырьмя подзадачами"));
        manager.addSubtask(new Subtask("Подзадача 1 эп.1000", "Подзадача 1001", "18.12.2024 18:00", 30L, 1000));
        manager.addSubtask(new Subtask("Подзадача 2 эп.1000", "Подзадача 1002", "18.12.2024 20:00", 60L, 1000));
        manager.addSubtask(new Subtask("Подзадача 3 эп.1000", "Подзадача 1003", "17.12.2024 12:00", 15L, 1000));
        manager.addSubtask(new Subtask("Подзадача 4 эп.1000", "Подзадача 1004", "18.12.2024 23:00", 15L, 1000));
    }

    public void allSubtasksNewTest() {
        fillEpicAndSubtasks();
        assertTrue(manager.getAllSubtasks().stream()
                .allMatch(subtask -> subtask.getStatus() == Status.NEW));
        assertTrue(manager.getByKeyEpic(1000).getStatus() == Status.NEW);
    }

    public void oneSubtaskInProgressTest() {
        fillEpicAndSubtasks();
        Subtask subtask = manager.getByKeySubtask(1002);
        subtask.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask);
        assertTrue(manager.getByKeyEpic(1000).getStatus() == Status.IN_PROGRESS);
    }

    public void allSubtasksDoneTest() {
        fillEpicAndSubtasks();
        manager.getAllSubtasks().forEach(subtask -> {subtask.setStatus(Status.DONE);
                                                    manager.updateSubtask(subtask);});
        assertTrue(manager.getByKeyEpic(1000).getStatus() == Status.DONE);
    }

    public void subtasksNewAndDoneTest() {
        fillEpicAndSubtasks();
        Subtask subtask = manager.getByKeySubtask(1002);
        subtask.setStatus(Status.DONE);
        manager.updateSubtask(subtask);
        assertTrue(manager.getByKeyEpic(1000).getStatus() == Status.IN_PROGRESS);
    }

    public void crossTimeTest() {
        fillManager();
        assertEquals(7, manager.getPrioritizedTasks().size());
        manager.addTask(new Task("Задача кросс-тайм", "Инфо", "16.12.2024 10:10", 90L));
        assertEquals(7, manager.getPrioritizedTasks().size());
    }

}
