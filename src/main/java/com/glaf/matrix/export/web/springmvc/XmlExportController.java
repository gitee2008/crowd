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

package com.glaf.matrix.export.web.springmvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import com.glaf.core.base.BaseTree;
import com.glaf.core.base.TreeModel;
import com.glaf.core.config.DatabaseConnectionConfig;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.domain.Database;
import com.glaf.core.identity.User;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.tree.helper.JacksonTreeHelper;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.Dom4jUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

import com.glaf.matrix.export.domain.XmlExport;
import com.glaf.matrix.export.handler.XmlDataHandler;
import com.glaf.matrix.export.handler.XmlExportDataHandler;
import com.glaf.matrix.export.query.XmlExportQuery;
import com.glaf.matrix.export.service.XmlExportService;
import com.glaf.matrix.util.SysParams;

import com.glaf.template.service.ITemplateService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/matrix/xmlExport")
@RequestMapping("/matrix/xmlExport")
public class XmlExportController {
	protected static final Log logger = LogFactory.getLog(XmlExportController.class);

	protected IDatabaseService databaseService;

	protected XmlExportService xmlExportService;

	protected ITemplateService templateService;

	public XmlExportController() {

	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String id = RequestUtils.getString(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					XmlExport xmlExport = xmlExportService.getXmlExport(x);
					if (xmlExport != null && (StringUtils.equals(xmlExport.getCreateBy(), loginContext.getActorId())
							|| loginContext.isSystemAdministrator())) {

					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			XmlExport xmlExport = xmlExportService.getXmlExport(id);
			if (xmlExport != null && (StringUtils.equals(xmlExport.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {

				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		long nodeParentId = RequestUtils.getLong(request, "nodeParentId");
		XmlExport xmlExport = xmlExportService.getXmlExport(RequestUtils.getString(request, "id"));
		if (xmlExport != null) {
			request.setAttribute("xmlExport", xmlExport);
			XmlExport parent = xmlExportService.getXmlExportByNodeId(xmlExport.getNodeParentId());
			if (parent != null) {
				request.setAttribute("parent", parent);
			}
		}

		if (nodeParentId > 0) {
			XmlExport parent = xmlExportService.getXmlExportByNodeId(nodeParentId);
			if (parent != null) {
				request.setAttribute("parent", parent);
			}
			List<XmlExport> children = xmlExportService.getAllChildren(nodeParentId);
			if (children != null && !children.isEmpty()) {
				if (xmlExport != null) {
					children.remove(xmlExport);
				}
				request.setAttribute("children", children);
			}
		} else {
			List<XmlExport> children = xmlExportService.getAllChildren(0);
			if (children != null && !children.isEmpty()) {
				if (xmlExport != null) {
					children.remove(xmlExport);
				}
				request.setAttribute("children", children);
			}
		}

		List<Integer> sortNoList = new ArrayList<Integer>();
		for (int i = 1; i < 50; i++) {
			sortNoList.add(i);
		}
		request.setAttribute("sortNoList", sortNoList);

		LoginContext loginContext = RequestUtils.getLoginContext(request);

		DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
		List<Database> activeDatabases = cfg.getActiveDatabases(loginContext);
		if (activeDatabases != null && !activeDatabases.isEmpty()) {

		}
		request.setAttribute("databases", activeDatabases);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("xmlExport.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/matrix/xmlExport/edit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/exportXml")
	public void exportXml(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SysParams.putInternalParams(params);
		long databaseId = RequestUtils.getLong(request, "databaseId");
		String expId = RequestUtils.getString(request, "expId");
		try {
			XmlExport xmlExport = xmlExportService.getXmlExport(expId);
			if (xmlExport != null && StringUtils.equals(xmlExport.getActive(), "Y")) {
				xmlExport.setParameter(params);
				XmlDataHandler xmlDataHandler = new XmlExportDataHandler();
				org.dom4j.Document document = DocumentHelper.createDocument();
				org.dom4j.Element root = document.addElement(xmlExport.getXmlTag());
				xmlExport.setElement(root);
				xmlDataHandler.addChild(xmlExport, root, databaseId);
				byte[] data = Dom4jUtils.getBytesFromPrettyDocument(document, "UTF-8");
				ResponseUtils.download(request, response, data,
						xmlExport.getTitle() + DateUtils.getNowYearMonthDayHHmmss() + ".xml");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		XmlExportQuery query = new XmlExportQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);
		/**
		 * 此处业务逻辑需自行调整
		 */
		if (!loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			query.createBy(actorId);
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
		int total = xmlExportService.getXmlExportCountByQueryCriteria(query);
		if (total > 0) {
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

			List<XmlExport> list = xmlExportService.getXmlExportsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (XmlExport xmlExport : list) {
					JSONObject rowJSON = xmlExport.toJsonObject();
					rowJSON.put("id", xmlExport.getId());
					rowJSON.put("rowId", xmlExport.getId());
					rowJSON.put("xmlExportId", xmlExport.getId());
					rowJSON.put("startIndex", ++start);
					rowsJSON.add(rowJSON);
				}

			}
		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", total);
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

		return new ModelAndView("/matrix/xmlExport/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("xmlExport.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/matrix/xmlExport/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/save")
	public byte[] save(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		XmlExport xmlExport = new XmlExport();
		try {
			Tools.populate(xmlExport, params);
			xmlExport.setName(request.getParameter("name"));
			xmlExport.setTitle(request.getParameter("title"));
			xmlExport.setSql(request.getParameter("sql"));
			xmlExport.setResultFlag(request.getParameter("resultFlag"));
			xmlExport.setNodeParentId(RequestUtils.getLong(request, "nodeParentId"));
			xmlExport.setLeafFlag(request.getParameter("leafFlag"));
			xmlExport.setType(request.getParameter("type"));
			xmlExport.setActive(request.getParameter("active"));
			xmlExport.setXmlTag(request.getParameter("xmlTag"));
			xmlExport.setTemplateId(request.getParameter("templateId"));
			xmlExport.setExternalAttrsFlag(request.getParameter("externalAttrsFlag"));
			xmlExport.setInterval(RequestUtils.getInt(request, "interval"));
			xmlExport.setSortNo(RequestUtils.getInt(request, "sortNo"));
			xmlExport.setCreateBy(actorId);
			xmlExport.setUpdateBy(actorId);

			this.xmlExportService.save(xmlExport);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveAs")
	public byte[] saveAs(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		try {
			String expId = RequestUtils.getString(request, "expId");
			if (expId != null) {
				String nid = xmlExportService.saveAs(expId, actorId);
				if (nid != null) {
					return ResponseUtils.responseJsonResult(true);
				}
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setDatabaseService(IDatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	@javax.annotation.Resource
	public void setTemplateService(ITemplateService templateService) {
		this.templateService = templateService;
	}

	@javax.annotation.Resource(name = "com.glaf.matrix.export.service.xmlExportService")
	public void setXmlExportService(XmlExportService xmlExportService) {
		this.xmlExportService = xmlExportService;
	}

	@RequestMapping("/showExport")
	public ModelAndView showExport(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		long nodeParentId = RequestUtils.getLong(request, "nodeParentId");
		XmlExport xmlExport = xmlExportService.getXmlExport(RequestUtils.getString(request, "expId"));
		if (xmlExport != null) {
			request.setAttribute("xmlExport", xmlExport);
			XmlExport parent = xmlExportService.getXmlExportByNodeId(xmlExport.getNodeParentId());
			if (parent != null) {
				request.setAttribute("parent", parent);
			}
		}

		if (nodeParentId > 0) {
			XmlExport parent = xmlExportService.getXmlExportByNodeId(nodeParentId);
			if (parent != null) {
				request.setAttribute("parent", parent);
			}
			List<XmlExport> children = xmlExportService.getAllChildren(nodeParentId);
			if (children != null && !children.isEmpty()) {
				if (xmlExport != null) {
					children.remove(xmlExport);
				}
				request.setAttribute("children", children);
			}
		} else {
			List<XmlExport> children = xmlExportService.getAllChildren(0);
			if (children != null && !children.isEmpty()) {
				if (xmlExport != null) {
					children.remove(xmlExport);
				}
				request.setAttribute("children", children);
			}
		}

		List<Integer> sortNoList = new ArrayList<Integer>();
		for (int i = 1; i < 50; i++) {
			sortNoList.add(i);
		}
		request.setAttribute("sortNoList", sortNoList);

		LoginContext loginContext = RequestUtils.getLoginContext(request);

		DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
		List<Database> activeDatabases = cfg.getActiveDatabases(loginContext);
		if (activeDatabases != null && !activeDatabases.isEmpty()) {

		}
		request.setAttribute("databases", activeDatabases);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("xmlExport.showExport");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/matrix/xmlExport/showExport", modelMap);
	}

	@ResponseBody
	@RequestMapping("/treeJson")
	public byte[] treeJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.debug("------------------------treeJson--------------------");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		long parentNodeId = RequestUtils.getLong(request, "parentNodeId");
		List<TreeModel> treeModels = new ArrayList<TreeModel>();
		JacksonTreeHelper treeHelper = new JacksonTreeHelper();
		ArrayNode responseJSON = new ObjectMapper().createArrayNode();
		try {
			List<XmlExport> children = xmlExportService.getAllChildren(parentNodeId);
			if (children != null && !children.isEmpty()) {
				for (XmlExport model : children) {
					TreeModel tree = new BaseTree();
					tree.setId(model.getNodeId());
					tree.setParentId(model.getNodeParentId());
					tree.setName(model.getTitle());
					tree.setSortNo(model.getSortNo());
					tree.setLevel(model.getLevel());
					treeModels.add(tree);
				}
				java.util.Collections.sort(treeModels);
				responseJSON = treeHelper.getTreeArrayNode(treeModels);
				logger.debug(responseJSON.toString());
			}
			return responseJSON.toString().getBytes("UTF-8");
		} catch (IOException e) {
			return responseJSON.toString().getBytes();
		}
	}

}
