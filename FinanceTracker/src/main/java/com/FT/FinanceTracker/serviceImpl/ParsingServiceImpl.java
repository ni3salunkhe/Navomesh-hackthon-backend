package com.FT.FinanceTracker.serviceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import com.FT.FinanceTracker.service.ParsingService;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

@Service
public class ParsingServiceImpl implements ParsingService {

    @Override
    public String extractText(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) contentType = "";

        try {
            if (contentType.equals("application/pdf")) {
                return extractFromPdf(file);
            } else if (contentType.contains("spreadsheet") || contentType.contains("excel") || file.getOriginalFilename().endsWith(".xlsx") || file.getOriginalFilename().endsWith(".xls")) {
                return extractFromExcel(file);
            } else if (contentType.equals("text/csv") || file.getOriginalFilename().endsWith(".csv")) {
                return extractFromCsv(file);
            } else if (contentType.startsWith("image/")) {
                return extractFromImage(file);
            } else {
                // Fallback to plain text if possible
                return new String(file.getBytes(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            throw new RuntimeException("Parsing failed for " + file.getOriginalFilename() + ": " + e.getMessage(), e);
        }
    }

    private String extractFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private String extractFromExcel(MultipartFile file) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                for (Cell cell : row) {
                    sb.append(getCellValueAsString(cell)).append(" ");
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: 
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            case FORMULA: return cell.getCellFormula();
            default: return "";
        }
    }

    private String extractFromCsv(MultipartFile file) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = file.getInputStream();
             java.io.InputStreamReader isr = new java.io.InputStreamReader(is);
             CSVReader reader = new CSVReader(isr)) {
            List<String[]> lines = reader.readAll();
            for (String[] line : lines) {
                sb.append(String.join(" ", line)).append("\n");
            }
        } catch (CsvException e) {
            throw new IOException("CSV parsing failed", e);
        }
        return sb.toString();
    }

    private String extractFromImage(MultipartFile file) {
        // OCR Logic using Tess4J
        // Note: This requires Tesseract native binaries installed on the host
        Tesseract tesseract = new Tesseract();
        // tesseract.setDatapath("/path/to/tessdata"); // Set this in production
        
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
            return tesseract.doOCR(image);
        } catch (Exception e) {
            // If OCR fails (e.g. native lib missing), log it and return empty or error
            System.err.println("OCR failed: " + e.getMessage());
            return "OCR_FAILED: " + e.getMessage();
        }
    }
}
