package com.packsendme.api.exchange.component;

import com.packsendme.lib.common.exchange.Exchange;
import com.packsendme.lib.common.exchange.ExchangeCountry;

public interface IExchangeAPI {
	
	public Exchange getExchangeCurrent(String current, String dtNow);
	public ExchangeCountry getCountriesCurrent(String countryCode);

	public Double parseExchange(String jsonData, String current);
	public ExchangeCountry parseCountryName(String jsonData, String country);


}
