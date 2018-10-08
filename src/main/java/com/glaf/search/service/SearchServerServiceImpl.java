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
import com.glaf.search.domain.SearchServer;
import com.glaf.search.mapper.SearchFieldMapper;
import com.glaf.search.mapper.SearchServerMapper;
import com.glaf.search.query.SearchServerQuery;

@Service("com.glaf.search.service.searchServerService")
@Transactional(readOnly = true)
public class SearchServerServiceImpl implements SearchServerService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected SearchFieldMapper searchFieldMapper;

	protected SearchServerMapper searchServerMapper;

	public SearchServerServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<SearchServer> list) {
		for (SearchServer searchServer : list) {
			if (StringUtils.isEmpty(searchServer.getId())) {
				searchServer.setId(idGenerator.getNextId("SYS_SEARCH_SERVER"));
				searchServer.setCreateTime(new Date());
			}
		}

		int batch_size = 50;
		List<SearchServer> rows = new ArrayList<SearchServer>(batch_size);

		for (SearchServer bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					searchServerMapper.bulkInsertSearchServer_oracle(rows);
				} else {
					searchServerMapper.bulkInsertSearchServer(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				searchServerMapper.bulkInsertSearchServer_oracle(rows);
			} else {
				searchServerMapper.bulkInsertSearchServer(rows);
			}
			rows.clear();
		}
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			searchFieldMapper.deleteSearchFieldByServerId(id);
			searchServerMapper.deleteSearchServerById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				searchFieldMapper.deleteSearchFieldByServerId(id);
				searchServerMapper.deleteSearchServerById(id);
			}
		}
	}

	public int count(SearchServerQuery query) {
		return searchServerMapper.getSearchServerCount(query);
	}

	public List<SearchServer> list(SearchServerQuery query) {
		List<SearchServer> list = searchServerMapper.getSearchServers(query);
		return list;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getSearchServerCountByQueryCriteria(SearchServerQuery query) {
		return searchServerMapper.getSearchServerCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<SearchServer> getSearchServersByQueryCriteria(int start, int pageSize, SearchServerQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<SearchServer> rows = sqlSessionTemplate.selectList("getSearchServers", query, rowBounds);
		return rows;
	}

	public SearchServer getSearchServer(String id) {
		if (id == null) {
			return null;
		}
		SearchServer searchServer = searchServerMapper.getSearchServerById(id);
		return searchServer;
	}

	@Transactional
	public void save(SearchServer searchServer) {
		if (StringUtils.isEmpty(searchServer.getId())) {
			searchServer.setId(UUID32.getUUID());
			searchServer.setCreateTime(new Date());
			searchServer.setLocked(0);
			searchServerMapper.insertSearchServer(searchServer);
		} else {
			searchServer.setUpdateTime(new Date());
			searchServerMapper.updateSearchServer(searchServer);
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

	@javax.annotation.Resource(name = "com.glaf.search.mapper.SearchServerMapper")
	public void setSearchServerMapper(SearchServerMapper searchServerMapper) {
		this.searchServerMapper = searchServerMapper;
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
