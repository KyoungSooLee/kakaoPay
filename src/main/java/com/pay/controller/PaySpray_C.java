package com.pay.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kakao.common.bean.ExceptionMsg;
import com.kakao.common.bean.ResponseObj;
import com.kakao.util.StringUtil;
import com.pay.service.PaySpray_S;

@Controller
public class PaySpray_C {
	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	@Autowired
	private PaySpray_S paySpraySvc;
	
	/***
	 * 뿌리기 API 요청
	 * @header  X-USER-ID : 사용자ID
	 * @header  X-ROOM-ID : 대화방ID
	 * @param  sprayAmt : 뿌릴 금액
	 * @param  cntPeople : 뿌릴 인원
	 * */
	@RequestMapping(value="/createPaySpray.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObj createPaySpray(HttpServletRequest req, @RequestHeader Map<String, Object> header, @RequestBody Map<String, Object> param) {
		ResponseObj resobj = new ResponseObj();

		try {
			
			//필수정보 체크
			if(StringUtil.isEmpty(String.valueOf(header.get("x-user-id")))
					|| StringUtil.isEmpty(String.valueOf(header.get("x-room-id")))
					|| (param.get("sprayAmt") == null || StringUtil.isEmpty(String.valueOf(param.get("sprayAmt"))))
					|| (param.get("cntPeople") == null || StringUtil.isEmpty(String.valueOf(param.get("cntPeople"))))
					){
				
				resobj.setErr(ExceptionMsg.ERR_CHECK_PARAM);
				throw new Exception();
			}
			
			param.put("userId", header.get("x-user-id"));			//사용자ID
			param.put("roomId", header.get("x-room-id"));			//대화방ID
			
			resobj =  paySpraySvc.createPaySpray(param);			//뿌리기 service 요청 
			
		}catch(Exception e){
			logger.error("Exception", e);
			
			if(resobj.getErrCode() == null || "".equals(resobj.getErrCode())){
				resobj.setErr(ExceptionMsg.ERR_SERVER_ERROR);
			}
		}
		return resobj;
	}
	
	/***
	 * 받기 API 요청
	 * @header  X-USER-ID : 사용자ID
	 * @header  X-ROOM-ID : 대화방ID
	 * @param  idx : 뿌리기 생성 key
	 * @param  tokenId : 토큰정보
	 * */
	@RequestMapping(value="/getAmt.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObj getAmt(HttpServletRequest req, @RequestHeader Map<String, Object> header, @RequestBody Map<String, Object> param) {
		ResponseObj resobj = new ResponseObj();

		try {
			
			//필수정보 체크
			if(StringUtil.isEmpty(String.valueOf(header.get("x-user-id")))
					|| StringUtil.isEmpty(String.valueOf(header.get("x-room-id")))
					|| (param.get("tokenId") == null ||StringUtil.isEmpty(String.valueOf(param.get("tokenId"))))
					|| (param.get("idx") == null ||StringUtil.isEmpty(String.valueOf(param.get("idx"))))
					){
				
				resobj.setErr(ExceptionMsg.ERR_CHECK_PARAM);
				throw new Exception();
			}
			
			param.put("userId", header.get("x-user-id"));
			param.put("roomId", header.get("x-room-id"));

			resobj =  paySpraySvc.getAmt(param);
			
			
		}catch(Exception e){
			logger.error("Exception", e);
			
			if(resobj.getErrCode() == null || "".equals(resobj.getErrCode())){
				resobj.setErr(ExceptionMsg.ERR_SERVER_ERROR);
			}
		}
		return resobj;
	}
	
	/**
	 * 조회 API
	 * 
	 * @header  X-USER-ID : 사용자ID
	 * @header  X-ROOM-ID : 대화방ID
	 * @param  tokenId : 토큰정보
	 * **/
	@RequestMapping(value="/getPaySprayInfo.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObj getPaySprayInfo(HttpServletRequest req, @RequestHeader Map<String, Object> header, @RequestBody Map<String, Object> param) {
		ResponseObj resobj = new ResponseObj();

		try {
			
			//필수정보 체크
			if(StringUtil.isEmpty((String) header.get("x-user-id"))
					|| StringUtil.isEmpty((String) header.get("x-room-id"))
					|| (param.get("tokenId") == null || StringUtil.isEmpty((String) param.get("tokenId")))
					){
				
				resobj.setErr(ExceptionMsg.ERR_CHECK_PARAM);
				throw new Exception();
			}
			
			param.put("userId", header.get("x-user-id"));
			param.put("roomId", header.get("x-room-id"));

			resobj =  paySpraySvc.selectPaySprayInfo(param);
			
		}catch(Exception e){
			logger.error("Exception", e);
			
			if(resobj.getErrCode() == null || "".equals(resobj.getErrCode())){
				resobj.setErr(ExceptionMsg.ERR_SERVER_ERROR);
			}
		}
		return resobj;
	}

}
