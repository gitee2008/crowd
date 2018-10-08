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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.glaf.search.domain.SearchField;
import com.glaf.search.domain.SearchServer;

public class ElasticSearchUtils {
	private static final Log logger = LogFactory.getLog(ElasticSearchUtils.class);

	/**
	 * 创建文档
	 * 
	 * @param serverURL 服务地址
	 * @param indexName 索引名称，相当于数据库名称
	 * @param type      索引类型，相当于数据库中的表名
	 * @param id        id名称，相当于每个表中某一行记录的标识
	 * @param json      json数据
	 */
	public static void createDocument(String serverURL, String indexName, String type, String id, String json) {
		HttpClient httpClient = new HttpClient();
		String url = serverURL + "/" + indexName + "/" + type + "/" + id;
		PutMethod putMethod = new PutMethod(url);
		RequestEntity requestEntity = null;
		try {
			requestEntity = new StringRequestEntity(json, "application/json", "UTF-8");
			putMethod.setRequestEntity(requestEntity);
			int statusCode = httpClient.executeMethod(putMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("method failed: " + putMethod.getStatusLine());
			} else {
				String responseBody = putMethod.getResponseBodyAsString();
				JSONObject responseJson = JSONObject.parseObject(responseBody);
				if (responseJson != null && "true".equals(responseJson.getString("created"))) {
					logger.debug("create index " + indexName + "/" + type + " success!");
				} else {
					logger.debug("create index " + indexName + "/" + type + " failure!");
				}
			}
		} catch (IOException ex) {
			logger.error("method error: " + ex.getMessage());
		} finally {
			putMethod.releaseConnection();
		}
	}

	/**
	 * 创建索引
	 * 
	 * @param searchServer
	 * @throws IOException
	 */
	public static void createIndex(SearchServer searchServer) throws IOException {
		HttpClient httpClient = new HttpClient();
		String server = searchServer.getServerURL();
		String indexName = searchServer.getIndexName();
		String url = server + "/" + indexName + "/";
		JSONObject analysisJson = new JSONObject();
		PutMethod putMethod = new PutMethod(url);
		RequestEntity requestEntity = null;
		try {
			analysisJson = generatorAnalysisJSON(searchServer);
			String jsonData = analysisJson.toJSONString();
			requestEntity = new StringRequestEntity(jsonData, "application/json", "UTF-8");
			putMethod.setRequestEntity(requestEntity);

			int statusCode = httpClient.executeMethod(putMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("method failed: " + putMethod.getStatusLine());
			} else {
				String responseBody = putMethod.getResponseBodyAsString();
				JSONObject responseJson = JSONObject.parseObject(responseBody);
				if (responseJson != null && "true".equals(responseJson.getString("created"))) {
					logger.debug("create index " + indexName + " success!");
				} else {
					logger.debug("create index " + indexName + " failure!");
				}
			}
		} catch (IOException ex) {
			logger.error("method error: " + ex.getMessage());
			throw ex;
		} finally {
			putMethod.releaseConnection();
		}
	}

	/**
	 * 创建映射
	 * 
	 * @param searchServer
	 * @throws IOException
	 */
	public static void createMapping(SearchServer searchServer) throws IOException {
		HttpClient httpClient = new HttpClient();
		String server = searchServer.getServerURL();
		String indexName = searchServer.getIndexName();
		String type = searchServer.getType();
		String url = server + "/" + indexName + "/" + type + "/_mapping";
		JSONObject analysisJson = new JSONObject();
		PutMethod putMethod = new PutMethod(url);
		RequestEntity requestEntity = null;
		try {
			analysisJson = generatorMappingJSON(searchServer);
			String jsonData = analysisJson.toJSONString();
			requestEntity = new StringRequestEntity(jsonData, "application/json", "UTF-8");
			putMethod.setRequestEntity(requestEntity);

			int statusCode = httpClient.executeMethod(putMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("method failed: " + putMethod.getStatusLine());
			} else {
				String responseBody = putMethod.getResponseBodyAsString();
				JSONObject responseJson = JSONObject.parseObject(responseBody);
				if (responseJson != null && "true".equals(responseJson.getString("created"))) {
					logger.debug("create createMapping " + indexName + "/" + type + " success!");
				} else {
					logger.debug("create createMapping " + indexName + "/" + type + " failure!");
				}
			}
		} catch (IOException ex) {
			logger.error("method error: " + ex.getMessage());
			throw ex;
		} finally {
			putMethod.releaseConnection();
		}
	}

