import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {
    private List<Integer> subtasks = new ArrayList<>();

    public EpicTask(String taskName, String info) {
        super(taskName, info);
    }

    public List<Integer> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Integer> subtasks) {
        this.subtasks = subtasks;
    }

}
