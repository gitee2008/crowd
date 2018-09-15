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

package com.glaf.matrix.export.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.el.ExpressionTools;
import com.glaf.core.entity.SqlExecutor;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.LowerLinkedMap;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.QueryUtils;

import com.glaf.matrix.export.domain.XmlExport;
import com.glaf.matrix.export.domain.XmlExportItem;
import com.glaf.matrix.export.service.XmlExportItemService;
import com.glaf.matrix.export.service.XmlExportService;

public class XmlExportDataHandler implements XmlDataHandler {

	protected static final Log logger = LogFactory.getLog(XmlExportDataHandler.class);

	/**
	 * 增加XML节点
	 * 
	 * @param root
	 * @param databaseId
	 */
	@Override
	public void addChild(XmlExport root, org.dom4j.Element element, long databaseId) {
		Map<String, Element> elementMap = new HashMap<String, Element>();
		IDatabaseService databaseService = ContextFactory.getBean("databaseService");
		XmlExportService xmlExportService = ContextFactory.getBean("com.glaf.matrix.export.service.xmlExportService");
		XmlExportItemService xmlExportItemService = ContextFactory
				.getBean("com.glaf.matrix.export.service.xmlExportItemService");
		List<XmlExport> list = xmlExportService.getAllChildren(root.getNodeId());
		if (list != null && !list.isEmpty()) {
			for (XmlExport export : list) {
				List<XmlExport> children = xmlExportService.getChildrenWithItems(export.getNodeId());
				export.setChildren(children);
				if (export.getNodeParentId() == root.getNodeId()) {
					root.addChild(export);
				}
			}
		}

		// List<XmlExport> children =
		// xmlExportService.getChildrenWithItems(root.getNodeId());
		// root.setChildren(children);

		List<XmlExportItem> items = xmlExportItemService.getXmlExportItemsByExpId(root.getId());
		root.setItems(items);

		Database srcDatabase = databaseService.getDatabaseById(databaseId);
		elementMap.put(root.getId(), element);
		logger.debug("---------------------------gen xml----------------------------");
		this.addChild(root, element, srcDatabase, elementMap);
	}

