package com.feeye.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class DateUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DateUtil.class);

	/**
	 * 判断当前时间是否已经超时，默认超时5分钟
	 * 超时返回true
	 * @param startDateTime 
	 * @param Runtime
	 * @return
	 */
	public static boolean IsRunningTimeOut(long startDateTime,long Runtime){
		try {
			if(Runtime<=0){
				Runtime = 5*60*1000;
			}
			Date nowDate = new Date();
			long lastDate=nowDate.getTime()-startDateTime;
			if(lastDate>Runtime){
				return true;
			}
		} catch (Exception e) {
			logger.error("error",e);
		}
		return false;
	}
	/**
	 * 判断当前时间是否已经超时5分钟
	 * 超时返回true
	 * @param startDateTime
	 * @return
	 */
	public static boolean IsRunningTimeOut(long startDateTime){
		try{
			Date nowDate = new Date();
			long lastDate=nowDate.getTime()-startDateTime;
			if(lastDate>5*60*1000){
				return true;
			}
		} catch (Exception e) {
			logger.error("error",e);
		}
		return false;
	}
    /**
     * <使用date的日期部分生成Calendar> <功能详细描述>
     * @param date
     * @return [参数说明]
     * @return Calendar [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     */
	public static Date parseDate(String datestr){
		Date date=null;
		try {
			date=new SimpleDateFormat("yyyy-MM-dd").parse(datestr);
		} catch (Exception e) {
			logger.error("parseDate(String)", e); //$NON-NLS-1$
			logger.error("error parseDate str:"+datestr);
		}
		return date;
	}
	
	public static Date parseTime(String datestr){
		Date date=null;
		try {
			date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(datestr);
		} catch (Exception e) {
			logger.error("parseTime(String)", e); //$NON-NLS-1$
			logger.error("error parseTime str:"+datestr);
		}
		return date;
	}
	public static String formatDate(Date date){
		String datestr=null;
		try {
			datestr=new SimpleDateFormat("yyyy-MM-dd").format(date);
		} catch (Exception e) {
			logger.error("formatDate(Date)", e); //$NON-NLS-1$
			logger.error("error formatDate date:"+date);
		}
		return datestr;
	}
	public static String formatTime(Date date){
		String datestr=null;
		try {
			datestr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		} catch (Exception e) {
			logger.error("formatTime(Date)", e); //$NON-NLS-1$
			logger.error("error formatTime date:"+date);
		}
		return datestr;
	}
	
    public static Calendar copyDatePart(Date date)
    {
        Calendar temp = Calendar.getInstance();
        temp.setTime(date);
        Calendar result = Calendar.getInstance();
        result.clear();
        result.set(temp.get(Calendar.YEAR), temp.get(Calendar.MONTH), temp
                .get(Calendar.DAY_OF_MONTH));
        result.getTime();
        return result;
    }

    public static Calendar copyDatePart(Calendar date)
    {
        Calendar result = Calendar.getInstance();
        result.clear();
        result.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date
                .get(Calendar.DAY_OF_MONTH));
        result.getTime();
        return result;
    }

    /**
     * <使用date的日期部分和小时及分钟生成Calendar> <功能详细描述>
     * @param date
     * @return [参数说明]
     * @return Calendar [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     */
    public static Calendar copyYMdHm(Date date)
    {
        Calendar temp = Calendar.getInstance();
        temp.setTime(date);
        Calendar result = Calendar.getInstance();
        result.clear();
        result.set(temp.get(Calendar.YEAR), temp.get(Calendar.MONTH), temp
                .get(Calendar.DAY_OF_MONTH), temp.get(Calendar.HOUR_OF_DAY),
                temp.get(Calendar.MINUTE));
        result.getTime();
        return result;
    }

    /**
     * <使用Date生成Calendar> <包含日期部分与时间部分>
     * @param date
     * @return [参数说明]
     * @return Calendar [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     */
    public static Calendar copyDateTime(Date date)
    {
        Calendar result = Calendar.getInstance();
        result.clear();
        result.setTime(date);
        return result;
    }

    /**
     * <通过字符串创建一个日期> <使用yyyy-MM-dd HH:mm格式字符串创建一个日期>
     * @param strDate 格式必须为yyyy-MM-dd HH:mm
     * @return [参数说明]
     * @return Date [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     */
    public static Date createDate(String strDate)
    {
        return createDate(strDate, "yyyy-MM-dd HH:mm");
    }
    
    
    public static Date createStartPointDate(String strDate)
    {
        if(strDate==null || strDate.length()==0)
        {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try
        {
            date = sdf.parse(strDate);
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            return null;
        }
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
    
    public static Date createEndPointDate(String strDate)
    {
        if(strDate==null || strDate.length()==0)
        {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try
        {
            date = sdf.parse(strDate);
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            return null;
        }
        Calendar c=sdf.getCalendar();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    public static Date createStartPointDate(Date date)
    {
        if(date==null)
        {
            return null;
        }      
        Calendar result = Calendar.getInstance();
        result.clear();
        result.setTime(date);
        result.set(Calendar.HOUR_OF_DAY, 0);
        result.set(Calendar.MINUTE, 0);
        result.set(Calendar.SECOND, 0);
        result.set(Calendar.MILLISECOND, 0);
        return result.getTime();
    }
    public static Date createEndPointDate(Date date)
    {
        if(date==null)
        {
            return null;
        }      
        Calendar result = Calendar.getInstance();
        result.clear();
        result.setTime(date);
        result.set(Calendar.HOUR_OF_DAY, 23);
        result.set(Calendar.MINUTE, 59);
        result.set(Calendar.SECOND, 59);
        result.set(Calendar.MILLISECOND, 999);
        return result.getTime();
    }

    /**
     * <通过字符串创建一个日期> <使用format格式字符串创建一个日期>
     * @param strDate 必须符合format格式
     * @param format 日期格式
     * @return [参数说明]
     * @return Date [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     */
    public static Date createDate(String strDate, String format)
    {
        Date result = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format,new Locale("US"));
        try
        {
            result = sdf.parse(strDate);
        }
        catch (ParseException e)
        {
			logger.error("createDate(String, String)", e); //$NON-NLS-1$
        }
        return result;
    }

    /**
     * <比较两个日期的日期部分> < srcDate大于destDate时 返回值大于0，相等时等于0，否则小于0>
     * @param srcDate
     * @param destDate
     * @return [参数说明]
     * @exception throws [违例类型] [违例说明]
     */
    public static int compareDatePart(Date srcDate, Date destDate)
    {
        Calendar cdSrc = copyDatePart(srcDate);
        Calendar cdDest = copyDatePart(destDate);
        Date src = cdSrc.getTime();
        Date dest = cdDest.getTime();
        int result = src.compareTo(dest);
        return result;
    }

    /** <计算两个日期之间的相隔天数>
     * <功能详细描述>
     * @param d1
     * @param d2
     * @return [参数说明]
     * @return int [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     */
    public static int getDaysBetween(Date d1, Date d2)
    {
        Calendar cd1=Calendar.getInstance();
        Calendar cd2=(Calendar) cd1.clone();
        cd1.setTime(d1);
        cd2.setTime(d2);
        
        if (cd1.after(cd2))
        { // swap dates so that d1 is start and d2 is end
            Calendar swap = cd1;
            cd1 = cd2;
            cd2 = swap;
        }
        int days = cd2.get(Calendar.DAY_OF_YEAR)
                - cd1.get(Calendar.DAY_OF_YEAR);
        int y2 = cd2.get(Calendar.YEAR);
        if (cd1.get(Calendar.YEAR) != y2)
        {
            cd1 = (Calendar) cd1.clone();
            do
            {
                days += cd1.getActualMaximum(Calendar.DAY_OF_YEAR);
                cd1.add(Calendar.YEAR, 1);
            }
            while (cd1.get(Calendar.YEAR) != y2);
        }
        return days;
    }
    /**  
     * 获得当前时间，格式yyyy-MM-dd hh:mm:ss  
     * @param format  
     * @return  
     */  
    public static String getCurrentDate(){   
        return getCurrentDate("yyyy-MM-dd hh:mm:ss");   
    }   
    /**  
     * 获得当前时间，格式自定义  
     * @param format  
     * @return  
     */  
    public static String getCurrentDate(String format){   
        Calendar day=Calendar.getInstance();    
        day.add(Calendar.DATE,0);    
        SimpleDateFormat sdf=new SimpleDateFormat(format);//"yyyy-MM-dd"   
        String date = sdf.format(day.getTime());   
        return date;   
    }   
    /**  
     * 获得昨天时间，格式自定义  
     * @param format  
     * @return  
     */  
    public static String getYesterdayDate(String format){   
        Calendar day=Calendar.getInstance();    
        day.add(Calendar.DATE,-1);    
        SimpleDateFormat sdf=new SimpleDateFormat(format);//"yyyy-MM-dd"   
        String date = sdf.format(day.getTime());   
        return date;   
    }   
     /**    
      * @param date1 需要比较的时间 不能为空(null),需要正确的日期格式 ,如：2009-09-12   
      * @param date2 被比较的时间  为空(null)则为当前时间    
      * @param stype 返回值类型   0为多少天，1为多少个月，2为多少年    
      * @return    
      * 举例：  
      * compareDate("2009-09-12", null, 0);//比较天  
      * compareDate("2009-09-12", null, 1);//比较月  
      * compareDate("2009-09-12", null, 2);//比较年  
      */    
     public static int compareDate(String startDay,String endDay,int stype){     
         int n = 0;     
         String[] u = {"天","月","年"};     
         String formatStyle = stype==1?"yyyy-MM":"yyyy-MM-dd";     
              
         endDay = endDay==null?getCurrentDate("yyyy-MM-dd"):endDay;     
              
         DateFormat df = new SimpleDateFormat(formatStyle);     
         Calendar c1 = Calendar.getInstance();     
         Calendar c2 = Calendar.getInstance();     
         try {     
             c1.setTime(df.parse(startDay));     
             c2.setTime(df.parse(endDay));   
         } catch (Exception e3) {     

			if (logger.isDebugEnabled()) {
				logger.debug("compareDate(String, String, int) - wrong occured"); //$NON-NLS-1$
			}     
         }     
         //List list = new ArrayList();     
         while (!c1.after(c2)) {                   // 循环对比，直到相等，n 就是所要的结果     
             //list.add(df.format(cgetTime()));    // 这里可以把间隔的日期存到数组中 打印出来     
             n++;     
             if(stype==1){     
                 c1.add(Calendar.MONTH, 1);          // 比较月份，月份+1     
             }     
             else{     
                 c1.add(Calendar.DATE, 1);           // 比较天数，日期+1     
             }     
         }     
         n = n-1;     
         if(stype==2){     
             n = (int)n/365;     
         }        
//         System.out.println(startDay+" -- "+endDay+" 相差多少"+u[stype]+":"+n);           
         return n;     
     }   
     /**  
      * 判断时间是否符合时间格式  
      */  
    public static boolean isDate(String date, String dateFormat) {   
        if (date != null) {   
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            format.setLenient(false);   
            try {   
                format.format(format.parse(date));   
            } catch (ParseException e) {   
                // TODO Auto-generated catch block   
                return false;   
            }   
            return true;   
        }   
        return false;   
    }   
     /**  
      * 实现给定某日期，判断是星期几  
      * date:必须yyyy-MM-dd格式  
      */  
     public static String getWeekday(String date){   
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");     
     SimpleDateFormat sdw = new SimpleDateFormat("E");     
     Date d = null;     
     try {     
         d = sd.parse(date);     
     } catch (ParseException e) {     

			logger.error("getWeekday(String)", e); //$NON-NLS-1$     
     }   
     return sdw.format(d);   
     }   
     /**  
      * 用来全局控制 上一周，本周，下一周的周数变化  
      */  
    private static int weeks = 0;   
    /**  
     * 获得当前日期与本周一相差的天数  
     */  
     private static int getMondayPlus() {   
         Calendar cd = Calendar.getInstance();   
         // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......   
         int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);   
         if (dayOfWeek == 1) {   
             return -6;   
         } else {   
             return 2 - dayOfWeek;   
         }   
     }   
     /**  
      * 获得本周星期一的日期  
      */  
     public static String getCurrentMonday(String format) {   
         weeks = 0;   
         int mondayPlus = getMondayPlus();   
         Calendar currentDate=Calendar.getInstance();   
         currentDate.add(Calendar.DATE, mondayPlus);   
         SimpleDateFormat sdf=new SimpleDateFormat(format);//"yyyy-MM-dd"   
         String date = sdf.format(currentDate.getTime());   
         return date;   
     }   
     /**  
      * 获得上周星期一的日期  
      */  
     public static String getPreviousMonday(String format) {   
         weeks--;   
         int mondayPlus = getMondayPlus();   
         Calendar currentDate=Calendar.getInstance();   
         currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);   
         SimpleDateFormat sdf=new SimpleDateFormat(format);//"yyyy-MM-dd"   
         String date = sdf.format(currentDate.getTime());   
         return date;   
     }   
     /**  
      * 获得下周星期一的日期  
      */  
     public static String getNextMonday(String format) {   
         weeks++;   
         int mondayPlus = getMondayPlus();   
    //     GregorianCalendar currentDate = new GregorianCalendar();   
         Calendar currentDate=Calendar.getInstance();   
         currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);   
         SimpleDateFormat sdf=new SimpleDateFormat(format);//"yyyy-MM-dd"   
         String date = sdf.format(currentDate.getTime());   
         return date;   
     }   
     /**  
      * 获得相应周的周日的日期  
      * 此方法必须写在getCurrentMonday，getPreviousMonday或getNextMonday方法之后  
      */  
     public static String getSunday(String format) {   
         int mondayPlus = getMondayPlus();   
         Calendar currentDate=Calendar.getInstance();   
         currentDate.add(Calendar.DATE, mondayPlus + 7 * weeks + 6);   
         SimpleDateFormat sdf=new SimpleDateFormat(format);//"yyyy-MM-dd"   
         String date = sdf.format(currentDate.getTime());   
         return date;   
     }   
  
  
