package com.triviktech.utilities.reports;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.triviktech.payloads.response.employee.EmployeePmsDto;
import com.triviktech.payloads.response.krakpi.KraKpiDto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class PmsCycleReport {

    public ByteArrayInputStream generatePdf(EmployeePmsDto dto) {
        try {
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.WHITE);
            Font sectionHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, BaseColor.BLACK);
            Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.BLACK);
            Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.DARK_GRAY);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);

            // Header with employee photo, title, logo
            PdfPTable headerTable = new PdfPTable(3);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1, 2, 1});

            Image empImage = null;
            try {
                if (dto.getPhotoPath() != null && !dto.getPhotoPath().isEmpty()) {
                    empImage = Image.getInstance(dto.getPhotoPath());
                    empImage.scaleToFit(70, 90);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            PdfPCell empImgCell = new PdfPCell();
            if (empImage != null) empImgCell.addElement(empImage);
            empImgCell.setBorder(Rectangle.NO_BORDER);
            empImgCell.setFixedHeight(60);
            headerTable.addCell(empImgCell);

            PdfPCell titleCell = new PdfPCell(new Phrase("Performance Review Form", titleFont));
            titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            titleCell.setBackgroundColor(new BaseColor(52, 152, 219));
            titleCell.setBorder(Rectangle.NO_BORDER);
            titleCell.setFixedHeight(25);
            headerTable.addCell(titleCell);

            Image logoImage = null;
            try {
                String logoUrl = "https://uploads.onecompiler.io/4344tsra5/4346c832f/IMG_20241226_113757-removebg.png";
                if (logoUrl != null && !logoUrl.isEmpty()) {
                    logoImage = Image.getInstance(logoUrl);
                    logoImage.scaleToFit(70, 60);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            PdfPCell logoCell = new PdfPCell();
            if (logoImage != null) logoCell.addElement(logoImage);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setFixedHeight(60);
            headerTable.addCell(logoCell);

            document.add(headerTable);
            document.add(new Paragraph("\n"));

            // Employee Section Header
            PdfPCell empSectionHeader = new PdfPCell(new Phrase("Employee Details", sectionHeaderFont));
            empSectionHeader.setBackgroundColor(new BaseColor(236, 240, 241));
            empSectionHeader.setPadding(4);
            empSectionHeader.setBorder(Rectangle.NO_BORDER);
            PdfPTable empSectionTable = new PdfPTable(1);
            empSectionTable.setWidthPercentage(100);
            empSectionTable.addCell(empSectionHeader);
            document.add(empSectionTable);

            // Employee Details in 2 columns of 3 rows each
            PdfPTable empDetailsTable = new PdfPTable(2);
            empDetailsTable.setWidthPercentage(100);
            empDetailsTable.setWidths(new float[]{1, 1});

            PdfPTable leftDetails = new PdfPTable(2);
            leftDetails.setWidthPercentage(100);
            leftDetails.addCell(createCell("Employee Name:", subTitleFont, true));
            leftDetails.addCell(createCell(dto.getName(), textFont, false));
            leftDetails.addCell(createCell("Designation:", subTitleFont, true));
            leftDetails.addCell(createCell(dto.getDesignation(), textFont, false));
            leftDetails.addCell(createCell("Due Date:", subTitleFont, true));
            leftDetails.addCell(createCell(formatDate(dto.getDueDate()), textFont, false));

            PdfPTable rightDetails = new PdfPTable(2);
            rightDetails.setWidthPercentage(100);
            rightDetails.addCell(createCell("Department:", subTitleFont, true));
            rightDetails.addCell(createCell(dto.getDepartment(), textFont, false));
            rightDetails.addCell(createCell("Employee Review Date:", subTitleFont, true));
            rightDetails.addCell(createCell(formatDate(dto.getEmployeeReviewDate()), textFont, false));
            rightDetails.addCell(createCell("Manager Review Date:", subTitleFont, true));
            rightDetails.addCell(createCell(formatDate(dto.getManagerReviewDate()), textFont, false));

            PdfPCell leftCell = new PdfPCell(leftDetails);
            leftCell.setBorder(Rectangle.NO_BORDER);
            PdfPCell rightCell = new PdfPCell(rightDetails);
            rightCell.setBorder(Rectangle.NO_BORDER);

            empDetailsTable.addCell(leftCell);
            empDetailsTable.addCell(rightCell);
            document.add(empDetailsTable);
            document.add(new Paragraph("\n"));

            // KRA -> KPI Mapping
            Map<String, List<KraKpiDto>> kraMap = new LinkedHashMap<>();
            for (KraKpiDto k : dto.getKraKpiDetails()) {
                kraMap.computeIfAbsent(k.getKra(), v -> new ArrayList<>()).add(k);
            }

            for (String kra : kraMap.keySet()) {
                PdfPCell kraHeader = new PdfPCell(new Phrase("KRA - " + kra, sectionHeaderFont));
                kraHeader.setBackgroundColor(new BaseColor(236, 240, 241));
                kraHeader.setPadding(4);
                kraHeader.setBorder(Rectangle.NO_BORDER);
                PdfPTable kraHeaderTable = new PdfPTable(1);
                kraHeaderTable.setWidthPercentage(100);
                kraHeaderTable.addCell(kraHeader);
                document.add(kraHeaderTable);

                PdfPTable kpiTable = new PdfPTable(new float[]{2.5f, 1.5f, 1.5f, 1.5f, 1.5f, 2f, 2f});
                kpiTable.setWidthPercentage(100);
                String[] headers = {"KPI's", "Weightage", "Self Rating", "Review-1", "Average", "Employee Remark", "Manager Remark"};
                for (String head : headers) {
                    PdfPCell cell = new PdfPCell(new Phrase(head, headerFont));
                    cell.setBackgroundColor(new BaseColor(52, 152, 219));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(3);
                    kpiTable.addCell(cell);
                }

                for (KraKpiDto k : kraMap.get(kra)) {
                    kpiTable.addCell(createCell(k.getKpi(), textFont, false));
                    kpiTable.addCell(createCell(String.valueOf(k.getWeightage()), textFont, false));
                    kpiTable.addCell(createCell(String.valueOf(k.getSelfScore()), textFont, false));
                    kpiTable.addCell(createCell(String.valueOf(k.getManagerScore()), textFont, false));
                    double avg = (k.getSelfScore() + k.getManagerScore()) / 2.0;
                    kpiTable.addCell(createCell(String.format("%.2f", avg), textFont, false));
                    kpiTable.addCell(createCell(k.getEmployeeRemark(), textFont, false));
                    kpiTable.addCell(createCell(k.getManagerRemark(), textFont, false));
                }
                document.add(kpiTable);
                document.add(new Paragraph("\n"));
            }

            // Score Section
            PdfPCell scoreHeader = new PdfPCell(new Phrase("Overall Score", sectionHeaderFont));
            scoreHeader.setBackgroundColor(new BaseColor(236, 240, 241));
            scoreHeader.setPadding(4);
            scoreHeader.setBorder(Rectangle.NO_BORDER);
            PdfPTable scoreHeaderTable = new PdfPTable(1);
            scoreHeaderTable.setWidthPercentage(100);
            scoreHeaderTable.addCell(scoreHeader);
            document.add(scoreHeaderTable);

            PdfPTable scoreTable = new PdfPTable(new float[]{2, 2, 2});
            scoreTable.setWidthPercentage(100);
            scoreTable.addCell(createCell("Category", subTitleFont, true));
            scoreTable.addCell(createCell("Self Score", subTitleFont, true));
            scoreTable.addCell(createCell("Review 1", subTitleFont, true));
            scoreTable.addCell(createCell("Total Score", textFont, false));
            scoreTable.addCell(createCell(String.format("%.2f", dto.getSelfScore()), textFont, false));
            scoreTable.addCell(createCell(String.format("%.2f", dto.getManagerScore()), textFont, false));
            scoreTable.addCell(createCell("Final Score", subTitleFont, true));
            PdfPCell finalScore = new PdfPCell(new Phrase(String.format("%.2f", dto.getFinalScore()), textFont));
            finalScore.setColspan(2);
            finalScore.setHorizontalAlignment(Element.ALIGN_CENTER);
            scoreTable.addCell(finalScore);
            document.add(scoreTable);
            document.add(new Paragraph("\n"));

            // Overall Remarks
            PdfPCell remarksHeader = new PdfPCell(new Phrase("Overall Remarks", sectionHeaderFont));
            remarksHeader.setBackgroundColor(new BaseColor(236, 240, 241));
            remarksHeader.setPadding(4);
            remarksHeader.setBorder(Rectangle.NO_BORDER);
            PdfPTable remarksTable = new PdfPTable(1);
            remarksTable.setWidthPercentage(100);
            remarksTable.addCell(remarksHeader);
            document.add(remarksTable);

            PdfPCell remarkContent = new PdfPCell(new Phrase(dto.getOverallRemark(), textFont));
            remarkContent.setPadding(4);
            remarkContent.setBackgroundColor(new BaseColor(236, 240, 241));
            PdfPTable finalRemark = new PdfPTable(1);
            finalRemark.setWidthPercentage(100);
            finalRemark.addCell(remarkContent);
            document.add(finalRemark);

            document.close();
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private PdfPCell createCell(String content, Font font, boolean isHeader) {
        PdfPCell cell = new PdfPCell(new Phrase(content != null ? content : "", font));
        cell.setPadding(3);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(isHeader ? Element.ALIGN_CENTER : Element.ALIGN_LEFT);
        if (isHeader) {
            cell.setBackgroundColor(new BaseColor(236, 240, 241));
        }
        return cell;
    }

    private String formatDate(String inputDate) {
        if (inputDate == null || inputDate.isEmpty()) return "";
        try {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = parser.parse(inputDate);
            return formatter.format(date);
        } catch (ParseException e) {
            // Try fallback without 'T'
            try {
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date date = parser.parse(inputDate);
                return formatter.format(date);
            } catch (ParseException ex) {
                return inputDate;
            }
        }
    }
}
