
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
        assertInstanceOf(InMemoryTaskManager.class, taskManager, "Объект InMemoryTaskManager не создан!");
        assertInstanceOf(HistoryManager.class, historyManager, "Объект HistoryManager не создан!");
    }

}