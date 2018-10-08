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
@Table(name = "SYS_SEARCH_SERVER")
public class SearchServer implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", length = 64, nullable = false)
	protected String id;

	/**
	 * 服务地址
	 */
	@Column(name = "SERVERURL_", length = 500)
	protected String serverURL;

	/**
	 * 索引名称
	 */
	@Column(name = "INDEXNAME_", length = 200)
	protected String indexName;

	/**
	 * 分类
	 */
	@Column(name = "TYPE_", length = 200)
	protected String type;

	/**
	 * 分词器
	 */
	@Column(name = "TOKENIZER_", length = 250)
	protected String tokenizer;

	/**
	 * 词元处理器
	 */
	@Column(name = "TOKENFILTER_", length = 250)
	protected String tokenFilter;

	/**
	 * 字符处理器
	 */
	@Column(name = "CHARACTERFILTER_", length = 250)
	protected String characterFilter;

	/**
	 * 分析器名称
	 */
	@Column(name = "ANALYZERNAME_", length = 200)
	protected String analyzerName;

	/**
	 * 分析器类型
	 */
	@Column(name = "ANALYZERTYPE_", length = 200)
	protected String analyzerType;

	/**
	 * 自定义过滤器
	 */
	@Column(name = "FILTER_", length = 250)
	protected String filter;

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

	@javax.persistence.Transient
	protected List<SearchField> searchFields = new ArrayList<SearchField>();

	public SearchServer() {

	}

	public void addField(SearchField searchField) {
		if (searchFields == null) {
			searchFields = new ArrayList<SearchField>();
		}
		searchFields.add(searchField);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SearchServer other = (SearchServer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getAnalyzerName() {
		return this.analyzerName;
	}

	public String getAnalyzerType() {
		return this.analyzerType;
	}

	public String getCharacterFilter() {
		return this.characterFilter;
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

	public String getFilter() {
		return this.filter;
	}

	public String getId() {
		return this.id;
	}

	public String getIndexName() {
		return this.indexName;
	}

	public int getLocked() {
		return this.locked;
	}

	public List<SearchField> getSearchFields() {
		return searchFields;
	}

	public String getServerURL() {
		return this.serverURL;
	}

	public String getTokenFilter() {
		return this.tokenFilter;
	}

	public String getTokenizer() {
		return this.tokenizer;
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

	public SearchServer jsonToObject(JSONObject jsonObject) {
		return SearchServerJsonFactory.jsonToObject(jsonObject);
	}

	public void setAnalyzerName(String analyzerName) {
		this.analyzerName = analyzerName;
	}

	public void setAnalyzerType(String analyzerType) {
		this.analyzerType = analyzerType;
	}

	public void setCharacterFilter(String characterFilter) {
		this.characterFilter = characterFilter;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setSearchFields(List<SearchField> searchFields) {
		this.searchFields = searchFields;
	}

	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	public void setTokenFilter(String tokenFilter) {
		this.tokenFilter = tokenFilter;
	}

	public void setTokenizer(String tokenizer) {
		this.tokenizer = tokenizer;
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
		return SearchServerJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return SearchServerJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
