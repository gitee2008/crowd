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

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.glaf.matrix.dataimport.domain.TableInput;
import com.glaf.matrix.dataimport.domain.TableInputColumn;
import com.glaf.matrix.dataimport.query.TableInputQuery;
import com.glaf.matrix.dataimport.query.TableInputColumnQuery;

@Transactional(readOnly = true)
public interface TableInputService {

	/**
	 * 删除列定义
	 * 
	 * @param columnId
	 */
	@Transactional
	void deleteColumn(String columnId);

	/**
	 * 删除表定义及管理查询
	 * 
	 * @param tableId
	 */
	@Transactional
	void deleteTable(String tableId);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<TableInput> getAllTableInputs();

	/**
	 * 根据表Id获取表对象
	 * 
	 * @return
	 */
	TableInput getTableInputById(String tableId);

	TableInput getTableInputByTableName(String tableName);

	/**
	 * 
	 * @param columnId
	 * @return
	 */
	TableInputColumn getTableInputColumn(String columnId);

	int getTableInputColumnCount(TableInputColumnQuery query);

	List<TableInputColumn> getTableInputColumns(TableInputColumnQuery query);

	List<TableInputColumn> getTableInputColumnsByTableId(String tableId);

	List<TableInputColumn> getTableInputColumnsByTableName(String tableName);

	List<TableInputColumn> getTableInputColumnsByTargetId(String targetId);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getTableInputCountByQueryCriteria(TableInputQuery query);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<TableInput> getTableInputsByQueryCriteria(int start, int pageSize, TableInputQuery query);

	@Transactional
	void insertColumns(String tableName, List<TableInputColumn> columns);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<TableInput> list(TableInputQuery query);

	/**
	 * 获取某个类型的表名称
	 * 
	 * @param type
	 * @return
	 */
	@Transactional
	String nextTableName(String type);

	/**
	 * 保存表定义信息
	 * 
	 * @return
	 */
	@Transactional
	void save(TableInput tableDefinition);

	/**
	 * 保存表定义信息
	 * 
	 * @return
	 */
	@Transactional
	void saveAs(TableInput tableDefinition, List<TableInputColumn> columns);

	/**
	 * 保存字段信息
	 * 
	 * @param tableName
	 * @param column
	 */
	@Transactional
	void saveColumn(String tableName, TableInputColumn column);

	/**
	 * 保存列定义信息
	 * 
	 * @param targetId
	 * @param columns
	 */
	@Transactional
	void saveColumns(String targetId, List<TableInputColumn> columns);

	/**
	 * 保存定义
	 * 
	 * @param tableName
	 * @param rows
	 */
	@Transactional
	void saveSystemTable(String tableName, List<TableInputColumn> rows);

	@Transactional
	void saveTargetColumn(String targetId, TableInputColumn column);

	/**
	 * 保存字段信息
	 * 
	 * @param column
	 */
	@Transactional
	void updateColumn(TableInputColumn column);

}