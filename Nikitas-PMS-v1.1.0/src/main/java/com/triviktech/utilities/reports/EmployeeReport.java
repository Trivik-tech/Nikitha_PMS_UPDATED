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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeReport {

    private final EmployeeInformationRepository employeeInformationRepository;
    private final ManagerRepository managerRepository;

    public EmployeeReport(EmployeeInformationRepository employeeInformationRepository, ManagerRepository managerRepository) {
        this.employeeInformationRepository = employeeInformationRepository;
        this.managerRepository = managerRepository;
    }

    /**
     * Generates an employee profile PDF and saves it in the user's Downloads folder.
     *
     * @param id Employee ID
     * @return true if success
     */
    public boolean generateEmployeeInfoReport(String id) {
        Optional<EmployeeInformation> optionalEmp = employeeInformationRepository.findById(id);
        Optional<Manager> optionalManager = managerRepository.findByManagerId(id);
        if (optionalEmp.isPresent()) {
            EmployeeInformation emp = optionalEmp.get();
            Manager manager = emp.getManager();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            try {
                // Dynamic path to user's Downloads folder
                String userHome = System.getProperty("user.home");
                String finalPath = userHome + "/Downloads/"+emp.getName()+"_" + emp.getEmpId() + ".pdf";

                Document document = new Document(PageSize.A4, 40, 40, 50, 40);
                PdfWriter.getInstance(document, new FileOutputStream(finalPath));
                document.open();

                // Fonts
                Font headingFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, new BaseColor(0, 102, 204));
                Font subHeadingFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                Font normalFont = new Font(Font.FontFamily.HELVETICA, 11);
                Font grayItalic = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);

                // === Header (Top row: Name + Title + Logo) ===
                PdfPTable headerTable = new PdfPTable(2);
                headerTable.setWidthPercentage(100);
                headerTable.setWidths(new float[]{3f, 1.2f});

                // Left part: Profile + Name
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
                nameCell.addElement(new Paragraph(emp.getCurrentDesignation() + "  | "+emp.getBranch(), grayItalic));
                leftHeader.addCell(nameCell);

                PdfPCell leftWrapper = new PdfPCell(leftHeader);
                leftWrapper.setBorder(Rectangle.NO_BORDER);
                headerTable.addCell(leftWrapper);

                // Right part: Logo aligned right
                Image logo = Image.getInstance("src/main/resources/static/images/nikithas-logo.png");
                logo.scaleAbsolute(90, 45);
                PdfPCell logoCell = new PdfPCell(logo);
                logoCell.setBorder(Rectangle.NO_BORDER);
                logoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                logoCell.setVerticalAlignment(Element.ALIGN_TOP);
                headerTable.addCell(logoCell);

                document.add(headerTable);
                document.add(Chunk.NEWLINE);

                // === Personal & Professional Details Table ===
                PdfPTable infoTable = new PdfPTable(2);
                infoTable.setWidthPercentage(100);
                infoTable.setWidths(new float[]{1, 1});
                infoTable.setSpacingBefore(10);

                PdfPCell personalCell = createSectionCell("Personal Information", new String[][]{
                        {"Employee ID", emp.getEmpId()},
                        {"Name", emp.getName()},
                        {"Date of Birth", sdf.format(emp.getDob())},
                        {"Category", emp.getCategory()}
                }, subHeadingFont, normalFont);

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

                // === Contact Details ===
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
//                System.out.println("PDF successfully saved to Downloads folder: " + finalPath);
                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (optionalManager.isPresent()) {

            Manager manager = optionalManager.get();

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            try {
                // Dynamic path to user's Downloads folder
                String userHome = System.getProperty("user.home");
                String finalPath = userHome + "/Downloads/"+manager.getName()+"_" + manager.getManagerId() + ".pdf";

                Document document = new Document(PageSize.A4, 40, 40, 50, 40);
                PdfWriter.getInstance(document, new FileOutputStream(finalPath));
                document.open();

                // Fonts
                Font headingFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, new BaseColor(0, 102, 204));
                Font subHeadingFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                Font normalFont = new Font(Font.FontFamily.HELVETICA, 11);
                Font grayItalic = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);

                // === Header (Top row: Name + Title + Logo) ===
                PdfPTable headerTable = new PdfPTable(2);
                headerTable.setWidthPercentage(100);
                headerTable.setWidths(new float[]{3f, 1.2f});

                // Left part: Profile + Name
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
                nameCell.addElement(new Paragraph(manager.getCurrentDesignation() + "  | "+manager.getBranch(), grayItalic));
                leftHeader.addCell(nameCell);

                PdfPCell leftWrapper = new PdfPCell(leftHeader);
                leftWrapper.setBorder(Rectangle.NO_BORDER);
                headerTable.addCell(leftWrapper);

                // Right part: Logo aligned right
                Image logo = Image.getInstance("src/main/resources/static/images/nikithas-logo.png");
                logo.scaleAbsolute(90, 45);
                PdfPCell logoCell = new PdfPCell(logo);
                logoCell.setBorder(Rectangle.NO_BORDER);
                logoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                logoCell.setVerticalAlignment(Element.ALIGN_TOP);
                headerTable.addCell(logoCell);

                document.add(headerTable);
                document.add(Chunk.NEWLINE);

                // === Personal & Professional Details Table ===
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

                // === Contact Details ===
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
//                System.out.println("PDF successfully saved to Downloads folder: " + finalPath);
                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        return false;
    }

    /**
     * Helper to create section cell with key-value layout
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

            // Header Table
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

            // Main Table
            PdfPTable table = new PdfPTable(5); // reduced column count
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2.5f, 3.5f, 3f, 5f, 3f}); // adjusted column widths
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
            int rowIndex = 0;

            for (EmployeeInformation emp : employees) {
                BaseColor bgColor = rowIndex % 2 == 0 ? evenRowColor : oddRowColor;
                addRow(table, new String[]{
                        emp.getEmpId(),
                        emp.getName(),
                        emp.getDepartment().getName(),
                        emp.getEmailId(),
                        formatDate(emp.getDateOfJoining())
                }, cellFont, bgColor);
                rowIndex++;
            }

            for (Manager mgr : managers) {
                BaseColor bgColor = rowIndex % 2 == 0 ? evenRowColor : oddRowColor;
                addRow(table, new String[]{
                        mgr.getManagerId(),
                        mgr.getName(),
                        mgr.getDepartment().getName(),
                        mgr.getOfficialEmailId(),
                        formatDate(mgr.getDateOfJoining())
                }, cellFont, bgColor);
                rowIndex++;
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

    private void addTableHeader(PdfPTable table, String headerTitle, Font font, BaseColor bgColor) {
        PdfPCell header = new PdfPCell(new Phrase(headerTitle, font));
        header.setBackgroundColor(bgColor);
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setVerticalAlignment(Element.ALIGN_MIDDLE);
        header.setPaddingTop(8f);
        header.setPaddingBottom(8f);
        header.setBorderColor(BaseColor.GRAY);
        header.setNoWrap(false);  // allow wrapping if needed
        table.addCell(header);
    }

    private void addRow(PdfPTable table, String[] data, Font font, BaseColor bgColor) {
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
            cell.setNoWrap(false); // Allow long email/name to wrap
            table.addCell(cell);
        }
    }

    private String formatDate(java.util.Date date) {
        if (date == null) return "-";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }



}
