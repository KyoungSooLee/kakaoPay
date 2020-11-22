/**
 * PRJ_TEMPLATE
 * com.kakao.common.dao.Common_Dao.java
 */
package com.kakao.common.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

/**
 * @author	: bingo
 * @date	: 
 * @desc	: 
 *
 */
@Repository
public class Common_Dao {

	private SqlSession sqlSession; 
	public void setSqlSession(SqlSession sess){
		sqlSession = sess;
	} 

	/**
	 * 단건 단일컬럼만 조회(조회조건없음)
	 * @param qryId
	 * @return
	 */
	public String selectOneStr(String qryId){
		return sqlSession.selectOne(qryId);
	}
	
	/**
	 * 단건 단일컬럼만 조회(조회조건있음)
	 * @param qryId
	 * @return
	 */
	public String selectOneStr(String qryId, Object param){
		return sqlSession.selectOne(qryId, param);
	}

	/**
	 * 단건 조회(조회조건없음)
	 * @param qryId
	 * @return
	 */
	public Map<String, Object> selectOne(String qryId){
		return sqlSession.selectOne(qryId);
	}
	
	/**
	 * 단건 조회(조회조건있음)
	 * @param qryId
	 * @param inputMap
	 * @return
	 */
	public Map<String, Object> selectOne(String qryId, Object param){
		return sqlSession.selectOne(qryId, param);
	}
	
	/**
	 * 단건 조회(VO리턴)
	 * @param qryId
	 * @param param
	 * @return
	 */
	public Object selectVo(String qryId, Object param){
		return sqlSession.selectOne(qryId, param);
	}

	/**
	 * 다건 조회(조회조건없음)
	 * @param qryID
	 * @return
	 */
	public List<Object> selectList(String qryID){
		return sqlSession.selectList(qryID);
	}

	/**
	 * 다건 조회(조회조건 문자)
	 * @param qryID
	 * @param param
	 * @return
	 */
	public List<Object> selectList(String qryID, String param){
		return sqlSession.selectList(qryID, param);
	}
	
	/**
	 * 다건 조회(조회조건 객체)
	 * @param qryID
	 * @param param
	 * @return
	 */
	public List<Object> selectList(String qryID, Object param){
		return sqlSession.selectList(qryID, param);
	}

	/**
	 * 데이타 추가
	 * @param qryID
	 * @param param
	 * @return
	 */
	public int create(String qryID, Object param){
		return sqlSession.insert(qryID, param);
	}
	
	/**
	 * 데이타 추가
	 * @param qryID
	 * @param param
	 * @return
	 */
	public Object createObj(String qryID, Object param){
		return sqlSession.insert(qryID, param);
	}
	
	/**
	 * 데이타 수정
	 * @param qryID
	 * @param param
	 * @return
	 */
	public int update(String qryID, Object param){
		return sqlSession.update(qryID, param);
	}
	
	/**
	 * 데이타 삭제
	 * @param qryID
	 * @param param
	 * @return
	 */
	public int delete(String qryID, Object param){
		return sqlSession.delete(qryID, param);
	}

}
