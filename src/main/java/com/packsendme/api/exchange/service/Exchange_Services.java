package com.packsendme.api.exchange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.packsendme.api.exchange.component.CurrconvAPI_Component;
import com.packsendme.api.exchange.config.Cache_Config;
import com.packsendme.api.exchange.dao.ExchangeImpl_DAO;
import com.packsendme.lib.common.constants.generic.HttpExceptionPackSend;
import com.packsendme.lib.common.exchange.Exchange;
import com.packsendme.lib.common.exchange.ExchangeCountry;
import com.packsendme.lib.common.response.Response;
import com.packsendme.lib.utility.ConvertFormat;

@Service
@ComponentScan({"com.packsendme.api.exchange.component"})
public class Exchange_Services {
	
	@Autowired
	Cache_Config cacheConfig;
	
	@Autowired
	ExchangeImpl_DAO<Exchange> exchangeBREImpl_DAO;
	
	@Autowired(required=true)
	CurrconvAPI_Component currconvAPI; 

	Exchange exchangeModel = null;
	
	private final String KEY_DEFAULT_CURRENT = "USD";
	
	
	//========================================================================================
	// METHOD POST|GET ::EXCHANGE-BRE -> CURRENT
	//========================================================================================//

	
	public ResponseEntity<?> getExchangeRate(String current) {
		Response<Exchange> responseObj = null;
		ConvertFormat dateFormat = new ConvertFormat();
		
		try {
			String nwDateS = dateFormat.convertDateNowToStringShort();

			// (1) Find In Cache IF current exist
			Exchange exchangeBRE_Cache = exchangeBREImpl_DAO.findOne(cacheConfig.exchangeBRE_SA, current);

			if(exchangeBRE_Cache != null) {
				if(exchangeBRE_Cache.dt_exchange.equals(nwDateS)){
					System.out.println(" --- NO API "+ nwDateS);
					responseObj = new Response<Exchange>(HttpExceptionPackSend.FOUND_EXCHANGE.value(),HttpExceptionPackSend.FOUND_EXCHANGE.getAction(), exchangeBRE_Cache);
				}
				else {
					System.out.println(" ");
					System.out.println(" --- 1 API "+ nwDateS);
					 // (2) Find In api.currconv.com
					Exchange exchangeBRE = getExchangeRateAPI(current, nwDateS, exchangeBRE_Cache);
					responseObj = new Response<Exchange>(HttpExceptionPackSend.FOUND_EXCHANGE.value(),HttpExceptionPackSend.FOUND_EXCHANGE.getAction(), exchangeBRE);
				}
			}
			else {
				System.out.println(" ");
				System.out.println(" --- 2 API "+ nwDateS);
				// (2) Find In api.currconv.com
				Exchange exchangeBRE = getExchangeRateAPI(current, nwDateS, exchangeBRE_Cache);
				responseObj = new Response<Exchange>(HttpExceptionPackSend.FOUND_EXCHANGE.value(),HttpExceptionPackSend.FOUND_EXCHANGE.getAction(), exchangeBRE);
			}
			return new ResponseEntity<>(responseObj, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			responseObj = new Response<Exchange>(HttpExceptionPackSend.FAIL_EXECUTION.value(),HttpExceptionPackSend.EXCHANGE_RATE.getAction(), null);
			return new ResponseEntity<>(responseObj, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public Exchange getExchangeRateAPI(String current, String  nwDateS, Exchange exchangeBRE_Cache) {
		
		 // (2) Find In api.currconv.com
		exchangeModel = currconvAPI.getExchangeCurrent(current, nwDateS);
		
		if(exchangeModel == null){
			if(exchangeBRE_Cache != null) {
				return exchangeBRE_Cache;
			}
			else {
				exchangeModel = new Exchange(KEY_DEFAULT_CURRENT, current, 0.0, nwDateS);
			}
		}
		else {
			// SAVE EXCHANGE NOW IN CACHE
			Thread t2 = new Thread(new Runnable() {
				@Override
		        public void run() {
					System.out.println(" Thread Thread Thread Thread Thread Thread Thread");
		        	try {
						Thread.sleep(1000);
						exchangeBREImpl_DAO.add(cacheConfig.exchangeBRE_SA, exchangeModel.toCurrent, exchangeModel);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		        }
		    });
			t2.start();
		}
		return exchangeModel;
	}
		
	//========================================================================================
	// METHOD GET ::EXCHANGE-BRE -> LIST COUNTRIES
	//========================================================================================//

	public ResponseEntity<?> getCountryByExchange(String countryCode) {
		Response<ExchangeCountry> responseObj = null;
		
		// (2) Find In api.currconv.com
		ExchangeCountry exchangeCountryModel = currconvAPI.getCountriesCurrent(countryCode);
		if(exchangeCountryModel != null) {
			responseObj = new Response<ExchangeCountry>(HttpExceptionPackSend.FOUND_EXCHANGE.value(),HttpExceptionPackSend.FOUND_EXCHANGE.getAction(), exchangeCountryModel);
			return new ResponseEntity<>(responseObj, HttpStatus.ACCEPTED);
		}
		else {
			responseObj = new Response<ExchangeCountry>(HttpExceptionPackSend.COUNTRY_NOT_FOUND.value(),HttpExceptionPackSend.COUNTRY_NOT_FOUND.getAction(), null);
			return new ResponseEntity<>(responseObj, HttpStatus.NOT_FOUND);
		}
	}
	
}
