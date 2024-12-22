
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest {

    @BeforeEach
    void presets() {
        super.manager = Managers.getDefault();
    }

    @Test
    void addTasksShouldAddTaskInMap() {
        fillManager();
        assertEquals(3, manager.getAllTasks().size(), "Количество добавленных Tasks не совпадает!");
        Task task = manager.getByKeyTask(1000);
        assertEquals("Задача 1", task.getTaskName(), "Название задачи с ID=1000 не совпадает!");
        assertEquals("Инфо зад.1", task.getInfo(), "Описание задачи с ID=1000 не совпадает!");
    }

    @Test
    void addEpicShouldAddEpicInMap() {
        fillManager();
        assertEquals(1, manager.getAllEpics().size(), "Количество добавленных Epics не совпадает!");
        Epic epic = manager.getByKeyEpic(1002);
        assertEquals("Задача эпик 1", epic.getTaskName(), "Название Epic с ID=1002 не совпадает!");
        assertEquals("Эпик с тремя подзадачами", epic.getInfo(),
                "Описание Epic с ID=1002 не совпадает!");
    }

    @Test
    void addSubtaskhouldAddSubtaskInMap() {
        fillManager();
        assertEquals(3, manager.getAllSubtasks().size(),
                "Количество добавленных Subtasks не совпадает!");
        Subtask subtask = manager.getByKeySubtask(1003);
        assertEquals("Подзадача 1 эп.1002", subtask.getTaskName(),
                "Название Subtask c ID=1003 не совпадает!");
        assertEquals("Инфо подзадачи 1", subtask.getInfo(),
                "Описание Subtask с ID=1003 не совпадает!");
    }

    @Test
    void updateTaskShouldUpdateTaskInMap() {
        fillManager();
        Task task = new Task("Измененный заголовок задачи с id 1000", "Измененное инфо задачи с id 1000");
        task.setId(1000);
        manager.updateTask(task);
        assertEquals("Измененный заголовок задачи с id 1000", manager.getByKeyTask(1000).getTaskName(),
                "Заголовки задач не совпадают!");
        assertNotEquals("Задача 1", manager.getByKeyTask(1000).getTaskName(),
                "Заголовок задачи не изменился!");
        assertEquals("Измененное инфо задачи с id 1000", manager.getByKeyTask(1000).getInfo(),
                "Описания задач не совпадают!");
        assertNotEquals("Инфо зад.1", manager.getByKeyTask(1000).getTaskName(),
                "Описание задачи не изменилось!");
    }

    @Test
    void updateEpicShouldUpdateEpicInMap() {
        fillManager();
        Epic epic = new Epic("Измененный заголовок эпика с id 1002", "Измененное инфо эпика с id 1002");
        epic.setSubtaskIds(manager.getByKeyEpic(1002).getSubtaskIds());
        epic.setId(1002);
        manager.updateEpic(epic);
        assertEquals("Измененный заголовок эпика с id 1002", manager.getByKeyEpic(1002).getTaskName(),
                "Заголовки задач не совпадают!");
        assertNotEquals("Подзадача 1 эп.1002", manager.getByKeyEpic(1002).getTaskName(),
                "Заголовок задачи не изменился!");
        assertEquals("Измененное инфо эпика с id 1002", manager.getByKeyEpic(1002).getInfo(),
                "Описания задач не совпадают!");
        assertNotEquals("Эпик с тремя подзадачами", manager.getByKeyEpic(1002).getTaskName(),
                "Описание задачи не изменилось!");

    }

    @Test
    void updateSubtaskShouldUpdateSubtaskInMap() {
        fillManager();
        Subtask subtask = new Subtask("Измененный заголовок сабтаска с id 1003",
                "Измененное инфо сабтаска с id 1003", 1002);
        subtask.setId(1003);
        manager.updateSubtask(subtask);
        assertNotNull(manager.getByKeySubtask(1003));
        assertEquals("Измененный заголовок сабтаска с id 1003",
                manager.getByKeySubtask(1003).getTaskName(), "Заголовки задач не совпадают!");
        assertEquals("Измененное инфо сабтаска с id 1003", manager.getByKeySubtask(1003).getInfo(),
                "Описания задач не совпадают!");
    }

    @Test
    void getAllTasksShouldReturnListOfAllTasks() {
        fillManager();
        List<Task> list;
        assertEquals(3, manager.getAllTasks().size(), "Количество добавленных Tasks не совпадает!");
        int id;
        boolean result = true;
        list = manager.getAllTasks();
        for (Task task : list) {
            id = task.getId();
            if (id != 1000 && id != 1001 && id != 1006) {
                result = false;
            }
        }
        assertTrue(result, "Список Tasks содержит задачу с неверным ID");
    }

    @Test
    void getAllEpicsShouldReturnListOfAllEpics() {
        fillManager();
        List<Epic> list;
        assertEquals(1, manager.getAllEpics().size(), "Количество добавленных Epics не совпадает!");
        int id;
        boolean result = true;
        list = manager.getAllEpics();
        for (Epic epic : list) {
            id = epic.getId();
            if (id != 1002) {
                result = false;
            }
        }
        assertTrue(result, "Список Epics содержит задачу с неверным ID");
    }


    @Test
    void getAllSubtasksShouldReturnListOfAllSubtasks() {
        fillManager();
        List<Subtask> list;
        assertEquals(3, manager.getAllSubtasks().size(), "Количество добавленных Subtasks не совпадает!");
        int id;
        boolean result = true;
        list = manager.getAllSubtasks();
        for (Subtask subtask : list) {
            id = subtask.getId();
            if (id != 1003 && id != 1004 && id != 1005) {
                result = false;
            }
        }
        assertTrue(result, "Список Subtasks содержит задачу с неверным ID");
    }


    @Test
    void removeTasksShouldDeleteAllTasks() {
        fillManager();
        assertEquals(3, manager.getAllTasks().size(), "Количество добавленных Tasks не совпадает!");
        manager.removeTasks();
        assertTrue(manager.getAllTasks().isEmpty(), "Количество Tasks не равно нулю!");

    }

    @Test
    void removeEpicsShouldDeleteAllEpics() {
        fillManager();
        assertEquals(1, manager.getAllEpics().size(), "Количество добавленных Epics не совпадает!");
        manager.removeEpics();
        assertTrue(manager.getAllEpics().isEmpty(), "Количество Epics не равно нулю!");
    }

    @Test
    void removeSubtasksShouldDeleteAllSubtasks() {
        fillManager();
        assertEquals(3, manager.getAllSubtasks().size(), "Количество добавленных Subtasks не совпадает!");
        manager.removeSubtasks();
        assertTrue(manager.getAllSubtasks().isEmpty(), "Количество Subtasks не равно нулю!");
    }

    @Test
    void getByKeyTaskShouldReturnTaskById() {
        fillManager();
        Task task = manager.getByKeyTask(1001);
        assertNotNull(task, "Task is NULL");
        assertEquals(1001, task.getId(), "ID Task'a не равно 1001");

    }

    @Test
    void getByKeyEpicShouldReturnEpicById() {
        fillManager();
        Epic epic = manager.getByKeyEpic(1002);
        assertNotNull(epic, "Epic is NULL");
        assertEquals(1002, epic.getId(), "ID Epic'a не равно 1002");
    }

    @Test
    void getByKeySubtaskShouldReturnSubtaskById() {
        fillManager();
        Subtask subtask = manager.getByKeySubtask(1003);
        assertNotNull(subtask, "Subtask is NULL");
        assertEquals(1003, subtask.getId(), "ID Subtask'a не равно 1003");
    }

    @Test
    void removeByIdTaskShouldDeleteTask() {
        fillManager();
        assertEquals(1000, manager.getByKeyTask(1000).getId(), "Task с ID=1000 отсутствует");
        manager.removeByIdTask(1000);
        assertNull(manager.getByKeyTask(1000), "Task не был удален!");
    }

    @Test
    void removeByIdSubtaskShouldDeleteSubtaskAndRecordInEpic() {
        fillManager();
        assertEquals(1003, manager.getByKeySubtask(1003).getId(), "Subtask с ID=1003 отсутствует");
        Epic epic;
        List<Integer> subtasksList;
        int epicID = manager.getByKeySubtask(1003).getEpicId();
        epic = manager.getByKeyEpic(epicID);
        subtasksList = epic.getSubtaskIds();
        boolean isInList = false;
        for (Integer subtaskID : subtasksList) {
            if (subtaskID == 1003) {
                isInList = true;
                break;
            }
        }
        assertTrue(isInList, "Запись о Subtask с ID=1003 в Epic с ID=1002 не найдена!");
        manager.removeByIdSubtask(1003);
        isInList = false;
        for (Integer subtaskID : subtasksList) {
            if (subtaskID == 1003) {
                isInList = true;
                break;
            }
        }
        assertFalse(isInList, "Запись о Subtask с ID=1003 в Epic с ID=1002 не была удалена!");
        assertNull(manager.getByKeySubtask(1003), "Subtask не был удален!");
    }

    @Test
    void removeByIdEpicShouldDeleteEpicAndHisSubtasks() {
        fillManager();
        assertEquals(1002, manager.getByKeyEpic(1002).getId(), "Epic с ID=1002 отсутствует");
        assertEquals(3, manager.getAllSubtasks().size());
        assertEquals(1, manager.getAllEpics().size());
        manager.removeByIdEpic(1002);

        assertEquals(0, manager.getAllSubtasks().size());
        assertEquals(0, manager.getAllEpics().size());
    }

    @Test
    void getSubtasksByEpicIdShouldReturnEpicsSubtasksList() {
        fillManager();
        assertEquals(3, manager.getSubtasksByEpicId(1002).size(), "Неверное количество Subtask'ов");
    }

    @Test
    void epicShouldNotContainDeletedSubtaskInHisList() {
        fillManager();
        Epic epic = manager.getByKeyEpic(1002);
        assertEquals(3, epic.getSubtaskIds().size());
        assertTrue(epic.getSubtaskIds().contains(1004));
        manager.removeByIdSubtask(1004);
        assertEquals(2, epic.getSubtaskIds().size());
        assertFalse(epic.getSubtaskIds().contains(1004));
    }

    @Test
    public void allSubtasksNewTest() {
        super.allSubtasksNewTest();
    }

    @Test
    public void oneSubtaskInProgressTest() {
        super.oneSubtaskInProgressTest();
    }

    @Test
    public void allSubtasksDoneTest() {
        super.allSubtasksDoneTest();
    }

    @Test
    public void subtasksNewAndDoneTest() {
        super.subtasksNewAndDoneTest();
    }

    @Test
    public void crossTimeTest() {
        super.crossTimeTest();
    }
}