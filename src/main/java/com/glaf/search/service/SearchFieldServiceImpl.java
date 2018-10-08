/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glaf.search.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.UUID32;
import com.glaf.search.domain.SearchField;
import com.glaf.search.mapper.SearchFieldMapper;
import com.glaf.search.query.SearchFieldQuery;

@Service("com.glaf.search.service.searchFieldService")
@Transactional(readOnly = true)
public class SearchFieldServiceImpl implements SearchFieldService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected SearchFieldMapper searchFieldMapper;

	public SearchFieldServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<SearchField> list) {
		for (SearchField searchField : list) {
			if (StringUtils.isEmpty(searchField.getId())) {
				searchField.setId(idGenerator.getNextId("SYS_SEARCH_FIELD"));
				searchField.setCreateTime(new Date());
			}
		}

		int batch_size = 50;
		List<SearchField> rows = new ArrayList<SearchField>(batch_size);

		for (SearchField bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					searchFieldMapper.bulkInsertSearchField_oracle(rows);
				} else {
					searchFieldMapper.bulkInsertSearchField(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				searchFieldMapper.bulkInsertSearchField_oracle(rows);
			} else {
				searchFieldMapper.bulkInsertSearchField(rows);
			}
			rows.clear();
		}
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			searchFieldMapper.deleteSearchFieldById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				searchFieldMapper.deleteSearchFieldById(id);
			}
		}
	}

	public int count(SearchFieldQuery query) {
		return searchFieldMapper.getSearchFieldCount(query);
	}

	public List<SearchField> list(SearchFieldQuery query) {
		List<SearchField> list = searchFieldMapper.getSearchFields(query);
		return list;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getSearchFieldCountByQueryCriteria(SearchFieldQuery query) {
		return searchFieldMapper.getSearchFieldCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<SearchField> getSearchFieldsByQueryCriteria(int start, int pageSize, SearchFieldQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<SearchField> rows = sqlSessionTemplate.selectList("getSearchFields", query, rowBounds);
		return rows;
	}

	public SearchField getSearchField(String id) {
		if (id == null) {
			return null;
		}
		SearchField searchField = searchFieldMapper.getSearchFieldById(id);
		return searchField;
	}

	@Transactional
	public void save(SearchField searchField) {
		if (StringUtils.isEmpty(searchField.getId())) {
			searchField.setId(UUID32.getUUID());
			searchField.setCreateTime(new Date());
			searchField.setLocked(0);
			searchFieldMapper.insertSearchField(searchField);
		} else {
			searchField.setUpdateTime(new Date());
			searchFieldMapper.updateSearchField(searchField);
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

	@javax.annotation.Resource(name = "com.glaf.search.mapper.SearchFieldMapper")
	public void setSearchFieldMapper(SearchFieldMapper searchFieldMapper) {
		this.searchFieldMapper = searchFieldMapper;
	}

	@javax.annotation.Resource
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
