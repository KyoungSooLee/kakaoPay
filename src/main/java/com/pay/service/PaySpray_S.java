/**
 * kakaoPay
 * com.pay.service.Main_S.java
 */
package com.pay.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.common.bean.ExceptionMsg;
import com.kakao.common.bean.ResponseObj;
import com.kakao.common.dao.Common_Dao;
import com.kakao.util.StringUtil;


/**
 * @author	: lks
 * @date	: 
 * @desc	: 
 *
 */
@Service
@Transactional
public class PaySpray_S{
	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	@Autowired
	private Common_Dao cdao;
	
	/**
	 * 뿌리기 API 호출 service
	 * @param  userId : 사용자ID
	 * @param  roomId : 대화방ID
	 * @param  sprayAmt - 뿌릴 금액
	 * @param  cntPeople - 뿌릴 인원
	 * */
	@Transactional
	public ResponseObj createPaySpray(Map<String, Object> param){

		ResponseObj resobj = new ResponseObj();
		
		try {
			
			
			String tokenId = getTokenId();				//토큰정보
			
			param.put("tokenId", tokenId);
			
			//마스터 정보 등록
			cdao.create("paySpray.createPaySpray", param);
			
			int idx = Integer.parseInt(String.valueOf(param.get("idx")));		//마스터 정보 등록시 생성된 idx 컬럼의 sequence 정보 
			
			if(idx < 1){			//등록실패시
				resobj.setErr(ExceptionMsg.ERR_CREATE_DATA);
				throw new Exception();
			}
			
			//상세등록을 위한 분배로직
			List<Map<String, Object>> queryList = new ArrayList<Map<String,Object>>();
			Map<String, Object> detailParam = null; 
			
			Random random = new Random();
			
			int cntPeople = (int) param.get("cntPeople");		//분배인원
			int sprayAmt = (int) param.get("sprayAmt");			//뿌리기금액
			int getAmt = 0;										//줍기금액
			
			for (int i = 1; i <= cntPeople; i++) {
				detailParam = new HashMap<>();
				detailParam.put("idx", idx);
				detailParam.put("roomId", param.get("roomId"));
				detailParam.put("tokenId", tokenId);
				
				if(i == cntPeople){		//마지막 줍기 금액은 남은금액을 전부 이용
					detailParam.put("getAmt", sprayAmt);
				}else {
					getAmt = random.nextInt(sprayAmt + 1);
					sprayAmt -= getAmt;
					
					detailParam.put("getAmt", getAmt);
				}
				
				queryList.add(detailParam);					//상세등록 쿼리 param
			}
			
			//상세 정보 등록 - 인원수별 랜덤 분배 금액 등록
			cdao.create("paySpray.createPaySprayDetail", queryList);
			
			resobj.setSucc(param);
			
		} catch (Exception e) {
			logger.error("Exception", e);
			
			if(resobj.getErrCode() == null || "".equals(resobj.getErrCode())){
				resobj.setErr(ExceptionMsg.ERR_SERVER_ERROR);
			}
		}
		
		return resobj;
	}
	
