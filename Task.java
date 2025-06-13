public class Task {
    private final String name;
    private String status;
    private final double time;

    public Task(String name, double time, String status) {
        this.name = name;
        this.time = time;
        this.status = status;

    }

    public String getName() {
        return name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public double getTime() {
        return time;
    }
}
