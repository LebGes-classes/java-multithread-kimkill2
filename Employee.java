import java.util.List;
public class Employee extends Thread {
    private final String fio;
    private Task task;
    private String status;
    private double taskProgress;
    private double workTime;
    private double restTime;
    public static List<Employee> workers = File.Employees;
    public static List<Task> Tasks = File.Tasks;

    public Employee(String fio, String status, double taskProgress) {
        this.fio = fio;
        this.status = status;
        this.taskProgress = taskProgress;
    }

    public Employee(String fio, String status, double taskProgress, Task task, double workTime, double restTime) {
        this.fio = fio;
        this.status = status;
        this.taskProgress = taskProgress;
        this.task = task;
        this.workTime = workTime;
        this.restTime = restTime;
    }



    public String getFio() {
        return fio;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public void setWorkTime(double workTime) {
        this.workTime = workTime;
    }

    public double getWorkTime() {
        return workTime;
    }

    public void setRestTime(double restTime) {
        this.restTime = restTime;
    }

    public double getRestTime() {
        return restTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTaskProgress() {
        return taskProgress;
    }

    public void setTaskProgress(double taskProgress) {
        this.taskProgress = taskProgress;
    }

    public synchronized void changeEmployeeStatus(String Name, Task task, String status, double taskProgress, double workTime, double restTime) {
        for (Employee elem : workers) {
            if (elem.getFio().equals(Name)) {
                elem.setTask(task);
                elem.setStatus(status);
                elem.setTaskProgress(taskProgress);
                elem.setWorkTime(elem.getWorkTime());
                elem.setRestTime(elem.getRestTime());
                this.taskProgress = 0;
                workers.set(workers.indexOf(elem), elem);
                File.setEmployees(workers);
            }
        }
    }

    public synchronized void changeTaskStatus(Task task, String status) {
        for (Task elem : File.Tasks) {
            if (elem.getName().equals(task.getName())) {
                Tasks.set(Tasks.indexOf(elem), task);
                File.setTasks(Tasks);
                if (status.equals("выполнен")) {
                    setTask(null);
                    task.setStatus("выполнен");
                    Tasks.set(Tasks.indexOf(elem), task);
                    File.setTasks(Tasks);
                }
                this.task = task;
                Tasks.set(Tasks.indexOf(elem), task);
                File.setTasks(Tasks);
            }
        }
    }

    @Override
    public void run() {
        if (task == null && status.equals("свободен")) {
            Task currentTask = MainThread.giveTask();
            if (currentTask != null) {
                this.task = currentTask;
                changeEmployeeStatus(getFio(), currentTask, "занят", taskProgress, workTime, restTime);
                changeTaskStatus(currentTask, "занят");
            }
        }

        if (status.equals("свободен")) {
            this.restTime = restTime + 1;
        }


        System.out.println(workTime + "|" + restTime);


        this.taskProgress = taskProgress + 1;

        if (task != null && "занят".equals(status)) {

            if (this.taskProgress == task.getTime()) {
                this.workTime = workTime + taskProgress;
                System.out.println("Задание выполнено by " + getFio());
                changeTaskStatus(task, "выполнен");
                changeEmployeeStatus(getFio(), null, "свободен", 0, workTime, restTime);
            }
        }

    }
}
