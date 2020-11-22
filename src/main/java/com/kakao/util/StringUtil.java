package com.kakao.util;

public class StringUtil {

	/**
	 * 입력한 값이 null 또는 null String 일 경우 true를 return 한다.
	 * 
	 * isEmpty("")		===> true
	 * isEmpty(null)	===> true
	 * isEmpty("1")		===> false
	 * 
	 * @param value
	 * @return boolean	 
	 */
	public static boolean isEmpty(String value) {
		if (value == null || "".equals(value.trim()))
			return true;
		return false;     
	}
	
	/**
	 * 입력한 값이 null일 경우 null String을 return 한다.
	 * 
	 * NVL(null)		===> ""
	 * NVL("abc  ")		===> "abc"	 
	 * 
	 * @param value
	 * @return String	 
	 */
	public static String NVL(String value) {
		return (value == null ? "" : value.trim());
	}
	
}
