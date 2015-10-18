package com.gmail.volodymyrdotsenko.cms.be.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

	public static SimpleDateFormat vttFormat = new SimpleDateFormat("HH:mm:ss.SSS");

	public static String convertToVtt(double second) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, (int) (second * 1000));

		return vttFormat.format(c.getTime());
	}

	public static Date vttToDate(String vtt) {
		try {
			return vtt != null && !vtt.isEmpty() ? vttFormat.parse(vtt) : null;
		} catch (ParseException e) {
			return null;
		}
	}

	public static String dateToVtt(Date vtt) {
		return vtt != null ? vttFormat.format(vtt) : "";
	}
}