package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> historyOfViews = new ArrayList<>();

    public void add(Task task) {
        if (task != null) {
            if (historyOfViews.size() >= 10) {
                historyOfViews.removeFirst();
            }
                historyOfViews.add(task);
        }
    }
    public List<Task> getHistory() {
        return historyOfViews;
    }
}
