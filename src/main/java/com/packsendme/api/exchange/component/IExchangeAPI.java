package com.packsendme.api.exchange.component;

import com.packsendme.exchange.bre.model.ExchangeBRE_Model;
import com.packsendme.exchange.bre.model.ExchangeCountryBRE_Model;

public interface IExchangeAPI {
	
	public ExchangeBRE_Model getExchangeCurrent(String current, String dtNow);
	public ExchangeCountryBRE_Model getCountriesCurrent(String countryCode);

	public Double parseExchange(String jsonData, String current);
	public ExchangeCountryBRE_Model parseCountryName(String jsonData, String country);


}
