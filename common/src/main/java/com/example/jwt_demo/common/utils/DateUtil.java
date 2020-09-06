package com.example.jwt_demo.common.utils;

import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;

public class DateUtil {

	public static String convertFromDate(Date date, String pattern) {
		return OffsetDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
				.format(DateTimeFormatter.ofPattern(pattern));
	}

	public static String convertFromOffsetDateTime(OffsetDateTime odt, String pattern) {
		return odt.format(DateTimeFormatter.ofPattern(pattern));
	}

	public static Date convertFromString(String dateStr, String pattern) {
		try {
			return FastDateFormat.getInstance(pattern).parse(dateStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static OffsetDateTime dateToOffsetDateTime(Date date) {
		return OffsetDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	public static Date offsetDateTimeToDate(OffsetDateTime odt) {
		return Date.from(odt.toInstant());
	}
}
