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
import com.glaf.search.domain.SearchServer;

/**
 * 
 * JSON工厂类
 *
 */
public class SearchServerJsonFactory {

	public static SearchServer jsonToObject(JSONObject jsonObject) {
		SearchServer model = new SearchServer();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getString("id"));
		}
		if (jsonObject.containsKey("serverURL")) {
			model.setServerURL(jsonObject.getString("serverURL"));
		}
		if (jsonObject.containsKey("indexName")) {
			model.setIndexName(jsonObject.getString("indexName"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("tokenizer")) {
			model.setTokenizer(jsonObject.getString("tokenizer"));
		}
		if (jsonObject.containsKey("tokenFilter")) {
			model.setTokenFilter(jsonObject.getString("tokenFilter"));
		}
		if (jsonObject.containsKey("characterFilter")) {
			model.setCharacterFilter(jsonObject.getString("characterFilter"));
		}
		if (jsonObject.containsKey("analyzerName")) {
			model.setAnalyzerName(jsonObject.getString("analyzerName"));
		}
		if (jsonObject.containsKey("analyzerType")) {
			model.setAnalyzerType(jsonObject.getString("analyzerType"));
		}
		if (jsonObject.containsKey("filter")) {
			model.setFilter(jsonObject.getString("filter"));
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

	public static JSONObject toJsonObject(SearchServer model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getServerURL() != null) {
			jsonObject.put("serverURL", model.getServerURL());
		}
		if (model.getIndexName() != null) {
			jsonObject.put("indexName", model.getIndexName());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getTokenizer() != null) {
			jsonObject.put("tokenizer", model.getTokenizer());
		}
		if (model.getTokenFilter() != null) {
			jsonObject.put("tokenFilter", model.getTokenFilter());
		}
		if (model.getCharacterFilter() != null) {
			jsonObject.put("characterFilter", model.getCharacterFilter());
		}
		if (model.getAnalyzerName() != null) {
			jsonObject.put("analyzerName", model.getAnalyzerName());
		}
		if (model.getAnalyzerType() != null) {
			jsonObject.put("analyzerType", model.getAnalyzerType());
		}
		if (model.getFilter() != null) {
			jsonObject.put("filter", model.getFilter());
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

	public static ObjectNode toObjectNode(SearchServer model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getServerURL() != null) {
			jsonObject.put("serverURL", model.getServerURL());
		}
		if (model.getIndexName() != null) {
			jsonObject.put("indexName", model.getIndexName());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getTokenizer() != null) {
			jsonObject.put("tokenizer", model.getTokenizer());
		}
		if (model.getTokenFilter() != null) {
			jsonObject.put("tokenFilter", model.getTokenFilter());
		}
		if (model.getCharacterFilter() != null) {
			jsonObject.put("characterFilter", model.getCharacterFilter());
		}
		if (model.getAnalyzerName() != null) {
			jsonObject.put("analyzerName", model.getAnalyzerName());
		}
		if (model.getAnalyzerType() != null) {
			jsonObject.put("analyzerType", model.getAnalyzerType());
		}
		if (model.getFilter() != null) {
			jsonObject.put("filter", model.getFilter());
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

	public static JSONArray listToArray(java.util.List<SearchServer> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (SearchServer model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<SearchServer> arrayToList(JSONArray array) {
		java.util.List<SearchServer> list = new java.util.ArrayList<SearchServer>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			SearchServer model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private SearchServerJsonFactory() {

	}

}
