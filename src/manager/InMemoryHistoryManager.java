package manager;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    List<Task> historyOfViews;
    private final Map<Integer, Node<Task>> mapOfNodes = new HashMap<>();
    private Node<Task> head = null;
    private Node<Task> tail = null;

    public void add(Task task) {
        if (task != null) {
            if (mapOfNodes.containsKey(task.getId())) {
                remove(task.getId());
            }
            Node<Task> node = new Node<>(task);
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
            mapOfNodes.put(task.getId(), node);
        }
    }

    public List<Task> getHistory() {
        historyOfViews = new ArrayList<>();
        Node<Task> node = head;
        while (node != null) {
            historyOfViews.add(node.getValue());
            node = node.getNext();
        }
        return historyOfViews;
    }

    public void remove(int id) {
        if (mapOfNodes.containsKey(id)) {
            Node<Task> node = mapOfNodes.get(id);
            if (node.getNext() == null && node.getPrev() == null) {
                head = null;
                tail = null;
                mapOfNodes.remove(id);
                return;
            }
            if (node.getPrev() != null) {
                node.getPrev().setNext(node.getNext());
            } else {
                node.getNext().setPrev(null);
                head = node.getNext();
            }
            if (node.getNext() != null) {
                node.getNext().setPrev(node.getPrev());
            } else {
                node.getPrev().setNext(null);
                tail = node.getPrev();
            }
            mapOfNodes.remove(id);
        }
    }
}

@SuppressWarnings({"checkstyle:ClassTypeParameterName", "checkstyle:OneTopLevelClass"})
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