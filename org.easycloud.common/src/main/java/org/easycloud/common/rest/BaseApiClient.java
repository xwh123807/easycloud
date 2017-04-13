package org.easycloud.common.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public abstract class BaseApiClient {
	private String baseUrl;

	private RestTemplate restTemplate;

	public BaseApiClient(String baseUrl) {
		setBaseUrl(baseUrl);
		restTemplate = new RestTemplate();
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl+"/";
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	private String bindUrl(String path) {
		String tmp = path.startsWith("/") ? path.substring(1) : path;
		if (!path.contains("?")){
			tmp = tmp.endsWith("/") ? tmp : tmp + "/";
		}
		return getBaseUrl() + tmp;
	}

	public <T> T getForObject(String path, Class<T> responseType, Object... uriVariables) throws RestClientException {
		return getRestTemplate().getForObject(bindUrl(path), responseType, uriVariables);
	}
	
	public <T> ResponseEntity<T> getForEntity(String path, Class<T> responseType, Object... uriVariables)
			throws RestClientException{
		return getRestTemplate().getForEntity(bindUrl(path), responseType, uriVariables);
	}
	
	public <T> ResponseEntity<T> postForEntity(String path, Object request, Class<T> responseType, Object... uriVariables)
			throws RestClientException{
		return getRestTemplate().postForEntity(bindUrl(path), request, responseType, uriVariables);
	}
	
	public void put(String path, Object request, Object... uriVariables) throws RestClientException {
		getRestTemplate().put(bindUrl(path), request, uriVariables);
	}
	
	public void delete(String path, Object... uriVariables) throws RestClientException {
		getRestTemplate().delete(bindUrl(path), uriVariables);
	}
}
