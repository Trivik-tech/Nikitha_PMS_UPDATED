package com.triviktech.utilities.xlsxsupport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class XlsxSupport {

    public static boolean checkXlsxContentType(MultipartFile file){
        return file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }


    public static List<?> convertXlsxToListOfEmployee(InputStream is) {
        List<?> employees = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(4); // Use 4 threads, you can adjust the number
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheet("data");

            int totalRows = sheet.getPhysicalNumberOfRows();
            List<Future<List<?>>> futures = new ArrayList<>();

            // Split the sheet into chunks, each chunk will be processed by a separate thread
            int chunkSize = totalRows / 4; // For example, split into 4 chunks
            for (int i = 0; i < 4; i++) {
                int startRow = i * chunkSize + 1; // skip the header row
                int endRow = (i == 3) ? totalRows : (i + 1) * chunkSize;


                Future<List<?>> future = executorService.submit(new RowProcessor(sheet, startRow, endRow));
                futures.add(future);
            }


            for (Future<List<?>> future : futures) {
//                employees.addAll(future.get());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown(); // Always shut down the executor
        }

        return employees;
    }

    // RowProcessor is a Callable to process a chunk of rows in parallel
    private static class RowProcessor implements Callable<List<?>> {
        private final XSSFSheet sheet;
        private final int startRow;
        private final int endRow;

        RowProcessor(XSSFSheet sheet, int startRow, int endRow) {
            this.sheet = sheet;
            this.startRow = startRow;
            this.endRow = endRow;
        }

        @Override
        public List<?> call() throws Exception {
            List<?> employees = new ArrayList<>();
            for (int i = startRow; i < endRow; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;


                Iterator<Cell> cells = row.iterator();
                int cid = 0;

                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    switch (cid) {

                    }
                    cid++;
                }

            }
            return employees;
        }
    }

}
