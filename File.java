import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.round;

public class File {
    public static List<Employee> Employees = getListOfWorkers();
    public static List<Task> Tasks = getListOfTasks();
    private static final String filePath = "C:\\Users\\0\\Desktop\\Multithread\\MultiThread.xlsx";
    public static int count = 1;
    public File() {

    }

    public static void setEmployees(List<Employee> employees) {
        Employees = employees;
    }

    public static void setTasks(List<Task> tasks) {
        Tasks = tasks;
    }

    public static synchronized List<Employee> getListOfWorkers() {
        int count = 0;
        List<Employee> workers = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (count > 0 && row != null) {
                    String name = row.getCell(0).getStringCellValue();
                    String status = row.getCell(1).getStringCellValue();
                    double taskProgress = 0;
                    workers.add(new Employee(name, status, taskProgress));
                }
                count++;
            }
        } catch (IOException e) {
            System.out.println("Error:" + e.getMessage());
        }
        return workers;
    }

    public static synchronized List<Employee> getListOfBusyWorkers() {
        int count = 0;
        List<Employee> workers = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (count > 0 && row != null && row.getCell(1).getStringCellValue().equals("занят")) {
                    String name = row.getCell(0).getStringCellValue();
                    String status = row.getCell(1).getStringCellValue();
                    double taskProgress = row.getCell(2).getNumericCellValue();
                    workers.add(new Employee(name, status, taskProgress));
                }
                count++;
            }
        } catch (IOException e) {
            System.out.println("Error:" + e.getMessage());
        }
        return workers;
    }

    public static synchronized List<Task> getListOfTasks() {
        List<Task> tasks = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            int count = 0;
            Sheet sheet = workbook.getSheetAt(1);
            for (Row row : sheet) {
                if (count > 0 && row != null) {
                    String name = row.getCell(0).getStringCellValue();
                    double time = row.getCell(1).getNumericCellValue();
                    String status = row.getCell(2).getStringCellValue();
                    tasks.add(new Task(name, time, status));
                }
                count++;
            }
        } catch (IOException e) {
            System.out.println("Error:" + e.getMessage());
        }
        return tasks;
    }

    public static synchronized void createResultSheet(Employee employee) {
        try (FileInputStream fis = new FileInputStream(filePath);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet2 = workbook.getSheetAt(1);
            for (Row row : sheet2) {
                row.getCell(2).setCellValue("выполнен");
            }

            Sheet sheet3 = workbook.getSheetAt(2);
            Row row1 = sheet3.createRow(0);
            row1.createCell(0).setCellValue("Время работы");
            row1.createCell(1).setCellValue("Время отдыха");
            row1.createCell(2).setCellValue("Эффективность работы");
            System.out.println(employee.getFio() + "|" + employee.getWorkTime() + "|" + employee.getRestTime() + "/" + count);
            sheet3.getRow(count).getCell(0).setCellValue(employee.getWorkTime());
            sheet3.getRow(count).getCell(1).setCellValue(employee.getRestTime());
            sheet3.getRow(count).getCell(2).setCellValue(employee.getWorkTime() / (employee.getWorkTime() + employee.getRestTime()));



        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
            workbook.close();
        }
            count++;
        }  catch (IOException e) {
            System.out.println("Error:" + e.getMessage());
        }

    }


        public static boolean checkAllEmployeesAndTasksStatus () {
            boolean flag = false;
            int count = 0;

            for (Employee employee : Employees) {
                if (employee.getStatus().equals("свободен")) {
                    count++;
                }
            }

            for (Task task : Tasks) {
                if (task.getStatus().equals("выполнен")) {
                    count++;
                }

            }

            if (count == (getListOfWorkers().size() + getListOfTasks().size())) {
                flag = true;
            }
            return flag;
        }
}
