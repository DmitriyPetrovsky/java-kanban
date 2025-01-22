package server;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import enums.Type;
import exceptions.DateTimeOverlayException;
import manager.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    Gson gson = HttpTaskServer.getGson();

    public SubtasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка /subtasks запроса от клиента.");
        String jsonString;
        String method = httpExchange.getRequestMethod();
        String[] splitString = super.getUriArray(httpExchange);
        switch (method) {
            case "GET":
                if (splitString[splitString.length - 1].equals("subtasks") && splitString.length == 2) {
                    jsonString = gson.toJson(taskManager.getAllSubtasks());
                    super.sendText(httpExchange, jsonString, 200);
                } else if (splitString[splitString.length - 2].equals("subtasks") &&
                        taskManager.getByKeySubtask(Integer.parseInt(splitString[splitString.length - 1])) != null) {
                    jsonString = gson.toJson(taskManager.getByKeySubtask(Integer.parseInt(splitString[splitString.length - 1])));
                    super.sendText(httpExchange, jsonString, 200);
                } else {
                    jsonString = "Подзадача с указанным ID отсутствует";
                    super.sendNotFound(httpExchange, jsonString);
                }
                break;
            case "POST":
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                JsonElement jsonElement = JsonParser.parseString(body);
                if (!jsonElement.isJsonObject()) {
                    super.sendHasInteractions(httpExchange, "Невозможно добавить подзадачу");
                    return;
                }
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                Subtask subtask = gson.fromJson(jsonObject, Subtask.class);
                subtask.setEndTime(subtask.getStartTime().plus(subtask.getDuration()));
                subtask.setType(Type.SUBTASK);
                if (splitString[splitString.length - 1].equals("subtasks") && splitString.length == 2) {
                    try {
                        taskManager.addSubtask(subtask);
                    } catch (DateTimeOverlayException e) {
                        super.sendHasInteractions(httpExchange, e.getMessage());
                    }
                    super.sendText(httpExchange, "Подзадача успешно добавлена", 201);
                } else if (splitString[splitString.length - 2].equals("subtasks") &&
                        taskManager.getByKeySubtask(Integer.parseInt(splitString[splitString.length - 1])) != null) {
                    subtask.setId(Integer.parseInt(splitString[splitString.length - 1]));
                    try {
                        taskManager.updateSubtask(subtask);
                    } catch (DateTimeOverlayException e) {
                        super.sendHasInteractions(httpExchange, e.getMessage());
                    }
                    super.sendText(httpExchange, "Подзадача успешно обновлена", 201);
                } else {
                    jsonString = "Подзадача с указанным ID отсутствует";
                    super.sendNotFound(httpExchange, jsonString);
                }
                break;
            case "DELETE":
                if (splitString[splitString.length - 2].equals("subtasks") &&
                        taskManager.getByKeySubtask(Integer.parseInt(splitString[splitString.length - 1])) != null) {
                    taskManager.removeByIdSubtask(Integer.parseInt(splitString[splitString.length - 1]));
                    super.sendText(httpExchange, "Подзадача успешно удалена", 200);
                } else {
                    super.sendNotFound(httpExchange, "Подзадача с указанным ID отсутствует");
                }
        }
    }
}