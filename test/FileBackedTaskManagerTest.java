import manager.FileBackedTaskManager;
import manager.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {
    private FileBackedTaskManager manager;
    private File file;

    @BeforeEach
    void setUp() throws IOException {
        file = File.createTempFile("temp", ".txt");
        manager = Managers.loadFromFile(file);
    }

    @AfterEach
    void tearDown() {
        file.delete();
    }

    @Test
    void addTask() {
        fillManager();
        assertEquals(2, manager.getAllTasks().size(), "Количество добавленных Tasks не совпадает!");
        Task task = manager.getByKeyTask(1000);
        assertEquals("Задача 1", task.getTaskName(), "Название задачи с ID=1000 не совпадает!");
        assertEquals("Инфо зад.1", task.getInfo(), "Описание задачи с ID=1000 не совпадает!");
    }

    @Test
    void saveTest() {
        fillManager();
        assertEquals(readFile().size(), 5, "Количество строк в файле не совпадает с количеством задач");
        assertEquals(readFile().get(2), "1001/TASK/Задача 2/Инфо зад.2/NEW/");
    }

    @Test
    void loadTest() {
        fillManager();
        manager.setTaskMap(new HashMap<>());
        manager.setEpicMap(new HashMap<>());
        manager.setSubtaskMap(new HashMap<>());
        assertEquals(manager.getAllEpics().size(), 0);
        assertEquals(manager.getAllSubtasks().size(), 0);
        assertEquals(manager.getAllTasks().size(), 0);
        manager.load(file);
        assertEquals(manager.getAllEpics().size(), 1);
        assertEquals(manager.getAllSubtasks().size(), 1);
        assertEquals(manager.getAllTasks().size(), 2);
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

    void fillManager() {
        manager.addTask(new Task("Задача 1", "Инфо зад.1"));
        manager.addTask(new Task("Задача 2", "Инфо зад.2"));
        manager.addEpic(new Epic("Задача эпик 1", "Эпик с подзадачей"));
        manager.addSubtask(new Subtask("Подзадача 1 эп.1004", "Инфо подзадачи 1", 1002));
    }


}