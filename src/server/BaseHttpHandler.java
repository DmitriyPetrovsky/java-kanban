package server;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {

    protected void sendText(HttpExchange h, String text, int rCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(rCode, resp.length);
        try (OutputStream os = h.getResponseBody()) {
            os.write(resp);
        }
        h.close();
    }

    protected void sendNotFound(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/text=utf-8");
        h.sendResponseHeaders(404, resp.length);
        try (OutputStream os = h.getResponseBody()) {
            os.write(resp);
        }
        h.close();
    }

    protected void sendHasInteractions(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/text=utf-8");
        h.sendResponseHeaders(406, resp.length);
        try (OutputStream os = h.getResponseBody()) {
            os.write(resp);
        }
        h.close();
    }


    protected String[] getUriArray(HttpExchange h) {
        URI uri = h.getRequestURI();
        String path = uri.getPath();
        String[] splitString = path.split("/");
        return splitString;
    }
}
