import javax.sound.midi.Track;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class Main {
    private static final int WORKING_DAY_HOURS = 8;
    public static final String FILE_PATH = "C:\\personality\\java\\sata\\thread_tracker2\\src\\main\\java\\tracker.xlsx";
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Task> tasksInWork;
    private static ArrayList<Task> tasks;


    public static void main(String[] args) {
        ExcelManager manager = new ExcelManager();
        try {
            manager.setUp(FILE_PATH);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        // каждый сотрудник работает по 8 часов в день
        // если во время его работы есть задачи, то он выполняет их, если нет
        // то простаивает

        tasks = manager.readTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks available");
            return;
        }

        ArrayList<Worker> workers = manager.readWorkers();
        if (workers.isEmpty()) {
            System.out.println("No workers found");
            return;
        }
        // + добавить возможность добавлять новые задачи во время дня
        tasksInWork = new ArrayList<>();
        for (Task task : tasks) {
            System.out.println(task.getNumber() + " " + task.getTimeLeft() + " " + task.getStatus());
            if (task.getStatus().equals("wait")) {
                tasksInWork.add(task);
            }
        }
        for (Worker worker : workers) {
            System.out.println(STR."\{worker.getName()} \{worker.getWorkingTime()} \{worker.getTotalTime()}");
            worker.setTasks(tasksInWork);
        }

        int days = 0;
        while (!tasksInWork.isEmpty()) {
            System.out.println("Начался день: " + ++days);
            ExecutorService executor = Executors.newFixedThreadPool(workers.size());
            for (Worker worker : workers) {
                executor.execute(worker);
            }

            executor.shutdown();
            try {
                executor.awaitTermination(1, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

            for (Task task : tasks) {
                System.out.println(STR."\{task.getNumber()} \{task.getTimeLeft()} \{task.getStatus()}");
            }
            manager.updateTasks(tasks);
            manager.updateWorkers(workers);


            System.out.println(STR."Day :\{days} ended");
            chooseOption();
        }
    }

    private static void chooseOption() {
        System.out.println("Выберите действие:");
        System.out.println("1. Добавить задачу");
        System.out.println("2. Начать новый день");
        int answer = Integer.valueOf(scanner.nextLine());
        if (answer < 1 || answer > 2) {
            System.out.println("Неверный ввод");
            scanner.nextLine();
            chooseOption();
            return;
        } else if (answer == 1) {
            newTask();
        }
    }

    private static void newTask() {
        System.out.println("Введите номер задачи");
        int number = Integer.valueOf(scanner.nextLine());
        System.out.println("Введите время выполнения");
        int time = Integer.valueOf(scanner.nextLine());
        Task task = new Task(number, time, "wait");
        tasks.add(task);
        tasksInWork.add(task);
        System.out.println("Успешно");
        scanner.nextLine();
    }
}