	/**
	 * 删除文档
	 * 
	 * @param serverURL 服务地址
	 * @param indexName
	 * @param type
	 * @param id
	 */
	public static void deleteDocument(String serverURL, String indexName, String type, String id) {
		HttpClient httpClient = new HttpClient();
		String url = serverURL + "/" + indexName + "/" + type + "/" + id;
		DeleteMethod deleteMethod = new DeleteMethod(url);
		try {
			int statusCode = httpClient.executeMethod(deleteMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("method failed: " + deleteMethod.getStatusLine());
			} else {
				String responseBody = deleteMethod.getResponseBodyAsString();
				JSONObject responseJson = JSONObject.parseObject(responseBody);
				if (responseJson != null && "false".equals(responseJson.getString("deleted"))) {
					logger.debug("delete index " + indexName + "/" + type + " success!");
				} else {
					logger.debug("delete index " + indexName + "/" + type + " failure!");
				}
			}
		} catch (IOException ex) {
			logger.error("method error: " + ex.getMessage());
		} finally {
			deleteMethod.releaseConnection();
		}
	}

	/**
	 * 删除索引
	 * 
	 * @param serverURL 服务地址
	 * @param indexName
	 */
	public static void deleteIndex(String serverURL, String indexName) {
		HttpClient httpClient = new HttpClient();
		String url = serverURL + "/" + indexName;
		DeleteMethod deleteMethod = new DeleteMethod(url);
		try {
			int statusCode = httpClient.executeMethod(deleteMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: " + deleteMethod.getStatusLine());
			} else {
				String responseBody = deleteMethod.getResponseBodyAsString();
				JSONObject responseJson = JSONObject.parseObject(responseBody);
				if (responseJson != null && "false".equals(responseJson.getString("deleted"))) {
					logger.debug("delete index " + indexName + " success!");
				} else {
					logger.debug("delete index " + indexName + " failure!");
				}
			}
		} catch (IOException e) {
			logger.error("Method error: " + e.getMessage());
		} finally {
			deleteMethod.releaseConnection();
		}
	}

	/**
	 * 生成索引分词配置
	 * 
	 * @param searchServer
	 * @return
	 */
	public static JSONObject generatorAnalysisJSON(SearchServer searchServer) {
		JSONObject analysisAllJson = new JSONObject();
		JSONObject analysisJson = new JSONObject();
		JSONObject analyzerJson = new JSONObject();
		JSONObject analyzerDetailJson = new JSONObject();
		String analyzerName = searchServer.getAnalyzerName();
		if (StringUtils.isEmpty(analyzerName)) {
			analyzerName = "ik_pinyin_analyzer";
		}
		String analyzerType = searchServer.getAnalyzerType();
		String tokenizer = searchServer.getTokenizer();
		String tokenFilter = searchServer.getTokenFilter();
		String characterFilter = searchServer.getCharacterFilter();
		if (StringUtils.isEmpty(analyzerType)) {
			analyzerType = "custom";
		}
		analyzerDetailJson.put("type", analyzerType);
		if (StringUtils.isEmpty(tokenizer)) {
			analyzerType = "ik_smart";
		}
		analyzerDetailJson.put("tokenizer", tokenizer);
		if (StringUtils.isEmpty(tokenFilter)) {
			tokenFilter = "my_pinyin,word_delimiter";
		}
		analyzerDetailJson.put("filter", tokenFilter.split(","));
		if (StringUtils.isNotEmpty(characterFilter)) {
			analyzerDetailJson.put("char_filter", characterFilter);
		}
		analyzerJson.put(analyzerName, analyzerDetailJson);
		analysisJson.put("analyzer", analyzerJson);
		String filter = searchServer.getFilter();
		if (StringUtils.isNotEmpty(filter)) {
			analysisJson.put("filter", JSONObject.parseObject(filter));
		}
		analysisAllJson.put("analysis", analysisJson);
		return analysisAllJson;
	}

