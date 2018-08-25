package com.glaf.crowd.service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.crowd.domain.*;
import com.glaf.crowd.query.*;

@Transactional(readOnly = true)
public interface ProductItemService {

	@Transactional
	void bulkInsert(List<ProductItem> list);

	/**
	 * 根据主键删除记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteById(String id);

	/**
	 * 根据主键删除多条记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteByIds(List<String> ids);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<ProductItem> list(ProductItemQuery query);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getProductItemCountByQueryCriteria(ProductItemQuery query);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<ProductItem> getProductItemsByQueryCriteria(int start, int pageSize, ProductItemQuery query);

	/**
	 * 根据主键获取一条记录
	 * 
	 * @return
	 */
	ProductItem getProductItem(String id);

	/**
	 * 保存一条记录
	 * 
	 * @return
	 */
	@Transactional
	void save(ProductItem productItem);

}
