package com.kakao.common.bean;

import java.util.HashMap;
import java.util.Map;

public class ExceptionMsg {
	
	public static final String NO_SEARCH_DATA 	= "E000";
	public static final String ERR_UPDATE_DATA 	= "E001";
	public static final String ERR_CREATE_DATA 	= "E002";
	public static final String ERR_DELETE_DATA  = "E003";

	public static final String ERR_CHECK_PARAM 	= "E901";
	public static final String ERR_SERVER_ERROR = "E999";

	public static final String ERR_CUSTOM_MSG 	= "E800";
	
	private static final Map<String, String[]> messages;
	static {
		messages = new HashMap<String, String[]>();

		
		messages.put( NO_SEARCH_DATA, new String[] {"조회결과가 없습니다.", "정상"}); //E000
		messages.put( ERR_UPDATE_DATA , new String[] {"수정중 오류가 발생하였습니다.", "데이터처리오류"}); //E001
		messages.put( ERR_CREATE_DATA , new String[] {"등록중 오류가 발생하였습니다.", "데이터처리오류"}); //E002
		messages.put( ERR_DELETE_DATA , new String[] {"삭제중 오류가 발생하였습니다.", "데이터처리오류"}); //E003

		messages.put( ERR_CUSTOM_MSG , new String[] {"", ""}); //E800
		
		messages.put( ERR_CHECK_PARAM , new String[] {"필수 입력값이 누락되었습니다.", "입력값오류"}); 	//E901
		messages.put( ERR_SERVER_ERROR , new String[] {"서버처리 중 오류가 발생하였습니다.", "서버오류"}); //E999
		
	}
	
	/**
	 * 사용자 출력용 메시지
	 * @param code
	 * @return
	 */
	public static String getUserMessage(String code) {
		return messages.get(code)[0];
	}
	
	/**
	 * 코드에 대한 문장
	 * @param code
	 * @return
	 */
	public static String getCodeText(String code) {
		return messages.get(code)[1];
	}
	
	/**
	 * 코드를 문자열로 변환한다.
	 * @param code
	 * @return '(code:codeText) message' 형식
	 */
	public static String toString(String code) {
		return String.format("(%d:%s) %s", code, getCodeText(code), getUserMessage(code));
	}

	public static boolean chkErr(String code){
		String[] msg = messages.get(code);
		if(msg!=null){
			return true;
		}
		return false;
	}
}
