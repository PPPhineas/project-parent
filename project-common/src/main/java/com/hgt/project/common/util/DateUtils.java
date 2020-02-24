package com.hgt.project.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

public class DateUtils {
	/**
	 * 日期格式化字符串
	 * @param dateTime
	 * @param formatter
	 * @return
	 */
	public static String dateFormat(LocalDateTime dateTime, String formatter) {
		try {
			DateTimeFormatter df = null;
			if (StringUtils.isNotBlank(formatter)) {
				df = DateTimeFormatter.ofPattern(formatter);
			} else {
				df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			}
			String localTime = df.format(dateTime);
			
			return localTime;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 获取当前时间，默认格式yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getCurrentDateTime() {
		try {
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime time = LocalDateTime.now();
			String localTime = df.format(time);
			return localTime;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 字符串转日期
	 * @param stringDate
	 * @param formatter
	 * @return
	 */
	public static LocalDateTime stringToDate(String stringDate, String formatter) {
		try {
			DateTimeFormatter df = DateTimeFormatter.ofPattern(formatter);
			LocalDateTime ldt = LocalDateTime.parse(stringDate, df);
			return ldt;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
