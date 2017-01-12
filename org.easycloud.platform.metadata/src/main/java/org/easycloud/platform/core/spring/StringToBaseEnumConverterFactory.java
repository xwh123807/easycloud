package org.easycloud.platform.core.spring;

import org.easycloud.platform.core.domain.BaseEnum;
import org.easycloud.platform.core.utils.ClassUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;


public class StringToBaseEnumConverterFactory implements ConverterFactory<String, BaseEnum> {
	@Override
	public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
		return new StringToEnum(targetType);
	}

	private static final class StringToEnum<T extends Enum> implements Converter<String, T> {

		private Class<T> targetType;

		public StringToEnum(Class<T> targetType) {
			this.targetType = targetType;
		}

		@Override
		public T convert(String source) {
			if (source.length() == 0) {
				return null;
			}
			try {
				return (T) Enum.valueOf(targetType, source);
			} catch (Exception e) {
				return (T) ClassUtil.getEnumByTitle(targetType, source);
			}
		}

	}
}
