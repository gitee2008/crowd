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

package com.glaf.matrix.dataimport.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.util.UUID32;
import com.glaf.matrix.dataimport.domain.TableInput;
import com.glaf.matrix.dataimport.domain.TableInputColumn;
import com.glaf.matrix.dataimport.mapper.TableInputMapper;
import com.glaf.matrix.dataimport.mapper.TableInputColumnMapper;
import com.glaf.matrix.dataimport.query.TableInputQuery;
import com.glaf.matrix.dataimport.query.TableInputColumnQuery;
import com.glaf.matrix.dataimport.service.TableInputService;
import com.glaf.matrix.dataimport.util.TableInputJsonFactory;

@Service("tableInputService")
@Transactional(readOnly = true)
public class TableInputServiceImpl implements TableInputService {
	protected final static Log logger = LogFactory.getLog(TableInputServiceImpl.class);

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected SqlSession sqlSession;

	protected TableInputColumnMapper tableInputColumnMapper;

	protected TableInputMapper tableInputMapper;

	public TableInputServiceImpl() {

	}

	public int count(TableInputQuery query) {
		if (StringUtils.isEmpty(query.getType())) {
			throw new RuntimeException(" type is null ");
		}
		return tableInputMapper.getTableInputCount(query);
	}

	@Transactional
	public void deleteColumn(String columnId) {
		CacheFactory.clear("table_input");
		tableInputColumnMapper.deleteTableInputColumnById(columnId);
	}

	/**
	 * 删除表定义
	 * 
	 * @param tableId
	 */
	@Transactional
	public void deleteTable(String tableId) {
		CacheFactory.clear("table_input");
		tableInputColumnMapper.deleteTableInputColumnByTableId(tableId);
		tableInputMapper.deleteTableInputById(tableId);
	}

	public List<TableInput> getAllTableInputs() {
		TableInputQuery query = new TableInputQuery();
		query.systemFlag("U");
		return tableInputMapper.getTableInputs(query);
	}