	/**
	 * 生成mapping结构体
	 * 
	 * @param searchServer
	 * @return
	 */
	public static JSONObject generatorMappingJSON(SearchServer searchServer) {
		List<SearchField> searchFields = searchServer.getSearchFields();
		JSONObject mappingAllJson = new JSONObject();
		JSONObject mappingJson = new JSONObject();
		JSONObject fieldJson = null;
		JSONObject pinyinFieldJson = null;
		JSONObject pinyinFieldItemJson = null;
		for (SearchField searchField : searchFields) {
			fieldJson = new JSONObject();
			String fieldCode = searchField.getCode();
			// 字段类型
			String fieldType = searchField.getType();
			fieldJson.put("type", fieldType);
			String format = searchField.getFormat();
			if (StringUtils.isNotEmpty(format)) {
				fieldJson.put("format", format);
			}
			// 不分词标识
			String analyzerFlag = searchField.getAnalyzerFlag();
			if (analyzerFlag != null && StringUtils.equals(analyzerFlag, "1")) {
				if (fieldCode.equals("_all")) {
					fieldJson.put("enabled", false);
				} else {
					fieldJson.put("index", "not_analyzed");
				}
			} else {
				// 索引分词器
				String analyzer = searchField.getIndexAnalyzer();
				if (StringUtils.isEmpty(analyzer)) {
					analyzer = searchServer.getAnalyzerName();
				}
				if (fieldCode.equals("_all")) {
					fieldJson.put("analyzer", analyzer);
				} else {
					// 字段分词器
					String search_analyzer = searchField.getFieldAnalyzer();
					if (StringUtils.isEmpty(search_analyzer)) {
						search_analyzer = analyzer;
					}
					// 词向量
					String term_vector = searchField.getTermVector();
					// 包含拼音分词器则单独对字段创建拼音索引
					if (search_analyzer.indexOf("pinyin") > -1) {
						pinyinFieldJson = new JSONObject();
						pinyinFieldItemJson = new JSONObject();
						pinyinFieldItemJson.put("type", fieldType);
						if (StringUtils.isNotEmpty(format)) {
							pinyinFieldItemJson.put("format", format);
						}
						pinyinFieldItemJson.put("analyzer", analyzer);
						pinyinFieldItemJson.put("search_analyzer", search_analyzer);
						pinyinFieldItemJson.put("boost", 10);
						pinyinFieldItemJson.put("term_vector", term_vector);
						pinyinFieldItemJson.put("store", "no");
						pinyinFieldJson.put("pinyin", pinyinFieldItemJson);
						fieldJson.put("fields", pinyinFieldJson);
					} else {
						fieldJson.put("analyzer", analyzer);
						fieldJson.put("search_analyzer", search_analyzer);
						fieldJson.put("term_vector", term_vector);
						pinyinFieldItemJson.put("store", "no");
						pinyinFieldItemJson.put("boost", 10);
					}
				}
			}
			if (fieldCode.equals("_all")) {
				mappingAllJson.put("_all", fieldJson);
			} else {
				mappingJson.put(fieldCode, fieldJson);
			}
			mappingAllJson.put("properties", mappingJson);
		}
		return mappingAllJson;
	}

	/**
	 * 获取查询体高亮显示设置
	 * 
	 * @param className 高亮样式名称
	 * @param fields
	 * @return
	 */
	public static JSONObject getHighLightJSON(String className, List<String> fields) {
		JSONObject highlightJSON = new JSONObject();
		List<String> preTagsList = new ArrayList<String>();
		List<String> postTagsList = new ArrayList<String>();
		JSONObject fieldJson = new JSONObject();
		if (fields != null) {
			for (String field : fields) {
				preTagsList.add("<em class=\"" + className + "\">");
				postTagsList.add("</em>");
				fieldJson.put(field, new JSONObject());
			}
		} else {
			preTagsList.add("<em class=\"" + className + "\">");
			postTagsList.add("</em>");
			highlightJSON.put("require_field_match", false);
			fieldJson.put("*", new JSONObject());
		}
		highlightJSON.put("fields", fieldJson);
		highlightJSON.put("pre_tags", preTagsList);
		highlightJSON.put("post_tags", postTagsList);
		return highlightJSON;
	}

	/**
	 * 获取多字段查询体JSON字符串
	 * 
	 * @param query
	 * @param matchType    best_fields 完全匹配占的分值高，部分匹配分值需乘以系数 most_fields 越多匹配分值越高
	 * @param queryFields  查询字段
	 * @param returnFields 返回字段
	 * @param className    高亮样式名称
	 * @return
	 */
	public static String getMultiMatchQueryAllJsonString(String query, String matchType, List<String> queryFields,
			List<String> returnFields, String className) {
		JSONObject queryJson = new JSONObject();
		JSONObject multiMatchJson = new JSONObject();
		JSONObject multiMatchContentJson = new JSONObject();
		if (CollectionUtils.isNotEmpty(returnFields)) {
			queryJson.put("_source", returnFields);
		}
		multiMatchContentJson.put("query", query);
		multiMatchContentJson.put("type", matchType);
		multiMatchContentJson.put("fields", queryFields);
		multiMatchJson.put("multi_match", multiMatchContentJson);
		queryJson.put("query", multiMatchJson);
		JSONObject highlightJSON = getHighLightJSON(className, queryFields);
		queryJson.put("highlight", highlightJSON);
		return queryJson.toJSONString();
	}

