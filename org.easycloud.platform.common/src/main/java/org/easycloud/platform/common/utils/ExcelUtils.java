package org.easycloud.platform.common.utils;

import java.io.IOException;

import org.easycloud.platform.common.utils.csv.CsvWriter;

public class ExcelUtils {

	/**
	 * 将数据写入到Excel中
	 * 
	 * @param fileName
	 * @param data
	 * @throws IOException
	 */
	public static void writeToFile(String fileName, String[] data) throws IOException {
		CsvWriter writer = new CsvWriter(fileName);
		writer.writeRecord(data);
		writer.close();
	}

	/**
	 * 将数据追加到已有文件中
	 * 
	 * @param fileName
	 * @param data
	 * @throws IOException
	 */
	public static void appendToFile(String fileName, String[][] data) throws IOException {
		CsvWriter writer = new CsvWriter(fileName);
		for (int i = 0; i < data.length; i++) {
			writer.writeRecord(data[i]);
		}
		writer.close();
	}

	/**
	 * 将数据写入到Excel中
	 * @param fileName
	 * @param fieldTitles
	 * @param data
	 * @throws IOException
	 */
	public static void writeToFile(String fileName, String[] fieldTitles, String[][] data) throws IOException {
		CsvWriter writer = new CsvWriter(fileName);
		writer.writeRecord(fieldTitles);
		for (int i = 0; i < data.length; i++) {
			writer.writeRecord(data[i]);
		}
		writer.close();
	}
}
