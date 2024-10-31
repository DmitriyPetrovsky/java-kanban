package tests;

import manager.HistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void managersCreatesTest() {
        InMemoryTaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertTrue(taskManager instanceof InMemoryTaskManager, "Объект InMemoryTaskManager не создан!");
        assertTrue(historyManager instanceof HistoryManager, "Объект HistoryManager не создан!");
    }

}