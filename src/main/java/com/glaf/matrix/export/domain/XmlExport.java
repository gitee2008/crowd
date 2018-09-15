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

package com.glaf.matrix.export.domain;

import java.io.*;
import java.util.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.util.DateUtils;
import com.glaf.matrix.export.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "SYS_XML_EXPORT")
public class XmlExport implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", length = 50, nullable = false)
	protected String id;

	/**
	 * 节点编号，树形结构的ID
	 */
	@Column(name = "NODEID_")
	protected long nodeId;

	/**
	 * 节点编号，树形结构的PARENTID
	 */
	@Column(name = "NODEPARENTID_")
	protected long nodeParentId;

	/**
	 * 标题
	 */
	@Column(name = "TITLE_", length = 200)
	protected String title;

	/**
	 * SQL语句
	 */
	@Lob
	@Column(name = "SQL_")
	protected String sql;

	/**
	 * 结果类型:S-单一记录,M-多条记录
	 */
	@Column(name = "RESULTFLAG_", length = 1)
	protected String resultFlag;

	/**
	 * 同步标识
	 */
	@Column(name = "SYNCFLAG_", length = 50)
	protected String syncFlag;

	/**
	 * 类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * XML标记
	 */
	@Column(name = "XMLTAG_", length = 200)
	protected String xmlTag;

	/**
	 * 模板编号
	 */
	@Column(name = "TEMPLATEID_", length = 50)
	protected String templateId;

	/**
	 * 外部属性定义
	 */
	@Column(name = "EXTERNALATTRSFLAG_", length = 50)
	protected String externalAttrsFlag;

	/**
	 * 时间间隔
	 */
	@Column(name = "INTERVAL_")
	protected int interval;

	/**
	 * 顺序号
	 */
	@Column(name = "SORTNO_")
	protected int sortNo;

	/**
	 * 是否有效
	 */
	@Column(name = "ACTIVE_", length = 1)
	protected String active;

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
	protected int level;

	@javax.persistence.Transient
	protected String blank;

	@javax.persistence.Transient
	protected XmlExport parent = null;

	@javax.persistence.Transient
	protected List<XmlExport> children = new ArrayList<XmlExport>();

	@javax.persistence.Transient
	protected List<XmlExportItem> items = new ArrayList<XmlExportItem>();

	@javax.persistence.Transient
	protected Map<String, Object> parentParameter = new HashMap<String, Object>();

	public XmlExport() {

	}

	public void addChild(XmlExport child) {
		if (children == null) {
			children = new ArrayList<XmlExport>();
		}
		children.add(child);
	}

	public void addItem(XmlExportItem item) {
		if (items == null) {
			items = new ArrayList<XmlExportItem>();
		}
		items.add(item);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		XmlExport other = (XmlExport) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getActive() {
		return active;
	}

	public String getBlank() {
		return blank;
	}

	public List<XmlExport> getChildren() {
		return children;
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

	public String getExternalAttrsFlag() {
		return externalAttrsFlag;
	}

	public String getId() {
		return this.id;
	}

	public int getInterval() {
		return this.interval;
	}

	public List<XmlExportItem> getItems() {
		return items;
	}

	public int getLevel() {
		return level;
	}

	public long getNodeId() {
		return this.nodeId;
	}

	public long getNodeParentId() {
		return this.nodeParentId;
	}

	public XmlExport getParent() {
		return parent;
	}

	public Map<String, Object> getParentParameter() {
		return parentParameter;
	}

	public String getResultFlag() {
		return resultFlag;
	}

	public int getSortNo() {
		return sortNo;
	}

	public String getSql() {
		return this.sql;
	}

	public String getSyncFlag() {
		return this.syncFlag;
	}

	public String getTemplateId() {
		return this.templateId;
	}

	public String getTitle() {
		return this.title;
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

	public String getXmlTag() {
		return xmlTag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public XmlExport jsonToObject(JSONObject jsonObject) {
		return XmlExportJsonFactory.jsonToObject(jsonObject);
	}

	public void setActive(String active) {
		this.active = active;
	}

	public void setBlank(String blank) {
		this.blank = blank;
	}

	public void setChildren(List<XmlExport> children) {
		this.children = children;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setExternalAttrsFlag(String externalAttrsFlag) {
		this.externalAttrsFlag = externalAttrsFlag;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public void setItems(List<XmlExportItem> items) {
		this.items = items;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}

	public void setNodeParentId(long nodeParentId) {
		this.nodeParentId = nodeParentId;
	}

	public void setParent(XmlExport parent) {
		this.parent = parent;
	}

	public void setParentParameter(Map<String, Object> parentParameter) {
		this.parentParameter = parentParameter;
	}

	public void setResultFlag(String resultFlag) {
		this.resultFlag = resultFlag;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public void setXmlTag(String xmlTag) {
		this.xmlTag = xmlTag;
	}

	public JSONObject toJsonObject() {
		return XmlExportJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return XmlExportJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}