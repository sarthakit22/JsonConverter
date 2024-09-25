package com.jsonconverter.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.jsonconverter.service.JsonService;

@Service
public class JsonServiceImpl implements JsonService {

	@SuppressWarnings("resource")
	@Override
	public String jsonConverter() {
		String filePath = "D:/Curam Team/Pro/input.xlsx";
		File file = new File(filePath);
		// Check if the file exists
		if (file.exists() && file.isFile()) {
			try (FileInputStream fis = new FileInputStream(file)) {
				Workbook workbook = new XSSFWorkbook(fis);
				Sheet sheet = workbook.getSheetAt(0);
				FileWriter fileWriter = new FileWriter("D:/Curam Team/Pro/output.txt");
				for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
					Row row = sheet.getRow(rowIndex);
					if (CommonService.isNotEmptyRow(row)) {
						String json = CommonService.jsonForming(row);
						fileWriter.write(json + "\n");
					}
				}
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			return "File not found at the specified path: " + filePath;
		}
		return "Conversion complete. Output written to output.jsonl";
	}

}
