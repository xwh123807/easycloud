package org.easycloud.platform.core.spring;

import java.util.Calendar;

import org.easycloud.platform.core.utils.DateUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class CalendarToStringConverterFactory implements ConverterFactory<Calendar, String>{

	@Override
	public <T extends String> Converter<Calendar, T> getConverter(Class<T> targetType) {
		return new CalendarToString(targetType);
	}

	private static final class CalendarToString<T extends Calendar> implements Converter<T, String>{

		public CalendarToString(Class<T> targetType) {
		}

		@Override
		public String convert(T source) {
			if (source == null) {
				return null;
			}
			return DateUtil.toString(source);
		}
		
	}
}
