package com.packsendme.api.exchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.packsendme.api.exchange.service.Exchange_Services;


@RestController
@RequestMapping("/exchange")
public class ExchangeBRE_Controller {
	
	@Autowired
	private Exchange_Services exchange_Services; 
	
	
	//========================================================================================
	// METHOD POST|GET ::EXCHANGE-BRE
	//========================================================================================//

	@GetMapping("/{current}")
	public ResponseEntity<?> getExchangeRate(@Validated  @PathVariable ("current") String current) {		
		try {
			return exchange_Services.getRate(current);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
		
}
