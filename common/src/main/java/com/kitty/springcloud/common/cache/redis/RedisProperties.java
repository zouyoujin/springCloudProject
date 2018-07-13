package com.kitty.springcloud.common.cache.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RedisProperties {

	@Value("${spring.redis.cluster.expireseconds}")
	private int expireseconds;
	
	@Value("${spring.redis.cluster.nodes}")
	private String clusterNodes;
	
	@Value("${spring.redis.cluster.password}")
	private String password;
	
	@Value("${spring.redis.cluster.timeout}")
	private int timeout;

	public int getExpireseconds() {
		return expireseconds;
	}

	public void setExpireseconds(int expireseconds) {
		this.expireseconds = expireseconds;
	}

	public String getClusterNodes() {
		return clusterNodes;
	}

	public void setClusterNodes(String clusterNodes) {
		this.clusterNodes = clusterNodes;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
