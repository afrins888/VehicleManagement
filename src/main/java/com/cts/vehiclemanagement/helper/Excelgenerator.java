package com.cts.vehiclemanagement.helper;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cts.vehiclemanagement.domain.Company;



public class Excelgenerator {
	 private List < Company > companyList;
	    private XSSFWorkbook workbook;
	    private XSSFSheet sheet;

	    public Excelgenerator(List < Company > companyList) {
	        this.companyList = companyList;
	        workbook = new XSSFWorkbook();
	    }
	    private void writeHeader() {
	        sheet = workbook.createSheet("company");
	        Row row = sheet.createRow(0);
	        CellStyle style = workbook.createCellStyle();
	        XSSFFont font = workbook.createFont();
	        font.setBold(true);
	        font.setFontHeight(16);
	        style.setFont(font);
	        createCell(row, 0, "companyid", style);
	        createCell(row, 1, "companyname", style);
	        createCell(row, 2, "address", style);
	        createCell(row, 3, "contactno", style);
	       createCell(row, 4, "createdBy", style);
	        createCell(row, 5, "updatedBy", style);
	        createCell(row, 6, "createdOn", style);
	        createCell(row, 7, "updatedOn", style);
	        

	    }
	    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
	        sheet.autoSizeColumn(columnCount);
	        Cell cell = row.createCell(columnCount);
	        if (valueOfCell instanceof Integer) {
	            cell.setCellValue((Integer) valueOfCell);
	        } else if (valueOfCell instanceof Long) {
	            cell.setCellValue((Long) valueOfCell);
	        } else if (valueOfCell instanceof Boolean) {
	            cell.setCellValue((Boolean) valueOfCell);
	        } else if (valueOfCell instanceof Date) {
	            cell.setCellValue( valueOfCell.toString());
	        } else {
	            cell.setCellValue((String) valueOfCell);
	        }
	        cell.setCellStyle(style);
	    }
	    private void write() {
	        int rowCount = 1;
	        CellStyle style = workbook.createCellStyle();
	        XSSFFont font = workbook.createFont();
	        font.setFontHeight(14);
	        style.setFont(font);
	        for (Company record: companyList) {
	            Row row = sheet.createRow(rowCount++);
	            int columnCount = 0;
	            createCell(row, columnCount++, record.getCompanyid(), style);
	            createCell(row, columnCount++, record.getCompanyname(), style);
	            createCell(row, columnCount++, record.getAddress(), style);
	            createCell(row, columnCount++, record.getContactno(), style);
	            createCell(row, columnCount++, record.getCreatedBy(), style);
	            createCell(row, columnCount++, record.getUpdatedBy(), style);
	            createCell(row, columnCount++, record.getCreatedOn(), style);
	            createCell(row, columnCount++, record.getUpdatedOn(), style);
	            
	        }
	    }
	    public void generateExcelFile(HttpServletResponse response) throws IOException {
	        writeHeader();
	        write();
	        ServletOutputStream outputStream = response.getOutputStream();
	        workbook.write(outputStream);
	        workbook.close();
	        outputStream.close();
	    }
	}



