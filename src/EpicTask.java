import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {
    private final List<Integer> subtasks = new ArrayList<>();

    public EpicTask(String taskName, String info) {
        super(taskName, info);
    }

    public List<Integer> getSubtasks() {
        return subtasks;
    }

}
