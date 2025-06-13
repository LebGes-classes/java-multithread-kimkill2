import java.util.ArrayList;
import java.util.List;

public class MainThread{

    public static synchronized Task giveTask() {
        List<Task> listOfTasks = File.Tasks;
        Task neededTask = null;
        for (int currentTask = 0; currentTask < listOfTasks.size(); currentTask++) {
            Task task = listOfTasks.get(currentTask);
            if (task.getStatus().equals("свободен")) {
                task.setStatus("занят");
                neededTask = task;
                break;
            }
        }

        return neededTask;
    }

    public void run() {
        List<Employee> workers = Employee.workers;
        boolean flag = true;
        int count = 1;
        Employee currentEmployee;
        while (flag) {
            System.out.println("День " + count + "-й");
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < workers.size(); j++) {
                    while (workers.get(j).isAlive()) {
                    }
                    currentEmployee = workers.get(j);
                    workers.set(j, new Employee(currentEmployee.getFio(), currentEmployee.getStatus(), currentEmployee.getTaskProgress(), currentEmployee.getTask(), currentEmployee.getWorkTime(), currentEmployee.getRestTime()));
                }
                System.out.println((i + 1) + "-й час");
                for (int j = 0; j < workers.size(); j++) {
                    workers.get(j).start();
                }
            }


            if (File.checkAllEmployeesAndTasksStatus()) {
                flag = false;
                for (int index = 0; index < workers.size(); index++) {
                    File.createResultSheet(workers.get(index));
                }
            }
            count++;

        }
        System.out.println("Рабочий день закончился");
        System.out.println("Все задания выполнены");
    }
}
