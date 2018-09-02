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

package com.glaf.matrix.dataimport.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.glaf.core.base.BaseItem;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.EntityService;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.service.ITablePageService;
import com.glaf.core.util.Constants;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.LowerLinkedMap;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;

import com.glaf.matrix.data.domain.DataModel;
import com.glaf.matrix.data.factory.DataItemFactory;
import com.glaf.matrix.dataimport.domain.TableInput;
import com.glaf.matrix.dataimport.domain.TableInputColumn;
import com.glaf.matrix.dataimport.service.TableInputService;

public class TableInputDataBean {
	protected final static Log logger = LogFactory.getLog(TableInputDataBean.class);

	protected EntityService entityService;

	protected IDatabaseService databaseService;

	protected TableInputService tableInputService;

	protected ITablePageService tablePageService;

	public IDatabaseService getDatabaseService() {
		if (databaseService == null) {
			databaseService = ContextFactory.getBean("databaseService");
		}
		return databaseService;
	}

	public DataModel getDataModel(LoginContext loginContext, TableInput tableInput, String uuid) {
		Map<String, Object> rowMap = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tenantId", loginContext.getTenantId());
		params.put("uuid", uuid);
		if (StringUtils.isNotEmpty(uuid)) {
			String tableName = tableInput.getTableName();
			StringBuilder sqlBuffer = new StringBuilder();
			sqlBuffer.append(" select E.*  from ").append(tableName).append(" E ");
			sqlBuffer.append(" where 1=1 ");
			sqlBuffer.append(" and E.UUID_ = #{uuid} ");
			if (StringUtils.isNotEmpty(loginContext.getTenantId())) {
				sqlBuffer.append(" and E.TENANTID_ = #{tenantId} ");
			}
			rowMap = getTablePageService().getOne(sqlBuffer.toString(), params);
		}
		if (rowMap != null && !rowMap.isEmpty()) {
			LowerLinkedMap dataMap = new LowerLinkedMap();
			dataMap.putAll(rowMap);
			DataModel dataModel = new DataModel();
			dataModel.setDataMap(rowMap);
			this.populate(rowMap, dataModel);
			return dataModel;
		}
		return null;
	}

	public DataModel getDataModelById(LoginContext loginContext, TableInput tableInput, long id) {
		Map<String, Object> rowMap = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tenantId", loginContext.getTenantId());
		params.put("id", id);
		if (id > 0) {
			String tableName = tableInput.getTableName();
			StringBuilder sqlBuffer = new StringBuilder();
			sqlBuffer.append(" select E.*  from ").append(tableName).append(" E ");
			sqlBuffer.append(" where 1=1 ");
			sqlBuffer.append(" and E.ID_ = #{id} ");
			if (StringUtils.isNotEmpty(loginContext.getTenantId())) {
				sqlBuffer.append(" and E.TENANTID_ = #{tenantId} ");
			}
			rowMap = getTablePageService().getOne(sqlBuffer.toString(), params);
		}
		if (rowMap != null && !rowMap.isEmpty()) {
			LowerLinkedMap dataMap = new LowerLinkedMap();
			dataMap.putAll(rowMap);
			DataModel dataModel = new DataModel();
			dataModel.setDataMap(rowMap);
			this.populate(rowMap, dataModel);
			return dataModel;
		}
		return null;
	}

	public EntityService getEntityService() {
		if (entityService == null) {
			entityService = ContextFactory.getBean("entityService");
		}
		return entityService;
	}

