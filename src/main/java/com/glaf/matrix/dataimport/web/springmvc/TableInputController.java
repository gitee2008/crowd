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
package com.glaf.matrix.dataimport.web.springmvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.config.DatabaseConnectionConfig;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.util.TableDomainFactory;
import com.glaf.core.factory.DataServiceFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.service.ITablePageService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

import com.glaf.matrix.dataimport.domain.TableInput;
import com.glaf.matrix.dataimport.domain.TableInputColumn;
import com.glaf.matrix.dataimport.query.TableInputQuery;
import com.glaf.matrix.dataimport.query.TableInputColumnQuery;
import com.glaf.matrix.dataimport.service.TableInputService;

@Controller("/matrix/tableInput")
@RequestMapping("/matrix/tableInput")
public class TableInputController {
	protected static final Log logger = LogFactory.getLog(TableInputController.class);

	protected IDatabaseService databaseService;

	protected TableInputService tableInputService;

	protected ITablePageService tablePageService;

	@RequestMapping("/columns")
	public ModelAndView columns(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/matrix/tableInput/columns", modelMap);
	}

	@RequestMapping("/columnsJson")
	@ResponseBody
	public byte[] columnsJson(HttpServletRequest request, ModelMap modelMap) throws IOException {
		String tableId = request.getParameter("tableId");
		JSONObject result = new JSONObject();
		List<TableInputColumn> list = tableInputService.getTableInputColumnsByTableId(tableId);
		if (list != null && !list.isEmpty()) {
			int start = 0;
			int total = list.size();
			result.put("total", total);
			result.put("totalCount", total);
			result.put("totalRecords", total);
			result.put("start", 0);
			result.put("startIndex", 0);
			result.put("limit", total);
			result.put("pageSize", total);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (TableInputColumn t : list) {
					JSONObject rowJSON = t.toJsonObject();
					rowJSON.put("startIndex", ++start);
					rowsJSON.add(rowJSON);
				}

			}
		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", 0);
		}
		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping("/columnsJsonArray")
	@ResponseBody
	public byte[] columnsJsonArray(HttpServletRequest request, ModelMap modelMap) throws IOException {
		String tableId = request.getParameter("tableId");
		JSONArray result = new JSONArray();
		JSONObject idJson = new JSONObject();
		idJson.put("columnName", "ID_");
		idJson.put("title", "编号");
		result.add(idJson);

		List<TableInputColumn> list = tableInputService.getTableInputColumnsByTableId(tableId);
		if (list != null && !list.isEmpty()) {
			for (TableInputColumn t : list) {
				JSONObject rowJSON = t.toJsonObject();
				result.add(rowJSON);
			}
		}

		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping("/databases")
	public ModelAndView databases(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
		List<Database> databases = cfg.getDatabases();
		request.setAttribute("databases", databases);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/matrix/tableInput/databases", modelMap);
	}

	/**
	 * 删除表字段
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/deleteColumn")
	@ResponseBody
	public byte[] deleteColumn(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			try {
				String tableId = request.getParameter("tableId");
				TableInput tableInput = null;
				if (StringUtils.isNotEmpty(tableId)) {
					tableInput = tableInputService.getTableInputById(tableId);
					if (tableInput != null) {
						if (!DBUtils.tableExists(tableInput.getTableName())) {
							tableInputService.deleteColumn(request.getParameter("id"));
							return ResponseUtils.responseResult(true);
						} else {
							TableInputColumn column = tableInputService.getTableInputColumn(request.getParameter("id"));
							if (column != null
									&& StringUtils.equalsIgnoreCase(column.getTableName(), tableInput.getTableName())) {
								List<ColumnDefinition> columns = DBUtils
										.getColumnDefinitions(tableInput.getTableName());
								if (columns != null && !columns.isEmpty()) {
									for (ColumnDefinition c : columns) {
										if (StringUtils.equalsIgnoreCase(column.getColumnName(), c.getColumnName())) {
											return ResponseUtils.responseJsonResult(false, "物理表的字段已经存在，不允许删除！");
										}
									}
								}
								tableInputService.deleteColumn(request.getParameter("id"));
								return ResponseUtils.responseResult(true);
							}
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * 删除表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/deleteTable")
	@ResponseBody
	public byte[] deleteTable(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			try {
				String tableId = request.getParameter("tableId");
				TableInput tableDefinition = null;
				if (StringUtils.isNotEmpty(tableId)) {
					tableDefinition = tableInputService.getTableInputById(tableId);
					if (tableDefinition != null) {
						if (!DBUtils.tableExists(tableDefinition.getTableName())) {
							tableInputService.deleteTable(tableId);
							return ResponseUtils.responseResult(true);
						} else {
							return ResponseUtils.responseJsonResult(false, "物理表已经存在，不允许删除！");
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/editColumn")
	public ModelAndView editColumn(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		logger.debug(RequestUtils.getParameterMap(request));
		String columnId = request.getParameter("id");
		if (StringUtils.isNotEmpty(columnId)) {
			TableInputColumn columnDefinition = tableInputService.getTableInputColumn(columnId);
			request.setAttribute("column", columnDefinition);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/matrix/tableInput/editColumn", modelMap);
	}

	@RequestMapping("/editTable")
	public ModelAndView editTable(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String tableId = request.getParameter("tableId");
		if (StringUtils.isNotEmpty(tableId)) {
			TableInput tableInput = tableInputService.getTableInputById(tableId);
			request.setAttribute("tableInput", tableInput);

			List<TableInputColumn> columns = tableInputService.getTableInputColumnsByTableId(tableId);
			request.setAttribute("columns", columns);
		}

		DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
		List<Database> databases = cfg.getDatabases();
		request.setAttribute("databases", databases);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/matrix/tableInput/editTable", modelMap);
	}

	@RequestMapping("/extColumns")
	public ModelAndView extColumns(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String targetId = request.getParameter("targetId");
		if (StringUtils.isNotEmpty(targetId)) {
			TableInputColumnQuery query = new TableInputColumnQuery();
			query.targetId(targetId);
			List<TableInputColumn> columns = tableInputService.getTableInputColumns(query);
			request.setAttribute("columns", columns);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/matrix/tableInput/extColumns", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		TableInputQuery query = new TableInputQuery();
		Tools.populate(query, params);
		query.setCreateBy(loginContext.getActorId());
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);
		query.type("useradd");

		if (!loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			query.createBy(actorId);
		}

		int start = 0;
		int limit = 50;
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
			limit = 50;
		}

		JSONObject result = new JSONObject();
		int total = tableInputService.getTableInputCountByQueryCriteria(query);
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

			List<TableInput> list = tableInputService.getTableInputsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (TableInput t : list) {
					JSONObject rowJSON = t.toJsonObject();
					rowJSON.put("id", t.getTableName());
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

	@ResponseBody
	@RequestMapping("/save")
	public byte[] save(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			Map<String, Object> params = RequestUtils.getParameterMap(request);
			String tableId = request.getParameter("tableId");
			TableInput tableDefinition = null;
			try {
				if (StringUtils.isNotEmpty(tableId)) {
					tableDefinition = tableInputService.getTableInputById(tableId);
				}
				if (tableDefinition == null) {
					String tableName = null;
					while (true) {
						if (tableName == null) {
							tableName = tableInputService.nextTableName("useradd");
						}
						if (DBUtils.tableExists(tableName)) {
							tableName = tableInputService.nextTableName("useradd");
						} else {
							tableDefinition = new TableInput();
							tableDefinition.setTableName(tableName);
							break;
						}
					}
				}

				Tools.populate(tableDefinition, params);
				tableDefinition.setCreateBy(actorId);
				tableDefinition.setCreateTime(new Date());
				tableDefinition.setSystemFlag("U");
				tableDefinition.setLocked(0);
				tableDefinition.setType("useradd");

				tableInputService.save(tableDefinition);

				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveAs")
	public byte[] saveAs(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			String tableId = request.getParameter("tableId");
			Map<String, Object> params = RequestUtils.getParameterMap(request);
			TableInput tableDefinition = null;
			try {
				if (StringUtils.isNotEmpty(tableId)) {
					tableDefinition = tableInputService.getTableInputById(tableId);
				}
				if (tableDefinition != null && tableDefinition.getColumns() != null
						&& !tableDefinition.getColumns().isEmpty()) {
					String tableName = null;
					while (true) {
						if (tableName == null) {
							tableName = tableInputService.nextTableName("useradd");
						}
						if (DBUtils.tableExists(tableName)) {
							tableName = tableInputService.nextTableName("useradd");
						} else {
							tableDefinition.setTableName(tableName);
							break;
						}
					}

					Tools.populate(tableDefinition, params);
					tableDefinition.setCreateBy(actorId);
					tableDefinition.setCreateTime(new Date());
					tableDefinition.setSystemFlag("U");
					tableDefinition.setLocked(0);
					tableDefinition.setType("useradd");

					tableInputService.saveAs(tableDefinition, tableDefinition.getColumns());

					return ResponseUtils.responseJsonResult(true);
				}
			} catch (Exception ex) {
				// ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveColumn")
	public byte[] saveColumn(HttpServletRequest request) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			String tableId = request.getParameter("tableId");
			TableInput tableDefinition = null;
			try {
				if (StringUtils.isNotEmpty(tableId)) {
					tableDefinition = tableInputService.getTableInputById(tableId);
				}
				if (tableDefinition != null) {
					String columnId = request.getParameter("id");
					if (StringUtils.isNotEmpty(columnId)) {
						TableInputColumn columnDefinition = tableInputService.getTableInputColumn(columnId);
						if (columnDefinition != null) {
							Tools.populate(columnDefinition, params);
							columnDefinition.setDataCode(request.getParameter("dataCode"));
							columnDefinition.setTableName(tableDefinition.getTableName());
							columnDefinition.setComment(request.getParameter("comment"));
							tableInputService.updateColumn(columnDefinition);
						}
					} else {
						TableInputColumn columnDefinition = new TableInputColumn();
						Tools.populate(columnDefinition, params);
						columnDefinition.setDataCode(request.getParameter("dataCode"));
						columnDefinition.setTableName(tableDefinition.getTableName());
						columnDefinition.setComment(request.getParameter("comment"));
						tableInputService.saveColumn(tableDefinition.getTableName(), columnDefinition);
					}
					return ResponseUtils.responseJsonResult(true);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveColumns")
	public byte[] saveColumns(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			String targetId = request.getParameter("targetId");
			try {
				if (StringUtils.isNotEmpty(targetId)) {
					TableInputColumnQuery query = new TableInputColumnQuery();
					query.targetId(targetId);
					List<TableInputColumn> columns = tableInputService.getTableInputColumns(query);
					for (TableInputColumn col : columns) {
						col.setTitle(request.getParameter("title_" + col.getId()));
						col.setColumnName(request.getParameter("columnName_" + col.getId()));
						col.setJavaType(request.getParameter("javaType_" + col.getId()));
						col.setLength(RequestUtils.getInt(request, "length_" + col.getId()));
					}
					tableInputService.saveColumns(targetId, columns);
					return ResponseUtils.responseJsonResult(true);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	/**
	 * 排序
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveSort")
	@ResponseBody
	public byte[] saveSort(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			String items = request.getParameter("items");
			if (StringUtils.isNotEmpty(items)) {
				int sort = 0;
				List<TableModel> rows = new ArrayList<TableModel>();
				StringTokenizer token = new StringTokenizer(items, ",");
				while (token.hasMoreTokens()) {
					String item = token.nextToken();
					if (StringUtils.isNotEmpty(item)) {
						sort++;
						TableModel t1 = new TableModel();
						t1.setTableName("SYS_INPUT_COLUMN");
						ColumnModel idColumn = new ColumnModel();
						idColumn.setColumnName("ID_");
						idColumn.setJavaType("String");
						idColumn.setValue(item);
						t1.setIdColumn(idColumn);
						ColumnModel sortColumn = new ColumnModel();
						sortColumn.setColumnName("ORDINAL_");
						sortColumn.setJavaType("Integer");
						sortColumn.setValue(sort);
						t1.addColumn(sortColumn);
						rows.add(t1);
					}
				}
				try {
					DataServiceFactory.getInstance().updateAllTableData(rows);
					return ResponseUtils.responseResult(true);
				} catch (Exception ex) {
					logger.error(ex);
				}
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@javax.annotation.Resource
	public void setDatabaseService(IDatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	@javax.annotation.Resource
	public void setTableInputService(TableInputService tableInputService) {
		this.tableInputService = tableInputService;
	}

	@javax.annotation.Resource
	public void setTablePageService(ITablePageService tablePageService) {
		this.tablePageService = tablePageService;
	}

	/**
	 * 显示排序页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/showSort")
	public ModelAndView showSort(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String tableId = request.getParameter("tableId");
		List<TableInputColumn> columns = tableInputService.getTableInputColumnsByTableId(tableId);
		request.setAttribute("columns", columns);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("table.showSort");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/matrix/tableInput/showSort", modelMap);
	}

	@RequestMapping
	public ModelAndView tables(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		TableInputQuery query = new TableInputQuery();
		query.locked(0);
		query.type("useradd");
		try {
			List<TableInput> tables = tableInputService.list(query);
			if (tables != null && !tables.isEmpty()) {
				request.setAttribute("tables", tables);
				request.setAttribute("updateAllSchema", true);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/matrix/tableInput/tables", modelMap);
	}

	/**
	 * 更新全部表结构
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateAllSchema")
	@ResponseBody
	public byte[] updateAllSchema(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			TableInput tableInput = null;
			List<TableInputColumn> extendColumns = null;
			TableInputQuery query = new TableInputQuery();
			query.locked(0);
			query.type("useradd");
			try {
				List<TableInput> tables = tableInputService.list(query);
				if (tables != null && !tables.isEmpty()) {
					for (TableInput table : tables) {
						String tableId = table.getTableId();
						if (StringUtils.isNotEmpty(tableId)) {
							tableInput = tableInputService.getTableInputById(tableId);
							if (tableInput != null) {
								extendColumns = tableInputService.getTableInputColumnsByTableId(tableId);
								List<ColumnDefinition> extColumns = new ArrayList<ColumnDefinition>();
								if (extendColumns != null && !extendColumns.isEmpty()) {
									for (TableInputColumn col : extendColumns) {
										ColumnDefinition c = new ColumnDefinition();
										org.apache.commons.beanutils.PropertyUtils.copyProperties(c, col);
										c.setColumnName(col.getColumnName());
										c.setJavaType(col.getJavaType());
										c.setLength(col.getLength());
										extColumns.add(c);
									}
								}
							}
						}
					}

					return ResponseUtils.responseResult(true);
				}
			} catch (Exception ex) {
				logger.error(ex);
			} finally {
				tableInput = null;
				extendColumns = null;
			}
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * 更新表结构
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateSchema")
	@ResponseBody
	public byte[] updateSchema(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			TableInput tableInput = null;
			String tableId = request.getParameter("tableId");
			if (StringUtils.isNotEmpty(tableId)) {
				try {
					tableInput = tableInputService.getTableInputById(tableId);
					if (tableInput != null) {
						List<TableInputColumn> extendColumns = tableInputService.getTableInputColumnsByTableId(tableId);
						List<ColumnDefinition> extColumns = new ArrayList<ColumnDefinition>();
						if (extendColumns != null && !extendColumns.isEmpty()) {
							for (TableInputColumn col : extendColumns) {
								ColumnDefinition c = new ColumnDefinition();
								org.apache.commons.beanutils.PropertyUtils.copyProperties(c, col);
								c.setColumnName(col.getColumnName());
								c.setJavaType(col.getJavaType());
								c.setLength(col.getLength());
								extColumns.add(c);
							}
						}
						TableDomainFactory.updateSchema(tableInput.getTableName(), extColumns);
						return ResponseUtils.responseResult(true);
					}
				} catch (Exception ex) {
					logger.error(ex);
				}
			}
		}
		return ResponseUtils.responseResult(false);
	}

}
