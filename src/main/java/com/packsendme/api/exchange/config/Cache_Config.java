package com.packsendme.api.exchange.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Cache_Config {

	@Value(value = "${redis.cache.exchangeBRE_SA}")
	public String exchangeBRE_SA;
	

}
