import java.util.List;

public class Task {
    private String taskName;
    private String info;
    private int id;
    private Status status;

    public Task(String taskName, String info) {
        this.taskName = taskName;
        this.info = info;
        this.status = Status.NEW;
        this.id = this.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;
        return id == task.id && taskName.equals(task.taskName) && info.equals(task.info) && status == task.status;
    }

    @Override
    public int hashCode() {
        int result = taskName.hashCode();
        result = 31 * result + info.hashCode();
        return Math.abs(result);
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

    public List<Integer> getSubtasks() {
        return null;
    }

    @Override
    public String toString() {
        return  "taskName='" + taskName + '\'' +
                ", info='" + info + '\'' +
                ", id=" + id +
                ", status=" + status;
    }

    public String getInstance(Task task) {
        if (task instanceof EpicTask) {
            return "Эпик";
        } else if (task instanceof Subtask) {
            return "Подзадача";
        } else if (task instanceof Task) {
            return "Задача";
        } else {
            return "";
        }
    }
}
