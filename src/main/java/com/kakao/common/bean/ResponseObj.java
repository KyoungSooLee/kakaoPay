/**
 * PRJ_TEMPLATE
 * com.kakao.common.bean.ResponseObj.java
 */
package com.kakao.common.bean;

/**
 * @author	: lks
 * @date	: 
 * @desc	: 
 *
 */
public class ResponseObj {

	private String rslt;
	private String errCode;
	private String msg;
	private String msg2;
	private Object obj;

	public String getRslt() {
		return rslt;
	}
	public void setRslt(String rslt) {
		this.rslt = rslt;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	
	public void setErr() {
		this.rslt = AppConstant.ERR;
		this.setErr(ExceptionMsg.ERR_SERVER_ERROR);
	}
	
	public void setErr(String errCd) {
		this.rslt = AppConstant.ERR;
		this.errCode = errCd;
		this.msg = ExceptionMsg.getUserMessage(errCd);
		this.msg2 = ExceptionMsg.getCodeText(errCd);
		this.obj = null;
	}
	
	public void setErrStr(String errCd, String msg) {
		this.rslt = AppConstant.ERR;
		this.errCode = errCd;
		this.msg = ExceptionMsg.getUserMessage(errCd) + msg;
		this.msg2 = ExceptionMsg.getCodeText(errCd);
		this.obj = null;
	}
	
	/**
	 * 거래성공세팅
	 */
	public void setSucc(){
		this.rslt = AppConstant.SUCC;
	}

	/**
	 * 거래성공 및 응답부 객체세팅
	 * @param obj
	 */
	public void setSucc(Object obj){
		this.rslt = AppConstant.SUCC;
		this.obj = obj;
	}
	public String getMsg2() {
		return msg2;
	}
	public void setMsg2(String msg2) {
		this.msg2 = msg2;
	}
}
