package org.easycloud.metadata.client;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public abstract class BaseApiClient {
	private String baseUrl;

	private RestTemplate restRemplate;

	public BaseApiClient(String baseUrl) {
		setBaseUrl(baseUrl);
		restRemplate = new RestTemplate();
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public RestTemplate getRestRemplate() {
		return restRemplate;
	}

	public void setRestRemplate(RestTemplate restRemplate) {
		this.restRemplate = restRemplate;
	}

	private String bindUrl(String path) {
		return getBaseUrl() + path;
	}

	public <T> T getForObject(String url, Class<T> responseType) throws RestClientException {
		return getRestRemplate().getForObject(bindUrl(url), responseType);
	}
}
