import enums.Status;
import manager.InMemoryTaskManager;
import manager.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public class Main {


    public static void main(String[] args) {

        InMemoryTaskManager manager = Managers.getDefault();
        List<Subtask> subtasks;
        List<Task> history;
        Subtask subtask;
        Epic epic;


        System.out.println("\nЗаполняем задачами...");
        System.out.println("Тест 1: Выводим все задачи:");
        fillManagerOne(manager);
        printAll(manager);
        System.out.println("------------------------------------------------");
        System.out.println("\nТест 2.1: Удаляем все подзадачи и снова выводим:");
        manager.removeSubtasks();
        printAll(manager);
        System.out.println("------------------------------------------------");
        System.out.println("\nТест 2.2: Удаляем все задачи и эпики снова выводим:\n");
        manager.removeEpics();
        manager.removeTasks();
        printAll(manager);
        System.out.println("------------------------------------------------");
        System.out.println("\nТест 3: Снова заполняем задачи...");
        fillManagerTwo(manager);
        printAll(manager);
        System.out.println("\n...и ищем эпик-задачу по ID 1011:");
        System.out.println(manager.getByKeyEpic(1011));
        System.out.println("------------------------------------------------");
        System.out.println("\nТест 4: Обновляем задачу с ID 1007:\n");
        epic = manager.getByKeyEpic(1007);
        epic.setTaskName("ИЗМЕНЕННЫЙ ЭПИК C ПОДЗАДАЧАМИ");
        epic.setInfo("ИЗМЕНЕННОЕ ИНФО ЭПИКА");
        manager.updateEpic(epic);
        printAll(manager);
        System.out.println("\nТест 5.1: Удаляем подзадачу с ID 1010:");
        manager.removeByIdSubtask(1010);
        printAll(manager);
        System.out.println("\nТест 5.2: Удаляем эпик с ID 1007 (вместе с подзадачей):");
        manager.removeByIdEpic(1007);
        printAll(manager);
        System.out.println("------------------------------------------------");
        System.out.println("\nТест 6: Выводим подзадачи эпика ID 1013:");
        System.out.println("...Очищаем, наполняем и выводим список задач...");
        manager.removeTasks();
        manager.removeSubtasks();
        manager.removeEpics();
        fillManagerThree(manager);
        printAll(manager);
        System.out.println("\n...и выводим подзадачи:");
        subtasks = manager.getSubtasksByEpicId(1013);
        for (Subtask tempSubtask : subtasks) {
            System.out.println(tempSubtask);
        }
        System.out.println("------------------------------------------------");
        System.out.println("\nТест 7: Играемся со статусами подзадач и смотрим на их Эпик");
        System.out.println("\nТест 7.1: Меняем статус подзадачи ID 1016 на IN_PROGRESS");
        subtask = manager.getByKeySubtask(1016);
        subtask.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask);
        printAll(manager);
        System.out.println("\nТест 7.2: Меняем статус подзадач ID 1016 и 1014 на DONE");
        subtask.setStatus(Status.DONE);
        subtask = manager.getByKeySubtask(1014);
        subtask.setStatus(Status.DONE);
        manager.updateSubtask(subtask);
        printAll(manager);
        System.out.println("\nТест 7.3: Меняем статус подзадачи ID 1014 на NEW");
        subtask.setStatus(Status.NEW);
        manager.updateSubtask(subtask);
        printAll(manager);
        System.out.println("\nТест 7.4: Пытаемся изменить статус Эпика с ID 1013 на DONE");
        epic = manager.getByKeyEpic(1013);
        epic.setStatus(Status.DONE);
        manager.updateEpic(epic);
        printAll(manager);
        System.out.println("\nТест 7.5: Удаляем подзадачи Эпика с ID 1013 и смотрим его статус");
        manager.removeByIdSubtask(1014);
        manager.removeByIdSubtask(1016);
        printAll(manager);
        history = manager.getHistory();
        System.out.println("\nТест 8: Проверяем историю");
        for (Task task : history) {
            System.out.println(task);
        }


    }

    public static void fillManagerOne(InMemoryTaskManager manager) {
        manager.addTask(new Task("Задача 1", "Инфо зад.1"));
        manager.addEpic(new Epic("Задача эпик 1", "Эпик с подзадачами"));
        manager.addSubtask(new Subtask("Подзадача 1", "Инфо подзадачи 1", 1001));
        manager.addTask(new Task("Задача 2", "Инфо зад.2"));
        manager.addSubtask(new Subtask("Подзадача 2", "Инфо подзадачи 2", 1001));
        manager.addEpic(new Epic("Задача эпик 2", "Эпик без подзадач"));
    }

    public static void fillManagerTwo(InMemoryTaskManager manager) {
        manager.addTask(new Task("Задача 1", "Инфо зад.1"));
        manager.addEpic(new Epic("Задача эпик 1", "Эпик с подзадачами"));
        manager.addSubtask(new Subtask("Подзадача 1", "Инфо подзадачи 1", 1007));
        manager.addTask(new Task("Задача 2", "Инфо зад.2"));
        manager.addSubtask(new Subtask("Подзадача 2", "Инфо подзадачи 2", 1007));
        manager.addEpic(new Epic("Задача эпик 2", "Эпик без подзадач"));
    }

    public static void fillManagerThree(InMemoryTaskManager manager) {
        manager.addTask(new Task("Задача 1", "Инфо зад.1"));
        manager.addEpic(new Epic("Задача эпик 1", "Эпик с подзадачами"));
        manager.addSubtask(new Subtask("Подзадача 1", "Инфо подзадачи 1", 1013));
        manager.addTask(new Task("Задача 2", "Инфо зад.2"));
        manager.addSubtask(new Subtask("Подзадача 2", "Инфо подзадачи 2", 1013));
        manager.addEpic(new Epic("Задача эпик 2", "Эпик без подзадач"));
    }

    public static void printAll(InMemoryTaskManager manager) {
        List<Epic> allEpics = manager.getAllEpics();
        List<Subtask> subtasks;
        List<Task> allTasks = manager.getAllTasks();
        for (Task task : allTasks) {
            System.out.println(task);
        }
        for (Epic epic : allEpics) {
            System.out.println(epic);
            subtasks = manager.getSubtasksByEpicId(epic.getId());
            for (Subtask subtask : subtasks) {
                System.out.println(subtask);
            }
        }
    }
}



