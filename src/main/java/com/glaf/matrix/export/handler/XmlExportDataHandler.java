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
import com.glaf.core.util.CaseInsensitiveHashMap;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.QueryUtils;
import com.glaf.core.util.StringTools;
import com.glaf.matrix.export.domain.XmlExport;
import com.glaf.matrix.export.domain.XmlExportItem;
import com.glaf.matrix.export.service.XmlExportService;

public class XmlExportDataHandler implements XmlDataHandler {

	protected static final Log logger = LogFactory.getLog(XmlExportDataHandler.class);

	/**
	 * 增加XML节点
	 * 
	 * @param xmlExport
	 * @param databaseId
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void addChild(XmlExport root, org.dom4j.Element element, long databaseId, Map<String, Element> elementMap) {
		IDatabaseService databaseService = ContextFactory.getBean("databaseService");
		XmlExportService xmlExportService = ContextFactory.getBean("com.glaf.matrix.export.service.xmlExportService");
		Map<String, Object> parameter = new HashMap<String, Object>();
		elementMap.put(root.getId(), element);
		Database srcDatabase = null;
		Connection srcConn = null;
		PreparedStatement srcPsmt = null;
		ResultSet srcRs = null;
		String value = null;
		try {
			srcDatabase = databaseService.getDatabaseById(databaseId);

			String sql = root.getSql();
			if (StringUtils.isNotEmpty(sql) && DBUtils.isLegalQuerySql(sql)) {
				Map<String, Object> params = root.getParameter();
				parameter.putAll(params);
				SqlExecutor sqlExecutor = QueryUtils.replaceSQL(sql, parameter);
				sql = sqlExecutor.getSql();
				srcConn = DBConnectionFactory.getConnection(srcDatabase.getName());
				srcPsmt = srcConn.prepareStatement(sql);
				if (sqlExecutor.getParameter() != null) {
					logger.debug("parameter:" + sqlExecutor.getParameter());
					List<Object> values = (List<Object>) sqlExecutor.getParameter();
					JdbcUtils.fillStatement(srcPsmt, values);
				}
				srcRs = srcPsmt.executeQuery();
				Map<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.putAll(params);
				List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

				if (StringUtils.equals(root.getResultFlag(), "S")) {
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

				/**
				 * 处理单一记录
				 */
				if (StringUtils.equals(root.getResultFlag(), "S")) {
					if (root.getItems() != null && !root.getItems().isEmpty()) {
						for (XmlExportItem item : root.getItems()) {
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
									root.getElement().addAttribute(item.getName(), value);
								}
							} else {
								root.getElement().addElement(item.getName(), value);
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
						if (root.getParent() != null) {
							parent = elementMap.get(root.getParent().getId());
							if (parent != null) {
								// 取父节点
								elem = parent.addElement(root.getXmlTag());
								logger.debug("------------加到父节点-----------");
							}
						}
						if (elem != null && root.getItems() != null && !root.getItems().isEmpty()) {
							for (XmlExportItem item : root.getItems()) {
								/**
								 * 处理属性
								 */
								if (StringUtils.equals(item.getTagFlag(), "A")) {
									if (StringUtils.isNotEmpty(item.getExpression())) {
										value = ExpressionTools.evaluate(item.getExpression(), rowMap);
									} else {
										value = ParamUtils.getString(rowMap, item.getName().toLowerCase());
									}
									if (StringUtils.isNotEmpty(value)) {
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
						List<XmlExport> children = xmlExportService.getChildrenWithItems(root.getNodeId());
						if (children != null && !children.isEmpty()) {
							this.processChildren(root, children, parameter, databaseId, elementMap);
						}
					}
				}
			} else {
				/**
				 * 未定义查询，只是XML节点的情况
				 */
				List<XmlExport> children = xmlExportService.getChildrenWithItems(root.getNodeId());
				if (children != null && !children.isEmpty()) {
					this.processChildren(root, children, parameter, databaseId, elementMap);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("execute sql error", ex);
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(srcRs);
			JdbcUtils.close(srcPsmt);
			JdbcUtils.close(srcConn);
		}
	}

	protected void processChildren(XmlExport root, List<XmlExport> children, Map<String, Object> parameter,
			long databaseId, Map<String, Element> elementMap) {
		if (children != null && !children.isEmpty()) {
			Element childElem = null;
			for (XmlExport child : children) {
				parameter.clear();
				parameter.putAll(root.getParameter());
				child.setParameter(parameter);

				if (child.getParent() != null) {
					Element parent = elementMap.get(child.getParent().getId());
					if (parent != null) {
						childElem = parent.addElement(child.getXmlTag());
					}
				}
				if (childElem != null) {
					child.setElement(childElem);
					elementMap.put(child.getId(), childElem);
				}
				logger.debug("-------------<" + child.getXmlTag() + ">" + child.getTitle() + "---------------------");
				this.addChild(child, childElem, databaseId, elementMap);
			}
		}
	}

	public Map<String, Object> toMap(ResultSet rs) throws SQLException {
		Map<String, Object> result = new CaseInsensitiveHashMap();
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		for (int i = 1; i <= count; i++) {
			String columnName = rsmd.getColumnLabel(i);
			if (StringUtils.isEmpty(columnName)) {
				columnName = rsmd.getColumnName(i);
			}
			Object object = rs.getObject(i);
			columnName = columnName.toLowerCase();
			String name = StringTools.camelStyle(columnName);
			result.put(name, object);
			result.put(columnName, object);
		}
		return result;
	}

}
