package tasks;

import enums.Type;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtaskIds = new ArrayList<>();

    public Epic(String taskName, String info) {
        super(taskName, info);
        this.setType(Type.EPIC);
    }

    public List<Integer> getSubtaskIds() {
        return this.subtaskIds;
    }

    public void setSubtaskIds(List<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public String toString() {
        String result;
        result = "Type = 'Эпик', ID = '" + getId() + "', name = '" + getTaskName() +
                "', info = '" + getInfo() + "', status = '" + getStatus() + "', subtasks = '" + subtaskIds.size()
                + "', startTime = '" + getStringStartTime() + "', duration = '" + getStringDuration()
                + "', endTime = '" + getStringEndTime() + "'.";
        return result;
    }
}
