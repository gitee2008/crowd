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

package com.glaf.matrix.dataimport.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.matrix.dataimport.util.TableInputJsonFactory;

/**
 * 数据输入表定义
 * 
 */
@Entity
@Table(name = "SYS_INPUT_TABLE")
public class TableInput implements java.io.Serializable, java.lang.Comparable<TableInput> {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TABLEID_", length = 50)
	protected String tableId;

	/**
	 * 表名
	 */
	@Column(name = "TABLENAME_", length = 30)
	protected String tableName;

	@Column(name = "NODEID_")
	protected Long nodeId;

	/**
	 * 聚合主键列集
	 */
	@Column(name = "AGGREGATIONKEY_", length = 500)
	protected String aggregationKey;

	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION_", length = 500)
	protected String description;

	/**
	 * 标题
	 */
	@Column(name = "ENGLISHTITLE_")
	protected String englishTitle;

	@Column(name = "PRIMARYKEY_", length = 200)
	protected String primaryKey;

	/**
	 * 排序列名
	 */
	@Column(name = "SORTCOLUMN_", length = 50)
	protected String sortColumn;

	/**
	 * 排序方式
	 */
	@Column(name = "SORTORDER_", length = 10)
	protected String sortOrder;

	/**
	 * 修订版本
	 */
	@Column(name = "REVISION_")
	protected int revision;

	@Column(name = "SORTNO_")
	protected int sortNo;

	@Column(name = "SYSTEMFLAG_", length = 2)
	protected String systemFlag;

	/**
	 * 审核标记
	 */
	@Column(name = "AUDITFLAG_", length = 10)
	protected String auditFlag;

	/**
	 * 树型结构标识， Y-树型，N-非树型
	 */
	@Column(name = "TREEFLAG_", length = 1)
	protected String treeFlag;

	/**
	 * 权限标识
	 */
	@Column(name = "PRIVILEGEFLAG_", length = 10)
	protected String privilegeFlag;

	/**
	 * 输入标识
	 */
	@Column(name = "INPUTFLAG_", length = 50)
	protected String inputFlag;

	/**
	 * 标题
	 */
	@Column(name = "TITLE_")
	protected String title;

	@Column(name = "TOPID_", length = 50)
	protected String topId;

	/**
	 * 表类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	@Transient
	protected boolean insertOnly;

	@Transient
	protected boolean updateAllowed = true;

	/**
	 * 开始行
	 */
	@Column(name = "STARTROW_")
	protected int startRow;

	/**
	 * 结束行
	 */
	@Column(name = "STOPROW_")
	protected int stopRow;

	/**
	 * 结束词
	 */
	@Column(name = "STOPWORD_", length = 100)
	protected String stopWord;

	/**
	 * 是否先删除再抓取数据
	 */
	@Column(name = "DELETEFETCH_", length = 1)
	protected String deleteFetch;

	@Column(name = "DELETEFLAG_")
	protected int deleteFlag;

	/**
	 * 是否锁定
	 */
	@Column(name = "LOCKED_")
	protected int locked;

	/**
	 * 创建人
	 */
	@Column(name = "CREATEBY_", length = 50)
	protected String createBy;

	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME_")
	protected Date createTime;

	@Transient
	protected TableInputColumn idColumn;

	@Transient
	protected List<TableInputColumn> columns = new ArrayList<TableInputColumn>();

	public TableInput() {

	}

	public void addColumn(TableInputColumn column) {
		if (columns == null) {
			columns = new java.util.ArrayList<TableInputColumn>();
		}
		if (!columns.contains(column)) {
			columns.add(column);
		}
	}

	public int compareTo(TableInput o) {
		if (o == null) {
			return -1;
		}

		TableInput field = o;

		int l = this.sortNo - field.getSortNo();

		int ret = 0;

		if (l > 0) {
			ret = 1;
		} else if (l < 0) {
			ret = -1;
		}
		return ret;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableInput other = (TableInput) obj;
		if (tableId == null) {
			if (other.tableId != null)
				return false;
		} else if (!tableId.equals(other.tableId))
			return false;
		return true;
	}

	public String getAggregationKey() {
		return aggregationKey;
	}

	public String getAuditFlag() {
		return auditFlag;
	}

	public List<TableInputColumn> getColumns() {
		return columns;
	}

	public String getCreateBy() {
		return createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public String getDeleteFetch() {
		return deleteFetch;
	}

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public String getDescription() {
		return description;
	}

	public String getEnglishTitle() {
		return englishTitle;
	}

	public Map<String, TableInputColumn> getFields() {
		Map<String, TableInputColumn> fieldMap = new LinkedHashMap<String, TableInputColumn>();
		if (columns != null && !columns.isEmpty()) {
			for (TableInputColumn column : columns) {
				fieldMap.put(column.getName(), column);
			}
		}
		return fieldMap;
	}

	public TableInputColumn getIdColumn() {
		return idColumn;
	}

	public String getInputFlag() {
		return inputFlag;
	}

	public int getLocked() {
		return locked;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public String getPrivilegeFlag() {
		return privilegeFlag;
	}

	public int getRevision() {
		return revision;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public int getSortNo() {
		return sortNo;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public int getStartRow() {
		return startRow;
	}

	public int getStopRow() {
		return stopRow;
	}

	public String getStopWord() {
		return stopWord;
	}

	public String getSystemFlag() {
		return systemFlag;
	}

	public String getTableId() {
		return tableId;
	}

	public String getTableName() {
		return tableName;
	}

	public String getTitle() {
		return title;
	}

	public String getTopId() {
		return topId;
	}

	public String getTreeFlag() {
		return treeFlag;
	}

	public String getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tableId == null) ? 0 : tableId.hashCode());
		return result;
	}

	public boolean isInsertOnly() {
		return insertOnly;
	}

	public boolean isUpdateAllowed() {
		return updateAllowed;
	}

	public TableInput jsonToObject(JSONObject jsonObject) {
		return TableInputJsonFactory.jsonToObject(jsonObject);
	}

	public void setAggregationKey(String aggregationKey) {
		this.aggregationKey = aggregationKey;
	}

	public void setAuditFlag(String auditFlag) {
		this.auditFlag = auditFlag;
	}

	public void setColumns(List<TableInputColumn> columns) {
		this.columns = columns;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDeleteFetch(String deleteFetch) {
		this.deleteFetch = deleteFetch;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEnglishTitle(String englishTitle) {
		this.englishTitle = englishTitle;
	}

	public void setIdColumn(TableInputColumn idColumn) {
		if (idColumn != null) {
			this.idColumn = idColumn;
			this.idColumn.setPrimaryKey(true);
			this.addColumn(idColumn);
		}
	}

	public void setInputFlag(String inputFlag) {
		this.inputFlag = inputFlag;
	}

	public void setInsertOnly(boolean insertOnly) {
		this.insertOnly = insertOnly;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public void setPrivilegeFlag(String privilegeFlag) {
		this.privilegeFlag = privilegeFlag;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public void setStopRow(int stopRow) {
		this.stopRow = stopRow;
	}

	public void setStopWord(String stopWord) {
		this.stopWord = stopWord;
	}

	public void setSystemFlag(String systemFlag) {
		this.systemFlag = systemFlag;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTopId(String topId) {
		this.topId = topId;
	}

	public void setTreeFlag(String treeFlag) {
		this.treeFlag = treeFlag;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUpdateAllowed(boolean updateAllowed) {
		this.updateAllowed = updateAllowed;
	}

	public JSONObject toJsonObject() {
		return TableInputJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return TableInputJsonFactory.toObjectNode(this);
	}

}