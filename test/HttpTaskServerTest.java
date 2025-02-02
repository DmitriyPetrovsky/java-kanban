import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskServerTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer server = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    @BeforeEach
    public void setUp() {
        manager.removeTasks();
        manager.removeEpics();
        server.start();
    }

    @AfterEach
    public void tearDown() {
        server.stop();
    }

    @Test
    public void testGetAllTasks() throws IOException, InterruptedException {
        manager.addTask(new Task("Test 1", "Testing task 1", "20.01.2025 09:00", 30L));
        manager.addTask(new Task("Test 2", "Testing task 2", "20.01.2025 10:00", 30L));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> deserializedTasks = gson.fromJson(response.body(), new TypeToken<List<Task>>() {}.getType());
        assertNotNull(deserializedTasks, "Задачи не возвращаются");
        assertEquals(2, deserializedTasks.size(), "Некорректное количество задач");
        assertEquals(manager.getAllTasks(), deserializedTasks, "Несовпадение списков задач");
    }

    @Test
    public void testGetTaskByID() throws IOException, InterruptedException {
        manager.addTask(new Task("Test 1", "Testing task 1", "20.01.2025 09:00", 30L));
        manager.addTask(new Task("Test 2", "Testing task 2", "20.01.2025 10:00", 30L));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1000");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Task deserializedTask = gson.fromJson(response.body(), new TypeToken<Task>() {}.getType());
        assertNotNull(deserializedTask, "Задача не возвращается");
        assertEquals(manager.getByKeyTask(1000), deserializedTask, "Несовпадение задач");
    }

    @Test
    public void testGetNonexistentTaskByID() throws IOException, InterruptedException {
        manager.addTask(new Task("Test 1", "Testing task 1", "20.01.2025 09:00", 30L));
        manager.addTask(new Task("Test 2", "Testing task 2", "20.01.2025 10:00", 30L));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/2000");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", "20.01.2025 09:00", 30L);
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Task> tasksFromManager = manager.getAllTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.getFirst().getTaskName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        manager.addTask(new Task("Test 2", "Testing task 2", "20.01.2025 09:00", 30L));
        Task task = new Task("UPDATED TASK NAME", "UPDATED TASK INFO", "20.01.2025 09:00", 30L);
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1000");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Task> tasksFromManager = manager.getAllTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("UPDATED TASK NAME", tasksFromManager.getFirst().getTaskName(), "Некорректное имя задачи");
    }

    @Test
    public void testAddCrosstimeTask() throws IOException, InterruptedException {
        manager.addTask(new Task("Test 1", "Testing task 1", "20.01.2025 09:00", 30L));
        Task task = new Task("Crossed-time task", "Testing task 2", "20.01.2025 09:15", 30L);
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());
        List<Task> tasksFromManager = manager.getAllTasks();
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 1", tasksFromManager.getFirst().getTaskName(), "Некорректное имя задачи");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        manager.addTask(new Task("Test 1", "Testing task 1", "20.01.2025 09:00", 30L));
        manager.addTask(new Task("Test 2", "Testing task 2", "20.01.2025 10:00", 30L));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1000");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        assertEquals(2, manager.getAllTasks().size(), "Некорректное количество задач");
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getAllTasks().size(), "Некорректное количество задач");
    }

    @Test
    public void testGetAllSubtasks() throws IOException, InterruptedException {
        manager.addEpic(new Epic("Эпик с ID 1000", "Описание"));
        manager.addSubtask(new Subtask("Test 1", "Testing subtask 1", "20.01.2025 09:00", 30L, 1000));
        manager.addSubtask(new Subtask("Test 2", "Testing subtask 2", "20.01.2025 10:00", 30L, 1000));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> deserializedSubtasks = gson.fromJson(response.body(), new TypeToken<List<Task>>() {}.getType());
        assertNotNull(deserializedSubtasks, "Задачи не возвращаются");
        assertEquals(2, deserializedSubtasks.size(), "Некорректное количество задач");
        assertEquals(manager.getAllSubtasks(), deserializedSubtasks, "Несовпадение списков задач");
    }

    @Test
    public void testGetSubtaskByID() throws IOException, InterruptedException {
        manager.addEpic(new Epic("Эпик с ID 1000", "Описание"));
        manager.addSubtask(new Subtask("Test 1", "Testing subtask 1", "20.01.2025 09:00", 30L, 1000));
        manager.addSubtask(new Subtask("Test 2", "Testing subtask 2", "20.01.2025 10:00", 30L, 1000));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/1001");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Subtask deserializedSubtask = gson.fromJson(response.body(), new TypeToken<Subtask>() {}.getType());
        assertNotNull(deserializedSubtask, "Подзадача не возвращается");
        assertEquals(manager.getByKeySubtask(1001), deserializedSubtask, "Несовпадение подзадач");
    }

    @Test
    public void testGetNonexistentSubtaskByID() throws IOException, InterruptedException {
        // создаём задачи
        manager.addEpic(new Epic("Эпик с ID 1000", "Описание"));
        manager.addSubtask(new Subtask("Test 1", "Testing subtask 1", "20.01.2025 09:00", 30L, 1000));
        manager.addSubtask(new Subtask("Test 2", "Testing subtask 2", "20.01.2025 10:00", 30L, 1000));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2000");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        manager.addEpic(new Epic("Эпик с ID 1000", "Описание"));
        manager.addSubtask(new Subtask("Test 1", "Testing subtask 1", "20.01.2025 09:00", 30L, 1000));
        Subtask subtask = new Subtask("Test 2", "Testing subtask 2", "20.01.2025 10:00", 30L, 1000);
        String taskJson = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Subtask> subtasksFromManager = manager.getAllSubtasks();
        assertNotNull(subtasksFromManager, "Подзадачи не возвращаются");
        assertEquals(2, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", manager.getByKeySubtask(1002).getTaskName(), "Некорректное имя задачи");
    }

    @Test
    public void testAddCrosstimeSubtask() throws IOException, InterruptedException {
        manager.addEpic(new Epic("Эпик с ID 1000", "Описание"));
        manager.addSubtask(new Subtask("Test 1", "Testing subtask 1", "20.01.2025 09:00", 30L, 1000));
        Subtask subtask = new Subtask("Test 2", "Testing subtask 2", "20.01.2025 09:15", 30L, 1000);
        String taskJson = gson.toJson(subtask);
        assertEquals(1, manager.getAllSubtasks().size(), "Некорректное количество подзадач");
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());
        assertEquals(1, manager.getAllSubtasks().size(), "Некорректное количество подзадач");
    }

    @Test
    public void testUpdateSubtask() throws IOException, InterruptedException {
        manager.addEpic(new Epic("Эпик с ID 1000", "Описание"));
        manager.addSubtask(new Subtask("Test 1", "Testing subtask 1", "20.01.2025 09:00", 30L, 1000));
        Subtask subtask = new Subtask("UPDATED SUBTASK", "Testing subtask 2", "20.01.2025 10:00", 30L, 1000);
        String taskJson = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/1001");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Subtask> subtasksFromManager = manager.getAllSubtasks();
        assertNotNull(subtasksFromManager, "Подзадачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("UPDATED SUBTASK", manager.getByKeySubtask(1001).getTaskName(), "Некорректное имя задачи");
    }

    @Test
    public void testDeleteSubask() throws IOException, InterruptedException {
        manager.addEpic(new Epic("Эпик с ID 1000", "Описание"));
        manager.addSubtask(new Subtask("Test 1", "Testing subtask 1", "20.01.2025 09:00", 30L, 1000));
        manager.addSubtask(new Subtask("Test 2", "Testing subtask 2", "20.01.2025 10:00", 30L, 1000));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/1001");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        assertEquals(2, manager.getAllSubtasks().size(), "Некорректное количество подзадач");
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getAllSubtasks().size(), "Некорректное количество подзадач");
    }

    @Test
    public void testGetAllEpics() throws IOException, InterruptedException {
        manager.addEpic(new Epic("Test 1", "Testing epic 1"));
        manager.addEpic(new Epic("Test 2", "Testing epic 2"));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> deserializedEpics = gson.fromJson(response.body(), new TypeToken<List<Epic>>() {}.getType());
        assertNotNull(deserializedEpics, "Эпики не возвращаются");
        assertEquals(2, deserializedEpics.size(), "Некорректное количество эпиков");
        assertEquals(manager.getAllEpics(), deserializedEpics, "Несовпадение списков эпиков");
    }

    @Test
    public void testGetEpicByID() throws IOException, InterruptedException {
        // создаём задачи
        manager.addEpic(new Epic("Test 1", "Testing epic 1"));
        manager.addEpic(new Epic("Test 2", "Testing epic 2"));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1000");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Epic deserializedEpic = gson.fromJson(response.body(), new TypeToken<Epic>() {}.getType());
        assertNotNull(deserializedEpic, "Эпик не возвращается");
        assertEquals(manager.getByKeyEpic(1000), deserializedEpic, "Несовпадение эпиков");
    }

    @Test
    public void testGetNonexistentEpicByID() throws IOException, InterruptedException {
        manager.addEpic(new Epic("Test 1", "Testing epic 1"));
        manager.addEpic(new Epic("Test 2", "Testing epic 2"));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/2000");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("EpicTest", "Testing epic");
        String taskJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Epic> epicsFromManager = manager.getAllEpics();
        assertNotNull(epicsFromManager, "Эпики не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество эпиков");
        assertEquals("EpicTest", epicsFromManager.getFirst().getTaskName(), "Некорректное имя эпика");
    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        manager.addEpic(new Epic("Test 1", "Testing epic 1"));
        manager.addEpic(new Epic("Test 2", "Testing epic 2"));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1000");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        assertEquals(2, manager.getAllEpics().size(), "Некорректное количество эпиков");
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getAllEpics().size(), "Некорректное количество эпиков");
    }

    @Test
    public void testGetHistory() throws IOException, InterruptedException {
        manager.addEpic(new Epic("Эпик с ID 1000", "Описание"));
        manager.addSubtask(new Subtask("Subtask 1", "Testing subtask 1", "20.01.2025 09:00", 30L, 1000));
        manager.addSubtask(new Subtask("Subtask 2", "Testing subtask 2", "20.01.2025 10:00", 30L, 1000));
        manager.addTask(new Task("Task 1", "Testing task 1", "20.01.2025 11:00", 30L));
        manager.addTask(new Task("Task 2", "Testing task 2", "20.01.2025 12:00", 30L));
        manager.getByKeyEpic(1000);
        manager.getByKeyTask(1003);
        manager.getByKeySubtask(1001);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> deserializedHistory = gson.fromJson(response.body(), new TypeToken<List<Task>>() {}.getType());
        assertNotNull(deserializedHistory, "История не возвращается");
        assertEquals(3, deserializedHistory.size(), "Некорректное количество задач в истории");
    }

    @Test
    public void testGetPrioritizedList() throws IOException, InterruptedException {
        manager.addEpic(new Epic("Эпик с ID 1000", "Описание"));
        manager.addSubtask(new Subtask("Subtask 1", "Testing subtask 1", "20.01.2025 09:00", 30L, 1000));
        manager.addSubtask(new Subtask("Subtask 2", "Testing subtask 2", "20.01.2025 10:00", 30L, 1000));
        manager.addTask(new Task("Task 1", "Testing task 1", "20.01.2025 06:00", 30L));
        manager.addTask(new Task("Task 2", "Testing task 2", "20.01.2025 12:00", 30L));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> deserializedHistory = gson.fromJson(response.body(), new TypeToken<List<Task>>() {}.getType());
        assertNotNull(deserializedHistory, "Список не возвращается");
        assertEquals("Task 1", deserializedHistory.getFirst().getTaskName(), "Некорректная сортировка");
    }

}
