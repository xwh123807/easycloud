package org.easycloud.platform.core.spring;

import java.util.Calendar;

import org.easycloud.platform.core.utils.DateUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class StringToCalendarConverterFactory implements ConverterFactory<String, Calendar>{

	@Override
	public <T extends Calendar> Converter<String, T> getConverter(Class<T> targetType) {
		return new StringToCalendar<>(targetType);
	}

	private static final class StringToCalendar<T extends Calendar> implements Converter<String, T>{

		private Class<T> targetType;

		public StringToCalendar(Class<T> targetType) {
			this.targetType = targetType;
		}
		
		@Override
		public T convert(String source) {
			if (source.length() == 0) {
				return null;
			}
			return DateUtil.parseCalendar(source, this.targetType);
		}
		
	}
}
