package server;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import enums.Status;
import enums.Type;
import exceptions.DateTimeOverlayException;
import manager.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    Gson gson = HttpTaskServer.getGson();

    public EpicsHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка /epics запроса от клиента.");

        String jsonString;
        String method = httpExchange.getRequestMethod();
        String[] splitString = super.getUriArray(httpExchange);
        switch (method) {
            case "GET":
                if (splitString[splitString.length - 1].equals("epics") && splitString.length == 2) {
                    jsonString = gson.toJson(taskManager.getAllEpics());
                    super.sendText(httpExchange, jsonString, 200);
                } else if (splitString[splitString.length - 2].equals("epics") &&
                        taskManager.getByKeyEpic(Integer.parseInt(splitString[splitString.length - 1])) != null) {
                    jsonString = gson.toJson(taskManager.getByKeyEpic(Integer.parseInt(splitString[splitString.length - 1])));
                    super.sendText(httpExchange, jsonString, 200);
                } else if (splitString[splitString.length - 3].equals("epics") &&
                        taskManager.getByKeyEpic(Integer.parseInt(splitString[splitString.length - 2])) != null &&
                        splitString[splitString.length - 1].equals("subtasks")) {
                    jsonString = gson.toJson(taskManager.getSubtasksByEpicId(Integer.parseInt(splitString[splitString.length - 2])));
                    super.sendText(httpExchange, jsonString, 200);
                } else {
                    jsonString = "Эпик с указанным ID отсутствует";
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
                Epic epic = gson.fromJson(jsonObject, Epic.class);
                epic.setType(Type.EPIC);
                epic.setStatus(Status.NEW);
                epic.setSubtaskIds(new ArrayList<Integer>());
                try {
                    taskManager.addEpic(epic);
                } catch (DateTimeOverlayException e) {
                    super.sendHasInteractions(httpExchange, e.getMessage());
                }
                super.sendText(httpExchange, "Эпик успешно добавлен", 201);
                break;
            case "DELETE":
                if (splitString[splitString.length - 2].equals("epics") &&
                        taskManager.getByKeyEpic(Integer.parseInt(splitString[splitString.length - 1])) != null) {
                    taskManager.removeByIdEpic(Integer.parseInt(splitString[splitString.length - 1]));
                    super.sendText(httpExchange, "Эпик успешно удален", 200);
                } else {
                    super.sendNotFound(httpExchange, "Эпик с указанным ID отсутствует");
                }
        }

    }
}