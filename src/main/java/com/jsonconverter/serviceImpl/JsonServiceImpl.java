package com.jsonconverter.serviceImpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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

	@Override
	public ResponseEntity<String> xmlToJsonL() {
		// Input XML file path
		String inputXmlFile = "D:/Curam Team/Pro/input.xml";
		// Output JSONL file path
		String outputJsonlFile = "D:/Curam Team/Pro/output.jsonl";
		try {
			// Read the XML file from the file path
			InputStream xmlInput = Files.newInputStream(Paths.get(inputXmlFile));
			BufferedReader reader = new BufferedReader(new InputStreamReader(xmlInput));

			// StringBuilder to store the entire XML content
			StringBuilder xmlContent = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				xmlContent.append(line.trim());
			}

			reader.close();

			// Regex pattern to match XML snippets with dynamic starting tags
			String regex = "<question-page.*?</question-page>";
			Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
			Matcher matcher = pattern.matcher(xmlContent);

			// Create JSONL file
			BufferedWriter writer = new BufferedWriter(new FileWriter(outputJsonlFile));

			// Process each matched XML snippet
			while (matcher.find()) {
				String matchedXml = matcher.group();

				// Convert the XML snippet to JSON
				XmlMapper xmlMapper = new XmlMapper();
				JsonNode node = xmlMapper.readTree(matchedXml);

				// Build the final JSON structure
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode finalJson = objectMapper.createObjectNode().set("question-page", node);

				// Write JSON in JSONL format
				writer.write(finalJson.toString()); // Write the JSON as a single line
				writer.newLine();
			}

			writer.close();

			return ResponseEntity.ok("File converted successfully. Output path: " + outputJsonlFile);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error during conversion: " + e.getMessage());
		}
	}

}
