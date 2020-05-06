package com.packsendme.api.exchange.component;

import java.util.Date;

import com.packsendme.exchange.bre.model.ExchangeBRE_Model;

public interface IExchangeAPI {
	
	public ExchangeBRE_Model getExchangeCurrent(String current, Date dtNow);
	
	public Double parseExchange(String jsonData, String current);


}
