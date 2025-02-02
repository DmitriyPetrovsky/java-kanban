package server;

import com.google.gson.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import enums.Method;
import enums.Type;
import exceptions.DateTimeOverlayException;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    Gson gson = HttpTaskServer.getGson();

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка /tasks запроса от клиента.");
        String jsonString;
        String method = httpExchange.getRequestMethod();
        String[] splitString = super.getUriArray(httpExchange);
        switch (method) {
            case Method.GET:
                if (splitString[splitString.length - 1].equals("tasks") && splitString.length == 2) {
                    jsonString = gson.toJson(taskManager.getAllTasks());
                    super.sendText(httpExchange, jsonString, 200);
                } else if (splitString[splitString.length - 2].equals("tasks") &&
                        taskManager.getByKeyTask(Integer.parseInt(splitString[splitString.length - 1])) != null) {
                    jsonString = gson.toJson(taskManager.getByKeyTask(Integer.parseInt(splitString[splitString.length - 1])));
                    super.sendText(httpExchange, jsonString, 200);
                } else {
                    jsonString = "Задача с указанным ID отсутствует";
                    super.sendNotFound(httpExchange, jsonString);
                }
                break;
            case Method.POST:
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                JsonElement jsonElement = JsonParser.parseString(body);
                if (!jsonElement.isJsonObject()) {
                    super.sendHasInteractions(httpExchange, "Невозможно добавить задачу");
                    return;
                }
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                Task task = gson.fromJson(jsonObject, Task.class);
                task.setEndTime(task.getStartTime().plus(task.getDuration()));
                task.setType(Type.TASK);
                if (splitString[splitString.length - 1].equals("tasks") && splitString.length == 2) {
                    try {
                        taskManager.addTask(task);
                    } catch (DateTimeOverlayException e) {
                        super.sendHasInteractions(httpExchange, e.getMessage());
                    }
                    super.sendText(httpExchange, "Задача успешно добавлена", 201);
                } else if (splitString[splitString.length - 2].equals("tasks") &&
                        taskManager.getByKeyTask(Integer.parseInt(splitString[splitString.length - 1])) != null) {
                    task.setId(Integer.parseInt(splitString[splitString.length - 1]));
                    try {
                        taskManager.updateTask(task);
                    } catch (DateTimeOverlayException e) {
                        super.sendHasInteractions(httpExchange, e.getMessage());
                    }
                    super.sendText(httpExchange, "Задача успешно обновлена", 201);
                } else {
                    jsonString = "Задача с указанным ID отсутствует";
                    super.sendNotFound(httpExchange, jsonString);
                }
                break;
            case Method.DELETE:
                if (splitString[splitString.length - 2].equals("tasks") &&
                        taskManager.getByKeyTask(Integer.parseInt(splitString[splitString.length - 1])) != null) {
                    taskManager.removeByIdTask(Integer.parseInt(splitString[splitString.length - 1]));
                    super.sendText(httpExchange, "Задача успешно удалена", 200);
                } else {
                    super.sendNotFound(httpExchange, "Задача с указанным ID отсутствует");
                }
        }
    }
}