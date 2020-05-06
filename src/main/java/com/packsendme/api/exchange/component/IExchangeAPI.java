package com.packsendme.api.exchange.component;

import com.packsendme.exchange.bre.model.ExchangeBRE_Model;

public interface IExchangeAPI {
	
	public ExchangeBRE_Model getExchangeCurrent(String current, String dtNow);
	
	public Double parseExchange(String jsonData, String current);


}