/**  
      *method 将字符串类型的日期转换为一个timestamp（时间戳记java.sql.Timestamp）  
      *@param dateString 需要转换为timestamp的字符串  
      *@return dataTime timestamp  
      */  
    public final static java.sql.Timestamp string2Time(String dateString) {   
        DateFormat dateFormat;   
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);// 设定格式   
        dateFormat.setLenient(false);   
        Date date = null;
        try {   
            date = dateFormat.parse(dateString);   
        } catch (ParseException e) {   
            // TODO Auto-generated catch block   
			logger.error("string2Time(String)", e); //$NON-NLS-1$   
        }   
//      java.sql.Timestamp dateTime = new java.sql.Timestamp(date.getTime());   
        return new java.sql.Timestamp(date.getTime());// Timestamp类型,timeDate.getTime()返回一个long型   
    }   
    
    public final static java.sql.Date string2Date(String dateString,String pattern) {   
        DateFormat dateFormat;   
        dateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);   
        dateFormat.setLenient(false);   
        Date date = null;
        try {   
            date = dateFormat.parse(dateString);   
        } catch (ParseException e) {   
            // TODO Auto-generated catch block   
			logger.error("string2Date(String, String)", e); //$NON-NLS-1$   
        }   
//      java.sql.Date dateTime = new java.sql.Date(date.getTime());// sql类型   
        return new java.sql.Date(date.getTime());   
    }  
  
    /**  
     *method 将字符串类型的日期转换为一个Date（java.sql.Date）  
     *   
     * @param dateString  
     *            需要转换为Date的字符串  
     *@return dataTime Date  
     */  
    public final static java.sql.Date string2Date(String dateString) {   
        DateFormat dateFormat;   
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);   
        dateFormat.setLenient(false);   
        Date date = null;
        try {   
            date = dateFormat.parse(dateString);   
        } catch (ParseException e) {   
            // TODO Auto-generated catch block   
			logger.error("string2Date(String)", e); //$NON-NLS-1$   
        }   
