package com.glaf.crowd.domain;

import java.io.*;
import java.util.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.util.DateUtils;
import com.glaf.crowd.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "T_PRODUCT_ITEM")
public class ProductItem implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", length = 64, nullable = false)
	protected String id;

	/**
	 * 租户编号
	 */
	@Column(name = "TENANTID_", length = 50)
	protected String tenantId;

	/**
	 * 分类
	 */
	@Column(name = "CATEGORY_", length = 50)
	protected String category;

	/**
	 * 发起人
	 */
	@Column(name = "ITEMNAME_", length = 100)
	protected String itemName;

	/**
	 * 地址
	 */
	@Column(name = "ITEMLOCATION_", length = 200)
	protected String itemLocation;

	/**
	 * 小图片地址
	 */
	@Column(name = "SMALLURL_", length = 250)
	protected String smallUrl;

	/**
	 * 图片地址
	 */
	@Column(name = "ITEMURL_", length = 250)
	protected String itemUrl;

	/**
	 * 标题
	 */
	@Column(name = "ITEMTITLE_", length = 250)
	protected String itemTitle;

	/**
	 * 内容描述
	 */
	@Column(name = "ITEMCONTENT_", length = 4000)
	protected String itemContent;

	/**
	 * 进度
	 */
	@Column(name = "ITEMSTATUS_")
	protected int itemStatus;

	/**
	 * 金额
	 */
	@Column(name = "ITEMMONEY_")
	protected double itemMoney;

	/**
	 * 有效天数
	 */
	@Column(name = "ITEMDAY_")
	protected int itemDay;

	/**
	 * 最后修改时间戳
	 */
	@Column(name = "LASTMODIFIED_")
	protected long lastModified;

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

	public ProductItem() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductItem other = (ProductItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getCategory() {
		return this.category;
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

	public String getId() {
		return this.id;
	}

	public String getItemContent() {
		return this.itemContent;
	}

	public int getItemDay() {
		return this.itemDay;
	}

	public String getItemLocation() {
		return this.itemLocation;
	}

	public double getItemMoney() {
		return this.itemMoney;
	}

	public String getItemName() {
		return this.itemName;
	}

	public int getItemStatus() {
		return this.itemStatus;
	}

	public String getItemTitle() {
		return this.itemTitle;
	}

	public String getItemUrl() {
		return this.itemUrl;
	}

	public long getLastModified() {
		return this.lastModified;
	}

	public String getSmallUrl() {
		return this.smallUrl;
	}

	public String getTenantId() {
		return this.tenantId;
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

	public ProductItem jsonToObject(JSONObject jsonObject) {
		return ProductItemJsonFactory.jsonToObject(jsonObject);
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setItemContent(String itemContent) {
		this.itemContent = itemContent;
	}

	public void setItemDay(int itemDay) {
		this.itemDay = itemDay;
	}

	public void setItemLocation(String itemLocation) {
		this.itemLocation = itemLocation;
	}

	public void setItemMoney(double itemMoney) {
		this.itemMoney = itemMoney;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public void setItemStatus(int itemStatus) {
		this.itemStatus = itemStatus;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public void setItemUrl(String itemUrl) {
		this.itemUrl = itemUrl;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	public void setSmallUrl(String smallUrl) {
		this.smallUrl = smallUrl;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public JSONObject toJsonObject() {
		return ProductItemJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return ProductItemJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
