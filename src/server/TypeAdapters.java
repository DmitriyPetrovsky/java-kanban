package server;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import manager.Managers;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class TypeAdapters {
    static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
            if (localDateTime != null) {
                jsonWriter.value(localDateTime.format(Managers.getDateTimeFormatter()));
            } else {
                jsonWriter.value("null");
            }
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            } else {
                String dateStr = jsonReader.nextString();
                if ("null".equals(dateStr)) {
                    return null;
                }
                return LocalDateTime.parse(dateStr, Managers.getDateTimeFormatter());
            }
        }
    }

    static class DurationTypeAdapter extends TypeAdapter<Duration> {

        @Override
        public void write(JsonWriter jsonWriter, Duration value) throws IOException {
            if (value == null) {
                jsonWriter.nullValue();
                return;
            }

            jsonWriter.value(value.toMinutes());
        }

        @Override
        public Duration read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }

            long minutes = jsonReader.nextLong();
            return Duration.ofMinutes(minutes);
        }
    }
}
