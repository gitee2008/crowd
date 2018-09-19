package com.glaf.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONArray;
import com.glaf.core.el.ExpressionTools;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.tree.component.TreeComponent;
import com.glaf.core.tree.component.TreeRepository;
import com.glaf.core.tree.helper.TreeHelper;
import com.glaf.core.tree.helper.TreeRepositoryBuilder;
import com.glaf.core.util.Dom4jUtils;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.LowerLinkedMap;
import com.glaf.core.util.ParamUtils;
import com.glaf.matrix.export.domain.XmlExport;
import com.glaf.matrix.export.domain.XmlExportItem;

public class TreeTest {

	public static void processChildTreeNode(XmlExport current, Element element, TreeComponent treeComponent) {
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
				processChildTreeNode(current, elem, child);
			}
		}
	}

	public static Map<String, Object> toMap(ResultSet rs) throws SQLException {
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

	public static void main(String[] args) throws Exception {
		System.setProperty("config.path", ".");
		// QueryHelper helper = new QueryHelper();
		// Map<String, Object> params = new HashMap<String, Object>();
		String sql = " select index_id as id, parent_id as parentid, tree_test_user9 as code, tree_test_user10 as name, treeid as treeid from tree_test order by treeid asc ";
		// List<Map<String, Object>> datalist = helper.getResultList(sql, params);
		List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
		Connection srcConn = null;
		PreparedStatement srcPsmt = null;
		ResultSet srcRs = null;
		try {
			srcConn = DBConnectionFactory.getConnection();
			srcPsmt = srcConn.prepareStatement(sql);
			srcRs = srcPsmt.executeQuery();
			while (srcRs.next()) {
				datalist.add(toMap(srcRs));
			}
			JdbcUtils.close(srcRs);
			JdbcUtils.close(srcPsmt);
			JdbcUtils.close(srcConn);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(srcRs);
			JdbcUtils.close(srcPsmt);
			JdbcUtils.close(srcConn);
		}

		List<TreeComponent> nodes = new ArrayList<TreeComponent>();
		for (Map<String, Object> dataMap : datalist) {
			TreeComponent model = new TreeComponent();
			model.setId(ParamUtils.getString(dataMap, "id"));
			model.setParentId(ParamUtils.getString(dataMap, "parentid"));
			model.setTitle(ParamUtils.getString(dataMap, "name"));
			model.setCode(ParamUtils.getString(dataMap, "code"));
			model.setTreeId(ParamUtils.getString(dataMap, "treeid"));
			model.setDataMap(dataMap);
			nodes.add(model);
		}

		TreeHelper treeHelper = new TreeHelper();
		JSONArray result = treeHelper.getJSONArray(nodes, 0);
		FileUtils.save("./test.json", result.toJSONString().getBytes());

		String value = null;
		org.dom4j.Document document = DocumentHelper.createDocument();
		org.dom4j.Element root = document.addElement("TreeNodes");

		XmlExport current = new XmlExport();
		current.setXmlTag("TreeNode");
		current.addAttr("ID", "编号");
		current.addAttr("ParentID", "父编号");
		current.addAttr("Name", "名称");
		current.addAttr("Code", "代码");
		current.addAttr("TreeID", "树编号");

		TreeRepositoryBuilder builder = new TreeRepositoryBuilder();
		TreeRepository treeRepository = builder.buildMyTree(nodes);
		List<TreeComponent> components = treeRepository.getTopTrees();
		if (components != null && !components.isEmpty()) {
			for (TreeComponent component : components) {
				Element element = root.addElement(current.getXmlTag());
				for (XmlExportItem item : current.getItems()) {
					/**
					 * 处理属性
					 */
					if (StringUtils.equals(item.getTagFlag(), "A")) {
						if (StringUtils.isNotEmpty(item.getExpression())) {
							value = ExpressionTools.evaluate(item.getExpression(), component.getDataMap());
						} else {
							value = ParamUtils.getString(component.getDataMap(), item.getName().trim().toLowerCase());
						}
						if (StringUtils.isNotEmpty(value)) {
							element.addAttribute(item.getName(), value);
						}
					} else {
						element.addElement(item.getName(), value);
					}
				}
				processChildTreeNode(current, element, component);
			}
		}

		byte[] data = Dom4jUtils.getBytesFromPrettyDocument(document, "UTF-8");
		System.out.println(new String(data));
		FileUtils.save("./test.xml", data);
	}

}