	public Map<String, Object> getRowMapById(LoginContext loginContext, TableInput tableInput, long id) {
		Map<String, Object> rowMap = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tenantId", loginContext.getTenantId());
		params.put("id", id);
		if (id > 0) {
			String tableName = tableInput.getTableName();
			StringBuilder sqlBuffer = new StringBuilder();
			sqlBuffer.append(" select E.*  from ").append(tableName).append(" E ");
			sqlBuffer.append(" where 1=1 ");
			sqlBuffer.append(" and E.ID_ = #{id} ");
			if (StringUtils.isNotEmpty(loginContext.getTenantId())) {
				sqlBuffer.append(" and E.TENANTID_ = #{tenantId} ");
			}
			rowMap = getTablePageService().getOne(sqlBuffer.toString(), params);
		}
		if (rowMap != null && !rowMap.isEmpty()) {
			LowerLinkedMap dataMap = new LowerLinkedMap();
			dataMap.putAll(rowMap);
			return dataMap;
		}
		return null;
	}

	public Map<String, Object> getSimpleRowMap(LoginContext loginContext, TableInput tableInput, String uuid) {
		Map<String, Object> rowMap = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tenantId", loginContext.getTenantId());
		params.put("uuid", uuid);
		if (StringUtils.isNotEmpty(uuid)) {
			String tableName = tableInput.getTableName();
			StringBuilder sqlBuffer = new StringBuilder();
			sqlBuffer.append(
					" select E.ID_ as \"id\", E.CREATEBY_ as \"createby\", E.ORGANIZATIONID_ as \"organizationid\" from ")
					.append(tableName).append(" E ");
			sqlBuffer.append(" where 1=1 ");
			sqlBuffer.append(" and E.UUID_ = #{uuid} ");
			if (StringUtils.isNotEmpty(loginContext.getTenantId())) {
				sqlBuffer.append(" and E.TENANTID_ = #{tenantId} ");
			}
			rowMap = getTablePageService().getOne(sqlBuffer.toString(), params);
		}
		if (rowMap != null && !rowMap.isEmpty()) {
			LowerLinkedMap dataMap = new LowerLinkedMap();
			dataMap.putAll(rowMap);
			return dataMap;
		}
		return null;
	}

	public ITablePageService getTablePageService() {
		if (tablePageService == null) {
			tablePageService = ContextFactory.getBean("tablePageService");
		}
		return tablePageService;
	}

	public TableInputService getTableInputService() {
		if (tableInputService == null) {
			tableInputService = ContextFactory.getBean("tableInputService");
		}
		return tableInputService;
	}

	public boolean hasPermission(LoginContext loginContext, TableInput tableInput, String privilege) {
		if (StringUtils.equals(tableInput.getPrivilegeFlag(), "N")) {
			return true;
		}
		if (loginContext.isSystemAdministrator()) {
			return true;
		}
		if (loginContext.getRoles().contains("TenantAdmin")) {
			return true;
		}

		return false;
	}

	public void populate(Map<String, Object> dataMap, DataModel dataModel) {
		dataModel.setId(ParamUtils.getLong(dataMap, "id_"));
		dataModel.setTopId(ParamUtils.getLong(dataMap, "topid_"));
		dataModel.setParentId(ParamUtils.getLong(dataMap, "parentid_"));
		dataModel.setApprover(ParamUtils.getString(dataMap, "approver_"));
		dataModel.setApprovalDate(ParamUtils.getDate(dataMap, "approvaldate_"));
		dataModel.setName(ParamUtils.getString(dataMap, "name_"));
		dataModel.setCode(ParamUtils.getString(dataMap, "code_"));
		dataModel.setDesc(ParamUtils.getString(dataMap, "desc_"));
		dataModel.setDiscriminator(ParamUtils.getString(dataMap, "discriminator_"));
		dataModel.setTitle(ParamUtils.getString(dataMap, "title_"));
		dataModel.setType(ParamUtils.getString(dataMap, "type_"));
		dataModel.setTreeId(ParamUtils.getString(dataMap, "treeid_"));
		dataModel.setGradeId(ParamUtils.getString(dataMap, "gradeid_"));
		dataModel.setOrganizationId(ParamUtils.getLong(dataMap, "organizationid_"));
		dataModel.setTenantId(ParamUtils.getString(dataMap, "tenantid_"));
		dataModel.setIcon(ParamUtils.getString(dataMap, "icon_"));
		dataModel.setIconCls(ParamUtils.getString(dataMap, "iconcls_"));
		dataModel.setLevel(ParamUtils.getInt(dataMap, "level_"));
		dataModel.setSortNo(ParamUtils.getInt(dataMap, "sortno_"));
		dataModel.setBusinessStatus(ParamUtils.getInt(dataMap, "business_status_"));
		dataModel.setCreateBy(ParamUtils.getString(dataMap, "createby_"));
		dataModel.setCreateTime(ParamUtils.getDate(dataMap, "createtime_"));
		dataModel.setUpdateBy(ParamUtils.getString(dataMap, "updateby_"));
		dataModel.setUpdateTime(ParamUtils.getDate(dataMap, "updatetime_"));
		dataModel.setDeleteFlag(ParamUtils.getInt(dataMap, "deleteflag_"));
		dataModel.setDeleteTime(ParamUtils.getDate(dataMap, "deletetime_"));
	}

