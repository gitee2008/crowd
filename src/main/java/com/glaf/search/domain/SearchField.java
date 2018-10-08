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

package com.glaf.search.domain;

import java.io.*;
import java.util.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.util.DateUtils;
import com.glaf.search.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "SYS_SEARCH_FIELD")
public class SearchField implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", length = 64, nullable = false)
	protected String id;

	/**
	 * 服务编号
	 */
	@Column(name = "SERVERID_", length = 64)
	protected String serverId;

	/**
	 * 代码
	 */
	@Column(name = "CODE_", length = 200)
	protected String code;

	/**
	 * 映射代码
	 */
	@Column(name = "MAPPINGCODE_", length = 200)
	protected String mappingCode;

	/**
	 * 分类
	 */
	@Column(name = "TYPE_", length = 200)
	protected String type;

	/**
	 * 格式
	 */
	@Column(name = "FORMAT_", length = 200)
	protected String format;

	/**
	 * 索引分词器
	 */
	@Column(name = "INDEXANALYZER_", length = 200)
	protected String indexAnalyzer;

	/**
	 * 字段分词器
	 */
	@Column(name = "FIELDANALYZER_", length = 200)
	protected String fieldAnalyzer;

	/**
	 * 词向量
	 */
	@Column(name = "TERMVECTOR_", length = 200)
	protected String termVector;

	/**
	 * 分词标识
	 */
	@Column(name = "ANALYZERFLAG_", length = 50)
	protected String analyzerFlag;

	/**
	 * 查询返回标识
	 */
	@Column(name = "SEARCHRETURNFLAG_", length = 50)
	protected String searchReturnFlag;

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

	/**
	 * 更新人
	 */
	@Column(name = "UPDATEBY_", length = 50)
	protected String updateBy;

	/**
	 * 更新时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME_")
	protected Date updateTime;

	public SearchField() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SearchField other = (SearchField) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getAnalyzerFlag() {
		return this.analyzerFlag;
	}

	public String getCode() {
		return this.code;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public String getCreateTimeString() {
		if (this.createTime != null) {
			return DateUtils.getDateTime(this.createTime);
		}
		return "";
	}

	public String getFieldAnalyzer() {
		return this.fieldAnalyzer;
	}

	public String getFormat() {
		return this.format;
	}

	public String getId() {
		return this.id;
	}

	public String getIndexAnalyzer() {
		return this.indexAnalyzer;
	}

	public int getLocked() {
		return this.locked;
	}

	public String getMappingCode() {
		return this.mappingCode;
	}

	public String getSearchReturnFlag() {
		return this.searchReturnFlag;
	}

	public String getServerId() {
		return this.serverId;
	}

	public String getTermVector() {
		return this.termVector;
	}

	public String getType() {
		return this.type;
	}

	public String getUpdateBy() {
		return this.updateBy;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public String getUpdateTimeString() {
		if (this.updateTime != null) {
			return DateUtils.getDateTime(this.updateTime);
		}
		return "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public SearchField jsonToObject(JSONObject jsonObject) {
		return SearchFieldJsonFactory.jsonToObject(jsonObject);
	}

	public void setAnalyzerFlag(String analyzerFlag) {
		this.analyzerFlag = analyzerFlag;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setFieldAnalyzer(String fieldAnalyzer) {
		this.fieldAnalyzer = fieldAnalyzer;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIndexAnalyzer(String indexAnalyzer) {
		this.indexAnalyzer = indexAnalyzer;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setMappingCode(String mappingCode) {
		this.mappingCode = mappingCode;
	}

	public void setSearchReturnFlag(String searchReturnFlag) {
		this.searchReturnFlag = searchReturnFlag;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public void setTermVector(String termVector) {
		this.termVector = termVector;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public JSONObject toJsonObject() {
		return SearchFieldJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return SearchFieldJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
