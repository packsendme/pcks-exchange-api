package com.packsendme.api.exchange.dao;

import java.util.Date;

public interface IExchange_DAO <T> {
	
	public boolean add(String cache, String value, T object);
	public boolean delete(String cache, String value);
	public T findOne(String cache,String value);

	
}