	/**
	 * 增加XML节点
	 * 
	 * @param xmlExport
	 * @param databaseId
	 */
	@SuppressWarnings("unchecked")
	public void addChild(XmlExport current, org.dom4j.Element element, Database srcDatabase,
			Map<String, Element> elementMap) {
		XmlExportItemService xmlExportItemService = ContextFactory
				.getBean("com.glaf.matrix.export.service.xmlExportItemService");
		Map<String, Object> parameter = new HashMap<String, Object>();
		elementMap.put(current.getId(), element);
		Connection srcConn = null;
		PreparedStatement srcPsmt = null;
		ResultSet srcRs = null;
		String value = null;
		try {
			if (current.getItems() == null || current.getItems().isEmpty()) {
				List<XmlExportItem> items = xmlExportItemService.getXmlExportItemsByExpId(current.getId());
				current.setItems(items);
			}
			String sql = current.getSql();
			if (StringUtils.isNotEmpty(sql) && DBUtils.isLegalQuerySql(sql)) {
				Map<String, Object> params = current.getParameter();
				parameter.putAll(params);
				SqlExecutor sqlExecutor = QueryUtils.replaceSQL(sql, parameter);
				sql = sqlExecutor.getSql();
				logger.debug("sql:" + sql);
				// logger.debug("params:" + parameter);
				srcConn = DBConnectionFactory.getConnection(srcDatabase.getName());
				srcPsmt = srcConn.prepareStatement(sql);

				if (sqlExecutor.getParameter() != null) {
					// logger.debug("parameter:" + sqlExecutor.getParameter());
					List<Object> values = (List<Object>) sqlExecutor.getParameter();
					JdbcUtils.fillStatement(srcPsmt, values);
				}
				srcRs = srcPsmt.executeQuery();
				Map<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.putAll(params);
				List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

				if (StringUtils.equals(current.getResultFlag(), "S")) {
					if (srcRs.next()) {
						dataMap.putAll(this.toMap(srcRs));
					}
				} else {
					while (srcRs.next()) {
						resultList.add(this.toMap(srcRs));
					}
				}

				JdbcUtils.close(srcRs);
				JdbcUtils.close(srcPsmt);
				JdbcUtils.close(srcConn);

				logger.debug("result size:" + resultList.size());

				/**
				 * 处理单一记录
				 */
				if (StringUtils.equals(current.getResultFlag(), "S")) {
					if (current.getItems() != null && !current.getItems().isEmpty()) {
						for (XmlExportItem item : current.getItems()) {
							/**
							 * 处理属性
							 */
							if (StringUtils.equals(item.getTagFlag(), "A")) {
								if (StringUtils.isNotEmpty(item.getExpression())) {
									value = ExpressionTools.evaluate(item.getExpression(), dataMap);
								} else {
									value = ParamUtils.getString(dataMap, item.getName().toLowerCase());
								}
								if (StringUtils.isNotEmpty(value)) {
									current.getElement().addAttribute(item.getName(), value);
								}
							} else {
								current.getElement().addElement(item.getName(), value);
							}
						}
					}
				} else {
					/**
					 * 处理多条记录
					 */
					Element elem = null;
					Element parent = null;
					for (Map<String, Object> rowMap : resultList) {
						if (current.getParent() != null) {
							if (StringUtils.isNotEmpty(current.getMapping())) {
								if (rowMap.get(current.getMapping().trim().toLowerCase()) != null) {
									parent = elementMap
											.get(rowMap.get(current.getMapping().trim().toLowerCase()).toString());
									if (parent != null) {
										logger.debug("##当前节点:" + current.getXmlTag() + " 父节点名称:" + parent.getName());
									}
								}
							}
							if (parent == null) {
								parent = elementMap.get(current.getParent().getId());
								if (parent != null) {
									logger.debug("当前节点:" + current.getXmlTag() + " 父节点名称:" + parent.getName());
									// 取父节点
									elem = parent.addElement(current.getXmlTag());
									// logger.debug("------------加到父节点-----------");
								}
							}
						}

						if (elem != null && current.getItems() != null && !current.getItems().isEmpty()) {
							for (XmlExportItem item : current.getItems()) {
								/**
								 * 处理属性
								 */
								if (StringUtils.equals(item.getTagFlag(), "A")) {
									if (StringUtils.isNotEmpty(item.getExpression())) {
										value = ExpressionTools.evaluate(item.getExpression(), rowMap);
									} else {
										value = ParamUtils.getString(rowMap, item.getName().trim().toLowerCase());
									}
									if (StringUtils.isNotEmpty(value)) {
										// if ("GCXM".equals(current.getXmlTag())) {
										// logger.debug(item.getName()+"-->" + value);
										// }
										elem.addAttribute(item.getName(), value);
									}
								} else {
									elem.addElement(item.getName(), value);
								}
							}
						}

						/**
						 * 处理每条记录的子孙节点
						 */
						List<XmlExport> children = current.getChildren();
						if (children != null && !children.isEmpty()) {
							current.setDataMap(rowMap);
							this.processChildren(current, children, parameter, srcDatabase, elementMap);
						}
					}
				}
			} else {
				/**
				 * 未定义查询，只是XML节点的情况
				 */
				List<XmlExport> children = current.getChildren();
				if (children != null && !children.isEmpty()) {
					this.processChildren(current, children, parameter, srcDatabase, elementMap);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("execute sql query error", ex);
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(srcRs);
			JdbcUtils.close(srcPsmt);
			JdbcUtils.close(srcConn);
		}
	}

	protected void processChildren(XmlExport current, List<XmlExport> children, Map<String, Object> parameter,
			Database srcDatabase, Map<String, Element> elementMap) {
		if (children != null && !children.isEmpty()) {
			Element childElem = null;
			for (XmlExport child : children) {
				parameter.clear();
				parameter.putAll(current.getParameter());
				if (StringUtils.isNotEmpty(current.getName())) {
					Set<Entry<String, Object>> entrySet = current.getParameter().entrySet();
					for (Entry<String, Object> entry : entrySet) {
						String key = entry.getKey();
						Object value = entry.getValue();
						parameter.put(current.getName() + "_" + key, value);
					}
				}

				if (StringUtils.isNotEmpty(current.getName())) {
					Set<Entry<String, Object>> entrySet = current.getDataMap().entrySet();
					for (Entry<String, Object> entry : entrySet) {
						String key = entry.getKey();
						Object value = entry.getValue();
						parameter.put(current.getName() + "_" + key, value);
					}
				}

				child.setParameter(parameter);

				// logger.debug("parameter:" + parameter);

				if (child.getParent() != null) {
					Element parent = elementMap.get(child.getParent().getId());
					if (parent != null) {
						childElem = parent.addElement(child.getXmlTag());
					}
				}
				if (childElem != null) {
					child.setElement(childElem);
					if (current.getDataMap() != null && StringUtils.isNotEmpty(child.getMapping())) {
						if (current.getDataMap().get(child.getMapping().trim().toLowerCase()) != null) {
							elementMap.put(current.getDataMap().get(child.getMapping().trim().toLowerCase()).toString(),
									childElem);
						}
					}
					elementMap.put(child.getId(), childElem);
				}
				logger.debug("-------------<" + child.getXmlTag() + ">" + child.getTitle() + "------------");
				this.addChild(child, childElem, srcDatabase, elementMap);
			}
		}
	}

	public Map<String, Object> toMap(ResultSet rs) throws SQLException {
		Map<String, Object> result = new LowerLinkedMap();
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		for (int i = 1; i <= count; i++) {
			String columnName = rsmd.getColumnLabel(i);
			if (StringUtils.isEmpty(columnName)) {
				columnName = rsmd.getColumnName(i);
			}
			Object object = rs.getObject(i);
			columnName = columnName.toLowerCase();
			result.put(columnName, object);
		}
		return result;
	}

}
