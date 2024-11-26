package tests;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private static TaskManager manager;

    @BeforeEach
    void presets() {
        manager = Managers.getDefault();
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

    }

    @Test
    void addTasksShouldAddTaskInMap() {
        assertEquals(4, manager.getAllTasks().size(), "Количество добавленных Tasks не совпадает!");
        Task task = manager.getByKeyTask(1000);
        assertEquals("Задача 1", task.getTaskName(), "Название задачи с ID=1000 не совпадает!");
        assertEquals("Инфо зад.1", task.getInfo(), "Описание задачи с ID=1000 не совпадает!");
    }

    @Test
    void addEpicShouldAddEpicInMap() {
        assertEquals(2, manager.getAllEpics().size(), "Количество добавленных Epics не совпадает!");
        Epic epic = manager.getByKeyEpic(1004);
        assertEquals("Задача эпик 1", epic.getTaskName(), "Название Epic с ID=1004 не совпадает!");
        assertEquals("Эпик с тремя подзадачами", epic.getInfo(),
                "Описание Epic с ID=1004 не совпадает!");
    }

    @Test
    void addSubtaskhouldAddSubtaskInMap() {
        assertEquals(4, manager.getAllSubtasks().size(),
                "Количество добавленных Subtasks не совпадает!");
        Subtask subtask = manager.getByKeySubtask(1005);
        assertEquals("Подзадача 1 эп.1004", subtask.getTaskName(),
                "Название Subtask c ID=1005 не совпадает!");
        assertEquals("Инфо подзадачи 1", subtask.getInfo(),
                "Описание Subtask с ID=1005 не совпадает!");
    }

    @Test
    void updateTaskShouldUpdateTaskInMap() {
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
        Epic epic = new Epic("Измененный заголовок эпика с id 1004", "Измененное инфо эпика с id 1004");
        epic.setSubtaskIds(manager.getByKeyEpic(1004).getSubtaskIds());
        epic.setId(1004);
        manager.updateEpic(epic);
        assertEquals("Измененный заголовок эпика с id 1004", manager.getByKeyEpic(1004).getTaskName(),
                "Заголовки задач не совпадают!");
        assertNotEquals("Подзадача 1 эп.1004", manager.getByKeyEpic(1004).getTaskName(),
                "Заголовок задачи не изменился!");
        assertEquals("Измененное инфо эпика с id 1004", manager.getByKeyEpic(1004).getInfo(),
                "Описания задач не совпадают!");
        assertNotEquals("Эпик с тремя подзадачами", manager.getByKeyEpic(1004).getTaskName(),
                "Описание задачи не изменилось!");

    }

    @Test
    void updateSubtaskShouldUpdateSubtaskInMap() {
        Subtask subtask = new Subtask("Измененный заголовок сабтаска с id 1005",
                "Измененное инфо сабтаска с id 1005", 1004);
        subtask.setId(1005);
        manager.updateSubtask(subtask);
        assertNotNull(manager.getByKeySubtask(1005));
        assertEquals("Измененный заголовок сабтаска с id 1005",
                manager.getByKeySubtask(1005).getTaskName(), "Заголовки задач не совпадают!");
        assertNotEquals("Подзадача 1 эп.1004", manager.getByKeySubtask(1005).getTaskName(),
                "Заголовок задачи не изменился!");
        assertEquals("Измененное инфо сабтаска с id 1005", manager.getByKeySubtask(1005).getInfo(),
                "Описания задач не совпадают!");
        assertNotEquals("Инфо подзадачи 1", manager.getByKeySubtask(1005).getTaskName(),
                "Описание задачи не изменилось!");
    }

    @Test
    void getAllTasksShouldReturnListOfAllTasks() {
        List<Task> list;
        assertEquals(4, manager.getAllTasks().size(), "Количество добавленных Tasks не совпадает!");
        int id;
        boolean result = true;
        list = manager.getAllTasks();
        for (Task task : list) {
            id = task.getId();
            if (id != 1000 && id != 1001 && id != 1002 && id != 1003) {
                result = false;
            }
        }
        assertTrue(result, "Список Tasks содержит задачу с неверным ID");
    }

    @Test
    void getAllEpicsShouldReturnListOfAllEpics() {
        List<Epic> list;
        assertEquals(2, manager.getAllEpics().size(), "Количество добавленных Epics не совпадает!");
        int id;
        boolean result = true;
        list = manager.getAllEpics();
        for (Epic epic : list) {
            id = epic.getId();
            if (id != 1004 && id != 1008) {
                result = false;
            }
        }
        assertTrue(result, "Список Epics содержит задачу с неверным ID");
    }


    @Test
    void getAllSubtasksShouldReturnListOfAllSubtasks() {
        List<Subtask> list;
        assertEquals(4, manager.getAllSubtasks().size(), "Количество добавленных Subtasks не совпадает!");
        int id;
        boolean result = true;
        list = manager.getAllSubtasks();
        for (Subtask subtask : list) {
            id = subtask.getId();
            if (id != 1005 && id != 1006 && id != 1007 && id != 1009) {
                result = false;
            }
        }
        assertTrue(result, "Список Subtasks содержит задачу с неверным ID");
    }


    @Test
    void removeTasksShouldDeleteAllTasks() {
        assertEquals(4, manager.getAllTasks().size(), "Количество добавленных Tasks не совпадает!");
        manager.removeTasks();
        assertEquals(0, manager.getAllTasks().size(), "Количество Tasks не равно нулю!");

    }

    @Test
    void removeEpicsShouldDeleteAllEpics() {
        assertEquals(2, manager.getAllEpics().size(), "Количество добавленных Epics не совпадает!");
        manager.removeEpics();
        assertEquals(0, manager.getAllEpics().size(), "Количество Epics не равно нулю!");
    }

    @Test
    void removeSubtasksShouldDeleteAllSubtasks() {
        assertEquals(2, manager.getAllEpics().size(), "Количество добавленных Epics не совпадает!");
        manager.removeEpics();
        assertEquals(0, manager.getAllEpics().size(), "Количество Epics не равно нулю!");
    }

    @Test
    void getByKeyTaskShouldReturnTaskById() {
        Task task = manager.getByKeyTask(1001);
        assertNotNull(task, "Task is NULL");
        assertEquals(1001, task.getId(), "ID Task'a не равно 1001");

    }

    @Test
    void getByKeyEpicShouldReturnEpicById() {
        Epic epic = manager.getByKeyEpic(1004);
        assertNotNull(epic, "Epic is NULL");
        assertEquals(1004, epic.getId(), "ID Epic'a не равно 1004");
    }

    @Test
    void getByKeySubtaskShouldReturnSubtaskById() {
        Subtask subtask = manager.getByKeySubtask(1005);
        assertNotNull(subtask, "Subtask is NULL");
        assertEquals(1005, subtask.getId(), "ID Subtask'a не равно 1005");
    }

    @Test
    void removeByIdTaskShouldDeleteTask() {
        assertEquals(1000, manager.getByKeyTask(1000).getId(), "Task с ID=1000 отсутствует");
        manager.removeByIdTask(1000);
        assertNull(manager.getByKeyTask(1000), "Task не был удален!");
    }

    @Test
    void removeByIdSubtaskShouldDeleteSubtaskAndRecordInEpic() {
        assertEquals(1005, manager.getByKeySubtask(1005).getId(), "Subtask с ID=1005 отсутствует");
        Epic epic;
        List<Integer> subtasksList;
        int epicID = manager.getByKeySubtask(1005).getEpicId();
        epic = manager.getByKeyEpic(epicID);
        subtasksList = epic.getSubtaskIds();
        boolean isInList = false;
        for (Integer subtaskID : subtasksList) {
            if (subtaskID == 1005) {
                isInList = true;
                break;
            }
        }
        assertTrue(isInList, "Запись о Subtask с ID=1005 в Epic с ID=1004 не найдена!");
        manager.removeByIdSubtask(1005);
        isInList = false;
        for (Integer subtaskID : subtasksList) {
            if (subtaskID == 1005) {
                isInList = true;
                break;
            }
        }
        assertFalse(isInList, "Запись о Subtask с ID=1005 в Epic с ID=1004 не была удалена!");
        assertNull(manager.getByKeySubtask(1005), "Subtask не был удален!");
    }

    @Test
    void removeByIdEpicShouldDeleteEpicAndHisSubtasks() {
        assertEquals(1004, manager.getByKeyEpic(1004).getId(), "Epic с ID=1004 отсутствует");
        manager.removeByIdEpic(1004);
        List<Subtask> allSubtasks = manager.getAllSubtasks();
        boolean isInMap = false;
        for (Subtask subtask : allSubtasks) {
            if (subtask.getEpicId() == 1004) {
                isInMap = true;
                break;
            }
        }
        assertFalse(isInMap, "Subtask Epic'a c ID=1004 не удален");
        assertNull(manager.getByKeyEpic(1004), "Epic не был удален!");
    }

    @Test
    void getSubtasksByEpicIdShouldReturnEpicsSubtasksList() {
        assertEquals(3, manager.getSubtasksByEpicId(1004).size(), "Неверное количество Subtask'ов");
    }

    @Test
    void EpicShouldNotContainDeletedSubtaskInHisList() {
        Epic epic = manager.getByKeyEpic(1004);
        assertEquals(3, epic.getSubtaskIds().size());
        assertTrue(epic.getSubtaskIds().contains(1005));
        manager.removeByIdSubtask(1005);
        assertEquals(2, epic.getSubtaskIds().size());
        assertFalse(epic.getSubtaskIds().contains(1005));
    }
}