	public JSONObject toJson(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		JSONObject result = new JSONObject();
		String tableId = request.getParameter("tableId");
		if (StringUtils.isNotEmpty(tableId)) {
			TableInput tableInput = getTableInputService().getTableInputById(tableId);
			if (tableInput != null) {

				if (!this.hasPermission(loginContext, tableInput, Constants.PRIVILEGE_READ)) {
					return result;
				}
				String tableName = tableInput.getTableName();

				List<TableInputColumn> columns = getTableInputService().getTableInputColumnsByTableId(tableId);
				if (columns != null && !columns.isEmpty()) {
					List<String> list = new ArrayList<String>();
					list.add("id_");
					list.add("parentid_");
					list.add("topid_");
					list.add("treeid_");
					list.add("uuid_");
					list.add("name_");
					list.add("code_");
					list.add("title_");
					list.add("type_");
					list.add("level_");
					list.add("sortno_");
					list.add("locked_");
					list.add("organizationid_");
					list.add("gradeid_");
					list.add("createby_");
					list.add("createtime_");
					list.add("business_status_");
					list.add("approvaldate_");
					list.add("approver_");

					Map<String, TableInputColumn> columnMap = new HashMap<String, TableInputColumn>();
					for (TableInputColumn column : columns) {
						columnMap.put(column.getId(), column);
						columnMap.put(column.getColumnName().toLowerCase(), column);
						list.add(column.getColumnName().toLowerCase());

						if (StringUtils.isNotEmpty(column.getDataCode())) {
							if (StringUtils.startsWith(column.getDataCode(), "@sys_dict:")) {
								long nodeId = Long
										.parseLong(column.getDataCode().substring(10, column.getDataCode().length()));
								List<BaseItem> items = DataItemFactory.getInstance().getDictoryItems(nodeId);
								column.setItems(items);
								if (items != null && !items.isEmpty()) {
									for (BaseItem item : items) {
										column.addProperty(item.getValue(), item.getName());
									}
								}
							} else if (StringUtils.startsWith(column.getDataCode(), "@sql:")) {
								String rowId = column.getDataCode().substring(5, column.getDataCode().length());
								List<BaseItem> items = DataItemFactory.getInstance().getSqlDataItems(loginContext,
										Long.parseLong(rowId), params);
								column.setItems(items);
								if (items != null && !items.isEmpty()) {
									for (BaseItem item : items) {
										column.addProperty(item.getValue(), item.getName());
									}
								}
							} else if (StringUtils.startsWith(column.getDataCode(), "@table:")) {
								String rowId = column.getDataCode().substring(7, column.getDataCode().length());
								List<BaseItem> items = DataItemFactory.getInstance().getTableItems(loginContext, rowId,
										params);
								column.setItems(items);
								if (items != null && !items.isEmpty()) {
									for (BaseItem item : items) {
										column.addProperty(item.getValue(), item.getName());
									}
								}
							}
						}
					}

					int start = 0;
					int limit = 20;
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
						limit = 20;
					}

					if (request.getAttribute("exportXls") != null) {
						limit = 50000;
					}

					StringBuilder sqlBuffer = new StringBuilder();
					sqlBuffer.append(" from ").append(tableName).append(" E ");
					sqlBuffer.append(" where 1=1 ");

					sqlBuffer.append(" and E.TABLEID_ = '").append(tableId).append("' ");

					if (!loginContext.isSystemAdministrator()) {
						sqlBuffer.append(" and E.TENANTID_ = '").append(loginContext.getTenantId()).append("' ");

						if (loginContext.getRoles().contains("SeniorManager")
								|| loginContext.getRoles().contains("TenantAdmin")) {
							Collection<Long> subOrganizationIds = loginContext.getSubOrganizationIds();
							if (subOrganizationIds == null) {
								subOrganizationIds = new HashSet<Long>();
							}
							subOrganizationIds.add(loginContext.getOrganizationId());

						}
					}

					try {
						int total = getTablePageService().getQueryCount(" select count(*) " + sqlBuffer.toString(),
								params);
						if (total > 0) {
							if (orderName != null && list.contains(orderName.trim().toLowerCase())) {
								sqlBuffer.append(" order by E.").append(orderName);
								if (StringUtils.equalsIgnoreCase(order, "desc")) {
									sqlBuffer.append(" desc ");
								}
							} else {
								if (StringUtils.isNotEmpty(tableInput.getSortColumn())) {
									sqlBuffer.append(" order by E.").append(tableInput.getSortColumn());
									if (StringUtils.isNotEmpty(tableInput.getSortOrder())) {
										sqlBuffer.append(" ").append(tableInput.getSortOrder());
									}
								} else {

									sqlBuffer.append(" order by E.SORTNO_ asc, E.CREATETIME_ desc ");

								}
							}
							List<Map<String, Object>> dataList = getTablePageService()
									.getListData(" select E.* " + sqlBuffer.toString(), params, start, limit);
							if (dataList != null && !dataList.isEmpty()) {
								JSONArray rowsJSON = new JSONArray();
								Map<String, Object> dataMap = null;
								int index = start;
								Date date = null;
								for (Map<String, Object> rowMap : dataList) {
									dataMap = new LowerLinkedMap();
									dataMap.putAll(rowMap);
									index++;
									JSONObject json = new JSONObject();
									for (String columnName : list) {
										if (StringUtils.equalsIgnoreCase("id_", columnName)) {
											json.put("id", dataMap.get(columnName));
										}
										if (StringUtils.equalsIgnoreCase("uuid_", columnName)) {
											json.put("uuid", dataMap.get(columnName));
										}
										TableInputColumn col = columnMap.get(columnName);
										if (col != null && col.getJavaType() != null) {
											switch (col.getJavaType()) {
											case "Date":
												date = ParamUtils.getDate(dataMap, columnName);
												if (date != null) {
													if (StringUtils.equals(col.getFormatter(), "yyyy-MM-dd")) {
														json.put(columnName, DateUtils.getDate(date));
													} else {
														json.put(columnName, DateUtils.getDateTime(date));
													}
												}
												break;
											default:
												if (col.getProperties() != null && !col.getProperties().isEmpty()) {
													json.put(columnName,
															col.getProperties().get(dataMap.get(columnName)));
												} else {
													json.put(columnName, dataMap.get(columnName));
												}
												break;
											}
										} else {
											json.put(columnName, dataMap.get(columnName));
										}
									}
									json.put("startIndex", index);
									rowsJSON.add(json);
								}
								result.put("rows", rowsJSON);
							}
						}

						result.put("total", total);
						result.put("totalCount", total);
						result.put("totalRecords", total);
						result.put("start", start);
						result.put("startIndex", start);
						result.put("limit", limit);
						result.put("pageSize", limit);

					} catch (Exception ex) {
						logger.error(ex);
					}
				}
			}
		}
		// logger.debug(result.toJSONString());
		return result;
	}

}
