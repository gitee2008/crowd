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
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.glaf.core.config.Environment;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.jdbc.BulkInsertBean;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.EntityService;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.service.ITablePageService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.RequestUtils;

import com.glaf.matrix.dataimport.bean.TableInputDataBean;
import com.glaf.matrix.dataimport.domain.TableInput;
import com.glaf.matrix.dataimport.domain.TableInputColumn;
import com.glaf.matrix.dataimport.parser.ParserFactory;
import com.glaf.matrix.dataimport.service.TableInputService;

@Controller("/matrix/tableInputData")
@RequestMapping("/matrix/tableInputData")
public class TableInputDataController {

	protected static final Log logger = LogFactory.getLog(TableInputDataController.class);

	protected EntityService entityService;

	protected IDatabaseService databaseService;

	protected ITableDataService tableDataService;

	protected TableInputService tableInputService;

	protected ITablePageService tablePageService;

	@RequestMapping("/datalist")
	public ModelAndView datalist(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		request.setAttribute("canEdit", true);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		String tableId = request.getParameter("tableId");
		if (StringUtils.isNotEmpty(tableId)) {
			TableInput tableInput = tableInputService.getTableInputById(tableId);
			if (tableInput != null) {
				List<TableInputColumn> columns = tableInputService.getTableInputColumnsByTableId(tableId);
				if (columns != null && !columns.isEmpty()) {
					List<TableInputColumn> list = new ArrayList<TableInputColumn>();
					for (TableInputColumn column : columns) {
						if (column.getDisplayType() == 2 || column.getDisplayType() == 4) {
							list.add(column);
						}
					}
					request.setAttribute("columns", columns);
					request.setAttribute("table", tableInput);
					request.setAttribute("tableInput", tableInput);
				}
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/matrix/tableInputData/datalist", modelMap);
	}

	@ResponseBody
	@RequestMapping("/json")
	public byte[] json(HttpServletRequest request) throws IOException {
		TableInputDataBean tableDataBean = new TableInputDataBean();
		JSONObject result = tableDataBean.toJson(request);
		return result.toJSONString().getBytes("UTF-8");
	}

	@javax.annotation.Resource
	public void setDatabaseService(IDatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	@javax.annotation.Resource
	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}

	@javax.annotation.Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

	@javax.annotation.Resource
	public void setTableInputService(TableInputService tableInputService) {
		this.tableInputService = tableInputService;
	}

	@javax.annotation.Resource
	public void setTablePageService(ITablePageService tablePageService) {
		this.tablePageService = tablePageService;
	}

	@RequestMapping("/showUpload")
	public ModelAndView showUpload(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		request.setAttribute("canEdit", true);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		String tableId = request.getParameter("tableId");
		if (StringUtils.isNotEmpty(tableId)) {
			TableInput tableInput = tableInputService.getTableInputById(tableId);
			if (tableInput != null) {
				request.setAttribute("table", tableInput);
				request.setAttribute("tableInput", tableInput);
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/matrix/tableInputData/showUpload", modelMap);
	}

	@RequestMapping(path = "/upload", method = RequestMethod.POST)
	public void upload(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("file") MultipartFile mFile) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String tableId = request.getParameter("tableId");
		if (StringUtils.isNotEmpty(tableId)) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			String systemName = Environment.DEFAULT_SYSTEM_NAME;
			TableInput tableInput = null;
			Connection conn = null;
			PrintWriter writer = null;
			try {
				tableInput = tableInputService.getTableInputById(tableId);
				if (tableInput != null) {
					String fileType = FileUtils.getFileExt(mFile.getOriginalFilename());
					List<Map<String, Object>> dataList = ParserFactory.getInstance().getParser(fileType)
							.parse(tableInput, mFile.getInputStream());
					if (dataList != null && !dataList.isEmpty()) {
						TableDefinition tableDefinition = new TableDefinition();
						tableDefinition.setTableName(tableInput.getTableName());

						if (tableInput.getDatabaseId() > 0) {
							Database db = databaseService.getDatabaseById(tableInput.getDatabaseId());
							systemName = db.getName();
						}

						conn = DBConnectionFactory.getConnection(systemName);
						conn.setAutoCommit(false);

						List<ColumnDefinition> columns = DBUtils.getColumnDefinitions(conn, tableInput.getTableName());
						tableDefinition.setColumns(columns);

						if (StringUtils.equals(tableInput.getDeleteFetch(), "Y")) {
							String sql = " delete from " + tableDefinition.getTableName() + " where CATEGORY_ = '"
									+ tableInput.getTableId() + "' ";
							DBUtils.executeSchemaResource(conn, sql);
						}
						BulkInsertBean bean = new BulkInsertBean();
						bean.bulkInsert(loginContext, conn, tableDefinition, dataList);
						conn.commit();

						writer = response.getWriter();
						writer.write("<br><h3><span style='color:#33cc33'>导入成功！</span></h3>");
						writer.flush();

					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
				writer = response.getWriter();
				writer.write("<br><h3><span style='color:#ff3333'>导入失败！</span></h3>");
				writer.flush();
			} finally {
				JdbcUtils.close(conn);
			}
		}
	}

}
