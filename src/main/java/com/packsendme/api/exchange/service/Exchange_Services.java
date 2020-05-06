package com.packsendme.api.exchange.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.packsendme.api.exchange.component.CurrconvAPI_Component;
import com.packsendme.api.exchange.config.Cache_Config;
import com.packsendme.api.exchange.dao.ExchangeImpl_DAO;
import com.packsendme.exchange.bre.model.ExchangeBRE_Model;
import com.packsendme.lib.common.constants.generic.HttpExceptionPackSend;
import com.packsendme.lib.common.response.Response;

@Service
@ComponentScan({"com.packsendme.api.exchange.dao,com.packsendme.api.exchange.component"})
public class Exchange_Services {
	
	@Autowired
	Cache_Config cacheConfig;
	
	@Autowired
	ExchangeImpl_DAO<ExchangeBRE_Model> exchangeBREImpl_DAO;
	
	@Autowired
	CurrconvAPI_Component currconvAPI; 

	ExchangeBRE_Model exchangeModel = null;

	public ResponseEntity<?> getRate(String current) {
		Response<ExchangeBRE_Model> responseObj = null;
		
		try {
			 // (1) Find In Cache IF current exist
			ExchangeBRE_Model exchangeBRE = exchangeBREImpl_DAO.findOne(cacheConfig.exchangeBRE_SA, current);
			
			if(exchangeBRE != null) {
				if(exchangeBRE.dt_exchange.equals(new Date())){
					responseObj = new Response<ExchangeBRE_Model>(HttpExceptionPackSend.FOUND_EXCHANGE.value(),HttpExceptionPackSend.FOUND_EXCHANGE.getAction(), exchangeBRE);
				}
				else {
					 // (2) Find In api.currconv.com
					exchangeModel = currconvAPI.getExchangeCurrent(current);
					responseObj = new Response<ExchangeBRE_Model>(HttpExceptionPackSend.FOUND_EXCHANGE.value(),HttpExceptionPackSend.FOUND_EXCHANGE.getAction(), exchangeModel);

					Thread t1 = new Thread(new Runnable() {
						@Override
				        public void run() {
				        	try {
								Thread.sleep(1000);
								exchangeBREImpl_DAO.add(cacheConfig.exchangeBRE_SA, exchangeModel.to, exchangeModel);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
				        }
				    });
					t1.start();
				}
			}
			else {
				 // (2) Find In api.currconv.com
				exchangeModel = currconvAPI.getExchangeCurrent(current);
				responseObj = new Response<ExchangeBRE_Model>(HttpExceptionPackSend.FOUND_EXCHANGE.value(),HttpExceptionPackSend.FOUND_EXCHANGE.getAction(), exchangeModel);
				Thread t2 = new Thread(new Runnable() {
					@Override
			        public void run() {
			        	try {
							Thread.sleep(1000);
							exchangeBREImpl_DAO.add(cacheConfig.exchangeBRE_SA, exchangeModel.to, exchangeModel);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
			        }
			    });
				t2.start();
			}
			return new ResponseEntity<>(responseObj, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			responseObj = new Response<ExchangeBRE_Model>(HttpExceptionPackSend.FAIL_EXECUTION.value(),HttpExceptionPackSend.EXCHANGE_RATE.getAction(), null);
			return new ResponseEntity<>(responseObj, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
