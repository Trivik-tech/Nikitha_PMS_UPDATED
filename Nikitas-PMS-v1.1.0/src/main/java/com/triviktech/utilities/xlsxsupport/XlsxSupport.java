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

/**
 * Utility class for processing XLSX (Excel) files related to Employee data.
 * <p>
 * Provides methods to:
 * - Validate if a file is an XLSX type.
 * - Convert XLSX data into a list of Employee objects.
 * <p>
 * Uses multithreading for faster row processing when reading large Excel files.
 */
public class XlsxSupport {

    /**
     * Checks if the uploaded file has XLSX content type.
     *
     * @param file MultipartFile to check
     * @return true if the file content type is XLSX, false otherwise
     */
    public static boolean checkXlsxContentType(MultipartFile file) {
        return file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    /**
     * Converts an XLSX InputStream to a list of Employee objects.
     * <p>
     * Uses multithreading to process chunks of rows concurrently for better performance.
     *
     * @param is InputStream of the XLSX file
     * @return List of Employee objects extracted from the Excel sheet
     */
    public static List<Employee> convertXlsxToListOfEmployee(InputStream is) {
        List<Employee> employees = new ArrayList<>();
        ExecutorService executorService = Executors.newCachedThreadPool();

        try (XSSFWorkbook workbook = new XSSFWorkbook(is)) {
            // Get sheet named "data"
            XSSFSheet sheet = workbook.getSheet("data");
            if (sheet == null) return employees;

            int totalRows = sheet.getPhysicalNumberOfRows();
            if (totalRows <= 1) return employees; // No data rows present

            // Multithreading setup
            List<Future<List<Employee>>> futures = new ArrayList<>();
            int threadCount = 4;
            int chunkSize = Math.max((totalRows - 1) / threadCount, 50); // divide rows into chunks
            int remainingRows = (totalRows - 1) % threadCount;

            int startRow = 1; // skip header
            for (int i = 0; i < threadCount; i++) {
                int endRow = startRow + chunkSize;
                if (i == threadCount - 1) endRow += remainingRows; // add remaining rows to last thread
                if (endRow > totalRows) endRow = totalRows;

                futures.add(executorService.submit(new RowProcessor(sheet, startRow, endRow)));
                startRow = endRow;
            }

            // Collect results from all threads
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

    /**
     * Callable class to process a range of rows from the Excel sheet.
     */
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

        /**
         * Processes rows in the assigned range and converts them to Employee objects.
         *
         * @return List of Employee objects for this row range
         */
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

                // Only add employees with non-empty ID
                if (employee.getEmpId() != null && !employee.getEmpId().isEmpty()) {
                    employees.add(employee);
                }
            }

            return employees;
        }

        /**
         * Converts a cell to a string, handling numeric, string, boolean, and date types.
         *
         * @param cell Cell to convert
         * @return String representation of the cell value
         */
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

        /**
         * Parses a string into a Date object using "dd-MM-yyyy" format.
         *
         * @param dateStr Date string
         * @return Date object or null if parsing fails
         */
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
