package com.vdqgpm.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {
	private String filePath;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	public ExcelReader(String filePath) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(new File(filePath));
		this.workbook = new XSSFWorkbook(fileInputStream);
	}

	public List<List<String>> readSheet(String sheetName) {
		List<List<String>> sheetData = new ArrayList<>();
		Sheet sheet = workbook.getSheet(sheetName);
		if (sheet != null) {
			for (Row row : sheet) {
				List<String> rowData = new ArrayList<>();
				for (Cell cell : row) {
					rowData.add(getCellValueAsString(cell));
				}
				sheetData.add(rowData);
			}
		}
		return sheetData;
	}

	private String getCellValueAsString(Cell cell) {
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue().toString();
			} else {
				return String.valueOf(cell.getNumericCellValue());
			}
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case FORMULA:
			return cell.getCellFormula();
		case BLANK:
			return "";
		default:
			return "";
		}
	}

	public void close() throws IOException {
		workbook.close();
	}

}
