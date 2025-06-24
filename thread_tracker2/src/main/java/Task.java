public class Task {
    private int number;
    private int timeLeft;
    private String status;
    public Task(int number, int timeLeft, String status) {
        this.number = number;
        this.timeLeft = timeLeft;
        this.status = status;
    }

    public int getNumber() {
        return number;
    }


    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

}
