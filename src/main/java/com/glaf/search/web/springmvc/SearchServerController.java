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

package com.glaf.search.web.springmvc;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;
import com.glaf.search.domain.SearchServer;
import com.glaf.search.query.SearchServerQuery;
import com.glaf.search.service.SearchServerService;
import com.glaf.search.util.SearchServerJsonFactory;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sys/searchServer")
@RequestMapping("/sys/searchServer")
public class SearchServerController {
	protected static final Log logger = LogFactory.getLog(SearchServerController.class);

	protected SearchServerService searchServerService;

	public SearchServerController() {

	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, HttpServletResponse response) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String id = RequestUtils.getString(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					SearchServer searchServer = searchServerService.getSearchServer(String.valueOf(x));
					if (searchServer != null
							&& (StringUtils.equals(searchServer.getCreateBy(), loginContext.getActorId())
									|| loginContext.isSystemAdministrator())) {
						searchServerService.deleteById(x);
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			SearchServer searchServer = searchServerService.getSearchServer(String.valueOf(id));
			if (searchServer != null && (StringUtils.equals(searchServer.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				searchServerService.deleteById(id);
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		SearchServer searchServer = searchServerService.getSearchServer(request.getParameter("id"));
		if (searchServer != null) {
			request.setAttribute("searchServer", searchServer);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("searchServer.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/searchServer/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SearchServerQuery query = new SearchServerQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		if (!loginContext.isSystemAdministrator()) {
			query.tenantId(loginContext.getTenantId());
		}

		int start = 0;
		int limit = 10;
		String orderName = null;
		String order = null;

		int pageNo = ParamUtils.getInt(params, "page");
		limit = ParamUtils.getInt(params, "rows");
		start = (pageNo - 1) * limit;
		orderName = ParamUtils.getString(params, "sortName");
		order = ParamUtils.getString(params, "sortOrder");

		if (start < 0) {
			start = 0;
		}

		if (limit <= 0) {
			limit = Paging.DEFAULT_PAGE_SIZE;
		}

		JSONObject result = new JSONObject();
		int total = searchServerService.getSearchServerCountByQueryCriteria(query);
		if (total > 0) {
			result.put("code", 0);
			result.put("total", total);
			result.put("totalCount", total);
			result.put("totalRecords", total);
			result.put("start", start);
			result.put("startIndex", start);
			result.put("limit", limit);
			result.put("pageSize", limit);

			if (StringUtils.isNotEmpty(orderName)) {
				query.setSortOrder(orderName);
				if (StringUtils.equals(order, "desc")) {
					query.setSortOrder(" desc ");
				}
			}

			List<SearchServer> list = searchServerService.getSearchServersByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				for (SearchServer searchServer : list) {
					JSONObject rowJSON = searchServer.toJsonObject();
					rowJSON.put("id", searchServer.getId());
					rowJSON.put("searchServerId", searchServer.getId());
					rowJSON.put("startIndex", ++start);
					rowsJSON.add(rowJSON);
				}

				result.put("rows", rowsJSON);

			}
		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", total);
			result.put("code", 0);
		}
		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/sys/searchServer/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("searchServer.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/sys/searchServer/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/save")
	public byte[] save(HttpServletRequest request, HttpServletResponse response) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		String json = request.getParameter("json");
		SearchServer searchServer = null;
		try {
			if (StringUtils.isNotEmpty(json)) {
				JSONObject jsonObject = JSON.parseObject(json);
				searchServer = SearchServerJsonFactory.jsonToObject(jsonObject);
				searchServer.setCreateBy(actorId);
				searchServer.setUpdateBy(actorId);

				this.searchServerService.save(searchServer);

				return ResponseUtils.responseJsonResult(true);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveSearchServer")
	public byte[] saveSearchServer(HttpServletRequest request, HttpServletResponse response) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SearchServer searchServer = new SearchServer();
		try {
			Tools.populate(searchServer, params);
			searchServer.setServerURL(request.getParameter("serverURL"));
			searchServer.setIndexName(request.getParameter("indexName"));
			searchServer.setType(request.getParameter("type"));
			searchServer.setTokenizer(request.getParameter("tokenizer"));
			searchServer.setTokenFilter(request.getParameter("tokenFilter"));
			searchServer.setCharacterFilter(request.getParameter("characterFilter"));
			searchServer.setAnalyzerName(request.getParameter("analyzerName"));
			searchServer.setAnalyzerType(request.getParameter("analyzerType"));
			searchServer.setFilter(request.getParameter("filter"));
			searchServer.setLocked(RequestUtils.getInt(request, "locked"));
			searchServer.setCreateBy(actorId);
			searchServer.setUpdateBy(actorId);

			this.searchServerService.save(searchServer);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.search.service.searchServerService")
	public void setSearchServerService(SearchServerService searchServerService) {
		this.searchServerService = searchServerService;
	}

}
