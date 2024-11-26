package tests;

import enums.Status;
import org.junit.jupiter.api.Test;
import tasks.Subtask;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void subtaskCreationTest() {
        Subtask subtask = new Subtask("Сабтаск-тест", "Описание сабтаска", 1004);
        assertEquals("Сабтаск-тест", subtask.getTaskName(), "Названия Subtask'ов не совпадают!");
        assertEquals("Описание сабтаска", subtask.getInfo(), "Описания Subtask'ов не совпадают!");
        assertEquals(0, subtask.getId(), "В качестве ID по умолчанию не присвоился \"0\"!");
        assertEquals(1004, subtask.getEpicId(), "Заданный ID Epic'a (1004) не присвоился");
        assertEquals(Status.NEW, subtask.getStatus(), "Статус Subtask'a не \"NEW\"");
    }

}