	/**
	 * 获取查询所有字段的查询体JSON字符串
	 * 
	 * @param query
	 * @param className 高亮样式名称
	 * @return
	 */
	public static String getQueryAllJsonString(String query, String className) {
		JSONObject queryJson = new JSONObject();
		JSONObject matchJson = new JSONObject();
		JSONObject allJson = new JSONObject();
		allJson.put("_all", query);
		matchJson.put("match", allJson);
		queryJson.put("query", matchJson);
		JSONObject highlightJSON = getHighLightJSON(className, null);
		queryJson.put("highlight", highlightJSON);
		return queryJson.toJSONString();
	}

	/**
	 * 获取总记录数
	 * 
	 * @param serverURL 服务地址
	 * @param indexName 索引名称
	 * @param type      索引类型
	 * @param queryJson 查询条件
	 * @return
	 */
	public static long getTotal(String serverURL, String indexName, String type, String queryJson) {
		JSONObject result = null;
		HttpClient httpClient = new HttpClient();
		String url = serverURL + "/" + indexName + "/" + type + "/_search";
		PostMethod postMethod = new PostMethod(url);
		RequestEntity requestEntity = null;
		long total = 0;
		try {
			JSONObject queryJsonObject = JSONObject.parseObject(queryJson);
			queryJsonObject.put("from", 0);
			queryJsonObject.put("size", 0);
			queryJson = queryJsonObject.toJSONString();
			requestEntity = new StringRequestEntity(queryJson, "application/json", "UTF-8");
			postMethod.setRequestEntity(requestEntity);

			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("method failed: " + postMethod.getStatusLine());
			} else {
				String responseBody = postMethod.getResponseBodyAsString();
				result = JSONObject.parseObject(responseBody);
				JSONObject hits = result.getJSONObject("hits");
				if (hits != null) {
					total = hits.getLong("total");
				}
			}
		} catch (IOException ex) {
			logger.error("method error: " + ex.getMessage());
		} finally {
			postMethod.releaseConnection();
		}
		return total;
	}

	/**
	 * 重建索引
	 * 
	 * @param serverURL    服务地址
	 * @param oldIndexName
	 * @param newIndexName
	 */
	public static void rebuildIndex(String serverURL, String oldIndexName, String newIndexName) {
		HttpClient httpClient = new HttpClient();
		String url = serverURL + "/_reindex";
		PostMethod postMethod = new PostMethod(url);
		RequestEntity requestEntity = null;
		String paramJson = "{\"source\": {\"index\": \"" + oldIndexName + "\"},\"dest\": {\"index\": \"" + newIndexName
				+ "\"}}";
		try {
			requestEntity = new StringRequestEntity(paramJson, "application/json", "UTF-8");
			postMethod.setRequestEntity(requestEntity);

			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("method failed: " + postMethod.getStatusLine());
			} else {
				String responseBody = postMethod.getResponseBodyAsString();
				JSONObject responseJson = JSONObject.parseObject(responseBody);
				if (responseJson != null && "true".equals(responseJson.getString("created"))) {
					logger.debug(oldIndexName + " rebuild index " + newIndexName + " success!");
				} else {
					logger.debug(oldIndexName + " rebuild index " + newIndexName + " failure!");
				}
			}
		} catch (IOException ex) {
			logger.error("method error: " + ex.getMessage());
		} finally {
			postMethod.releaseConnection();
		}
	}

	/**
	 * 执行搜索
	 * 
	 * @param serverURL 服务地址
	 * @param indexname 索引名称
	 * @param type      索引类型
	 * @param queryJson 查询条件
	 * @return
	 */
	public static JSONObject search(String serverURL, String indexName, String type, String queryJson) {
		JSONObject result = null;
		HttpClient httpClient = new HttpClient();
		String url = serverURL + "/" + indexName + "/" + type + "/_search";
		PostMethod postMethod = new PostMethod(url);
		RequestEntity requestEntity = null;
		try {
			requestEntity = new StringRequestEntity(queryJson, "application/json", "UTF-8");
			postMethod.setRequestEntity(requestEntity);

			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("method failed: " + postMethod.getStatusLine());
			} else {
				String responseBody = postMethod.getResponseBodyAsString();
				result = JSONObject.parseObject(responseBody);
			}
		} catch (IOException ex) {
			logger.error("method error: " + ex.getMessage());
		} finally {
			postMethod.releaseConnection();
		}
		return result;
	}

	/**
	 * 分页执行搜索
	 * 
	 * @param serverURL 服务地址
	 * @param indexname 索引名称
	 * @param type      索引类型
	 * @param queryJson 查询条件
	 * @param pageNo    页编号
	 * @param pageSize  每页记录数
	 * @return
	 */
	public static JSONObject searchPaging(String serverURL, String indexName, String type, String queryJson, int pageNo,
			int pageSize) {
		JSONObject result = null;
		HttpClient httpClient = new HttpClient();
		String url = serverURL + "/" + indexName + "/" + type + "/_search";
		PostMethod postMethod = new PostMethod(url);
		RequestEntity requestEntity = null;
		try {
			JSONObject queryJsonObject = JSONObject.parseObject(queryJson);
			long from = (pageNo - 1) * pageSize;
			queryJsonObject.put("from", from);
			queryJsonObject.put("size", pageSize);
			queryJson = queryJsonObject.toJSONString();
			requestEntity = new StringRequestEntity(queryJson, "application/json", "UTF-8");
			postMethod.setRequestEntity(requestEntity);

			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("method failed: " + postMethod.getStatusLine());
			} else {
				String responseBody = postMethod.getResponseBodyAsString();
				result = JSONObject.parseObject(responseBody);
			}
		} catch (IOException ex) {
			logger.error("method error: " + ex.getMessage());
		} finally {
			postMethod.releaseConnection();
		}
		return result;
	}

	/**
	 * 更新文档
	 * 
	 * @param serverURL 服务地址
	 * @param indexName 索引名称
	 * @param type      索引类型
	 * @param id        文档id
	 * @param json      json数据
	 */
	public static void updateDocument(String serverURL, String indexName, String type, String id, String json) {
		HttpClient httpClient = new HttpClient();
		String url = serverURL + "/" + indexName + "/" + type + "/" + id;
		PutMethod putMethod = new PutMethod(url);
		RequestEntity requestEntity = null;
		try {
			requestEntity = new StringRequestEntity(json, "application/json", "UTF-8");
			putMethod.setRequestEntity(requestEntity);

			int statusCode = httpClient.executeMethod(putMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("method failed: " + putMethod.getStatusLine());
			} else {
				String responseBody = putMethod.getResponseBodyAsString();
				JSONObject responseJson = JSONObject.parseObject(responseBody);
				if (responseJson != null && "false".equals(responseJson.getString("created"))
						&& responseJson.getInteger("_version") > 1) {
					logger.debug("update document " + indexName + "/" + type + " success!");
				} else {
					logger.debug("update document " + indexName + "/" + type + " failure!");
				}
			}
		} catch (IOException ex) {
			logger.error("method error: " + ex.getMessage());
		} finally {
			putMethod.releaseConnection();
		}
	}

	/**
	 * 更新索引
	 * 
	 * @param serverURL 服务地址
	 * @param indexName 索引名称
	 * @param json      json数据
	 */
	public static void updateIndex(String serverURL, String indexName, String json) {
		HttpClient httpClient = new HttpClient();
		String url = serverURL + "/" + indexName;
		PutMethod putMethod = new PutMethod(url);
		RequestEntity requestEntity = null;
		try {
			requestEntity = new StringRequestEntity(json, "application/json", "UTF-8");
			putMethod.setRequestEntity(requestEntity);

			int statusCode = httpClient.executeMethod(putMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("method failed: " + putMethod.getStatusLine());
			} else {
				String responseBody = putMethod.getResponseBodyAsString();
				JSONObject responseJson = JSONObject.parseObject(responseBody);
				if (responseJson != null && "false".equals(responseJson.getString("created"))
						&& responseJson.getInteger("_version") > 1) {
					logger.debug("update index " + indexName + " success!");
				} else {
					logger.debug("update index " + indexName + " failure!");
				}
			}
		} catch (IOException ex) {
			logger.error("method error: " + ex.getMessage());
		} finally {
			putMethod.releaseConnection();
		}
	}

	public static void main(String args[]) {
		String server = "http://127.0.0.1:9200";
		String indexName = "index";
		String typeName = "test";
		String id = "4";
		String jsonData = "{\"content\":\"中美贸易战，中国不惧美国\"}";
		ElasticSearchUtils.createDocument(server, indexName, typeName, id, jsonData);
		String queryJson = "{\"query\":\"中国\"}";
		JSONObject object = ElasticSearchUtils.searchPaging(server, indexName, typeName, queryJson, 1, 10);
		System.out.println(object.toJSONString());
		List<String> fields = new ArrayList<String>();
		fields.add("content");
		String queryStr = ElasticSearchUtils.getMultiMatchQueryAllJsonString("中美", "most_fields", fields, fields,
				"red-highlight");
		object = ElasticSearchUtils.searchPaging(server, indexName, typeName, queryStr, 1, 1);
		System.out.println(object.toJSONString());
	}
}
