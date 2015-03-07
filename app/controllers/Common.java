package controllers;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import play.Play;

public class Common {
	
    private final static SimpleDateFormat DF = new SimpleDateFormat("dd MMM yyyy");
    private final static SimpleDateFormat DF_MONTH = new SimpleDateFormat("MMM yyyy");
    private final static SimpleDateFormat DF_SQL_MONTH = new SimpleDateFormat("MM-yyyy");
    private final static SimpleDateFormat DF_SQL_MONTH_START = new SimpleDateFormat("yyyy-MM-01");
    private final static SimpleDateFormat DF_SQL_YEAR = new SimpleDateFormat("yyyy");
    private final static SimpleDateFormat DF_SQL_YEAR_START = new SimpleDateFormat("yyyy-01-01");
    
    public final static int ONE_MONTH = -1;
    public final static int TWO_MONTHS = -2;
    public final static int THREE_MONTHS = -3;
    public final static int SIX_MONTHS = -6;
    public final static int ONE_YEAR = -12;
    public final static int DEFAULT_SALES_PERIOD = ONE_MONTH;
    
    public Common() {}

	public static boolean isDev() {
        return play.api.Play.isDev(play.api.Play.current());
	}
	
	
	public static String getPriceString(BigDecimal price) {
		if (null == price) return "";
		return getPriceString(price.doubleValue());
	}
	
	public static String getDateString(Date date) {
		if (null == date) return "";
		return DF.format(date);
	}
	
	public static String getPriceString(double price) {
		if (price >= 0)
			return String.format("£%,.2f", price);
		return String.format("-£%,.2f", -price);
	}
	
	public static Double getMargin(BigDecimal sale, BigDecimal cogs) {
		if (sale.doubleValue() == 0) return 0d;
		BigDecimal grossProfit = sale.subtract(cogs);
		double margin = grossProfit.divide(sale, 2, RoundingMode.HALF_UP).doubleValue();
		return margin;
	}
	
	public static String getMarginString(double margin) {
		if (margin >= 0)
			return String.format("%,.1f%%", margin * 100);
		return String.format("-%,.1f%%", -margin * 100);
	}
	
	public static List<String> getValidAddress() {
		return Play.application().configuration().getStringList("turnbulls.valid.ipaddresses");
	}
	
	public static String getMarginString(BigDecimal margin) {
		if (null == margin) return "";
		return getMarginString(margin.doubleValue());
	}
	
	/**
	 * Remove terms used for margin control
	 * @param term
	 * @return
	 */
	public static String filterTerm(String term) {
		if (term.equals("TRADE")) return "";
		if (term.equals("RETAIL")) return "";
		return term;
	}
	
