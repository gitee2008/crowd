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

package com.glaf.search.query;

import com.glaf.core.query.DataQuery;

public class SearchFieldQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String serverId;
	protected String code;
	protected String codeLike;
	protected String mappingCode;
	protected String mappingCodeLike;
	protected String type;
	protected String typeLike;
	protected String format;
	protected String formatLike;
	protected String indexAnalyzer;
	protected String indexAnalyzerLike;
	protected String fieldAnalyzer;
	protected String fieldAnalyzerLike;
	protected String termVector;
	protected String termVectorLike;
	protected String analyzerFlag;
	protected String searchReturnFlag;

	public SearchFieldQuery() {

	}

	public String getServerId() {
		return serverId;
	}

	public String getCode() {
		return code;
	}

	public String getCodeLike() {
		if (codeLike != null && codeLike.trim().length() > 0) {
			if (!codeLike.startsWith("%")) {
				codeLike = "%" + codeLike;
			}
			if (!codeLike.endsWith("%")) {
				codeLike = codeLike + "%";
			}
		}
		return codeLike;
	}

	public String getMappingCode() {
		return mappingCode;
	}

	public String getMappingCodeLike() {
		if (mappingCodeLike != null && mappingCodeLike.trim().length() > 0) {
			if (!mappingCodeLike.startsWith("%")) {
				mappingCodeLike = "%" + mappingCodeLike;
			}
			if (!mappingCodeLike.endsWith("%")) {
				mappingCodeLike = mappingCodeLike + "%";
			}
		}
		return mappingCodeLike;
	}

	public String getType() {
		return type;
	}

	public String getTypeLike() {
		if (typeLike != null && typeLike.trim().length() > 0) {
			if (!typeLike.startsWith("%")) {
				typeLike = "%" + typeLike;
			}
			if (!typeLike.endsWith("%")) {
				typeLike = typeLike + "%";
			}
		}
		return typeLike;
	}

	public String getFormat() {
		return format;
	}

	public String getFormatLike() {
		if (formatLike != null && formatLike.trim().length() > 0) {
			if (!formatLike.startsWith("%")) {
				formatLike = "%" + formatLike;
			}
			if (!formatLike.endsWith("%")) {
				formatLike = formatLike + "%";
			}
		}
		return formatLike;
	}

	public String getIndexAnalyzer() {
		return indexAnalyzer;
	}

	public String getIndexAnalyzerLike() {
		if (indexAnalyzerLike != null && indexAnalyzerLike.trim().length() > 0) {
			if (!indexAnalyzerLike.startsWith("%")) {
				indexAnalyzerLike = "%" + indexAnalyzerLike;
			}
			if (!indexAnalyzerLike.endsWith("%")) {
				indexAnalyzerLike = indexAnalyzerLike + "%";
			}
		}
		return indexAnalyzerLike;
	}

	public String getFieldAnalyzer() {
		return fieldAnalyzer;
	}

	public String getFieldAnalyzerLike() {
		if (fieldAnalyzerLike != null && fieldAnalyzerLike.trim().length() > 0) {
			if (!fieldAnalyzerLike.startsWith("%")) {
				fieldAnalyzerLike = "%" + fieldAnalyzerLike;
			}
			if (!fieldAnalyzerLike.endsWith("%")) {
				fieldAnalyzerLike = fieldAnalyzerLike + "%";
			}
		}
		return fieldAnalyzerLike;
	}

	public String getTermVector() {
		return termVector;
	}

	public String getTermVectorLike() {
		if (termVectorLike != null && termVectorLike.trim().length() > 0) {
			if (!termVectorLike.startsWith("%")) {
				termVectorLike = "%" + termVectorLike;
			}
			if (!termVectorLike.endsWith("%")) {
				termVectorLike = termVectorLike + "%";
			}
		}
		return termVectorLike;
	}

	public String getAnalyzerFlag() {
		return analyzerFlag;
	}

	public String getSearchReturnFlag() {
		return searchReturnFlag;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCodeLike(String codeLike) {
		this.codeLike = codeLike;
	}

	public void setMappingCode(String mappingCode) {
		this.mappingCode = mappingCode;
	}

	public void setMappingCodeLike(String mappingCodeLike) {
		this.mappingCodeLike = mappingCodeLike;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTypeLike(String typeLike) {
		this.typeLike = typeLike;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setFormatLike(String formatLike) {
		this.formatLike = formatLike;
	}

	public void setIndexAnalyzer(String indexAnalyzer) {
		this.indexAnalyzer = indexAnalyzer;
	}

	public void setIndexAnalyzerLike(String indexAnalyzerLike) {
		this.indexAnalyzerLike = indexAnalyzerLike;
	}

	public void setFieldAnalyzer(String fieldAnalyzer) {
		this.fieldAnalyzer = fieldAnalyzer;
	}

	public void setFieldAnalyzerLike(String fieldAnalyzerLike) {
		this.fieldAnalyzerLike = fieldAnalyzerLike;
	}

	public void setTermVector(String termVector) {
		this.termVector = termVector;
	}

	public void setTermVectorLike(String termVectorLike) {
		this.termVectorLike = termVectorLike;
	}

	public void setAnalyzerFlag(String analyzerFlag) {
		this.analyzerFlag = analyzerFlag;
	}

	public void setSearchReturnFlag(String searchReturnFlag) {
		this.searchReturnFlag = searchReturnFlag;
	}

	public SearchFieldQuery serverId(String serverId) {
		if (serverId == null) {
			throw new RuntimeException("serverId is null");
		}
		this.serverId = serverId;
		return this;
	}

	public SearchFieldQuery code(String code) {
		if (code == null) {
			throw new RuntimeException("code is null");
		}
		this.code = code;
		return this;
	}

	public SearchFieldQuery codeLike(String codeLike) {
		if (codeLike == null) {
			throw new RuntimeException("code is null");
		}
		this.codeLike = codeLike;
		return this;
	}

	public SearchFieldQuery mappingCode(String mappingCode) {
		if (mappingCode == null) {
			throw new RuntimeException("mappingCode is null");
		}
		this.mappingCode = mappingCode;
		return this;
	}

	public SearchFieldQuery mappingCodeLike(String mappingCodeLike) {
		if (mappingCodeLike == null) {
			throw new RuntimeException("mappingCode is null");
		}
		this.mappingCodeLike = mappingCodeLike;
		return this;
	}

	public SearchFieldQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public SearchFieldQuery typeLike(String typeLike) {
		if (typeLike == null) {
			throw new RuntimeException("type is null");
		}
		this.typeLike = typeLike;
		return this;
	}

	public SearchFieldQuery format(String format) {
		if (format == null) {
			throw new RuntimeException("format is null");
		}
		this.format = format;
		return this;
	}

	public SearchFieldQuery formatLike(String formatLike) {
		if (formatLike == null) {
			throw new RuntimeException("format is null");
		}
		this.formatLike = formatLike;
		return this;
	}

	public SearchFieldQuery indexAnalyzer(String indexAnalyzer) {
		if (indexAnalyzer == null) {
			throw new RuntimeException("indexAnalyzer is null");
		}
		this.indexAnalyzer = indexAnalyzer;
		return this;
	}

	public SearchFieldQuery indexAnalyzerLike(String indexAnalyzerLike) {
		if (indexAnalyzerLike == null) {
			throw new RuntimeException("indexAnalyzer is null");
		}
		this.indexAnalyzerLike = indexAnalyzerLike;
		return this;
	}

	public SearchFieldQuery fieldAnalyzer(String fieldAnalyzer) {
		if (fieldAnalyzer == null) {
			throw new RuntimeException("fieldAnalyzer is null");
		}
		this.fieldAnalyzer = fieldAnalyzer;
		return this;
	}

	public SearchFieldQuery fieldAnalyzerLike(String fieldAnalyzerLike) {
		if (fieldAnalyzerLike == null) {
			throw new RuntimeException("fieldAnalyzer is null");
		}
		this.fieldAnalyzerLike = fieldAnalyzerLike;
		return this;
	}

	public SearchFieldQuery termVector(String termVector) {
		if (termVector == null) {
			throw new RuntimeException("termVector is null");
		}
		this.termVector = termVector;
		return this;
	}

	public SearchFieldQuery termVectorLike(String termVectorLike) {
		if (termVectorLike == null) {
			throw new RuntimeException("termVector is null");
		}
		this.termVectorLike = termVectorLike;
		return this;
	}

	public SearchFieldQuery analyzerFlag(String analyzerFlag) {
		if (analyzerFlag == null) {
			throw new RuntimeException("analyzerFlag is null");
		}
		this.analyzerFlag = analyzerFlag;
		return this;
	}

	public SearchFieldQuery searchReturnFlag(String searchReturnFlag) {
		if (searchReturnFlag == null) {
			throw new RuntimeException("searchReturnFlag is null");
		}
		this.searchReturnFlag = searchReturnFlag;
		return this;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("serverId".equals(sortColumn)) {
				orderBy = "E.SERVERID_" + a_x;
			}

			if ("code".equals(sortColumn)) {
				orderBy = "E.CODE_" + a_x;
			}

			if ("mappingCode".equals(sortColumn)) {
				orderBy = "E.MAPPINGCODE_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
			}

			if ("format".equals(sortColumn)) {
				orderBy = "E.FORMAT_" + a_x;
			}

			if ("indexAnalyzer".equals(sortColumn)) {
				orderBy = "E.INDEXANALYZER_" + a_x;
			}

			if ("fieldAnalyzer".equals(sortColumn)) {
				orderBy = "E.FIELDANALYZER_" + a_x;
			}

			if ("termVector".equals(sortColumn)) {
				orderBy = "E.TERMVECTOR_" + a_x;
			}

			if ("analyzerFlag".equals(sortColumn)) {
				orderBy = "E.ANALYZERFLAG_" + a_x;
			}

			if ("searchReturnFlag".equals(sortColumn)) {
				orderBy = "E.SEARCHRETURNFLAG_" + a_x;
			}

			if ("locked".equals(sortColumn)) {
				orderBy = "E.LOCKED_" + a_x;
			}

			if ("createBy".equals(sortColumn)) {
				orderBy = "E.CREATEBY_" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME_" + a_x;
			}

			if ("updateBy".equals(sortColumn)) {
				orderBy = "E.UPDATEBY_" + a_x;
			}

			if ("updateTime".equals(sortColumn)) {
				orderBy = "E.UPDATETIME_" + a_x;
			}

		}
		return orderBy;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("serverId", "SERVERID_");
		addColumn("code", "CODE_");
		addColumn("mappingCode", "MAPPINGCODE_");
		addColumn("type", "TYPE_");
		addColumn("format", "FORMAT_");
		addColumn("indexAnalyzer", "INDEXANALYZER_");
		addColumn("fieldAnalyzer", "FIELDANALYZER_");
		addColumn("termVector", "TERMVECTOR_");
		addColumn("analyzerFlag", "ANALYZERFLAG_");
		addColumn("searchReturnFlag", "SEARCHRETURNFLAG_");
		addColumn("locked", "LOCKED_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

}