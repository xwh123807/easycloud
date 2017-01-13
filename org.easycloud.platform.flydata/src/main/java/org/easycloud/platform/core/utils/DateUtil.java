package org.easycloud.platform.core.utils;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

public class DateUtil {
	private static Log log = LogFactory.getLog(DateUtil.class);

	@SuppressWarnings("unchecked")
	public static <T extends Date> T parseDate(String value, Class<T> targetClass) {
		Assert.notNull(value, "value must not be null");
		Assert.notNull(targetClass, "Target class must not be null");

		if (Date.class == targetClass) {
			return (T) dateStrToDate(value);
		} else if (Timestamp.class == targetClass) {
			return (T) datetimeStrToTimestamp(value);
		} else if (java.sql.Date.class == targetClass) {
			return (T) dateStrToSqlDate(value);
		} else if (java.sql.Time.class == targetClass) {
			return (T) timeStrToSqlTime(value);
		} else {
			throw new IllegalArgumentException(
					"Cannot convert String [" + value + "] to target class [" + targetClass.getName() + "]");
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends Calendar> T parseCalendar(String value, Class<T> targetClass) {
		Assert.notNull(value, "value must not be null");
		Assert.notNull(targetClass, "Target class must not be null");

		if (Calendar.class == targetClass) {
			return (T) dateStrToCalendar(value);
		} else {
			throw new IllegalArgumentException(
					"Cannot convert String [" + value + "] to target class [" + targetClass.getName() + "]");
		}
	}

	public static String toString(Date value) {
		Assert.notNull(value, "value must not be null");
		if (value.getClass() == Date.class) {
			return dateToStr(value);
		} else if (value.getClass() == Timestamp.class) {
			return timestampToStr((Timestamp) value);
		} else if (value.getClass() == Time.class) {
			return timeToStr((Time) value);
		} else if (value.getClass() == java.sql.Date.class) {
			return sqldateToStr((java.sql.Date) value);
		} else {
			throw new IllegalArgumentException("Cannot convert String [" + value + "] to target class [String]");
		}
	}

	public static String toString(Calendar source) {
		return dateToStr(source);
	}

	public static String sqldateToStr(java.sql.Date value) {
		return dateToStr(value);
	}

	public static String timestampToStr(Timestamp value) {
		return datetimeToStr(value);
	}

	public static Time timeStrToSqlTime(String value) {
		Date date = null;
		try {
			date = new SimpleDateFormat("HH:mm:ss").parse(value);
		} catch (ParseException e) {
			if (log.isErrorEnabled()) {
				log.error("时间转换错误，格式HH:mm:ss[" + value + "] , 错误信息：" + e.getMessage());
			}
			return null;
		}
		return new Time(date.getTime());
	}

	/**
	 * 系统时间
	 * 
	 * @return
	 */
	public static Calendar now() {
		return Calendar.getInstance();
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	public static String datetimeToStr(Calendar date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date.getTime());
	}

	/**
	 * 日期字符串到日期格式的转换，格式yyyy-mm-dd
	 * 
	 * @param datestr
	 * @return
	 */
	public static Date dateStrToDate(String datestr) {
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(datestr);
		} catch (ParseException e) {
			if (log.isErrorEnabled()) {
				log.error("时间转换错误，格式yyyy-MM-dd[" + datestr + "], 错误信息：" + e.getMessage());
			}
		}
		return date;
	}

	public static java.sql.Date dateStrToSqlDate(String value) {
		java.sql.Date date = new java.sql.Date(dateStrToDate(value).getTime());
		return date;
	}

	/**
	 * 日期时间字符串转换为日期类型
	 * 
	 * @param datetimestr
	 * @return
	 */
	public static Date datetimeStrToDate(String datetimestr) {
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(datetimestr);
		} catch (ParseException e) {
			if (log.isErrorEnabled()) {
				log.error("时间转换错误，格式yyyy-MM-dd HH:mm:ss[" + datetimestr + "], 错误信息：" + e.getMessage());
			}
		}
		return date;
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToStr(Calendar date) {
		return dateToStr(date.getTime());
	}

	/**
	 * 日期转换为字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToStr(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	/**
	 * 日期时间类型转换为字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String datetimeToStr(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	public static String timeToStr(Time sqlTimeField) {
		return sqlTimeField.toString();
	}

	public static Calendar dateStrToCalendar(String value) {
		Calendar date = Calendar.getInstance();
		date.setTime(dateStrToDate(value));
		return date;
	}

	public static Timestamp datetimeStrToTimestamp(String value) {
		return new Timestamp(datetimeStrToDate(value).getTime());
	}

	public static Date nowDate() {
		return now().getTime();
	}

	public static Time nowSqlTime() {
		return new java.sql.Time(nowDate().getTime());
	}

	public static java.sql.Date nowSqlDate() {
		return new java.sql.Date(nowDate().getTime());
	}

	public static Timestamp nowSqlTimestamp() {
		return new Timestamp(Calendar.getInstance().getTime().getTime());
	}

}
