package com.packsendme.api.exchange.component;

import java.util.Collections;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.packsendme.exchange.bre.model.ExchangeBRE_Model;

@Component
public class CurrconvAPI_Component implements IExchangeAPI {
	
	@Value(value = "${api.exchange.currconvAPI.url}")
	public String currconvAPI;

	@Value(value = "${api.exchange.currconvAPI.key}")
	public String key;

	private final String KEY_DEFAULT_CURRENT = "USD";
	
	@Override
	public ExchangeBRE_Model getExchangeCurrent(String current, Date dtNow) {
		ExchangeBRE_Model exchangeBRE_Model = new ExchangeBRE_Model();
		try {
			RestTemplate restTemplate = new RestTemplate();
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			HttpEntity request = new HttpEntity(headers);
			
			Map<String, String> uriParam = new HashMap<>();
		    uriParam.put("current", current);
		    uriParam.put("key", key);
		    
		    ResponseEntity<String> response = restTemplate.exchange(
		    		currconvAPI,
		    		HttpMethod.GET, 
		    		request,
                    String.class,
                    uriParam);
		    
			if (response.getStatusCode() == HttpStatus.OK) {
				exchangeBRE_Model.value = parseExchange(response.getBody(), current);
				exchangeBRE_Model.dt_exchange = dtNow;
				exchangeBRE_Model.to = current;
				exchangeBRE_Model.from = KEY_DEFAULT_CURRENT;
			}
		    return exchangeBRE_Model;
		}
		catch (Exception e ) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Double parseExchange(String jsonData, String current) {
		String key = KEY_DEFAULT_CURRENT+"_"+current;
		double valueResult = 0.00;
		
    	JSONParser parser = new JSONParser();
    	JSONObject jsonObject;
    	
		try {
			jsonObject = (JSONObject) parser.parse(jsonData);
			String resultS = jsonObject.get(key).toString();
			valueResult = Double.parseDouble(resultS);
			return valueResult;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return valueResult;
	}
	
	
	
	

}
