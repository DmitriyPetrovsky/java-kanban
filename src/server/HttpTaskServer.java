package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    TaskManager taskManager;
    HttpServer httpServer;

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapters.LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new TypeAdapters.DurationTypeAdapter())
                .create();
    }

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;


        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks", new TasksHandler(taskManager));
            httpServer.createContext("/epics", new EpicsHandler(taskManager));
            httpServer.createContext("/subtasks", new SubtasksHandler(taskManager));
            httpServer.createContext("/history", new HistoryHandler(taskManager));
            httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));

            System.out.println("Сервер успешно запущен на порту " + PORT);
        } catch (IOException e) {
            System.out.println("Не удалось создать HTTP-сервер");
        }
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

    public static void main(String[] args) {
        TaskManager tm = Managers.getDefault();
        HttpTaskServer taskServer = new HttpTaskServer(tm);
        taskServer.start();
        tm.addTask(new Task("Задача 1", "Инфо зад.1", "18.12.2024 12:00", 90L));
        tm.addTask(new Task("Задача 2", "Инфо зад.2"));
        tm.addEpic(new Epic("Задача эпик 1", "Эпик с тремя подзадачами"));
        tm.addSubtask(new Subtask("Подзадача 1 эп.1002", "Инфо подзадачи 1", "18.12.2024 18:00", 30L, 1002));
        tm.addSubtask(new Subtask("Подзадача 2 эп.1002", "Инфо подзадачи 2", "18.12.2024 20:00", 60L, 1002));
        tm.addSubtask(new Subtask("Подзадача 3 эп.1002", "Инфо подзадачи 3", "18.12.2024 22:00", 15L, 1002));
        tm.addTask(new Task("Задача 3", "Инфо зад.3", "16.12.2024 10:00", 45L));
    }
}