//      java.sql.Date dateTime = new java.sql.Date(date.getTime());// sql类型   
        return new java.sql.Date(date.getTime());   
    }   
  
    public final static String date2String(Date date)
    {
    	DateFormat dateFormat;   
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);   
        return dateFormat.format(date);
    }
    public final static String date2String(Date date,String pattern)
    {
    	DateFormat dateFormat;   
        dateFormat = new SimpleDateFormat(pattern,  Locale.US);   
        return dateFormat.format(date);
    }
    
    public String addMonths(String basicDate, int n) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = null;
        try {
            date = simpleDateFormat.parse(basicDate);
        } catch (ParseException ex) {
           
        }
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.MONTH, 1);
        simpleDateFormat.format(calender.getTime());
        return simpleDateFormat.format(calender.getTime()).toString();
    }

    public static int getWeek(String date) {
        Date TheDate = StringToSqlDate(date);
        Calendar c = Calendar.getInstance();
        c.setTime(TheDate);
        int result = c.get(Calendar.DAY_OF_WEEK);
        return result;
    }

    public static Date StringToSqlDate(String strDate) {
        java.sql.Date thedate = null;
        if (!strDate.equals("")) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date cDate = null;
            try {
                cDate = df.parse(strDate);
            } catch (ParseException ex) {
            }
            thedate = new java.sql.Date(cDate.getTime());
        }
        return thedate;
    }

    public static int getMonthNum(String date) {
        Date TheDate = StringToSqlDate(date);
        Calendar c = Calendar.getInstance();
        c.setTime(TheDate);
        int result = c.getActualMaximum(Calendar.DATE);
        return result;
    }

    public static  boolean CheckNumber(String value) {
        boolean result = false;
        if (value.matches("[0-9]*")) {
            result = true;
        }
        return result;
    }
    public static  boolean isDate (String pInput) {
        if(pInput == null){
            return false;
        }
        String regEx = "^((\\d{2}(([02468][048])|([13579][26]))-((((0?[1357"
                + "8])|(1[02]))-((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])"
                + "|(11))-((0?[1-9])|([1-2][0-9])|(30)))|(0?2-((0?[1-9])|([1-"
                + "2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]"
                + "))[\\-\\/\\s]?((((0?[13578])|(1[02]))-((0?[1-9])|([1-2][0-"
                + "9])|(3[01])))|(((0?[469])|(11))-((0?[1-9])|([1-2][0-9])|(3"
                + "0)))|(0?2-((0?[1-9])|(1[0-9])|(2[0-8]))))))$";
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(pInput);
        return matcher.matches();
    }
    
    public static int nDaysBetweenTwoDate(String firstString, String secondString) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date firstDate = null;
        Date secondDate = null;
        try {
            firstDate = df.parse(firstString);
            secondDate = df.parse(secondString);
        } catch (Exception e) {
        }

        int nDay = (int) ((secondDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000));
        return nDay;
    }

    public static int nSecondsBetweenTwoDate(String firstString, String secondString) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date firstDate = null;
        Date secondDate = null;
        try {
            firstDate = df.parse(firstString);
            secondDate = df.parse(secondString);
        } catch (Exception e) {
        }

        int nSecond = (int) ((secondDate.getTime() - firstDate.getTime()));
        return nSecond;
    }
    public static String nDaysAfterOneDateString(String basicDate, int n) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date tmpDate = null;
        try {
            tmpDate = df.parse(basicDate);
        } catch (Exception e) {
        }
        long nDay = (tmpDate.getTime() / (24 * 60 * 60 * 1000) + 1 + n) * (24 * 60 * 60 * 1000);
        tmpDate.setTime(nDay);
        return df.format(tmpDate);
    }

    public  static String GetDateTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String mDateTime = formatter.format(cal.getTime());
        return (mDateTime);
    }
    
    /*
     * 获取N天后的日期
     */
    public static Date getDateAfter(Date d, int day) {   
        Calendar now = Calendar.getInstance();   
        now.setTime(d);   
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);   
        return now.getTime();   
    }
    
    public static Date getCurrentDateTime(){
    	Calendar temp = Calendar.getInstance();
    	return temp.getTime();
    }
    
    /**
     * 获取当前日期是星期几<br>
     * 
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"周日","周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }
    
    /**
     * 对比两日期相差多少秒
     */
    public static long compareTwoDateTime(String d1,String d2) {
       
    	SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date1 = sdf.parse(d1);
			Date date2 = sdf.parse(d2);
			
			return (date2.getTime()-date1.getTime())/1000;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        return 0;
    }
    
    
    /*
     * 获取N天前的日期
     */
    public static Date getDateBefore(Date d, int day) {   
    	Calendar now = Calendar.getInstance();   
    	now.setTime(d);   
    	now.set(Calendar.DATE, now.get(Calendar.DATE) - day);   
    	return now.getTime();   
    }
    
   public static void main(String[] args) {
	   
	   
	   System.out.println(getWeekday("2015-09-21"));
	   System.out.println(getWeekOfDate(new Date()));
	   Calendar cal = Calendar.getInstance();
       cal.setTime(DateUtil.createDate("2015-09-20","yyyy-MM-dd"));
       int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
       System.out.println(w);
}
}
