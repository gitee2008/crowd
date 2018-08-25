package com.glaf.crowd.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class ProductItemQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected List<String> ids;
	protected Collection<String> appActorIds;
	protected String category;
	protected String categoryLike;
	protected List<String> categorys;
	protected String itemName;
	protected String itemNameLike;
	protected List<String> itemNames;
	protected String itemLocation;
	protected String itemLocationLike;
	protected List<String> itemLocations;
	protected String smallUrl;
	protected String smallUrlLike;
	protected List<String> smallUrls;
	protected String itemUrl;
	protected String itemUrlLike;
	protected List<String> itemUrls;
	protected String itemTitle;
	protected String itemTitleLike;
	protected List<String> itemTitles;
	protected String itemContent;
	protected String itemContentLike;
	protected List<String> itemContents;
	protected Double itemMoneyGreaterThanOrEqual;
	protected Double itemMoneyLessThanOrEqual;

	public ProductItemQuery() {

	}

	public Collection<String> getAppActorIds() {
		return appActorIds;
	}

	public void setAppActorIds(Collection<String> appActorIds) {
		this.appActorIds = appActorIds;
	}

	public String getCategory() {
		return category;
	}

	public String getCategoryLike() {
		if (categoryLike != null && categoryLike.trim().length() > 0) {
			if (!categoryLike.startsWith("%")) {
				categoryLike = "%" + categoryLike;
			}
			if (!categoryLike.endsWith("%")) {
				categoryLike = categoryLike + "%";
			}
		}
		return categoryLike;
	}

	public List<String> getCategorys() {
		return categorys;
	}

	public String getItemName() {
		return itemName;
	}

	public String getItemNameLike() {
		if (itemNameLike != null && itemNameLike.trim().length() > 0) {
			if (!itemNameLike.startsWith("%")) {
				itemNameLike = "%" + itemNameLike;
			}
			if (!itemNameLike.endsWith("%")) {
				itemNameLike = itemNameLike + "%";
			}
		}
		return itemNameLike;
	}

	public List<String> getItemNames() {
		return itemNames;
	}

	public String getItemLocation() {
		return itemLocation;
	}

	public String getItemLocationLike() {
		if (itemLocationLike != null && itemLocationLike.trim().length() > 0) {
			if (!itemLocationLike.startsWith("%")) {
				itemLocationLike = "%" + itemLocationLike;
			}
			if (!itemLocationLike.endsWith("%")) {
				itemLocationLike = itemLocationLike + "%";
			}
		}
		return itemLocationLike;
	}

	public List<String> getItemLocations() {
		return itemLocations;
	}

	public String getSmallUrl() {
		return smallUrl;
	}

	public String getSmallUrlLike() {
		if (smallUrlLike != null && smallUrlLike.trim().length() > 0) {
			if (!smallUrlLike.startsWith("%")) {
				smallUrlLike = "%" + smallUrlLike;
			}
			if (!smallUrlLike.endsWith("%")) {
				smallUrlLike = smallUrlLike + "%";
			}
		}
		return smallUrlLike;
	}

	public List<String> getSmallUrls() {
		return smallUrls;
	}

	public String getItemUrl() {
		return itemUrl;
	}

	public String getItemUrlLike() {
		if (itemUrlLike != null && itemUrlLike.trim().length() > 0) {
			if (!itemUrlLike.startsWith("%")) {
				itemUrlLike = "%" + itemUrlLike;
			}
			if (!itemUrlLike.endsWith("%")) {
				itemUrlLike = itemUrlLike + "%";
			}
		}
		return itemUrlLike;
	}

	public List<String> getItemUrls() {
		return itemUrls;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public String getItemTitleLike() {
		if (itemTitleLike != null && itemTitleLike.trim().length() > 0) {
			if (!itemTitleLike.startsWith("%")) {
				itemTitleLike = "%" + itemTitleLike;
			}
			if (!itemTitleLike.endsWith("%")) {
				itemTitleLike = itemTitleLike + "%";
			}
		}
		return itemTitleLike;
	}

	public List<String> getItemTitles() {
		return itemTitles;
	}

	public String getItemContent() {
		return itemContent;
	}

	public String getItemContentLike() {
		if (itemContentLike != null && itemContentLike.trim().length() > 0) {
			if (!itemContentLike.startsWith("%")) {
				itemContentLike = "%" + itemContentLike;
			}
			if (!itemContentLike.endsWith("%")) {
				itemContentLike = itemContentLike + "%";
			}
		}
		return itemContentLike;
	}

	public List<String> getItemContents() {
		return itemContents;
	}

	public Double getItemMoneyGreaterThanOrEqual() {
		return itemMoneyGreaterThanOrEqual;
	}

	public Double getItemMoneyLessThanOrEqual() {
		return itemMoneyLessThanOrEqual;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setCategoryLike(String categoryLike) {
		this.categoryLike = categoryLike;
	}

	public void setCategorys(List<String> categorys) {
		this.categorys = categorys;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public void setItemNameLike(String itemNameLike) {
		this.itemNameLike = itemNameLike;
	}

	public void setItemNames(List<String> itemNames) {
		this.itemNames = itemNames;
	}

	public void setItemLocation(String itemLocation) {
		this.itemLocation = itemLocation;
	}

	public void setItemLocationLike(String itemLocationLike) {
		this.itemLocationLike = itemLocationLike;
	}

	public void setItemLocations(List<String> itemLocations) {
		this.itemLocations = itemLocations;
	}

	public void setSmallUrl(String smallUrl) {
		this.smallUrl = smallUrl;
	}

	public void setSmallUrlLike(String smallUrlLike) {
		this.smallUrlLike = smallUrlLike;
	}

	public void setSmallUrls(List<String> smallUrls) {
		this.smallUrls = smallUrls;
	}

	public void setItemUrl(String itemUrl) {
		this.itemUrl = itemUrl;
	}

	public void setItemUrlLike(String itemUrlLike) {
		this.itemUrlLike = itemUrlLike;
	}

	public void setItemUrls(List<String> itemUrls) {
		this.itemUrls = itemUrls;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public void setItemTitleLike(String itemTitleLike) {
		this.itemTitleLike = itemTitleLike;
	}

	public void setItemTitles(List<String> itemTitles) {
		this.itemTitles = itemTitles;
	}

	public void setItemContent(String itemContent) {
		this.itemContent = itemContent;
	}

	public void setItemContentLike(String itemContentLike) {
		this.itemContentLike = itemContentLike;
	}

	public void setItemContents(List<String> itemContents) {
		this.itemContents = itemContents;
	}

	public void setItemMoneyGreaterThanOrEqual(Double itemMoneyGreaterThanOrEqual) {
		this.itemMoneyGreaterThanOrEqual = itemMoneyGreaterThanOrEqual;
	}

	public void setItemMoneyLessThanOrEqual(Double itemMoneyLessThanOrEqual) {
		this.itemMoneyLessThanOrEqual = itemMoneyLessThanOrEqual;
	}

	public ProductItemQuery category(String category) {
		if (category == null) {
			throw new RuntimeException("category is null");
		}
		this.category = category;
		return this;
	}

	public ProductItemQuery categoryLike(String categoryLike) {
		if (categoryLike == null) {
			throw new RuntimeException("category is null");
		}
		this.categoryLike = categoryLike;
		return this;
	}

	public ProductItemQuery categorys(List<String> categorys) {
		if (categorys == null) {
			throw new RuntimeException("categorys is empty ");
		}
		this.categorys = categorys;
		return this;
	}

	public ProductItemQuery itemName(String itemName) {
		if (itemName == null) {
			throw new RuntimeException("itemName is null");
		}
		this.itemName = itemName;
		return this;
	}

	public ProductItemQuery itemNameLike(String itemNameLike) {
		if (itemNameLike == null) {
			throw new RuntimeException("itemName is null");
		}
		this.itemNameLike = itemNameLike;
		return this;
	}

	public ProductItemQuery itemNames(List<String> itemNames) {
		if (itemNames == null) {
			throw new RuntimeException("itemNames is empty ");
		}
		this.itemNames = itemNames;
		return this;
	}

	public ProductItemQuery itemLocation(String itemLocation) {
		if (itemLocation == null) {
			throw new RuntimeException("itemLocation is null");
		}
		this.itemLocation = itemLocation;
		return this;
	}

	public ProductItemQuery itemLocationLike(String itemLocationLike) {
		if (itemLocationLike == null) {
			throw new RuntimeException("itemLocation is null");
		}
		this.itemLocationLike = itemLocationLike;
		return this;
	}

	public ProductItemQuery itemLocations(List<String> itemLocations) {
		if (itemLocations == null) {
			throw new RuntimeException("itemLocations is empty ");
		}
		this.itemLocations = itemLocations;
		return this;
	}

	public ProductItemQuery smallUrl(String smallUrl) {
		if (smallUrl == null) {
			throw new RuntimeException("smallUrl is null");
		}
		this.smallUrl = smallUrl;
		return this;
	}

	public ProductItemQuery smallUrlLike(String smallUrlLike) {
		if (smallUrlLike == null) {
			throw new RuntimeException("smallUrl is null");
		}
		this.smallUrlLike = smallUrlLike;
		return this;
	}

	public ProductItemQuery smallUrls(List<String> smallUrls) {
		if (smallUrls == null) {
			throw new RuntimeException("smallUrls is empty ");
		}
		this.smallUrls = smallUrls;
		return this;
	}

	public ProductItemQuery itemUrl(String itemUrl) {
		if (itemUrl == null) {
			throw new RuntimeException("itemUrl is null");
		}
		this.itemUrl = itemUrl;
		return this;
	}

	public ProductItemQuery itemUrlLike(String itemUrlLike) {
		if (itemUrlLike == null) {
			throw new RuntimeException("itemUrl is null");
		}
		this.itemUrlLike = itemUrlLike;
		return this;
	}

	public ProductItemQuery itemUrls(List<String> itemUrls) {
		if (itemUrls == null) {
			throw new RuntimeException("itemUrls is empty ");
		}
		this.itemUrls = itemUrls;
		return this;
	}

	public ProductItemQuery itemTitle(String itemTitle) {
		if (itemTitle == null) {
			throw new RuntimeException("itemTitle is null");
		}
		this.itemTitle = itemTitle;
		return this;
	}

	public ProductItemQuery itemTitleLike(String itemTitleLike) {
		if (itemTitleLike == null) {
			throw new RuntimeException("itemTitle is null");
		}
		this.itemTitleLike = itemTitleLike;
		return this;
	}

	public ProductItemQuery itemTitles(List<String> itemTitles) {
		if (itemTitles == null) {
			throw new RuntimeException("itemTitles is empty ");
		}
		this.itemTitles = itemTitles;
		return this;
	}

	public ProductItemQuery itemContent(String itemContent) {
		if (itemContent == null) {
			throw new RuntimeException("itemContent is null");
		}
		this.itemContent = itemContent;
		return this;
	}

	public ProductItemQuery itemContentLike(String itemContentLike) {
		if (itemContentLike == null) {
			throw new RuntimeException("itemContent is null");
		}
		this.itemContentLike = itemContentLike;
		return this;
	}

	public ProductItemQuery itemContents(List<String> itemContents) {
		if (itemContents == null) {
			throw new RuntimeException("itemContents is empty ");
		}
		this.itemContents = itemContents;
		return this;
	}

	public ProductItemQuery itemMoneyGreaterThanOrEqual(Double itemMoneyGreaterThanOrEqual) {
		if (itemMoneyGreaterThanOrEqual == null) {
			throw new RuntimeException("itemMoney is null");
		}
		this.itemMoneyGreaterThanOrEqual = itemMoneyGreaterThanOrEqual;
		return this;
	}

	public ProductItemQuery itemMoneyLessThanOrEqual(Double itemMoneyLessThanOrEqual) {
		if (itemMoneyLessThanOrEqual == null) {
			throw new RuntimeException("itemMoney is null");
		}
		this.itemMoneyLessThanOrEqual = itemMoneyLessThanOrEqual;
		return this;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("tenantId".equals(sortColumn)) {
				orderBy = "E.TENANTID_" + a_x;
			}

			if ("category".equals(sortColumn)) {
				orderBy = "E.CATEGORY_" + a_x;
			}

			if ("itemName".equals(sortColumn)) {
				orderBy = "E.ITEMNAME_" + a_x;
			}

			if ("itemLocation".equals(sortColumn)) {
				orderBy = "E.ITEMLOCATION_" + a_x;
			}

			if ("smallUrl".equals(sortColumn)) {
				orderBy = "E.SMALLURL_" + a_x;
			}

			if ("itemUrl".equals(sortColumn)) {
				orderBy = "E.ITEMURL_" + a_x;
			}

			if ("itemTitle".equals(sortColumn)) {
				orderBy = "E.ITEMTITLE_" + a_x;
			}

			if ("itemContent".equals(sortColumn)) {
				orderBy = "E.ITEMCONTENT_" + a_x;
			}

			if ("itemStatus".equals(sortColumn)) {
				orderBy = "E.ITEMSTATUS_" + a_x;
			}

			if ("itemMoney".equals(sortColumn)) {
				orderBy = "E.ITEMMONEY_" + a_x;
			}

			if ("itemDay".equals(sortColumn)) {
				orderBy = "E.ITEMDAY_" + a_x;
			}

			if ("lastModified".equals(sortColumn)) {
				orderBy = "E.LASTMODIFIED_" + a_x;
			}

			if ("createBy".equals(sortColumn)) {
				orderBy = "E.CREATEBY_" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME_" + a_x;
			}

			if ("updateBy".equals(sortColumn)) {
				orderBy = "E.UPDATEBY_" + a_x;
			}

			if ("updateTime".equals(sortColumn)) {
				orderBy = "E.UPDATETIME_" + a_x;
			}

		}
		return orderBy;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("tenantId", "TENANTID_");
		addColumn("category", "CATEGORY_");
		addColumn("itemName", "ITEMNAME_");
		addColumn("itemLocation", "ITEMLOCATION_");
		addColumn("smallUrl", "SMALLURL_");
		addColumn("itemUrl", "ITEMURL_");
		addColumn("itemTitle", "ITEMTITLE_");
		addColumn("itemContent", "ITEMCONTENT_");
		addColumn("itemStatus", "ITEMSTATUS_");
		addColumn("itemMoney", "ITEMMONEY_");
		addColumn("itemDay", "ITEMDAY_");
		addColumn("lastModified", "LASTMODIFIED_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

}