	/**
	 * 줍기 API 호출 service
	 * @param  userId : 사용자ID
	 * @param  roomId : 대화방ID
	 * @param  idx : 뿌리기 생성 key
	 * @param  tokenId : 토큰정보
	 * */
	@Transactional
	public ResponseObj getAmt(Map<String, Object> param){

		ResponseObj resobj = new ResponseObj();
		
		try {
			
			//받기 가능한건이 존재하는지 조회
			Map<String, Object> mapCheckGetAmt = cdao.selectOne("paySpray.selectCheckGetAmt", param);
			
			if(mapCheckGetAmt != null && !mapCheckGetAmt.isEmpty()){		//받기 가능한 정보가 존재하는경우 해당 정보를 응답값에 설정

				if ("N".equals(String.valueOf(mapCheckGetAmt.get("timeFlag")))){
					
					//뿌리기 한건은 10분간만 유효함
					resobj.setErrStr(ExceptionMsg.ERR_CUSTOM_MSG, "마감되었습니다. 다음 기회에!");
					throw new Exception();
					
				}else if(StringUtil.NVL(String.valueOf(param.get("userId"))).equals(StringUtil.NVL(String.valueOf(mapCheckGetAmt.get("createUserId"))))){
					
					//자신이 뿌리기한 건은 받을수 없음
					resobj.setErrStr(ExceptionMsg.ERR_CUSTOM_MSG, "자신이 뿌리기한 건은 자신이 받을 수 없습니다.");
					throw new Exception();
				}
				
				param.put("seq", mapCheckGetAmt.get("seq"));		//받기 가능한 seq key setting
				param.put("idx", mapCheckGetAmt.get("idx"));		//받기 가능한 idx key setting
				
				//마스터 정보 수정
				int cnt = cdao.update("paySpray.updateGetAmt", param);
				
				if(cnt < 1){			//수정실패시
					resobj.setErr(ExceptionMsg.ERR_UPDATE_DATA);
					throw new Exception();
				}
				
				Map<String, Object> resMap = new HashMap<>();
				resMap.putAll(mapCheckGetAmt);
				resMap.put("userId", param.get("userId"));
				
				resobj.setSucc(resMap);
				
			}else {															//받기 기능기 불가능한 경우
				//이미 받기 기능을 실행한 유저인지 확인
				Map<String, Object> mapCheckGetAmtDup = cdao.selectOne("paySpray.selectCheckGetAmtDup", param);
				if(mapCheckGetAmtDup != null && !mapCheckGetAmtDup.isEmpty()){
					resobj.setErrStr(ExceptionMsg.ERR_CUSTOM_MSG, "한번만 받기가 가능합니다.");
					throw new Exception();
				}
				
				resobj.setErrStr(ExceptionMsg.ERR_CUSTOM_MSG, "받기 가능한 정보가 없습니다.");
				throw new Exception();
			}			
			
			
		} catch (Exception e) {
			logger.error("Exception", e);
			
			if(resobj.getErrCode() == null || "".equals(resobj.getErrCode())){
				resobj.setErr(ExceptionMsg.ERR_SERVER_ERROR);
			}
		}
		
		return resobj;
	}
	

	/**
	 * 조회 API 호출 service
	 * 
	 * @param  userId : 사용자ID
	 * @param  roomId : 대화방ID
	 * @param  tokenId : 토큰정보
	 * */
	public ResponseObj selectPaySprayInfo(Map<String, Object> param){

		ResponseObj resobj = new ResponseObj();
		
		try {
			
			//받기 가능한건이 존재하는지 조회
			Map<String, Object> paySprayInfo = cdao.selectOne("paySpray.selectPaySprayInfo", param);
			
			if(paySprayInfo != null && !paySprayInfo.isEmpty()){		//뿌리기 정보가 조회하는경우 응답값 설정
				
				List<Object> endGetAmtInfo = cdao.selectList("selectPaySprayDetailInfo", paySprayInfo);
				
				paySprayInfo.put("endGetAmt", endGetAmtInfo);
				
				resobj.setSucc(paySprayInfo);
			}else {
				resobj.setErr(ExceptionMsg.NO_SEARCH_DATA);				//조회결과 없음
			}			
			
		} catch (Exception e) {
			logger.error("Exception", e);
			
			if(resobj.getErrCode() == null || "".equals(resobj.getErrCode())){
				resobj.setErr(ExceptionMsg.ERR_SERVER_ERROR);
			}
		}
		
		return resobj;
	}
	
	/**
	 * 고유 koken 정보 생성
	 * */
	private String getTokenId(){
		String tokenId = "";
		for (int i = 1; i <= 3; i++) {
			tokenId += String.valueOf((char)((Math.random() * 26) + 65));
	    }
		
		return tokenId;
	}

}
