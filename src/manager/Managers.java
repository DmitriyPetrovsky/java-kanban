package manager;

import java.io.File;
import java.time.format.DateTimeFormatter;

public class Managers {

    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fbtm = new FileBackedTaskManager(file);
        fbtm.load(file);
        return fbtm;
    }

    public static DateTimeFormatter getDtf() {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    }
}
