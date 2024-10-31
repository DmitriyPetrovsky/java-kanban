package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtaskIds = new ArrayList<>();

    public Epic(String taskName, String info) {
        super(taskName, info);
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(List<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public String toString() {
        String result;
        result = "Type = 'Эпик', ID = '" + getId() + "', name = '" + getTaskName() +
                "', info = '" + getInfo() + "', status = '" + getStatus() + "', subtasks = '" + subtaskIds.size() + "'.";
        return result;
    }

}
