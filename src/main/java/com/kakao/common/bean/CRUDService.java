/**
 * PRJ_TEMPLATE
 * com.kakao.common.bean.CRUDService.java
 */
package com.kakao.common.bean;

import java.util.List;

/**
 * @author	: bingo
 * @date	: 
 * @desc	: 
 *
 */
public interface CRUDService<T> {

	void create(T param);
	void update(T param);
	void delete(T param);
	List<Object> getList();
	List<Object> getListWithParam(T param);
}
