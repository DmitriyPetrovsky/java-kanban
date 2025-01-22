package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    Gson gson = HttpTaskServer.getGson();

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка /prioritized запроса от клиента.");

        String jsonString;
        String method = httpExchange.getRequestMethod();
        String[] splitString = super.getUriArray(httpExchange);
        if (method.equals("GET") && splitString[splitString.length - 1].equals("prioritized") && splitString.length == 2) {
            jsonString = gson.toJson(taskManager.getPrioritizedTasks());
            super.sendText(httpExchange, jsonString, 200);
        }
    }
}