package org.myfly.platform.core.cache.internal;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class RedisServerInfo {
	@Autowired
	private JedisConnectionFactory jedisConnectionFactory;

	public Properties getRedisServerInfo() {
		return jedisConnectionFactory.getConnection().info();
	}

	public Properties getRedisServerInfo(String section) {
		return jedisConnectionFactory.getConnection().info(section);
	}
}