	public static Date getPeriod(Long ms, int offset) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(ms));
		cal.add(Calendar.MONTH, offset);
		return cal.getTime();
	}

	public static Date getDayOffset(Date date, int offset) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, offset);
		return cal.getTime();
	}

	public static String getSqlMonth(Date date) {
		return DF_SQL_MONTH.format(date);
	}

	public static String getSqlMonthStart(Date date) {
		return DF_SQL_MONTH_START.format(date);
	}

	public static String getSqlNextMonthStart(Date date) {
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		return DF_SQL_MONTH_START.format(cal.getTime());
	}

	/**
	 * Return true if current month...
	 * @param period
	 * @return
	 */
	public static boolean isCurrentPeriod(String periodStart) {
		if (periodStart.equals(DF_SQL_MONTH_START.format(new Date())))
			return true;
		return false;
	}
	
	public static String getMonth(Date date) {
		return DF_MONTH.format(date);
	}

	public static String getSqlYear(Date date) {
		return DF_SQL_YEAR.format(date);
	}
	
	public static String getSqlYearStart(Date date) {
		return DF_SQL_YEAR_START.format(date);
	}
	
    public static Date getDateFromForm(Map<String, String[]> values) {
    	final String year = values.get("year")[0];
    	final String month = values.get("month")[0];
    	
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(year), Integer.parseInt(month), 1);
    	return cal.getTime();
    }

	public static Date getCurrentPeriod() {
		return getStartOfMonth(new Date());
	}
	
	public static Date getYearEnd(Date date) {
		Calendar today = Calendar.getInstance();
		today.setTime(date);
		int year = today.get(Calendar.YEAR);
		int day = 1;
		return getCalendar(year, 11, day);
	}
	
	public static Date getStartOfMonth(Date date) {
		Calendar today = Calendar.getInstance();
		today.setTime(date);
		int month = today.get(Calendar.MONTH);
		int year = today.get(Calendar.YEAR);
		int day = 1;
		return getCalendar(year, month, day);
	}
	
	public static Date getEndOfMonth(Date date) {
		Calendar today = Calendar.getInstance();
		today.setTime(date);
		today.add(Calendar.MONTH, 1);
		today.set(Calendar.DATE, 1);
		today.add(Calendar.DATE, -1);		
		return today.getTime();
	}
	
	public static Date getCalendar(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
		Date date = cal.getTime();
		return date;
	}

	/**
	 * Return a list of dates between two provided dates
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<Date> getPeriods(Date startDate, Date endDate) {
		Calendar startCal = Calendar.getInstance(); 
		startCal.setTime(startDate);
		Calendar endCal = Calendar.getInstance(); 
		endCal.setTime(endDate);

		Calendar iterCal = Calendar.getInstance(); 
		iterCal.setTime(startDate);
		List<Date> dates = new ArrayList<Date>();
		while (true) {
			dates.add(iterCal.getTime());
			iterCal.add(Calendar.MONTH, 1);
			if (iterCal.after(endCal))
				break;
		}
		return dates;
	}
	
	public static int getMonthCount(Date startDate, Date endDate) {
		Calendar startCal = Calendar.getInstance(); 
		startCal.setTime(startDate);
		Calendar endCal = Calendar.getInstance(); 
		endCal.setTime(endDate);

		Calendar iterCal = Calendar.getInstance(); 
		iterCal.setTime(startDate);
		int count = 0;
		while (iterCal.before(endCal)) {
			count++;
			iterCal.add(Calendar.MONTH, 1);
		}
		return count;
	}
	
	/**
	 * Compare two dates, 0 if within variance or 1 is c
	 * @param zoho
	 * @param contact
	 * @param variance
	 * @return
	 */
	public static int dateCompare(Date d1, Date d2, long variance) {
		if (null == d2) 
			return -1;
		if (null == d1) 
			return 1;
		long t1 = d1.getTime();
		long t2 = d2.getTime();
		long diff = t1 - t2;
		if (Math.abs(diff) < variance) return 0;
		if (t1 < t2) return -1;
		return 1;
	}
	

	
    public static Map<String, Integer> getSalesPeriodMap() {
    	Map<String, Integer> periodMap = new LinkedHashMap<String, Integer>();
    	periodMap.put("One Month", ONE_MONTH);
    	periodMap.put("Two Months", TWO_MONTHS);
    	periodMap.put("Three Months", THREE_MONTHS);
    	periodMap.put("Six Months", SIX_MONTHS);
    	periodMap.put("One Year", ONE_YEAR);
    	return periodMap;
    }
    
    public static String getCurrentPeriod(Integer period) {
	    for (Entry<String, Integer> entry : getSalesPeriodMap().entrySet()) {
	        if (period.equals(entry.getValue()) == false) continue;
	        return  entry.getKey();
	    }
	    return "";
    }
    
    public static String getDataFile(String filename) {
        String folder = Play.application().configuration().getString("turnbulls.data.folder");
        return getFile(folder, filename);
    }
    
    public static String getReportFile(String filename) {
        String folder = Play.application().configuration().getString("turnbulls.report.folder");
        return getFile(folder, filename);
    }
    
    private static String getFile(String folder, String filename) {
        if (folder.endsWith("/") == false)
        	return folder + "/" + filename;
        return folder + filename;
    }
    

}

