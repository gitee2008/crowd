package com.glaf.crowd.util;

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.glaf.core.util.DateUtils;
import com.glaf.crowd.domain.*;

/**
 * 
 * JSON工厂类
 *
 */
public class ProductItemJsonFactory {

	public static ProductItem jsonToObject(JSONObject jsonObject) {
		ProductItem model = new ProductItem();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getString("id"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("category")) {
			model.setCategory(jsonObject.getString("category"));
		}
		if (jsonObject.containsKey("itemName")) {
			model.setItemName(jsonObject.getString("itemName"));
		}
		if (jsonObject.containsKey("itemLocation")) {
			model.setItemLocation(jsonObject.getString("itemLocation"));
		}
		if (jsonObject.containsKey("smallUrl")) {
			model.setSmallUrl(jsonObject.getString("smallUrl"));
		}
		if (jsonObject.containsKey("itemUrl")) {
			model.setItemUrl(jsonObject.getString("itemUrl"));
		}
		if (jsonObject.containsKey("itemTitle")) {
			model.setItemTitle(jsonObject.getString("itemTitle"));
		}
		if (jsonObject.containsKey("itemContent")) {
			model.setItemContent(jsonObject.getString("itemContent"));
		}
		if (jsonObject.containsKey("itemStatus")) {
			model.setItemStatus(jsonObject.getInteger("itemStatus"));
		}
		if (jsonObject.containsKey("itemMoney")) {
			model.setItemMoney(jsonObject.getDouble("itemMoney"));
		}
		if (jsonObject.containsKey("itemDay")) {
			model.setItemDay(jsonObject.getInteger("itemDay"));
		}
		if (jsonObject.containsKey("lastModified")) {
			model.setLastModified(jsonObject.getLong("lastModified"));
		}
		if (jsonObject.containsKey("createBy")) {
			model.setCreateBy(jsonObject.getString("createBy"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}
		if (jsonObject.containsKey("updateBy")) {
			model.setUpdateBy(jsonObject.getString("updateBy"));
		}
		if (jsonObject.containsKey("updateTime")) {
			model.setUpdateTime(jsonObject.getDate("updateTime"));
		}

		return model;
	}

	public static JSONObject toJsonObject(ProductItem model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getCategory() != null) {
			jsonObject.put("category", model.getCategory());
		}
		if (model.getItemName() != null) {
			jsonObject.put("itemName", model.getItemName());
		}
		if (model.getItemLocation() != null) {
			jsonObject.put("itemLocation", model.getItemLocation());
		}
		if (model.getSmallUrl() != null) {
			jsonObject.put("smallUrl", model.getSmallUrl());
		}
		if (model.getItemUrl() != null) {
			jsonObject.put("itemUrl", model.getItemUrl());
		}
		if (model.getItemTitle() != null) {
			jsonObject.put("itemTitle", model.getItemTitle());
		}
		if (model.getItemContent() != null) {
			jsonObject.put("itemContent", model.getItemContent());
		}
		jsonObject.put("itemStatus", model.getItemStatus());
		jsonObject.put("itemMoney", model.getItemMoney());
		jsonObject.put("itemDay", model.getItemDay());
		jsonObject.put("lastModified", model.getLastModified());
		if (model.getCreateBy() != null) {
			jsonObject.put("createBy", model.getCreateBy());
		}
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		if (model.getUpdateBy() != null) {
			jsonObject.put("updateBy", model.getUpdateBy());
		}
		if (model.getUpdateTime() != null) {
			jsonObject.put("updateTime", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_date", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_datetime", DateUtils.getDateTime(model.getUpdateTime()));
		}
		return jsonObject;
	}
	
	public static JSONObject toJsonObject2(ProductItem model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());

		if (model.getCategory() != null) {
			jsonObject.put("category", model.getCategory());
		}
		if (model.getItemName() != null) {
			jsonObject.put("itemName", model.getItemName());
		}
		if (model.getItemLocation() != null) {
			jsonObject.put("itemLocation", model.getItemLocation());
		}
		if (model.getSmallUrl() != null) {
			jsonObject.put("smallUrl", model.getSmallUrl());
		}
		if (model.getItemUrl() != null) {
			jsonObject.put("itemUrl", model.getItemUrl());
		}
		if (model.getItemTitle() != null) {
			jsonObject.put("itemTitle", model.getItemTitle());
		}
		if (model.getItemContent() != null) {
			jsonObject.put("itemContent", model.getItemContent());
		}
		jsonObject.put("itemStatus", model.getItemStatus());
		jsonObject.put("itemMoney", model.getItemMoney());
		jsonObject.put("itemDay", model.getItemDay());
		jsonObject.put("lastModified", model.getLastModified());
		
		return jsonObject;
	}

	public static ObjectNode toObjectNode(ProductItem model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getCategory() != null) {
			jsonObject.put("category", model.getCategory());
		}
		if (model.getItemName() != null) {
			jsonObject.put("itemName", model.getItemName());
		}
		if (model.getItemLocation() != null) {
			jsonObject.put("itemLocation", model.getItemLocation());
		}
		if (model.getSmallUrl() != null) {
			jsonObject.put("smallUrl", model.getSmallUrl());
		}
		if (model.getItemUrl() != null) {
			jsonObject.put("itemUrl", model.getItemUrl());
		}
		if (model.getItemTitle() != null) {
			jsonObject.put("itemTitle", model.getItemTitle());
		}
		if (model.getItemContent() != null) {
			jsonObject.put("itemContent", model.getItemContent());
		}
		jsonObject.put("itemStatus", model.getItemStatus());
		jsonObject.put("itemMoney", model.getItemMoney());
		jsonObject.put("itemDay", model.getItemDay());
		jsonObject.put("lastModified", model.getLastModified());
		if (model.getCreateBy() != null) {
			jsonObject.put("createBy", model.getCreateBy());
		}
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		if (model.getUpdateBy() != null) {
			jsonObject.put("updateBy", model.getUpdateBy());
		}
		if (model.getUpdateTime() != null) {
			jsonObject.put("updateTime", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_date", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_datetime", DateUtils.getDateTime(model.getUpdateTime()));
		}
		return jsonObject;
	}

	public static JSONArray listToArray(java.util.List<ProductItem> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (ProductItem model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<ProductItem> arrayToList(JSONArray array) {
		java.util.List<ProductItem> list = new java.util.ArrayList<ProductItem>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			ProductItem model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private ProductItemJsonFactory() {

	}

}
