package org.easycloud.platform.flydata.spring;

import java.util.Date;

import org.easycloud.platform.metadata.utils.DateUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class DateToStringConverterFactory implements ConverterFactory<Date, String>{

	@Override
	public <T extends String> Converter<Date, T> getConverter(Class<T> targetType) {
		return new DateToString(targetType);
	}

	private static final class DateToString<T extends Date> implements Converter<T, String>{

		private Class<T> targetType;

		public DateToString(Class<T> targetType) {
			this.targetType = targetType;
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
