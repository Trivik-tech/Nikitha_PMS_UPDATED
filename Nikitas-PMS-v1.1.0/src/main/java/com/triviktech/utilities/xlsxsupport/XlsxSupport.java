package com.triviktech.utilities.xlsxsupport;

import com.triviktech.payloads.request.employee.Employee;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

public class XlsxSupport {

    public static boolean checkXlsxContentType(MultipartFile file) {
        return file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public static List<Employee> convertXlsxToListOfEmployee(InputStream is) {
        List<Employee> employees = new ArrayList<>();
        ExecutorService executorService = Executors.newCachedThreadPool();

        try (XSSFWorkbook workbook = new XSSFWorkbook(is)) {
            XSSFSheet sheet = workbook.getSheet("data");
            if (sheet == null) return employees;

            int totalRows = sheet.getPhysicalNumberOfRows();
            if (totalRows <= 1) return employees;

            List<Future<List<Employee>>> futures = new ArrayList<>();
            int threadCount = 4;
            int chunkSize = Math.max((totalRows - 1) / threadCount, 50);
            int remainingRows = (totalRows - 1) % threadCount;

            int startRow = 1;
            for (int i = 0; i < threadCount; i++) {
                int endRow = startRow + chunkSize;
                if (i == threadCount - 1) endRow += remainingRows;
                if (endRow > totalRows) endRow = totalRows;

                futures.add(executorService.submit(new RowProcessor(sheet, startRow, endRow)));
                startRow = endRow;
            }

            for (Future<List<Employee>> future : futures) {
                List<Employee> chunkEmployees = future.get();
                if (chunkEmployees != null && !chunkEmployees.isEmpty()) {
                    employees.addAll(chunkEmployees);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }

        return employees;
    }

    private static class RowProcessor implements Callable<List<Employee>> {
        private final XSSFSheet sheet;
        private final int startRow;
        private final int endRow;
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        RowProcessor(XSSFSheet sheet, int startRow, int endRow) {
            this.sheet = sheet;
            this.startRow = startRow;
            this.endRow = endRow;
        }

        @Override
        public List<Employee> call() {
            List<Employee> employees = new ArrayList<>();

            for (int i = startRow; i < endRow; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Employee employee = new Employee();

                employee.setEmpId(getStringValue(row.getCell(0)));
                employee.setName(getStringValue(row.getCell(1)));
                employee.setDob(parseDate(getStringValue(row.getCell(2))));
                employee.setDateOfJoining(parseDate(getStringValue(row.getCell(3))));
                employee.setCurrentDesignation(getStringValue(row.getCell(4)));
                employee.setDepartment(getStringValue(row.getCell(5)));
                employee.setBranch(getStringValue(row.getCell(6)));
                employee.setCategory(getStringValue(row.getCell(7)));
                employee.setLastWorkingDate(parseDate(getStringValue(row.getCell(8))));
                employee.setOfficialEmailId(getStringValue(row.getCell(9)));
                employee.setMobileNumber(getStringValue(row.getCell(10)));
                employee.setReportingManager(getStringValue(row.getCell(11)));
                employee.setEmailId(getStringValue(row.getCell(12)));

                if (employee.getEmpId() != null && !employee.getEmpId().isEmpty()) {
                    employees.add(employee);
                }
            }

            return employees;
        }

        private String getStringValue(Cell cell) {
            if (cell == null) return "";
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue().trim();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return dateFormat.format(cell.getDateCellValue());
                    } else {
                        double val = cell.getNumericCellValue();
                        return (val == Math.floor(val)) ? String.format("%.0f", val) : String.valueOf(val);
                    }
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    try {
                        return cell.getStringCellValue();
                    } catch (Exception e) {
                        return String.valueOf(cell.getNumericCellValue());
                    }
                default:
                    return "";
            }
        }

        private Date parseDate(String dateStr) {
            try {
                if (dateStr != null && !dateStr.trim().isEmpty()) {
                    return dateFormat.parse(dateStr.trim());
                }
            } catch (Exception ignored) {
            }
            return null;
        }
    }
}