	public TableInput getTableInputById(String tableId) {
		if (tableId == null) {
			return null;
		}
		String cacheKey = "table_input_" + tableId;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("table_input", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					com.alibaba.fastjson.JSONObject json = JSON.parseObject(text);
					TableInput model = TableInputJsonFactory.jsonToObject(json);
					if (model != null) {
						return model;
					}
				} catch (Exception ex) {
				}
			}
		}

		TableInput tableInput = tableInputMapper.getTableInputById(tableId);
		if (tableInput != null) {
			List<TableInputColumn> columns = tableInputColumnMapper.getTableInputColumnsByTableId(tableId);
			if (columns != null && !columns.isEmpty()) {
				tableInput.setColumns(columns);
				for (TableInputColumn column : columns) {
					if (StringUtils.equalsIgnoreCase(column.getColumnName(), tableInput.getPrimaryKey())) {
						logger.debug("##PrimaryKey:" + column.toJsonObject().toJSONString());
						tableInput.setIdColumn(column);
						columns.remove(column);
						break;
					}
					if (column.isPrimaryKey()) {
						logger.debug("##PrimaryKey:" + column.toJsonObject().toJSONString());
						tableInput.setIdColumn(column);
						columns.remove(column);
						break;
					}
				}
			}
			CacheFactory.put("table_input", cacheKey, tableInput.toJsonObject().toJSONString());
		}

		return tableInput;
	}

	public TableInputColumn getTableInputColumn(String columnId) {
		return tableInputColumnMapper.getTableInputColumnById(columnId);
	}

	public int getTableInputColumnCount(TableInputColumnQuery query) {
		return tableInputColumnMapper.getTableInputColumnCount(query);
	}

	public List<TableInputColumn> getTableInputColumns(TableInputColumnQuery query) {
		return tableInputColumnMapper.getTableInputColumns(query);
	}

	public List<TableInputColumn> getTableInputColumnsByTableId(String tableId) {
		TableInput tableInput = this.getTableInputById(tableId);
		if (tableInput != null) {
			return tableInput.getColumns();
		}
		return null;
	}

	public List<TableInputColumn> getTableInputColumnsByTargetId(String targetId) {
		return tableInputColumnMapper.getTableInputColumnsByTargetId(targetId);
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getTableInputCountByQueryCriteria(TableInputQuery query) {
		if (StringUtils.isEmpty(query.getType())) {
			throw new RuntimeException(" type is null ");
		}
		return tableInputMapper.getTableInputCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<TableInput> getTableInputsByQueryCriteria(int start, int pageSize, TableInputQuery query) {
		if (StringUtils.isEmpty(query.getType())) {
			throw new RuntimeException(" type is null ");
		}
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<TableInput> rows = sqlSession.selectList("getTableInputs", query, rowBounds);
		return rows;
	}

	@Transactional
	public void insertColumns(String tableId, List<TableInputColumn> columns) {

		TableInput tableInput = tableInputMapper.getTableInputById(tableId);
		if (tableInput != null) {
			String cacheKey = "table_input_" + tableInput.getTableId();
			CacheFactory.remove("table_input", cacheKey);
			cacheKey = "table_input_" + tableId;
			CacheFactory.remove("table_input", cacheKey);

			for (TableInputColumn column : columns) {
				String id = column.getId();
				if (id == null || tableInputColumnMapper.getTableInputColumnById(id) == null) {
					column.setId(UUID32.getUUID());
					column.setSystemFlag("1");
					column.setTableName(tableInput.getTableName());
					column.setTableId(tableInput.getTableId());
					tableInputColumnMapper.insertTableInputColumn(column);
				}
			}
		}
	}

	public List<TableInput> list(TableInputQuery query) {
		if (StringUtils.isEmpty(query.getType())) {
			throw new RuntimeException(" type is null ");
		}
		List<TableInput> list = tableInputMapper.getTableInputs(query);
		return list;
	}

	/**
	 * 获取某个类型的表名称
	 * 
	 * @param type
	 * @return
	 */
	@Transactional
	public String nextTableName(String type) {
		Long nextId = idGenerator.nextId(type);
		return type.toUpperCase() + nextId;
	}

	@Transactional
	public void save(TableInput tableInput) {
		if (StringUtils.isEmpty(tableInput.getType())) {
			throw new RuntimeException(" type is null ");
		}
		String tableName = tableInput.getTableName();
		tableName = tableName.toUpperCase();
		tableInput.setTableName(tableName);
		TableInput table = tableInputMapper.getTableInputById(tableInput.getTableId());
		if (table == null) {
			tableInput.setTableId(UUID32.getUUID());
			tableInput.setCreateTime(new Date());
			tableInput.setRevision(1);
			tableInputMapper.insertTableInput(tableInput);
		} else {
			String cacheKey = "table_input_" + tableInput.getTableId();
			CacheFactory.remove("table_input", cacheKey);
			cacheKey = "table_input_" + tableName;
			CacheFactory.remove("table_input", cacheKey);

			tableInput.setRevision(tableInput.getRevision() + 1);
			tableInputMapper.updateTableInput(tableInput);
		}

		if (tableInput.getColumns() != null && !tableInput.getColumns().isEmpty()) {
			for (TableInputColumn column : tableInput.getColumns()) {
				String id = column.getId();
				if (id == null || tableInputColumnMapper.getTableInputColumnById(id) == null) {
					column.setId(UUID32.getUUID());
					column.setTableName(tableName);
					column.setTableId(tableInput.getTableId());
					if ("SYS".equals(tableInput.getType())) {
						column.setSystemFlag("1");
					}
					tableInputColumnMapper.insertTableInputColumn(column);
				} else {
					tableInputColumnMapper.updateTableInputColumn(column);
				}
			}
		}
	}

	/**
	 * 保存表定义信息
	 * 
	 * @return
	 */
	@Transactional
	public void saveAs(TableInput tableInput, List<TableInputColumn> columns) {
		tableInput.setTableId(UUID32.getUUID());
		tableInputMapper.insertTableInput(tableInput);
		for (TableInputColumn column : columns) {
			column.setId(UUID32.getUUID());
			column.setSystemFlag("Y");
			column.setTableName(tableInput.getTableName());
			column.setTableId(tableInput.getTableId());
			column.setColumnName(column.getColumnName());
			tableInputColumnMapper.insertTableInputColumn(column);
		}
	}

	@Transactional
	public void saveColumn(String tableId, TableInputColumn columnDefinition) {
		TableInput tableInput = getTableInputById(tableId);
		if (tableInput != null) {
			String cacheKey = "table_input_" + tableInput.getTableId();
			CacheFactory.remove("table_input", cacheKey);
			cacheKey = "table_input_" + tableId;
			CacheFactory.remove("table_input", cacheKey);

			List<TableInputColumn> columns = tableInput.getColumns();
			boolean exists = false;
			int sortNo = 1;
			if (columns != null && !columns.isEmpty()) {
				sortNo = columns.size();
				for (TableInputColumn column : columns) {
					if (StringUtils.equalsIgnoreCase(column.getColumnName(), columnDefinition.getColumnName())) {
						column.setValueExpression(columnDefinition.getValueExpression());
						column.setFormula(columnDefinition.getFormula());
						column.setName(columnDefinition.getName());
						column.setTitle(columnDefinition.getTitle());
						column.setEnglishTitle(columnDefinition.getEnglishTitle());
						column.setHeight(columnDefinition.getHeight());
						column.setLength(columnDefinition.getLength());
						column.setPrecision(columnDefinition.getPrecision());
						column.setScale(columnDefinition.getScale());
						column.setFrozen(columnDefinition.isFrozen());
						column.setNullable(columnDefinition.isNullable());
						column.setSortable(columnDefinition.isSortable());
						column.setDisplayType(columnDefinition.getDisplayType());
						column.setDiscriminator(columnDefinition.getDiscriminator());
						column.setFormatter(columnDefinition.getFormatter());
						column.setLink(columnDefinition.getLink());
						column.setOrdinal(columnDefinition.getOrdinal());
						column.setRegex(columnDefinition.getRegex());
						column.setSummaryExpr(columnDefinition.getSummaryExpr());
						column.setSummaryType(columnDefinition.getSummaryType());
						column.setExportFlag(columnDefinition.getExportFlag());
						tableInputColumnMapper.updateTableInputColumn(column);
						exists = true;
						break;
					}
				}
			}

			if (!exists) {
				if (StringUtils.isEmpty(columnDefinition.getColumnName())) {
					columnDefinition.setColumnName(
							(tableInput.getTableName() + "_user" + idGenerator.getNextId(tableInput.getTableName()))
									.toLowerCase());
				}
				columnDefinition.setId(UUID32.getUUID());
				columnDefinition.setTableName(tableInput.getTableName());
				columnDefinition.setTableId(tableInput.getTableId());
				columnDefinition.setOrdinal(++sortNo);
				tableInputColumnMapper.insertTableInputColumn(columnDefinition);
			}
		}
	}

	@Transactional
	public void saveColumns(String targetId, List<TableInputColumn> columns) {
		CacheFactory.clear("table_input");
		tableInputColumnMapper.deleteTableInputColumnByTargetId(targetId);
		if (columns != null && !columns.isEmpty()) {
			for (TableInputColumn col : columns) {
				if (StringUtils.isEmpty(col.getId())) {
					col.setId(UUID32.getUUID());
				}
				col.setTargetId(targetId);
				tableInputColumnMapper.insertTableInputColumn(col);
			}
		}
	}

	@Transactional
	public void saveSystemTable(String tableId, List<TableInputColumn> columns) {
		TableInput tableInput = tableInputMapper.getTableInputById(tableId);
		if (tableInput == null) {
			tableInput = new TableInput();
			tableInput.setType("SYS");
			tableInput.setSystemFlag("Y");
			tableInput.setRevision(1);
			tableInput.setDeleteFlag(0);
			tableInput.setLocked(0);

			tableInput.setCreateBy("system");
			tableInput.setCreateTime(new Date());
			tableInput.setTableId(UUID32.getUUID());
			tableInputMapper.insertTableInput(tableInput);
		} else {

			String cacheKey = "table_input_" + tableInput.getTableId();
			CacheFactory.remove("table_input", cacheKey);
			cacheKey = "table_input_" + tableId;
			CacheFactory.remove("table_input", cacheKey);

			tableInputMapper.updateTableInput(tableInput);
		}
		tableInputColumnMapper.deleteTableInputColumnByTableId(tableInput.getTableId());
		for (TableInputColumn column : columns) {
			column.setId(UUID32.getUUID());
			column.setSystemFlag("Y");
			column.setTableName(tableInput.getTableName());
			column.setTableId(tableInput.getTableId());
			tableInputColumnMapper.insertTableInputColumn(column);
		}
	}

	@Transactional
	public void saveTargetColumn(String targetId, TableInputColumn column) {
		CacheFactory.clear("table_input");
		if (StringUtils.isNotEmpty(column.getId())) {
			TableInputColumn model = this.getTableInputColumn(column.getId());
			if (model == null) {
				column.setTargetId(targetId);
				tableInputColumnMapper.insertTableInputColumn(column);
			}
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
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@javax.annotation.Resource
	public void setTableInputColumnMapper(TableInputColumnMapper tableInputColumnMapper) {
		this.tableInputColumnMapper = tableInputColumnMapper;
	}

	@javax.annotation.Resource
	public void setTableInputMapper(TableInputMapper tableInputMapper) {
		this.tableInputMapper = tableInputMapper;
	}

	/**
	 * 保存字段信息
	 * 
	 * @param columnDefinition
	 */
	@Transactional
	public void updateColumn(TableInputColumn column) {
		CacheFactory.clear("table_input");
		tableInputColumnMapper.updateTableInputColumn(column);
	}

}