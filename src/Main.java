import java.util.HashMap;


public class Main {


    public static void main(String[] args) {
        HashMap<Integer, Task> allTasks;
        Task task;

        System.out.println("Заполняем задачами...");
        fillManagerOne();
        System.out.println("Тест 1: Выводим все задачи:");
        Manager.printAll();
        System.out.println("------------------------------------------------");
        System.out.println("Тест 2: Удаляем все задачи и снова выводим:");
        Manager.removeAll();
        Manager.printAll();
        System.out.println("------------------------------------------------");
        System.out.println("Тест 3: Снова заполняем задачи...");
        fillManagerTwo();
        Manager.printAll();
        System.out.println("...и ищем задачу по ID 1011:");
        System.out.println(Manager.getByKey(1011));
        System.out.println("------------------------------------------------");
        System.out.println("Тест 4: Обновляем задачу с ID 1007:\n");
        allTasks = Manager.getAllTasks();
        task = allTasks.get(1007);
        task.setTaskName("ИЗМЕНЕННЫЙ ЭПИК C ПОДЗАДАЧАМИ");
        task.setInfo("ИЗМЕНЕННОЕ ИНФО ЭПИКА");
        Manager.addTask(task);
        Manager.printAll();
        System.out.println("Тест 5.1: Удаляем подзадачу с ID 1010:");
        Manager.removeTaskById(1010);
        Manager.printAll();
        System.out.println("Тест 5.2: Удаляем эпик с ID 1007 (вместе с подзадачами):");
        Manager.removeTaskById(1007);
        Manager.printAll();
        System.out.println("------------------------------------------------");
        System.out.println("Тест 6: Выводим подзадачи эпика ID 1013:");
        System.out.println("...Очищаем, наполняем и выводим список всех задач...");
        Manager.removeAll();
        fillManagerThree();
        Manager.printAll();
        System.out.println("...Выводим результат:");
        System.out.println(Manager.getSubtasks(1013));
        System.out.println("------------------------------------------------");
        System.out.println("Тест 7: Играемся со статусами подзадач и смотрим на их Эпик\n");
        System.out.println("Тест 7.1: Меняем статус подзадачи ID 1016 на IN_PROGRESS");
        allTasks = Manager.getAllTasks();
        task = allTasks.get(1016);
        task.setStatus(Status.IN_PROGRESS);
        Manager.addTask(task);
        Manager.printAll();
        System.out.println("Тест 7.2: Меняем статус подзадач ID 1016 и 1014 на DONE\n");
        allTasks = Manager.getAllTasks();
        task = allTasks.get(1016);
        task.setStatus(Status.DONE);
        Manager.addTask(task);
        task = allTasks.get(1014);
        task.setStatus(Status.DONE);
        Manager.addTask(task);
        Manager.printAll();
        System.out.println("\nТест 7.3: Меняем статус подзадачи ID 1016 на NEW");
        allTasks = Manager.getAllTasks();
        task = allTasks.get(1016);
        task.setStatus(Status.NEW);
        Manager.addTask(task);
        Manager.printAll();
        System.out.println("Тест 7.4: Пытаемся изменить статус Эпика с ID 1013 на DONE");
        allTasks = Manager.getAllTasks();
        task = allTasks.get(1013);
        task.setStatus(Status.DONE);
        Manager.addTask(task);
        Manager.printAll();


    }


    public static void fillManagerOne() {
        Manager.addTask(new Task("Задача 1", "Инфо зад.1"));
        Manager.addTask(new EpicTask("Задача эпик 1", "Эпик с подзадачами"));
        Manager.addTask(new Subtask("Подзадача 1", "Инфо подзадачи 1", 1001));
        Manager.addTask(new Task("Задача 2", "Инфо зад.2"));
        Manager.addTask(new Subtask("Подзадача 2", "Инфо подзадачи 2", 1001));
        Manager.addTask(new EpicTask("Задача эпик 2", "Эпик без подзадач"));


    }

    public static void fillManagerTwo() {
        Manager.addTask(new Task("Задача 1", "Инфо зад.1"));
        Manager.addTask(new EpicTask("Задача эпик 1", "Эпик с подзадачами"));
        Manager.addTask(new Subtask("Подзадача 1", "Инфо подзадачи 1", 1007));
        Manager.addTask(new Task("Задача 2", "Инфо зад.2"));
        Manager.addTask(new Subtask("Подзадача 2", "Инфо подзадачи 2", 1007));
        Manager.addTask(new EpicTask("Задача эпик 2", "Эпик без подзадач"));
    }

    public static void fillManagerThree() {
        Manager.addTask(new Task("Задача 1", "Инфо зад.1"));
        Manager.addTask(new EpicTask("Задача эпик 1", "Эпик с подзадачами"));
        Manager.addTask(new Subtask("Подзадача 1", "Инфо подзадачи 1", 1013));
        Manager.addTask(new Task("Задача 2", "Инфо зад.2"));
        Manager.addTask(new Subtask("Подзадача 2", "Инфо подзадачи 2", 1013));
        Manager.addTask(new EpicTask("Задача эпик 2", "Эпик без подзадач"));
    }
}
