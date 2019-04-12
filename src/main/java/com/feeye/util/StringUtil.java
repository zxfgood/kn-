package com.feeye.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: StringUtil
 * @Description: TODO(工具类)
 * @author huangdongbin
 * @date 2014-4-9 下午04:00:26
 */
public class StringUtil {

	/**
	 * @Title: toString
	 * @Description:TODO(把数组转换成字符串 )
	 * @param arrays
	 * @return
	 * @author huangdongbin
	 * @return String 返回类型
	 * @throws
	 */
	public static String toString(String[] arrays) {
		if (arrays == null || arrays.length == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arrays.length; i++) {
			sb.append(arrays[i]);
			if (i < arrays.length - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	/**
	 * @Title: replaceNull
	 * @Description:TODO(转换 null字符)
	 * @param value
	 * @return
	 * @author huangdongbin
	 * @return String 返回类型
	 * @throws
	 */
	public static String replaceNull(String value) {
		if (value == null || "null".equals(value)) {
			return "";
		}
		return value;
	}

	/**
	 * @Title: isContain
	 * @Description:TODO(包含)
	 * @param array
	 * @param text
	 * @return
	 * @author huangdongbin
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean isContain(String[] array, String text) {
		if (array == null || array.length == 0) {
			return false;
		}
		for (String policy : array) {
			if (policy.equalsIgnoreCase(text)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断字符是否为空
	 */
	public static boolean isEmpty(String str) {
		return str == null || "".equals(str.trim());
	}
	/**
	 * 判断字符串 是否不空
	 * @param str
	 * @return
	 */

	public static boolean isNotEmpty(String str) {
		return str != null && !"".equals(str.trim());
	}
	
	/**
	 * 判断字符串是否相等（不含 大小写）
	 * @param str1
	 * @param str2
	 * @return
	 */

	public static boolean equalsIgnoreCase(String str1, String str2) {
		return ((str1 == null) ? false : (str2 == null) ? true : str1
				.equalsIgnoreCase(str2));
	}
	
	/**
	 * 判断字符串是否相等
	 * @param str1
	 * @param str2
	 * @return
	 */

	public static boolean equals(String str1, String str2) {
		return ((str1 == null) ? false : (str2 == null) ? true : str1
				.equals(str2));
	}
	
	/**
	 * @Title: splitFlightLine
	 * @Description:TODO(拆分航线)
	 * @param list
	 * @return 
	 * @author 
	 * @return List<String> 返回类型
	 * @throws
	 */
	 public  static List<String> splitFlightLine(String flightline){
			List<String> reList=new ArrayList<String>();
				 if(flightline!=null&&flightline.indexOf("/")!=-1){
					 String[] liness= flightline.split("-");
					 String[] lines=null;
					 if(liness.length==1){
						 lines=new String[2];
						 if(flightline.endsWith("-")){
							 lines[0]=liness[0];
							 lines[1]="";
						 }else{
							 lines[0]="";
							 lines[1]=liness[0];
						 }
					 }else{
						 lines=liness;
					 }
					 String[] lines1=lines[0].indexOf("/")!=-1?lines[0].split("/"):new String[]{lines[0]};
					 String[] lines2=lines[1].indexOf("/")!=-1?lines[1].split("/"):new String[]{lines[1]};
					 for(String ln1:lines1){
						 for(String ln2:lines2){
							 	if(ln1.equalsIgnoreCase(ln2)){
							 		continue;
							 	}
								reList.add(ln1+"-"+ln2);
						 }
					 }
				 }else{
					 reList.add(flightline);
				 }
			return reList;
		}

	/**
	 * 验证当前电话号码是否是手机号码
	 * 
	 * @param str
	 *            电话号码
	 * @return 是返回true;否返回false
	 */
	public static boolean isMobileNum(String str) {
		String regex = "^(13[0-9]{9}|15[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8})$";
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile(regex); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}
	/**
	 * @Description:    判断是否为邮箱
	 * @Author:         zxf
	 * @Date:     2019/3/26 12:02
	 * @Param [string]
	 * @return boolean，true为邮箱，false代表不为邮箱
	 */
	public static boolean isEmail(String string) {
		if (string == null)
			return false;
		String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern p;
		Matcher m;
		p = Pattern.compile(regEx1);
		m = p.matcher(string);
		if (m.matches())
			return true;
		else
			return false;
	}
}
