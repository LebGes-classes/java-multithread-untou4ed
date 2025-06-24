import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExcelManager {
    private static Workbook workbook;
    private static boolean opened = false;
    private static String filePath;
    private static FileInputStream inputFile;


    public void setUp(String filePath) throws IOException {
        ExcelManager.filePath = filePath;
        inputFile = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(inputFile);
    }

    // возвращаем список текущих задач
    public ArrayList<Task> readTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
            // Получаем первый лист
            Sheet sheet = workbook.getSheetAt(1);
            // Перебираем строки
            int ind = 0;
            for (Row row : sheet) {
                if (ind == 0) {
                    ind = 1;
                    continue;
                }
                // Перебираем ячейки в строке
                int number = (int) row.getCell(0).getNumericCellValue();
                int timeLeft = (int) row.getCell(1).getNumericCellValue();
                String status = row.getCell(2).getStringCellValue();
                Task task = new Task(number, timeLeft, status);
                tasks.add(task);
            }
        return tasks;
    }

        // возвращаем список работников
        public ArrayList<Worker> readWorkers (){
            ArrayList<Worker> workers = new ArrayList<>();
            // Получаем первый лист
            Sheet sheet = workbook.getSheetAt(0);
            // Перебираем строки
            int ind = 0;
            for (Row row : sheet) {
                if (ind == 0) {
                    ind = 1;
                    continue;
                }
                // Перебираем ячейки в строке
                String name = row.getCell(0).getStringCellValue();
                String surname = row.getCell(1).getStringCellValue();
                int workingTime = (int) row.getCell(2).getNumericCellValue();
                int chillingTime = (int) row.getCell(3).getNumericCellValue();
                int totalTime = (int) row.getCell(4).getNumericCellValue();
                int effectivity = (int) row.getCell(5).getNumericCellValue();
                Worker worker = new Worker(name, surname, workingTime, chillingTime, totalTime, effectivity);
                workers.add(worker);
            }
            return workers;
        }

        public void updateWorkers (List < Worker > workers) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 0; i < workers.size(); i++) {
                Worker worker = workers.get(i);
                Row row = sheet.createRow(i + 1); // Пропускаем строку заголовков
                Cell cell = row.createCell(0);
                cell.setCellValue(worker.getName());
                cell = row.createCell(1);
                cell.setCellValue(worker.getSurname());
                cell = row.createCell(2);
                cell.setCellValue(worker.getWorkingTime());
                cell = row.createCell(3);
                cell.setCellValue(worker.getChillingTime());
                cell = row.createCell(4);
                cell.setCellValue(worker.getTotalTime());
                cell = row.createCell(5);
                if (worker.getTotalTime() != 0) {
                    cell.setCellValue((worker.getWorkingTime() / ((float) worker.getTotalTime())) * 100);
                } else {
                    cell.setCellValue(0);
                }
            }
            try {
                workbook.write(new FileOutputStream(filePath));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        public void updateTasks(List <Task> tasks) {
            Sheet sheet = workbook.getSheetAt(1);
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                Row row = sheet.createRow(i + 1); // Пропускаем строку заголовков
                Cell cell = row.createCell(0);
                cell.setCellValue(task.getNumber());
                cell = row.createCell(1);
                cell.setCellValue(task.getTimeLeft());
                cell = row.createCell(2);
                cell.setCellValue(task.getStatus());
            }
            try {
                workbook.write(new FileOutputStream(filePath));
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

