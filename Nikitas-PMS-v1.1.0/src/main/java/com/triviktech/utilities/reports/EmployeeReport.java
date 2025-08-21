package com.triviktech.utilities.reports;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.manager.Manager;
import com.triviktech.repositories.employee.EmployeeInformationRepository;
import com.triviktech.repositories.manager.ManagerRepository;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Service class responsible for generating employee and manager PDF reports.
 * <p>
 * Provides functionality to generate:
 * 1. Individual employee/manager profile PDF
 * 2. Consolidated employee and manager list PDF
 * <p>
 * Uses iText library for PDF generation.
 */
@Service
public class EmployeeReport {

    private final EmployeeInformationRepository employeeInformationRepository;
    private final ManagerRepository managerRepository;

    public EmployeeReport(EmployeeInformationRepository employeeInformationRepository, ManagerRepository managerRepository) {
        this.employeeInformationRepository = employeeInformationRepository;
        this.managerRepository = managerRepository;
    }

    /**
     * Generates a PDF report for a single employee or manager based on ID.
     * <p>
     * The PDF is saved in the user's Downloads folder with a filename
     * containing the name and ID of the employee/manager.
     *
     * @param id Employee or Manager ID
     * @return true if PDF is successfully generated, false otherwise
     */
    public boolean generateEmployeeInfoReport(String id) {
        // Try to fetch employee and manager from repositories
        Optional<EmployeeInformation> optionalEmp = employeeInformationRepository.findById(id);
        Optional<Manager> optionalManager = managerRepository.findByManagerId(id);

        if (optionalEmp.isPresent()) {
            EmployeeInformation emp = optionalEmp.get();
            Manager manager = emp.getManager(); // fetch reporting manager
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            try {
                // Path to save PDF dynamically in Downloads
                String userHome = System.getProperty("user.home");
                String finalPath = userHome + "/Downloads/" + emp.getName() + "_" + emp.getEmpId() + ".pdf";

                // Initialize PDF document
                Document document = new Document(PageSize.A4, 40, 40, 50, 40);
                PdfWriter.getInstance(document, new FileOutputStream(finalPath));
                document.open();

                // Define fonts for headings and content
                Font headingFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, new BaseColor(0, 102, 204));
                Font subHeadingFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                Font normalFont = new Font(Font.FontFamily.HELVETICA, 11);
                Font grayItalic = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);

                // === Header section with profile image and logo ===
                PdfPTable headerTable = new PdfPTable(2);
                headerTable.setWidthPercentage(100);
                headerTable.setWidths(new float[]{3f, 1.2f});

                // Left: profile image + name + designation + branch
                PdfPTable leftHeader = new PdfPTable(2);
                leftHeader.setWidths(new float[]{1f, 3f});
                leftHeader.setWidthPercentage(100);

                Image profile = Image.getInstance("src/main/resources/static/images/profile1.jpg");
                profile.scaleAbsolute(60, 60);
                PdfPCell profileCell = new PdfPCell(profile);
                profileCell.setBorder(Rectangle.NO_BORDER);
                profileCell.setRowspan(2);
                profileCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                leftHeader.addCell(profileCell);

                PdfPCell nameCell = new PdfPCell();
                nameCell.setBorder(Rectangle.NO_BORDER);
                nameCell.addElement(new Paragraph(emp.getName(), headingFont));
                nameCell.addElement(new Paragraph(emp.getCurrentDesignation() + "  | " + emp.getBranch(), grayItalic));
                leftHeader.addCell(nameCell);

                PdfPCell leftWrapper = new PdfPCell(leftHeader);
                leftWrapper.setBorder(Rectangle.NO_BORDER);
                headerTable.addCell(leftWrapper);

                // Right: Company logo
                Image logo = Image.getInstance("src/main/resources/static/images/nikithas-logo.png");
                logo.scaleAbsolute(90, 45);
                PdfPCell logoCell = new PdfPCell(logo);
                logoCell.setBorder(Rectangle.NO_BORDER);
                logoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                logoCell.setVerticalAlignment(Element.ALIGN_TOP);
                headerTable.addCell(logoCell);

                document.add(headerTable);
                document.add(Chunk.NEWLINE);

                // === Personal & Professional Details ===
                PdfPTable infoTable = new PdfPTable(2);
                infoTable.setWidthPercentage(100);
                infoTable.setWidths(new float[]{1, 1});
                infoTable.setSpacingBefore(10);

                // Personal Information section
                PdfPCell personalCell = createSectionCell("Personal Information", new String[][]{
                        {"Employee ID", emp.getEmpId()},
                        {"Name", emp.getName()},
                        {"Date of Birth", sdf.format(emp.getDob())},
                        {"Category", emp.getCategory()}
                }, subHeadingFont, normalFont);

                // Professional Information section
                PdfPCell professionalCell = createSectionCell("Professional Details", new String[][]{
                        {"Department", emp.getDepartment().getName()},
                        {"Branch", emp.getBranch()},
                        {"Designation", emp.getCurrentDesignation()},
                        {"Reporting Manager", manager != null ? manager.getName() : "-"},
                        {"Date of Joining", sdf.format(emp.getDateOfJoining())},
                        {"Last Working Day", emp.getLastWorkingDay() != null ? sdf.format(emp.getLastWorkingDay()) : "-"}
                }, subHeadingFont, normalFont);

                infoTable.addCell(personalCell);
                infoTable.addCell(professionalCell);
                document.add(infoTable);
                document.add(Chunk.NEWLINE);

                // === Contact Information ===
                PdfPTable contactTable = new PdfPTable(1);
                contactTable.setWidthPercentage(100);
                contactTable.setSpacingBefore(10);

                PdfPCell contactCell = createSectionCell("Contact Information", new String[][]{
                        {"Email Address", emp.getEmailId()},
                        {"Official Email ID", emp.getOfficialEmailId()},
                        {"Mobile Number", emp.getMobileNumber()}
                }, subHeadingFont, normalFont);

                contactTable.addCell(contactCell);
                document.add(contactTable);

                document.close();
                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (optionalManager.isPresent()) {
            // Similar logic for manager PDF
            Manager manager = optionalManager.get();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            try {
                String userHome = System.getProperty("user.home");
                String finalPath = userHome + "/Downloads/" + manager.getName() + "_" + manager.getManagerId() + ".pdf";

                Document document = new Document(PageSize.A4, 40, 40, 50, 40);
                PdfWriter.getInstance(document, new FileOutputStream(finalPath));
                document.open();

                Font headingFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, new BaseColor(0, 102, 204));
                Font subHeadingFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                Font normalFont = new Font(Font.FontFamily.HELVETICA, 11);
                Font grayItalic = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);

                // Header section
                PdfPTable headerTable = new PdfPTable(2);
                headerTable.setWidthPercentage(100);
                headerTable.setWidths(new float[]{3f, 1.2f});

                PdfPTable leftHeader = new PdfPTable(2);
                leftHeader.setWidths(new float[]{1f, 3f});
                leftHeader.setWidthPercentage(100);

                Image profile = Image.getInstance("src/main/resources/static/images/profile1.jpg");
                profile.scaleAbsolute(60, 60);
                PdfPCell profileCell = new PdfPCell(profile);
                profileCell.setBorder(Rectangle.NO_BORDER);
                profileCell.setRowspan(2);
                profileCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                leftHeader.addCell(profileCell);

                PdfPCell nameCell = new PdfPCell();
                nameCell.setBorder(Rectangle.NO_BORDER);
                nameCell.addElement(new Paragraph(manager.getName(), headingFont));
                nameCell.addElement(new Paragraph(manager.getCurrentDesignation() + "  | " + manager.getBranch(), grayItalic));
                leftHeader.addCell(nameCell);

                PdfPCell leftWrapper = new PdfPCell(leftHeader);
                leftWrapper.setBorder(Rectangle.NO_BORDER);
                headerTable.addCell(leftWrapper);

                Image logo = Image.getInstance("src/main/resources/static/images/nikithas-logo.png");
                logo.scaleAbsolute(90, 45);
                PdfPCell logoCell = new PdfPCell(logo);
                logoCell.setBorder(Rectangle.NO_BORDER);
                logoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                logoCell.setVerticalAlignment(Element.ALIGN_TOP);
                headerTable.addCell(logoCell);

                document.add(headerTable);
                document.add(Chunk.NEWLINE);

                PdfPTable infoTable = new PdfPTable(2);
                infoTable.setWidthPercentage(100);
                infoTable.setWidths(new float[]{1, 1});
                infoTable.setSpacingBefore(10);

                PdfPCell personalCell = createSectionCell("Personal Information", new String[][]{
                        {"Employee ID", manager.getManagerId()},
                        {"Name", manager.getName()},
                        {"Date of Birth", sdf.format(manager.getDob())},
                        {"Category", manager.getCategory()}
                }, subHeadingFont, normalFont);

                PdfPCell professionalCell = createSectionCell("Professional Details", new String[][]{
                        {"Department", manager.getDepartment().getName()},
                        {"Branch", manager.getBranch()},
                        {"Designation", manager.getCurrentDesignation()},
                        {"Reporting Manager", manager != null ? manager.getReportingManager() : "-"},
                        {"Date of Joining", sdf.format(manager.getDateOfJoining())},
                        {"Last Working Day", manager.getLastWorkingDate() != null ? sdf.format(manager.getLastWorkingDate()) : "-"}
                }, subHeadingFont, normalFont);

                infoTable.addCell(personalCell);
                infoTable.addCell(professionalCell);
                document.add(infoTable);
                document.add(Chunk.NEWLINE);

                PdfPTable contactTable = new PdfPTable(1);
                contactTable.setWidthPercentage(100);
                contactTable.setSpacingBefore(10);

                PdfPCell contactCell = createSectionCell("Contact Information", new String[][]{
                        {"Email Address", manager.getEmailId()},
                        {"Official Email ID", manager.getOfficialEmailId()},
                        {"Mobile Number", manager.getMobileNumber()}
                }, subHeadingFont, normalFont);

                contactTable.addCell(contactCell);
                document.add(contactTable);

                document.close();
                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * Creates a section in the PDF with a title and key-value rows.
     *
     * @param title      Section title
     * @param rows       Key-value pairs to display
     * @param titleFont  Font for section title
     * @param valueFont  Font for values
     * @return PdfPCell representing the section
     */
    private PdfPCell createSectionCell(String title, String[][] rows, Font titleFont, Font valueFont) {
        PdfPCell cell = new PdfPCell();
        cell.setPadding(10);
        cell.setBorder(Rectangle.BOX);
        cell.setBackgroundColor(new BaseColor(245, 245, 245));

        Paragraph titlePara = new Paragraph(title, titleFont);
        titlePara.setSpacingAfter(5);
        cell.addElement(titlePara);
        cell.addElement(new LineSeparator());

        for (String[] row : rows) {
            cell.addElement(new Paragraph("• " + row[0] + ": " + row[1], valueFont));
        }

        return cell;
    }

    /**
     * Generates a consolidated PDF report of all employees and managers.
     *
     * @return true if PDF is generated successfully, false otherwise
     */
    public boolean generateEmployeeListPdf() {
        List<EmployeeInformation> employees = employeeInformationRepository.findAll();
        List<Manager> managers = managerRepository.findAll();

        String userHome = System.getProperty("user.home");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String date = LocalDate.now().format(formatter);
        String pdfPath = userHome + "/Downloads/Employee_List_Report_" + date + ".pdf";

        Document document = new Document(PageSize.A4, 36, 36, 36, 36);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
            document.open();

            // Header section
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{8f, 2f});

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY);
            PdfPCell titleCell = new PdfPCell(new Phrase("Employee List Report", titleFont));
            titleCell.setBorder(Rectangle.NO_BORDER);
            titleCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            titleCell.setPaddingBottom(5);
            headerTable.addCell(titleCell);

