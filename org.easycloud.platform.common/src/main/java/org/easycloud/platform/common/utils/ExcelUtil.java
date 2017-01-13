package org.easycloud.platform.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	public static XSSFWorkbook readExcel(URL url) throws IOException {
		return readExcel(url.openStream());
	}

	public static XSSFWorkbook readExcel(InputStream stream) throws IOException {
		return new XSSFWorkbook(stream);
	}

	@SuppressWarnings("static-access")
	public static String getValue(XSSFCell xssfRow, String replaceStr) {
		String value = null;
		try {
			if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
				value = String.valueOf(xssfRow.getBooleanCellValue());
			} else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
				value = String.valueOf(xssfRow.getNumericCellValue());
			} else {
				value = String.valueOf(xssfRow.getStringCellValue());
			}
			if (replaceStr != null) {
				value = value.replaceAll("\n", replaceStr);
			}
		} catch (Exception e) {
		}
		return value;
	}

	public static String getValue(XSSFCell xssfRow) {
		return getValue(xssfRow, "");
	}

	public static String getCellValue(XSSFSheet sheet, int row, int col) {
		return getValue(sheet.getRow(row).getCell(col));
	}
}
