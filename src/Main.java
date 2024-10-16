import java.util.HashMap;
import java.util.Map;

public class Main {


    public static void main(String[] args) {
            System.out.println("Заполняем задачами...");
            fillManager();
            System.out.println("Тест 1: Выводим все задачи:");
            Manager.printAll();
            System.out.println("------------------------------------------------");
            System.out.println("Тест 2: Удаляем все задачи и снова выводим:");
            Manager.removeAll();
            Manager.printAll();
            System.out.println("------------------------------------------------");
            System.out.println("Тест 3: Снова заполняем задачи и ищем задачу по ID 1348835871:");
            fillManager();
            System.out.println(Manager.getByKey(1982598898));
            System.out.println("------------------------------------------------");
            System.out.println("Тест 4: Обновляем задачу с ID 1348835871:");
            System.out.println("------------------------------------------------");
            System.out.println("Тест 5.1: Удаляем подзадачу с ID 1136743801:");
            Manager.removeTaskById(1136743801);
            Manager.printAll();
            System.out.println("Тест 5.2: Удаляем эпик с ID 544277405 (вместе с подзадачами):");
            Manager.removeTaskById(544277405);
            Manager.printAll();
            System.out.println("------------------------------------------------");
            System.out.println("Тест 6: Выводим подзадачи эпика ID 544277405:");
            System.out.println("...Очищаем и наполняем список задач");
            Manager.removeAll();
            fillManager();
            System.out.println(Manager.getSubtasks(544277405));

    }


    public static void fillManager() {
        Manager.addTask(new Task("Задача 1", "Инфо зад.1"));
        Manager.addTask(new EpicTask("Задача эпик 1", "Эпик с подзадачами"));
        Manager.addTask(new Subtask("Подзадача 1", "Инфо подзадачи 1",544277405));
        Manager.addTask(new Task("Задача 2", "Инфо зад.2"));
        Manager.addTask(new Subtask("Подзадача 2", "Инфо подзадачи 2",544277405));
        Manager.addTask(new EpicTask("Задача эпик 2", "Эпик без подзадач"));


    }
}
