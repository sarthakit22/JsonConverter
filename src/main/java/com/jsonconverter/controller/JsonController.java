package com.jsonconverter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jsonconverter.service.JsonService;

@RestController
@CrossOrigin("*")
public class JsonController {

	@Autowired
	private JsonService service;

	@GetMapping("/json")
	public String jsonConverter() {
		return service.jsonConverter();
	}
	
	@GetMapping("/json-convert")
	public ResponseEntity<String> xmlToJsonL() {
		return service.xmlToJsonL();
	}

}
