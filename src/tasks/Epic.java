package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtasks = new ArrayList<>();

    public Epic(String taskName, String info) {
        super(taskName, info);
    }

    public List<Integer> getSubtasks() {
        return subtasks;
    }

    public String toString() {
        String result;
        result = "Type = 'Эпик', ID = '" + getId() + "', name = '" + getTaskName() +
                "', info = '" + getInfo() + "', status = '" + getStatus() + "', subtasks = '" + subtasks.size() + "'.";
        return result;
    }

}
