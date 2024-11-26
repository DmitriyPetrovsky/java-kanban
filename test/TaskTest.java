
import enums.Status;
import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void taskCreationTest() {
        Task task = new Task("Новая задача", "Описание новой задачи");
        assertEquals("Новая задача", task.getTaskName(), "Названия задач не совпадают!");
        assertEquals("Описание новой задачи", task.getInfo(), "Описания задач не совпадают!");
        assertEquals(0, task.getId(), "В качестве ID по умолчанию не присвоился \"0\"!");
        assertEquals(Status.NEW, task.getStatus(), "Статус задачи не \"NEW\"");
    }

}