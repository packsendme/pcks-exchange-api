package com.packsendme.api.exchange.component;

import java.util.Collections;
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
import com.packsendme.exchange.bre.model.ExchangeCountryBRE_Model;
import com.packsendme.lib.utility.FormatValueMoney;

@Component
public class CurrconvAPI_Component implements IExchangeAPI {
	
	@Value(value = "${api.exchange.currconvAPI.url-current}")
	public String currconvAPI_current;

	@Value(value = "${api.exchange.currconvAPI.url-country}")
	public String currconvAPI_countries;
	
	@Value(value = "${api.exchange.currconvAPI.key}")
	public String key;

	private final String KEY_DEFAULT_CURRENT = "USD";
	
	FormatValueMoney moneyFormat = new FormatValueMoney();
	
	@Override
	public ExchangeBRE_Model getExchangeCurrent(String current, String dtNow) {
		ExchangeBRE_Model exchangeBRE_Model = null;
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
		    		currconvAPI_current,
		    		HttpMethod.GET, 
		    		request,
                    String.class,
                    uriParam);
		    
			if (response.getStatusCode() == HttpStatus.OK) {
				Double value = parseExchange(response.getBody(), current);
				exchangeBRE_Model = new ExchangeBRE_Model(KEY_DEFAULT_CURRENT, current, value, dtNow);
			}
		    return exchangeBRE_Model;
		}
		catch (Exception e ) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ExchangeCountryBRE_Model getCountriesCurrent(String countryCode) {
		ExchangeCountryBRE_Model exchangeCountryBRE_Model = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			HttpEntity request = new HttpEntity(headers);
			
			Map<String, String> uriParam = new HashMap<>();
		    uriParam.put("key", key);
		    
		    ResponseEntity<String> response = restTemplate.exchange(
		    		currconvAPI_countries,
		    		HttpMethod.GET, 
		    		request,
                    String.class,
                    uriParam);
		    
			if (response.getStatusCode() == HttpStatus.OK) {
				exchangeCountryBRE_Model = parseCountryName(response.getBody(), countryCode);
			}
			return exchangeCountryBRE_Model;
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
			return moneyFormat.formatDouble(valueResult);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return valueResult;
	}
	
	
	@Override
	public ExchangeCountryBRE_Model parseCountryName(String jsonData, String countryCode) {
		JSONParser parser = new JSONParser();
		JSONObject jsonObject;
		ExchangeCountryBRE_Model exchangeCountry = null; 
		try {
			jsonObject = (JSONObject) parser.parse(jsonData);
			String jsonParse = jsonObject.get("results").toString();
			jsonObject = (JSONObject) parser.parse(jsonParse);
			String jsonParseCountry = jsonObject.get(countryCode).toString();
			jsonObject = (JSONObject) parser.parse(jsonParseCountry);
			String jsonParseCountryName = jsonObject.get("name").toString();
			exchangeCountry = new ExchangeCountryBRE_Model(jsonObject.get("currencyId").toString(), jsonObject.get("name").toString(), jsonObject.get("currencySymbol").toString());
			System.out.println(" COUNTRY NAME "+ jsonParseCountryName);
			return exchangeCountry;
		}
		catch (Exception e ) {
			e.printStackTrace();
			return null;
		}
	}
	

}
