import java.util.ArrayList;

public class Worker implements Runnable {
    private String name;
    private String surname;
    private int workingTime;
    private int chillingTime;
    private int totalTime;
    Task currentTask;
    private ArrayList<Task> tasksInWork;
    private int effectivity;

    public Worker(String name, String surname, int workingTime, int chillingTime,  int totalTime, int effectivity) {
        this.name = name;
        this.surname = surname;
        this.effectivity = effectivity;
        this.workingTime = workingTime;
        this.chillingTime = chillingTime;
        this.totalTime = totalTime;
    }

    public void run() {
        System.out.println("Worker " + name + " started working");
        for (int i = 0; i < 8; i++) {
            try {
                Thread.sleep(1000); // 1 секунда = 1 час (условно)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (currentTask != null && currentTask.getStatus().equals("done")) {
                tasksInWork.remove(currentTask);
                getNewTask();
            } else if (currentTask == null) {
                getNewTask();
            }
            totalTime++;
            if (currentTask != null && !currentTask.getStatus().equals("done")) {
                currentTask.setTimeLeft(currentTask.getTimeLeft() - 1);
                if (currentTask.getTimeLeft() == 0) {
                    currentTask.setStatus("done");
                    System.out.println("Worker " + name + " ended task № " + currentTask.getNumber());
                }
                workingTime++;
            } else {
                System.out.println("Worker " + name + " is chilling");
                chillingTime++;
            }
        }
    }
    public String getName() {
        return name;
    }
    public void getNewTask() {
        synchronized (tasksInWork) {
            for (Task task : tasksInWork) {
                if (task.getStatus().equals("wait")) {
                    currentTask = task;
                    currentTask.setStatus("inWork");
                    System.out.println("Worker " + name + " taked task № " + currentTask.getNumber());
                    break;
                }
            }
        }
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public Task getCurrentTask() {
        return currentTask;
    }


    public void setTasks(ArrayList<Task> tasksInWork) {
        this.tasksInWork = tasksInWork;
    }

    public int getWorkingTime() {
        return workingTime;
    }

    public int getChillingTime() {
        return chillingTime;
    }

    public int getTotalTime() {
        return totalTime;
    }
    public String getSurname() {
        return surname;
    }
}
