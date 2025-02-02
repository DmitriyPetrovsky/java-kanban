import manager.FileBackedTaskManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tasks.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest extends TaskManagerTest {
    private File file;

    @BeforeEach
    void setUp() throws IOException {
        file = File.createTempFile("temp", ".txt");
        manager = new FileBackedTaskManager(file);
    }

    @AfterEach
    void tearDown() {
        file.delete();
    }

    @Test
    void addTask() {
        fillManager();
        assertEquals(3, manager.getAllTasks().size(), "Количество добавленных Tasks не совпадает!");
        Task task = manager.getByKeyTask(1000);
        assertEquals("Задача 1", task.getTaskName(), "Название задачи с ID=1000 не совпадает!");
        assertEquals("Инфо зад.1", task.getInfo(), "Описание задачи с ID=1000 не совпадает!");
    }

    @Test
    void saveTest() {
        fillManager();
        assertEquals(readFile().size(), 8, "Количество строк в файле не совпадает с количеством задач");
        assertEquals(readFile().get(1), "1000/TASK/Задача 1/Инфо зад.1/NEW/18.12.2024 12:00/90/");
    }

    @Test
    void loadTest() {
        fillManager();
        assertEquals(manager.getAllEpics().size(), 1);
        assertEquals(manager.getAllSubtasks().size(), 3);
        assertEquals(manager.getAllTasks().size(), 3);
        TaskManager newManager = Managers.loadFromFile(file);
        assertEquals(newManager.getAllEpics().size(), 1);
        assertEquals(newManager.getAllSubtasks().size(), 3);
        assertEquals(newManager.getAllTasks().size(), 3);
    }

    List<String> readFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            List<String> list = new ArrayList<>();
            while (br.ready()) {
                list.add(br.readLine());
            }
            return list;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
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