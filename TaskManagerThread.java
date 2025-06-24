import java.util.Scanner;

public class TaskManagerThread extends Thread {

    private Scanner scanner;

    public TaskManagerThread(Scanner scanner) {
        this.scanner = scanner;
    }

    public void run() {
        System.out.println();
        String answer = scanner.nextLine();
        System.out.println(answer);
    }
}
