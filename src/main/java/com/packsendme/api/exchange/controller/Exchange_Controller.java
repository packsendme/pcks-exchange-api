package com.packsendme.api.exchange.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.packsendme.api.exchange.service.Exchange_Services;


@RestController
@RequestMapping("/api/exchange")
public class Exchange_Controller {
	
	@Autowired
	private Exchange_Services exchange_Services; 
	
	private Map<String,Object> header = new HashMap<String,Object>();
	
	//========================================================================================
	// METHOD POST|GET ::EXCHANGE-BRE -> CURRENT
	//========================================================================================//

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/rate/{current}")
	public ResponseEntity<?> getExchange(
			@RequestHeader("isoLanguageCode") String isoLanguageCode, 
			@RequestHeader("isoCountryCode") String isoCountryCode,
			@RequestHeader("isoCurrencyCode") String isoCurrencyCode,
			@RequestHeader("originApp") String originApp,
			@Validated  @PathVariable ("current") String current) {		
		try {
			header.put("isoLanguageCode", isoLanguageCode);
			header.put("isoCountryCode", isoCountryCode);
			header.put("isoCurrencyCode", isoCurrencyCode);
			header.put("originApp", originApp);
			
			return exchange_Services.getExchangeRate(current);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//========================================================================================
	// METHOD GET ::EXCHANGE-BRE -> LIST COUNTRIES
	//========================================================================================//

	@GetMapping("/country/{countryCode}")
	public ResponseEntity<?> getCountry(@Validated  @PathVariable ("countryCode") String countryCode) {		
		try {
			return exchange_Services.getCountryByExchange(countryCode);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
		
}
