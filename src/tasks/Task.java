package tasks;

import enums.Status;

public class Task {
    private String taskName;
    private String info;
    private int id;
    private Status status;

    public Task(String taskName, String info) {
        this.taskName = taskName;
        this.info = info;
        this.status = Status.NEW;
        this.id = 0;
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
                "', info = '" + getInfo() + "', status = '" + getStatus() + "'.";
        return result;
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
}
