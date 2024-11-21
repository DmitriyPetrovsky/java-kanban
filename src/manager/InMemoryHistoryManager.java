package manager;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> historyOfViews = new ArrayList<>();
    private Map<Integer, Node> mapOfNodes = new HashMap<>();
    private Node<Task> head = null;
    private Node<Task> tail = null;

    public void add(Task task) {
        if (task != null) {
            if (mapOfNodes.containsKey(task.getId())) {
                mapOfNodes.remove(task.getId());
            }
            Node node = new Node(task);
            if (head == null) {
                head = node;
            } else if (tail == null) {
                tail = node;
                tail.setPrev(head);
                head.setNext(node);
            } else {
                node.setPrev(tail);
                tail.setNext(node);
                tail = node;
            }
        }
    }

    public List<Task> getHistory() {
        Node<Task> node = head;
        while (node.getNext() != null) {
            historyOfViews.add(node.getValue());
            node = node.getNext();
        }
        return historyOfViews;
    }

    public void remove(int id) {

    }
}

class Node<Task> {
    private Task value;
    private Node<Task> next;
    private Node<Task> prev;

    public Node(Task value) {
        this.value = value;
        this.next = null;
        this.prev = null;
    }


    public Task getValue() {
        return value;
    }

    public void setValue(Task value) {
        this.value = value;
    }

    public Node<Task> getNext() {
        return next;
    }

    public void setNext(Node<Task> next) {
        this.next = next;
    }

    public Node<Task> getPrev() {
        return prev;
    }

    public void setPrev(Node<Task> prev) {
        this.prev = prev;
    }
}