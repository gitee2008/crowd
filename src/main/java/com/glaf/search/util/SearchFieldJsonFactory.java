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

package com.glaf.search.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.util.DateUtils;
import com.glaf.search.domain.SearchField;

/**
 * 
 * JSON工厂类
 *
 */
public class SearchFieldJsonFactory {

	public static SearchField jsonToObject(JSONObject jsonObject) {
		SearchField model = new SearchField();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getString("id"));
		}
		if (jsonObject.containsKey("serverId")) {
			model.setServerId(jsonObject.getString("serverId"));
		}
		if (jsonObject.containsKey("code")) {
			model.setCode(jsonObject.getString("code"));
		}
		if (jsonObject.containsKey("mappingCode")) {
			model.setMappingCode(jsonObject.getString("mappingCode"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("format")) {
			model.setFormat(jsonObject.getString("format"));
		}
		if (jsonObject.containsKey("indexAnalyzer")) {
			model.setIndexAnalyzer(jsonObject.getString("indexAnalyzer"));
		}
		if (jsonObject.containsKey("fieldAnalyzer")) {
			model.setFieldAnalyzer(jsonObject.getString("fieldAnalyzer"));
		}
		if (jsonObject.containsKey("termVector")) {
			model.setTermVector(jsonObject.getString("termVector"));
		}
		if (jsonObject.containsKey("analyzerFlag")) {
			model.setAnalyzerFlag(jsonObject.getString("analyzerFlag"));
		}
		if (jsonObject.containsKey("searchReturnFlag")) {
			model.setSearchReturnFlag(jsonObject.getString("searchReturnFlag"));
		}
		if (jsonObject.containsKey("locked")) {
			model.setLocked(jsonObject.getInteger("locked"));
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

	public static JSONObject toJsonObject(SearchField model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getServerId() != null) {
			jsonObject.put("serverId", model.getServerId());
		}
		if (model.getCode() != null) {
			jsonObject.put("code", model.getCode());
		}
		if (model.getMappingCode() != null) {
			jsonObject.put("mappingCode", model.getMappingCode());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getFormat() != null) {
			jsonObject.put("format", model.getFormat());
		}
		if (model.getIndexAnalyzer() != null) {
			jsonObject.put("indexAnalyzer", model.getIndexAnalyzer());
		}
		if (model.getFieldAnalyzer() != null) {
			jsonObject.put("fieldAnalyzer", model.getFieldAnalyzer());
		}
		if (model.getTermVector() != null) {
			jsonObject.put("termVector", model.getTermVector());
		}
		if (model.getAnalyzerFlag() != null) {
			jsonObject.put("analyzerFlag", model.getAnalyzerFlag());
		}
		if (model.getSearchReturnFlag() != null) {
			jsonObject.put("searchReturnFlag", model.getSearchReturnFlag());
		}
		jsonObject.put("locked", model.getLocked());
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

	public static ObjectNode toObjectNode(SearchField model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getServerId() != null) {
			jsonObject.put("serverId", model.getServerId());
		}
		if (model.getCode() != null) {
			jsonObject.put("code", model.getCode());
		}
		if (model.getMappingCode() != null) {
			jsonObject.put("mappingCode", model.getMappingCode());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getFormat() != null) {
			jsonObject.put("format", model.getFormat());
		}
		if (model.getIndexAnalyzer() != null) {
			jsonObject.put("indexAnalyzer", model.getIndexAnalyzer());
		}
		if (model.getFieldAnalyzer() != null) {
			jsonObject.put("fieldAnalyzer", model.getFieldAnalyzer());
		}
		if (model.getTermVector() != null) {
			jsonObject.put("termVector", model.getTermVector());
		}
		if (model.getAnalyzerFlag() != null) {
			jsonObject.put("analyzerFlag", model.getAnalyzerFlag());
		}
		if (model.getSearchReturnFlag() != null) {
			jsonObject.put("searchReturnFlag", model.getSearchReturnFlag());
		}
		jsonObject.put("locked", model.getLocked());
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

	public static JSONArray listToArray(java.util.List<SearchField> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (SearchField model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<SearchField> arrayToList(JSONArray array) {
		java.util.List<SearchField> list = new java.util.ArrayList<SearchField>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			SearchField model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private SearchFieldJsonFactory() {

	}

}