            Image logo = Image.getInstance("src/main/resources/static/images/nikithas-logo.png");
            logo.scaleToFit(60, 60);
            PdfPCell logoCell = new PdfPCell(logo, false);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            logoCell.setVerticalAlignment(Element.ALIGN_TOP);
            headerTable.addCell(logoCell);

            document.add(headerTable);

            // Table for employees/managers
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2.5f, 3.5f, 3f, 5f, 3f});
            table.setSpacingBefore(10f);

            Font headFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
            BaseColor headerColor = new BaseColor(0, 102, 204);
            addTableHeader(table, "ID", headFont, headerColor);
            addTableHeader(table, "Name", headFont, headerColor);
            addTableHeader(table, "Department", headFont, headerColor);
            addTableHeader(table, "Email", headFont, headerColor);
            addTableHeader(table, "Date of Joining", headFont, headerColor);

            Font cellFont = new Font(Font.FontFamily.HELVETICA, 10);
            BaseColor evenRowColor = new BaseColor(245, 245, 245);
            BaseColor oddRowColor = BaseColor.WHITE;

            // Generate table rows asynchronously for performance
            List<CompletableFuture<List<PdfPCell>>> employeeFutures = employees.stream()
                    .map(emp -> CompletableFuture.supplyAsync(() -> {
                        int index = employees.indexOf(emp);
                        BaseColor bgColor = index % 2 == 0 ? evenRowColor : oddRowColor;
                        return prepareRow(new String[]{
                                emp.getEmpId(),
                                emp.getName(),
                                emp.getDepartment().getName(),
                                emp.getEmailId(),
                                formatDate(emp.getDateOfJoining())
                        }, cellFont, bgColor);
                    }))
                    .collect(Collectors.toList());

            List<CompletableFuture<List<PdfPCell>>> managerFutures = managers.stream()
                    .map(mgr -> CompletableFuture.supplyAsync(() -> {
                        int index = employees.size() + managers.indexOf(mgr);
                        BaseColor bgColor = index % 2 == 0 ? evenRowColor : oddRowColor;
                        return prepareRow(new String[]{
                                mgr.getManagerId(),
                                mgr.getName(),
                                mgr.getDepartment().getName(),
                                mgr.getOfficialEmailId(),
                                formatDate(mgr.getDateOfJoining())
                        }, cellFont, bgColor);
                    }))
                    .collect(Collectors.toList());

            List<CompletableFuture<List<PdfPCell>>> allFutures = new ArrayList<>();
            allFutures.addAll(employeeFutures);
            allFutures.addAll(managerFutures);

            // Wait for completion and collect all rows
            List<List<PdfPCell>> rows = allFutures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());

            // Add rows to table
            for (List<PdfPCell> row : rows) {
                for (PdfPCell cell : row) {
                    table.addCell(cell);
                }
            }

            document.add(table);
            document.close();
            writer.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Adds a header cell to the PDF table
     *
     * @param table  Table to which the header is added
     * @param headerTitle Text for header
     * @param font   Font of the header text
     * @param bgColor Background color of header
     */
    private void addTableHeader(PdfPTable table, String headerTitle, Font font, BaseColor bgColor) {
        PdfPCell header = new PdfPCell(new Phrase(headerTitle, font));
        header.setBackgroundColor(bgColor);
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setVerticalAlignment(Element.ALIGN_MIDDLE);
        header.setPaddingTop(8f);
        header.setPaddingBottom(8f);
        header.setBorderColor(BaseColor.GRAY);
        header.setNoWrap(false);
        table.addCell(header);
    }

    /**
     * Prepares a list of cells for a row in the table
     *
     * @param data    Array of string values
     * @param font    Font for cell content
     * @param bgColor Background color for row
     * @return List of PdfPCell
     */
    private List<PdfPCell> prepareRow(String[] data, Font font, BaseColor bgColor) {
        List<PdfPCell> cells = new ArrayList<>();
        for (String cellText : data) {
            PdfPCell cell = new PdfPCell(new Phrase(cellText, font));
            cell.setPaddingTop(6f);
            cell.setPaddingBottom(6f);
            cell.setPaddingLeft(4f);
            cell.setPaddingRight(4f);
            cell.setBackgroundColor(bgColor);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(BaseColor.LIGHT_GRAY);
            cell.setNoWrap(false);
            cells.add(cell);
        }
        return cells;
    }

    /**
     * Formats Date object to "dd-MM-yyyy" string format
     *
     * @param date Date object
     * @return formatted string or "-" if date is null
     */
    private String formatDate(java.util.Date date) {
        if (date == null) return "-";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }
}
