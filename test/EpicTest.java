

import enums.Status;
import org.junit.jupiter.api.Test;
import tasks.Epic;

import static org.junit.jupiter.api.Assertions.*;


class EpicTest {
    @Test
    void epicCreationTest() {
        Epic epic = new Epic("Эпик-тест", "Описание эпика");
        assertEquals("Эпик-тест", epic.getTaskName(), "Названия Epic'ов не совпадают!");
        assertEquals("Описание эпика", epic.getInfo(), "Описания Epic'ов не совпадают!");
        assertEquals(0, epic.getId(), "В качестве ID по умолчанию не присвоился \"0\"!");
        assertEquals(0, epic.getSubtaskIds().size(), "Список ID подзадач не равен \"0\"");
        assertEquals(Status.NEW, epic.getStatus(), "Статус Epic'a не \"NEW\"");
    }

}