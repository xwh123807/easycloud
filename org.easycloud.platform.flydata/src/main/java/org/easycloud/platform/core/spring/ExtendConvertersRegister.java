package org.easycloud.platform.core.spring;

import org.springframework.core.convert.support.GenericConversionService;

/**
 * 扩展数据类型转换器注册器
 * @author xiangwanhong
 *
 */
public class ExtendConvertersRegister {

	/**
	 * 注册扩展数据类型转换器，平台加载时调用
	 * @param conversionService
	 */
	public static void registerExtendConverters(GenericConversionService conversionService) {
		conversionService.addConverterFactory(new StringToDateConverterFactory());
		conversionService.addConverterFactory(new StringToCalendarConverterFactory());
		conversionService.addConverterFactory(new StringToBooleanConverterFactory());
		conversionService.addConverterFactory(new DateToStringConverterFactory());
		//conversionService.addConverterFactory(new BaseEnumToStringConverterFactory());
		conversionService.addConverterFactory(new CalendarToStringConverterFactory());
		//conversionService.addConverterFactory(new StringToBaseEnumConverterFactory());
		conversionService.addConverterFactory(new StringToKeyEntityConverterFactory());
	}

}
