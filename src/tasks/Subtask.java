package tasks;

import enums.Type;
import manager.Managers;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String taskName, String info, int epicId) {
        super(taskName, info);
        this.epicId = epicId;
        this.setType(Type.SUBTASK);
    }

    public Subtask(String taskName, String info, String startTimeString, long durationLong, int epicId) {
        super(taskName, info, startTimeString, durationLong);
        this.epicId = epicId;
        super.setStartTime(LocalDateTime.parse(startTimeString, Managers.getDtf()));
        super.setDuration(Duration.ofMinutes(durationLong));
        super.setEndTime(super.getStartTime().plus(super.getDuration()));
        this.setType(Type.SUBTASK);
    }

    public int getEpicId() {
        return this.epicId;
    }

    public String toString() {
        String result;
        result = "--> Type = 'Подзадача', ID = '" + getId() + "', name = '" + getTaskName() +
                "', info = '" + getInfo() + "', status = '" + getStatus() + "', EpicID = '" + getEpicId()
                + " startTime = '" + getStringStartTime() + "', duration = '" + getStringDuration()
                + "', endTime = '" + getStringEndTime() + "'.";
        return result;
    }

    public static int minTimeCompare(Subtask s1, Subtask s2) {
        if (s1.getStartTime().isBefore(s2.getStartTime())) {
            return -1;
        } else if (s1.getStartTime().isAfter(s2.getStartTime())) {
            return 1;
        } else {
            return 0;
        }
    }

    public static int maxTimeCompare(Subtask s1, Subtask s2) {
        if (s1.getEndTime().isAfter(s2.getEndTime())) {
            return -1;
        } else if (s1.getEndTime().isBefore(s2.getEndTime())) {
            return 1;
        } else {
            return 0;
        }
    }
}
