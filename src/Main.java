import enums.Status;
import manager.Manager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.Map;

public class Main {


    public static void main(String[] args) {
        Map<Integer, Epic> allEpics;
        Map<Integer, Subtask> allSubtasks;
        Subtask subtask;
        Epic epic;
        Manager manager = new Manager();

        System.out.println("\nЗаполняем задачами...");
        System.out.println("Тест 1: Выводим все задачи:");
        fillManagerOne(manager);
        manager.printAll();
        System.out.println("------------------------------------------------");
        System.out.println("\nТест 2: Удаляем все задачи и снова выводим:");
        manager.removeAll();
        manager.printAll();
        System.out.println("------------------------------------------------");
        System.out.println("\nТест 3: Снова заполняем задачи...");
        fillManagerTwo(manager);
        manager.printAll();
        System.out.println("\n...и ищем задачу по ID 1011:");
        System.out.println(manager.getByKeyEpic(1011));
        System.out.println("------------------------------------------------");
        System.out.println("\nТест 4: Обновляем задачу с ID 1007:\n");
        epic = manager.getAllEpics().get(1007);
        epic.setTaskName("ИЗМЕНЕННЫЙ ЭПИК C ПОДЗАДАЧАМИ");
        epic.setInfo("ИЗМЕНЕННОЕ ИНФО ЭПИКА");
        manager.addEpic(epic);
        manager.printAll();
        System.out.println("\nТест 5.1: Удаляем подзадачу с ID 1010:");
        manager.removeByIdSubtask(1010);
        manager.printAll();
        System.out.println("\nТест 5.2: Удаляем эпик с ID 1007 (вместе с подзадачей):");
        manager.removeByIdEpic(1007);
        manager.printAll();
        System.out.println("------------------------------------------------");
        System.out.println("\nТест 6: Выводим подзадачи эпика ID 1013:");
        System.out.println("...Очищаем, наполняем и выводим список задач...");
        manager.removeAll();
        fillManagerThree(manager);
        manager.printAll();
        System.out.println("\n...и выводим подзадачи:");
        System.out.println(manager.getSubtasks(1013));
        System.out.println("------------------------------------------------");
        System.out.println("\nТест 7: Играемся со статусами подзадач и смотрим на их Эпик");
        System.out.println("\nТест 7.1: Меняем статус подзадачи ID 1016 на IN_PROGRESS");
        allSubtasks = manager.getAllSubtasks();
        subtask = allSubtasks.get(1016);
        subtask.setStatus(Status.IN_PROGRESS);
        manager.addSubtask(subtask);
        manager.printAll();
        System.out.println("\nТест 7.2: Меняем статус подзадач ID 1016 и 1014 на DONE");
        subtask.setStatus(Status.DONE);
        manager.addSubtask(subtask);
        subtask = allSubtasks.get(1014);
        subtask.setStatus(Status.DONE);
        manager.addSubtask(subtask);
        manager.printAll();
        System.out.println("\nТест 7.3: Меняем статус подзадачи ID 1014 на NEW");
        subtask.setStatus(Status.NEW);
        manager.addSubtask(subtask);
        manager.printAll();
        System.out.println("\nТест 7.4: Пытаемся изменить статус Эпика с ID 1013 на DONE");
        allEpics = manager.getAllEpics();
        epic = allEpics.get(1013);
        epic.setStatus(Status.DONE);
        manager.addEpic(epic);
        manager.printAll();
        System.out.println("\nТест 7.5: Удаляем подзадачи Эпика с ID 1013 и смотрим его статус");
        manager.removeByIdSubtask(1014);
        manager.removeByIdSubtask(1016);
        manager.printAll();
    }

    public static void fillManagerOne(Manager manager) {
        manager.addTask(new Task("Задача 1", "Инфо зад.1"));
        manager.addEpic(new Epic("Задача эпик 1", "Эпик с подзадачами"));
        manager.addSubtask(new Subtask("Подзадача 1", "Инфо подзадачи 1", 1001));
        manager.addTask(new Task("Задача 2", "Инфо зад.2"));
        manager.addSubtask(new Subtask("Подзадача 2", "Инфо подзадачи 2", 1001));
        manager.addEpic(new Epic("Задача эпик 2", "Эпик без подзадач"));
    }

    public static void fillManagerTwo(Manager manager) {
        manager.addTask(new Task("Задача 1", "Инфо зад.1"));
        manager.addEpic(new Epic("Задача эпик 1", "Эпик с подзадачами"));
        manager.addSubtask(new Subtask("Подзадача 1", "Инфо подзадачи 1", 1007));
        manager.addTask(new Task("Задача 2", "Инфо зад.2"));
        manager.addSubtask(new Subtask("Подзадача 2", "Инфо подзадачи 2", 1007));
        manager.addEpic(new Epic("Задача эпик 2", "Эпик без подзадач"));
    }

    public static void fillManagerThree(Manager manager) {
        manager.addTask(new Task("Задача 1", "Инфо зад.1"));
        manager.addEpic(new Epic("Задача эпик 1", "Эпик с подзадачами"));
        manager.addSubtask(new Subtask("Подзадача 1", "Инфо подзадачи 1", 1013));
        manager.addTask(new Task("Задача 2", "Инфо зад.2"));
        manager.addSubtask(new Subtask("Подзадача 2", "Инфо подзадачи 2", 1013));
        manager.addEpic(new Epic("Задача эпик 2", "Эпик без подзадач"));
    }
}
