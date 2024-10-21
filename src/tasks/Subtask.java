package tasks;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String taskName, String info, int epicId) {
        super(taskName, info);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public String toString() {
        String result;
        result = "--> Type = 'Подзадача', ID = '" + getId() + "', name = '" + getTaskName() +
                "', info = '" + getInfo() + "', status = '" + getStatus() + "', EpicID = '" + getEpicId() + "'.";
        return result;
    }
}
