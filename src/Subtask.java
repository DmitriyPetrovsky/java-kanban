public class Subtask extends Task {
    private final int epicId;

    public Subtask(String taskName, String info, int epicId) {
        super(taskName, info);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}
