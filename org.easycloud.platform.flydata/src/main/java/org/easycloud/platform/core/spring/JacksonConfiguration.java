package org.easycloud.platform.core.spring;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class JacksonConfiguration extends WebMvcConfigurerAdapter {
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(jacksonMessageConverter());
		super.configureMessageConverters(converters);
	}

	private MappingJackson2HttpMessageConverter jacksonMessageConverter() {
		MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		//TODO  Hibernate5Module
		//mapper.registerModule(new Hibernate5Module());
		messageConverter.setObjectMapper(mapper);
		return messageConverter;
	}
}
