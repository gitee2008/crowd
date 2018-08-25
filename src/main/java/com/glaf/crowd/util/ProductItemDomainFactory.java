package com.glaf.crowd.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.glaf.core.base.DataRequest;
import com.glaf.core.base.DataRequest.FilterDescriptor;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.util.DBUtils;

/**
 * 
 * 实体数据工厂类
 *
 */
public class ProductItemDomainFactory {

	public static final String TABLENAME = "T_PRODUCT_ITEM";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("category", "CATEGORY_");
		columnMap.put("itemName", "ITEMNAME_");
		columnMap.put("itemLocation", "ITEMLOCATION_");
		columnMap.put("smallUrl", "SMALLURL_");
		columnMap.put("itemUrl", "ITEMURL_");
		columnMap.put("itemTitle", "ITEMTITLE_");
		columnMap.put("itemContent", "ITEMCONTENT_");
		columnMap.put("itemStatus", "ITEMSTATUS_");
		columnMap.put("itemMoney", "ITEMMONEY_");
		columnMap.put("itemDay", "ITEMDAY_");
		columnMap.put("lastModified", "LASTMODIFIED_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");
		columnMap.put("updateBy", "UPDATEBY_");
		columnMap.put("updateTime", "UPDATETIME_");

		javaTypeMap.put("id", "String");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("category", "String");
		javaTypeMap.put("itemName", "String");
		javaTypeMap.put("itemLocation", "String");
		javaTypeMap.put("smallUrl", "String");
		javaTypeMap.put("itemUrl", "String");
		javaTypeMap.put("itemTitle", "String");
		javaTypeMap.put("itemContent", "String");
		javaTypeMap.put("itemStatus", "Integer");
		javaTypeMap.put("itemMoney", "Double");
		javaTypeMap.put("itemDay", "Integer");
		javaTypeMap.put("lastModified", "Long");
		javaTypeMap.put("createBy", "String");
		javaTypeMap.put("createTime", "Date");
		javaTypeMap.put("updateBy", "String");
		javaTypeMap.put("updateTime", "Date");
	}

	public static Map<String, String> getColumnMap() {
		return columnMap;
	}

	public static Map<String, String> getJavaTypeMap() {
		return javaTypeMap;
	}

	public static TableDefinition getTableDefinition() {
		return getTableDefinition(TABLENAME);
	}

	public static TableDefinition getTableDefinition(String tableName) {
		tableName = tableName.toUpperCase();
		TableDefinition tableDefinition = new TableDefinition();
		tableDefinition.setTableName(tableName);
		tableDefinition.setName("ProductItem");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("String");
		idColumn.setLength(64);
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition tenantId = new ColumnDefinition();
		tenantId.setName("tenantId");
		tenantId.setColumnName("TENANTID_");
		tenantId.setJavaType("String");
		tenantId.setLength(50);
		tableDefinition.addColumn(tenantId);

		ColumnDefinition category = new ColumnDefinition();
		category.setName("category");
		category.setColumnName("CATEGORY_");
		category.setJavaType("String");
		category.setLength(50);
		tableDefinition.addColumn(category);

		ColumnDefinition itemName = new ColumnDefinition();
		itemName.setName("itemName");
		itemName.setColumnName("ITEMNAME_");
		itemName.setJavaType("String");
		itemName.setLength(100);
		tableDefinition.addColumn(itemName);

		ColumnDefinition itemLocation = new ColumnDefinition();
		itemLocation.setName("itemLocation");
		itemLocation.setColumnName("ITEMLOCATION_");
		itemLocation.setJavaType("String");
		itemLocation.setLength(200);
		tableDefinition.addColumn(itemLocation);

		ColumnDefinition smallUrl = new ColumnDefinition();
		smallUrl.setName("smallUrl");
		smallUrl.setColumnName("SMALLURL_");
		smallUrl.setJavaType("String");
		smallUrl.setLength(250);
		tableDefinition.addColumn(smallUrl);

		ColumnDefinition itemUrl = new ColumnDefinition();
		itemUrl.setName("itemUrl");
		itemUrl.setColumnName("ITEMURL_");
		itemUrl.setJavaType("String");
		itemUrl.setLength(250);
		tableDefinition.addColumn(itemUrl);

		ColumnDefinition itemTitle = new ColumnDefinition();
		itemTitle.setName("itemTitle");
		itemTitle.setColumnName("ITEMTITLE_");
		itemTitle.setJavaType("String");
		itemTitle.setLength(250);
		tableDefinition.addColumn(itemTitle);

		ColumnDefinition itemContent = new ColumnDefinition();
		itemContent.setName("itemContent");
		itemContent.setColumnName("ITEMCONTENT_");
		itemContent.setJavaType("String");
		itemContent.setLength(4000);
		tableDefinition.addColumn(itemContent);

		ColumnDefinition itemStatus = new ColumnDefinition();
		itemStatus.setName("itemStatus");
		itemStatus.setColumnName("ITEMSTATUS_");
		itemStatus.setJavaType("Integer");
		tableDefinition.addColumn(itemStatus);

		ColumnDefinition itemMoney = new ColumnDefinition();
		itemMoney.setName("itemMoney");
		itemMoney.setColumnName("ITEMMONEY_");
		itemMoney.setJavaType("Double");
		tableDefinition.addColumn(itemMoney);

		ColumnDefinition itemDay = new ColumnDefinition();
		itemDay.setName("itemDay");
		itemDay.setColumnName("ITEMDAY_");
		itemDay.setJavaType("Integer");
		tableDefinition.addColumn(itemDay);

		ColumnDefinition lastModified = new ColumnDefinition();
		lastModified.setName("lastModified");
		lastModified.setColumnName("LASTMODIFIED_");
		lastModified.setJavaType("Long");
		tableDefinition.addColumn(lastModified);

		ColumnDefinition createBy = new ColumnDefinition();
		createBy.setName("createBy");
		createBy.setColumnName("CREATEBY_");
		createBy.setJavaType("String");
		createBy.setLength(50);
		tableDefinition.addColumn(createBy);

		ColumnDefinition createTime = new ColumnDefinition();
		createTime.setName("createTime");
		createTime.setColumnName("CREATETIME_");
		createTime.setJavaType("Date");
		tableDefinition.addColumn(createTime);

		ColumnDefinition updateBy = new ColumnDefinition();
		updateBy.setName("updateBy");
		updateBy.setColumnName("UPDATEBY_");
		updateBy.setJavaType("String");
		updateBy.setLength(50);
		tableDefinition.addColumn(updateBy);

		ColumnDefinition updateTime = new ColumnDefinition();
		updateTime.setName("updateTime");
		updateTime.setColumnName("UPDATETIME_");
		updateTime.setJavaType("Date");
		tableDefinition.addColumn(updateTime);

		return tableDefinition;
	}

	public static TableDefinition createTable() {
		TableDefinition tableDefinition = getTableDefinition(TABLENAME);
		if (!DBUtils.tableExists(TABLENAME)) {
			DBUtils.createTable(tableDefinition);
		} else {
			DBUtils.alterTable(tableDefinition);
		}
		return tableDefinition;
	}

	public static TableDefinition createTable(String tableName) {
		TableDefinition tableDefinition = getTableDefinition(tableName);
		if (!DBUtils.tableExists(tableName)) {
			DBUtils.createTable(tableDefinition);
		} else {
			DBUtils.alterTable(tableDefinition);
		}
		return tableDefinition;
	}

	public static void processDataRequest(DataRequest dataRequest) {
		if (dataRequest != null) {
			if (dataRequest.getFilter() != null) {
				if (dataRequest.getFilter().getField() != null) {
					dataRequest.getFilter().setColumn(columnMap.get(dataRequest.getFilter().getField()));
					dataRequest.getFilter().setJavaType(javaTypeMap.get(dataRequest.getFilter().getField()));
				}

				List<FilterDescriptor> filters = dataRequest.getFilter().getFilters();
				for (FilterDescriptor filter : filters) {
					filter.setParent(dataRequest.getFilter());
					if (filter.getField() != null) {
						filter.setColumn(columnMap.get(filter.getField()));
						filter.setJavaType(javaTypeMap.get(filter.getField()));
					}

					List<FilterDescriptor> subFilters = filter.getFilters();
					for (FilterDescriptor f : subFilters) {
						f.setColumn(columnMap.get(f.getField()));
						f.setJavaType(javaTypeMap.get(f.getField()));
						f.setParent(filter);
					}
				}
			}
		}
	}

	private ProductItemDomainFactory() {

	}

}
