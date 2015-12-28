package cn.hexiao.flog.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.hexiao.flog.util.DateUtil;

public class TestCanlendar {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -2);
		calendar.set(Calendar.YEAR, 2007);
		calendar.set(2007, 11, 1);
		System.out.println(calendar);
		
		Calendar c2 = (Calendar)calendar.clone();
		c2.set(Calendar.MONTH, 2);
		
		System.out.println(calendar);
		System.out.println(c2);
		Calendar rightNow = Calendar.getInstance();
		System.out.println(rightNow.after(calendar));
//		calendar.setTime(new Date());
		int month = calendar.get(Calendar.MONTH);
		int date = calendar.get(Calendar.DATE);
		System.out.println(calendar.get(Calendar.YEAR));
		System.out.println(month);
		System.out.println(date);
		System.out.println(Calendar.FEBRUARY);
		
		int max = calendar.getActualMaximum(Calendar.DATE);
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		
		System.out.println(max);
		System.out.println(Integer.MAX_VALUE);
		System.out.println(Long.MAX_VALUE);
		Date cu = null;
		try {
			cu = df.parse("2007-01");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date date1 = DateUtil.getStartOfMonth(cu);
		Date date2 = DateUtil.getEndOfMonth(cu);
		System.out.println(date1.toLocaleString());
		System.out.println(date2.toLocaleString());
	}

}
