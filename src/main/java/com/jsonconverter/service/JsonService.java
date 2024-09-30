package com.jsonconverter.service;

import org.springframework.http.ResponseEntity;

public interface JsonService {

	public String jsonConverter();

	public ResponseEntity<String> xmlToJsonL();

}
