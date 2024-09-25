package com.jsonconverter.serviceImpl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import com.google.gson.Gson;

public class CommonService {

	public static boolean isNotEmptyRow(Row row) {
		if (row != null) {
			for (Cell cell : row) {
				if (cell.getCellType() != CellType.BLANK) {
					return true; // Found a non-empty value
				}
			}
			return false; // All values are empty or null
		}
		return false;
	}

	public static String jsonForming(Row row) {

		Map<String, Object> title = new LinkedHashMap<>();
		title.put("id", row.getCell(3).toString());

		Map<String, Object> explainerTitle = new LinkedHashMap<>();
		explainerTitle.put("id", row.getCell(4).toString());

		Map<String, Object> explainerDescription = new LinkedHashMap<>();
		explainerDescription.put("id", row.getCell(5).toString());

		Map<String, Object> explainer = new LinkedHashMap<>();
		explainer.put("title", explainerTitle);
		explainer.put("description", explainerDescription);

		Map<String, Object> cluster = new LinkedHashMap<>();
		cluster.put("explainer", explainer);

		Map<String, Object> questionPage = new LinkedHashMap<>();
		questionPage.put("id", row.getCell(0).toString());
		questionPage.put("show-back-button", row.getCell(1).toString().toLowerCase()); // boolean value, not string
		questionPage.put("show-save-exit-button", row.getCell(2).toString().toLowerCase()); // boolean value, not string
		questionPage.put("title", title);
		questionPage.put("cluster", cluster);

		Map<String, Object> finalJson = new LinkedHashMap<>();
		finalJson.put("question-page", questionPage);

		// Creating Gson object (without pretty printing)
		Gson gson = new Gson();

		// Convert Map to JSON string
		return gson.toJson(finalJson);

	}

}
