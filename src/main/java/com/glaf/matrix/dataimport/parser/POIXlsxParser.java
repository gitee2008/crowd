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

package com.glaf.matrix.dataimport.parser;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ExcelXlsxUtils;
import com.glaf.core.util.LowerLinkedMap;
import com.glaf.core.util.UUID32;
import com.glaf.matrix.dataimport.domain.TableInput;
import com.glaf.matrix.dataimport.domain.TableInputColumn;

public class POIXlsxParser implements DataParser {

	protected final static Log logger = LogFactory.getLog(POIXlsxParser.class);

	@SuppressWarnings("resource")
	public List<Map<String, Object>> parse(TableInput tableInput, java.io.InputStream data) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		XSSFWorkbook wb = null;
		try {
			wb = new XSSFWorkbook(data);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		int sheetCount = wb.getNumberOfSheets();
		for (int r = 0; r < sheetCount; r++) {
			int colCount = 0;
			String strValue = null;
			Object value = null;
			XSSFRow row = null;
			XSSFCell cell = null;
			XSSFSheet sheet = wb.getSheetAt(r);
			int rowCount = sheet.getPhysicalNumberOfRows();
			for (int i = 0; i < rowCount; i++) {
				int startRow = tableInput.getStartRow();// 从1开始
				if (startRow > 0 && i < startRow - 1) {
					continue;// 跳过开始行
				}
				row = sheet.getRow(i);
				colCount = row.getPhysicalNumberOfCells();
				LowerLinkedMap rowMap = new LowerLinkedMap();
				rowMap.put("tableid", tableInput.getTableId());
				rowMap.put("tableid_", tableInput.getTableId());

				for (TableInputColumn column : tableInput.getColumns()) {
					if (column.getPosition() > 0 && column.getPosition() <= colCount) {
						cell = row.getCell(column.getPosition() - 1);
						if (cell == null) {
							continue;
						}
						if (StringUtils.equals(column.getJavaType(), "Integer")) {
							strValue = ExcelXlsxUtils.getValue(cell, 0);
							value = Integer.parseInt(strValue);
						} else if (StringUtils.equals(column.getJavaType(), "Long")) {
							strValue = ExcelXlsxUtils.getValue(cell, 0);
							value = Long.parseLong(strValue);
						} else if (StringUtils.equals(column.getJavaType(), "Double")) {
							strValue = ExcelXlsxUtils.getValue(cell, column.getPrecision());
							value = Double.parseDouble(strValue);
						} else if (StringUtils.equals(column.getJavaType(), "Date")) {
							strValue = ExcelXlsxUtils.getStringOrDateValue(cell, 0);
							value = DateUtils.toDate(strValue);
						} else {
							strValue = ExcelXlsxUtils.getValue(cell, 0);
							value = strValue;
						}
						if (value != null) {
							rowMap.put(column.getName().toLowerCase(), value);
							rowMap.put(column.getColumnName().toLowerCase(), value);
						}
					}
				}

				if (tableInput.getIdColumn() != null && tableInput.getIdColumn().isRequired()) {
					if (rowMap.get(tableInput.getIdColumn().getColumnName().toLowerCase()) != null) {
						dataList.add(rowMap);
					}
				} else {
					rowMap.put("uuid_", UUID32.getUUID());
					dataList.add(rowMap);
				}
			}
		}

		return dataList;
	}

}
