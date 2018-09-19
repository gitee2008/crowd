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
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

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
import com.glaf.core.tree.component.TreeComponent;
import com.glaf.core.tree.component.TreeRepository;
import com.glaf.core.tree.helper.JacksonTreeHelper;
import com.glaf.core.tree.helper.XmlTreeHelper;
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

	protected IDatabaseService databaseService;

	protected XmlExportService xmlExportService;

	protected XmlExportItemService xmlExportItemService;

	public XmlExportDataHandler() {

	}

	/**
	 * 增加XML节点
	 * 
	 * @param xmlExport
	 *            导出定义
	 * @param root
	 *            根节点
	 * @param databaseId
	 *            数据库编号
	 */
	@Override
	public void addChild(XmlExport xmlExport, org.dom4j.Element root, long databaseId) {
		List<XmlExport> list = getXmlExportService().getAllChildren(xmlExport.getNodeId());
		if (list != null && !list.isEmpty()) {
			for (XmlExport export : list) {
				List<XmlExport> children = getXmlExportService().getChildrenWithItems(export.getNodeId());
				export.setChildren(children);
				if (export.getNodeParentId() == xmlExport.getNodeId()) {
					xmlExport.addChild(export);
				}
			}
		}

		List<XmlExportItem> items = getXmlExportItemService().getXmlExportItemsByExpId(xmlExport.getId());
		xmlExport.setItems(items);

		Database srcDatabase = getDatabaseService().getDatabaseById(databaseId);

		if (xmlExport.getNodeParentId() == 0) {// 顶层节点，只能有一个根节点
			// 根据定义补上根节点的属性
		}

		List<XmlExport> children = getXmlExportService().getChildrenWithItems(xmlExport.getNodeId());
		if (children != null && !children.isEmpty()) {
			xmlExport.setElement(root);
			logger.debug("---------------------------gen child xml----------------------------");
			for (XmlExport child : children) {
				child.setParent(xmlExport);
				int retry = 0;
				boolean success = false;
				while (retry < 3 && !success) {
					try {
						retry++;
						this.addChild(child, srcDatabase);
						success = true;
					} catch (Exception ex) {
						logger.error(ex);
						try {
							TimeUnit.MILLISECONDS.sleep(20 + new Random().nextInt(50));
						} catch (InterruptedException e) {
						}
					}
				}
			}
		}
	}

	/**
	 * 增加XML节点
	 * 
	 * @param xmlExport
	 * @param databaseId
	 */
	@SuppressWarnings("unchecked")
	public void addChild(XmlExport current, Database srcDatabase) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		Connection srcConn = null;
		PreparedStatement srcPsmt = null;
		ResultSet srcRs = null;
		String value = null;
		try {
			if (current.getItems() == null || current.getItems().isEmpty()) {
				List<XmlExportItem> items = getXmlExportItemService().getXmlExportItemsByExpId(current.getId());
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
					// logger.debug("params:" + parameter);
					logger.debug("parameter:" + sqlExecutor.getParameter());
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
				current.setDataList(resultList);

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

					/**
					 * 处理树形结构的叶子节点
					 */
					if (StringUtils.equals(current.getTreeFlag(), "Y")
							&& StringUtils.equals(current.getLeafFlag(), "Y")) {
						List<TreeComponent> trees = new ArrayList<TreeComponent>();
						for (Map<String, Object> rowMap : resultList) {
							TreeComponent tree = new TreeComponent();
							tree.setId(ParamUtils.getString(rowMap, "ext_tree_id"));
							tree.setParentId(ParamUtils.getString(rowMap, "ext_tree_parentid"));
							tree.setDataMap(rowMap);
							if (StringUtils.equals(tree.getParentId(), "0")
									|| StringUtils.equals(tree.getParentId(), "-1")) {
								tree.setParentId(null);
							}
							trees.add(tree);
						}
						// this.processTreeNode(current, trees);
						Map<String, String> elemMap = new HashMap<String, String>();
						for (XmlExportItem item : current.getItems()) {
							elemMap.put(item.getName(), item.getTagFlag());
						}
						XmlTreeHelper xmlTreeHelper = new XmlTreeHelper();
						xmlTreeHelper.appendChild(current.getElement(), current.getXmlTag(), elemMap, trees);
					} else {
						for (Map<String, Object> rowMap : resultList) {
							/**
							 * 在这个节点的父节点上添加下级节点
							 */
							elem = current.getParent().getElement().addElement(current.getXmlTag());
							// logger.debug("----<" + current.getXmlTag() + ">" + current.getTitle() +
							// "----");
							// logger.debug("elem:" + elem);
							// logger.debug("current.getItems():" + current.getItems());
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
											elem.addAttribute(item.getName(), value);
										}
									} else {
										elem.addElement(item.getName(), value);
									}
								}
							}

							// logger.debug("elem:" + elem);

							/**
							 * 处理每条记录的子孙节点
							 */
							List<XmlExport> children = current.getChildren();
							if (children == null || children.isEmpty()) {
								if (!StringUtils.equals(current.getLeafFlag(), "Y")) {
									children = getXmlExportService().getChildrenWithItems(current.getNodeId());
								}
							}
							// logger.debug("->children:" + children);
							if (children != null && !children.isEmpty()) {
								// current.setDataMap(rowMap);
								current.setElement(elem);

								if (StringUtils.isNotEmpty(current.getName())) {
									Set<Entry<String, Object>> entrySet = current.getParameter().entrySet();
									for (Entry<String, Object> entry : entrySet) {
										String key = entry.getKey();
										Object val = entry.getValue();
										parameter.put(current.getName() + "_" + key, val);
									}
								}

								if (StringUtils.isNotEmpty(current.getMapping())) {
									Set<Entry<String, Object>> entrySet = current.getParameter().entrySet();
									for (Entry<String, Object> entry : entrySet) {
										String key = entry.getKey();
										Object val = entry.getValue();
										parameter.put(current.getMapping() + "_" + key, val);
									}
								}

								for (XmlExport child : children) {

									if (StringUtils.isNotEmpty(current.getName())) {
										Set<Entry<String, Object>> entrySet = rowMap.entrySet();
										for (Entry<String, Object> entry : entrySet) {
											String key = entry.getKey();
											Object val = entry.getValue();
											parameter.put(current.getName() + "_" + key, val);
										}
									}

									if (StringUtils.isNotEmpty(current.getMapping())) {
										Set<Entry<String, Object>> entrySet = rowMap.entrySet();
										for (Entry<String, Object> entry : entrySet) {
											String key = entry.getKey();
											Object val = entry.getValue();
											parameter.put(current.getMapping() + "_" + key, val);
										}
									}

									child.setParent(current);
									child.setParameter(parameter);

									int retry = 0;
									boolean success = false;
									while (retry < 3 && !success) {
										try {
											retry++;
											this.addChild(child, srcDatabase);
											success = true;
										} catch (Exception ex) {
											logger.error(ex);
											try {
												TimeUnit.MILLISECONDS.sleep(20 + new Random().nextInt(50));
											} catch (InterruptedException e) {
											}
										}
									}
								}
							}
						}
					}
				}
			} else {
				/**
				 * 未定义查询，只是XML节点的情况
				 */
				List<XmlExport> children = current.getChildren();
				if (children == null || children.isEmpty()) {
					if (!StringUtils.equals(current.getLeafFlag(), "Y")) {
						children = getXmlExportService().getChildrenWithItems(current.getNodeId());
					}
				}
				// logger.debug("->children:" + children);
				if (children != null && !children.isEmpty()) {
					for (XmlExport child : children) {
						child.setParent(current);
						if (current.getElement() == null) {
							Element elem = current.getParent().getElement().addElement(child.getXmlTag());
							current.setElement(elem);
						}
						/**
						 * 在子节点的父节点上添加下级节点
						 */
						if (current.getElement() != null) {
							Element childElem = current.getElement().addElement(child.getXmlTag());
							child.setElement(childElem);
							logger.debug("---------<" + child.getXmlTag() + ">" + child.getTitle() + "------------");
							int retry = 0;
							boolean success = false;
							while (retry < 3 && !success) {
								try {
									retry++;
									this.addChild(child, srcDatabase);
									success = true;
								} catch (Exception ex) {
									logger.error(ex);
									try {
										TimeUnit.MILLISECONDS.sleep(20 + new Random().nextInt(50));
									} catch (InterruptedException e) {
									}
								}
							}
						}
					}
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

	protected void processTreeNode(XmlExport current, List<TreeComponent> trees) {
		JacksonTreeHelper builder = new JacksonTreeHelper();
		TreeRepository treeRepository = builder.buildTree(trees);
		if (treeRepository != null) {
			String value = null;
			List<TreeComponent> components = treeRepository.getTopTrees();
			if (components != null && !components.isEmpty()) {
				for (TreeComponent component : components) {
					Element element = current.getParent().getElement().addElement(current.getXmlTag());
					for (XmlExportItem item : current.getItems()) {
						/**
						 * 处理属性
						 */
						if (StringUtils.equals(item.getTagFlag(), "A")) {
							if (StringUtils.isNotEmpty(item.getExpression())) {
								value = ExpressionTools.evaluate(item.getExpression(), component.getDataMap());
							} else {
								value = ParamUtils.getString(component.getDataMap(),
										item.getName().trim().toLowerCase());
							}
							if (StringUtils.isNotEmpty(value)) {
								element.addAttribute(item.getName(), value);
							}
						} else {
							element.addElement(item.getName(), value);
						}
					}
					this.processChildTreeNode(current, element, component);
				}
			}
		}
	}

	protected void processChildTreeNode(XmlExport current, Element element, TreeComponent treeComponent) {
		List<TreeComponent> components = treeComponent.getComponents();
		if (components != null && !components.isEmpty()) {
			String value = null;
			Element elem = element.addElement(current.getXmlTag());
			for (TreeComponent child : components) {
				for (XmlExportItem item : current.getItems()) {
					/**
					 * 处理属性
					 */
					if (StringUtils.equals(item.getTagFlag(), "A")) {
						if (StringUtils.isNotEmpty(item.getExpression())) {
							value = ExpressionTools.evaluate(item.getExpression(), child.getDataMap());
						} else {
							value = ParamUtils.getString(child.getDataMap(), item.getName().trim().toLowerCase());
						}
						if (StringUtils.isNotEmpty(value)) {
							elem.addAttribute(item.getName(), value);
						}
					} else {
						elem.addElement(item.getName(), value);
					}
				}
				this.processChildTreeNode(current, elem, child);
			}
		}
	}

	public IDatabaseService getDatabaseService() {
		if (databaseService == null) {
			databaseService = ContextFactory.getBean("databaseService");
		}
		return databaseService;
	}

	public XmlExportItemService getXmlExportItemService() {
		if (xmlExportItemService == null) {
			xmlExportItemService = ContextFactory.getBean("com.glaf.matrix.export.service.xmlExportItemService");
		}
		return xmlExportItemService;
	}

	public XmlExportService getXmlExportService() {
		if (xmlExportService == null) {
			xmlExportService = ContextFactory.getBean("com.glaf.matrix.export.service.xmlExportService");
		}
		return xmlExportService;
	}

	public void setDatabaseService(IDatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	public void setXmlExportItemService(XmlExportItemService xmlExportItemService) {
		this.xmlExportItemService = xmlExportItemService;
	}

	public void setXmlExportService(XmlExportService xmlExportService) {
		this.xmlExportService = xmlExportService;
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
