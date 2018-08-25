package com.glaf.crowd.service;

import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.id.*;
import com.glaf.core.dao.*;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.*;

import com.glaf.crowd.mapper.*;
import com.glaf.crowd.domain.*;
import com.glaf.crowd.query.*;

@Service("com.glaf.crowd.service.productItemService")
@Transactional(readOnly = true)
public class ProductItemServiceImpl implements ProductItemService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected ProductItemMapper productItemMapper;

	public ProductItemServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<ProductItem> list) {
		for (ProductItem productItem : list) {
			if (StringUtils.isEmpty(productItem.getId())) {
				productItem.setId(UUID32.getUUID());
				productItem.setCreateTime(new Date());
			}
		}

		int batch_size = 50;
		List<ProductItem> rows = new ArrayList<ProductItem>(batch_size);

		for (ProductItem bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					productItemMapper.bulkInsertProductItem_oracle(rows);
				} else {
					productItemMapper.bulkInsertProductItem(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				productItemMapper.bulkInsertProductItem_oracle(rows);
			} else {
				productItemMapper.bulkInsertProductItem(rows);
			}
			rows.clear();
		}
	}

	public int count(ProductItemQuery query) {
		return productItemMapper.getProductItemCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			productItemMapper.deleteProductItemById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				productItemMapper.deleteProductItemById(id);
			}
		}
	}

	public ProductItem getProductItem(String id) {
		if (id == null) {
			return null;
		}
		ProductItem productItem = productItemMapper.getProductItemById(id);
		return productItem;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getProductItemCountByQueryCriteria(ProductItemQuery query) {
		return productItemMapper.getProductItemCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<ProductItem> getProductItemsByQueryCriteria(int start, int pageSize, ProductItemQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<ProductItem> rows = sqlSessionTemplate.selectList("getProductItems", query, rowBounds);
		return rows;
	}

	public List<ProductItem> list(ProductItemQuery query) {
		List<ProductItem> list = productItemMapper.getProductItems(query);
		return list;
	}

	@Transactional
	public void save(ProductItem productItem) {
		if (StringUtils.isEmpty(productItem.getId())) {
			productItem.setId(UUID32.getUUID());
			productItem.setCreateTime(new Date());
			productItemMapper.insertProductItem(productItem);
		} else {
			productItem.setUpdateTime(new Date());
			productItem.setLastModified(System.currentTimeMillis());
			productItemMapper.updateProductItem(productItem);
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@javax.annotation.Resource(name = "com.glaf.crowd.mapper.ProductItemMapper")
	public void setProductItemMapper(ProductItemMapper productItemMapper) {
		this.productItemMapper = productItemMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
