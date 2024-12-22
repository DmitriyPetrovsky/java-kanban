package tasks;

import enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private String taskName;
    private String info;
    private int id;
    private Status status;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration duration;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task(String taskName, String info) {
        this.taskName = taskName;
        this.info = info;
        this.status = Status.NEW;
        this.id = 0;
    }

    public Task(String taskName, String info, String startTimeString, long durationLong) {
        this.taskName = taskName;
        this.info = info;
        this.status = Status.NEW;
        this.id = 0;
        this.startTime = LocalDateTime.parse(startTimeString, dtf);
        this.duration = Duration.ofMinutes(durationLong);
        this.endTime = startTime.plus(duration);
    }

    public String getTaskName() {
        return taskName;
    }

    public String getInfo() {
        return info;
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String toString() {
        String result;
        result = "Type = 'Задача', ID = '" + getId() + "', name = '" + getTaskName() +
                "', info = '" + getInfo() + "', status = '" + getStatus() + "', startTime ='"
                + getStringStartTime() + "', duration = '" + getStringDuration()
                + "', endTime = '" + getStringEndTime() + "'.";
        return result;
    }

    public static int compareByDate(Task t1, Task t2) {
        if (t1.getStartTime() != null && t2.getStartTime() != null) {
            if (t1.getStartTime().isBefore(t2.getStartTime())) {
                return -1;
            } else if (t1.getStartTime().isAfter(t2.getStartTime())) {
                return 1;
            } else {
                return 1;
            }
        } else if (t1.getStartTime() == null && t2.getStartTime() != null) {
            return 1;
        } else if (t1.getStartTime() != null && t2.getStartTime() == null) {
            return -1;
        }
            return 1;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public DateTimeFormatter getDtf() {
        return dtf;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getStringStartTime() {
        if (getStartTime() != null) {
            return getStartTime().format(getDtf());
        }
        return "[null]";
    }

    public String getStringDuration() {
        if (getDuration() != null) {
            return String.valueOf(getDuration().toMinutes());
        }
        return "[null]";
    }

    public String getStringEndTime() {
        if (getEndTime() != null) {
            return getEndTime().format(getDtf());
        }
        return "[null]";
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;

        return id == task.id && taskName.equals(task.taskName) && info.equals(task.info) && status == task.status;
    }

    @Override
    public int hashCode() {
        int result = taskName.hashCode();
        result = 31 * result + info.hashCode();
        result = 31 * result + id;
        result = 31 * result + status.hashCode();
        return result;
    }

    public boolean overlaps(Task otherTask) {
        return !(this.endTime.isBefore(otherTask.getStartTime()) || otherTask.getEndTime().isBefore(this.startTime));
    }
}
