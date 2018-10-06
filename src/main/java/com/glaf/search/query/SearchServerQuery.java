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

public class SearchServerQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String serverURL;
	protected String serverURLLike;
	protected String indexName;
	protected String indexNameLike;
	protected String type;
	protected String typeLike;
	protected String tokenizer;
	protected String tokenizerLike;
	protected String tokenFilter;
	protected String tokenFilterLike;
	protected String characterFilter;
	protected String characterFilterLike;
	protected String analyzerName;
	protected String analyzerNameLike;
	protected String analyzerType;
	protected String analyzerTypeLike;
	protected String filter;
	protected String filterLike;

	public SearchServerQuery() {

	}

	public String getServerURL() {
		return serverURL;
	}

	public String getServerURLLike() {
		if (serverURLLike != null && serverURLLike.trim().length() > 0) {
			if (!serverURLLike.startsWith("%")) {
				serverURLLike = "%" + serverURLLike;
			}
			if (!serverURLLike.endsWith("%")) {
				serverURLLike = serverURLLike + "%";
			}
		}
		return serverURLLike;
	}

	public String getIndexName() {
		return indexName;
	}

	public String getIndexNameLike() {
		if (indexNameLike != null && indexNameLike.trim().length() > 0) {
			if (!indexNameLike.startsWith("%")) {
				indexNameLike = "%" + indexNameLike;
			}
			if (!indexNameLike.endsWith("%")) {
				indexNameLike = indexNameLike + "%";
			}
		}
		return indexNameLike;
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

	public String getTokenizer() {
		return tokenizer;
	}

	public String getTokenizerLike() {
		if (tokenizerLike != null && tokenizerLike.trim().length() > 0) {
			if (!tokenizerLike.startsWith("%")) {
				tokenizerLike = "%" + tokenizerLike;
			}
			if (!tokenizerLike.endsWith("%")) {
				tokenizerLike = tokenizerLike + "%";
			}
		}
		return tokenizerLike;
	}

	public String getTokenFilter() {
		return tokenFilter;
	}

	public String getTokenFilterLike() {
		if (tokenFilterLike != null && tokenFilterLike.trim().length() > 0) {
			if (!tokenFilterLike.startsWith("%")) {
				tokenFilterLike = "%" + tokenFilterLike;
			}
			if (!tokenFilterLike.endsWith("%")) {
				tokenFilterLike = tokenFilterLike + "%";
			}
		}
		return tokenFilterLike;
	}

	public String getCharacterFilter() {
		return characterFilter;
	}

	public String getCharacterFilterLike() {
		if (characterFilterLike != null && characterFilterLike.trim().length() > 0) {
			if (!characterFilterLike.startsWith("%")) {
				characterFilterLike = "%" + characterFilterLike;
			}
			if (!characterFilterLike.endsWith("%")) {
				characterFilterLike = characterFilterLike + "%";
			}
		}
		return characterFilterLike;
	}

	public String getAnalyzerName() {
		return analyzerName;
	}

	public String getAnalyzerNameLike() {
		if (analyzerNameLike != null && analyzerNameLike.trim().length() > 0) {
			if (!analyzerNameLike.startsWith("%")) {
				analyzerNameLike = "%" + analyzerNameLike;
			}
			if (!analyzerNameLike.endsWith("%")) {
				analyzerNameLike = analyzerNameLike + "%";
			}
		}
		return analyzerNameLike;
	}

	public String getAnalyzerType() {
		return analyzerType;
	}

	public String getAnalyzerTypeLike() {
		if (analyzerTypeLike != null && analyzerTypeLike.trim().length() > 0) {
			if (!analyzerTypeLike.startsWith("%")) {
				analyzerTypeLike = "%" + analyzerTypeLike;
			}
			if (!analyzerTypeLike.endsWith("%")) {
				analyzerTypeLike = analyzerTypeLike + "%";
			}
		}
		return analyzerTypeLike;
	}

	public String getFilter() {
		return filter;
	}

	public String getFilterLike() {
		if (filterLike != null && filterLike.trim().length() > 0) {
			if (!filterLike.startsWith("%")) {
				filterLike = "%" + filterLike;
			}
			if (!filterLike.endsWith("%")) {
				filterLike = filterLike + "%";
			}
		}
		return filterLike;
	}

	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	public void setServerURLLike(String serverURLLike) {
		this.serverURLLike = serverURLLike;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public void setIndexNameLike(String indexNameLike) {
		this.indexNameLike = indexNameLike;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTypeLike(String typeLike) {
		this.typeLike = typeLike;
	}

	public void setTokenizer(String tokenizer) {
		this.tokenizer = tokenizer;
	}

	public void setTokenizerLike(String tokenizerLike) {
		this.tokenizerLike = tokenizerLike;
	}

	public void setTokenFilter(String tokenFilter) {
		this.tokenFilter = tokenFilter;
	}

	public void setTokenFilterLike(String tokenFilterLike) {
		this.tokenFilterLike = tokenFilterLike;
	}

	public void setCharacterFilter(String characterFilter) {
		this.characterFilter = characterFilter;
	}

	public void setCharacterFilterLike(String characterFilterLike) {
		this.characterFilterLike = characterFilterLike;
	}

	public void setAnalyzerName(String analyzerName) {
		this.analyzerName = analyzerName;
	}

	public void setAnalyzerNameLike(String analyzerNameLike) {
		this.analyzerNameLike = analyzerNameLike;
	}

	public void setAnalyzerType(String analyzerType) {
		this.analyzerType = analyzerType;
	}

	public void setAnalyzerTypeLike(String analyzerTypeLike) {
		this.analyzerTypeLike = analyzerTypeLike;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public void setFilterLike(String filterLike) {
		this.filterLike = filterLike;
	}

	public SearchServerQuery serverURL(String serverURL) {
		if (serverURL == null) {
			throw new RuntimeException("serverURL is null");
		}
		this.serverURL = serverURL;
		return this;
	}

	public SearchServerQuery serverURLLike(String serverURLLike) {
		if (serverURLLike == null) {
			throw new RuntimeException("serverURL is null");
		}
		this.serverURLLike = serverURLLike;
		return this;
	}

	public SearchServerQuery indexName(String indexName) {
		if (indexName == null) {
			throw new RuntimeException("indexName is null");
		}
		this.indexName = indexName;
		return this;
	}

	public SearchServerQuery indexNameLike(String indexNameLike) {
		if (indexNameLike == null) {
			throw new RuntimeException("indexName is null");
		}
		this.indexNameLike = indexNameLike;
		return this;
	}

	public SearchServerQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public SearchServerQuery typeLike(String typeLike) {
		if (typeLike == null) {
			throw new RuntimeException("type is null");
		}
		this.typeLike = typeLike;
		return this;
	}

	public SearchServerQuery tokenizer(String tokenizer) {
		if (tokenizer == null) {
			throw new RuntimeException("tokenizer is null");
		}
		this.tokenizer = tokenizer;
		return this;
	}

	public SearchServerQuery tokenizerLike(String tokenizerLike) {
		if (tokenizerLike == null) {
			throw new RuntimeException("tokenizer is null");
		}
		this.tokenizerLike = tokenizerLike;
		return this;
	}

	public SearchServerQuery tokenFilter(String tokenFilter) {
		if (tokenFilter == null) {
			throw new RuntimeException("tokenFilter is null");
		}
		this.tokenFilter = tokenFilter;
		return this;
	}

	public SearchServerQuery tokenFilterLike(String tokenFilterLike) {
		if (tokenFilterLike == null) {
			throw new RuntimeException("tokenFilter is null");
		}
		this.tokenFilterLike = tokenFilterLike;
		return this;
	}

	public SearchServerQuery characterFilter(String characterFilter) {
		if (characterFilter == null) {
			throw new RuntimeException("characterFilter is null");
		}
		this.characterFilter = characterFilter;
		return this;
	}

	public SearchServerQuery characterFilterLike(String characterFilterLike) {
		if (characterFilterLike == null) {
			throw new RuntimeException("characterFilter is null");
		}
		this.characterFilterLike = characterFilterLike;
		return this;
	}

	public SearchServerQuery analyzerName(String analyzerName) {
		if (analyzerName == null) {
			throw new RuntimeException("analyzerName is null");
		}
		this.analyzerName = analyzerName;
		return this;
	}

	public SearchServerQuery analyzerNameLike(String analyzerNameLike) {
		if (analyzerNameLike == null) {
			throw new RuntimeException("analyzerName is null");
		}
		this.analyzerNameLike = analyzerNameLike;
		return this;
	}

	public SearchServerQuery analyzerType(String analyzerType) {
		if (analyzerType == null) {
			throw new RuntimeException("analyzerType is null");
		}
		this.analyzerType = analyzerType;
		return this;
	}

	public SearchServerQuery analyzerTypeLike(String analyzerTypeLike) {
		if (analyzerTypeLike == null) {
			throw new RuntimeException("analyzerType is null");
		}
		this.analyzerTypeLike = analyzerTypeLike;
		return this;
	}

	public SearchServerQuery filter(String filter) {
		if (filter == null) {
			throw new RuntimeException("filter is null");
		}
		this.filter = filter;
		return this;
	}

	public SearchServerQuery filterLike(String filterLike) {
		if (filterLike == null) {
			throw new RuntimeException("filter is null");
		}
		this.filterLike = filterLike;
		return this;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("serverURL".equals(sortColumn)) {
				orderBy = "E.SERVERURL_" + a_x;
			}

			if ("indexName".equals(sortColumn)) {
				orderBy = "E.INDEXNAME_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
			}

			if ("tokenizer".equals(sortColumn)) {
				orderBy = "E.TOKENIZER_" + a_x;
			}

			if ("tokenFilter".equals(sortColumn)) {
				orderBy = "E.TOKENFILTER_" + a_x;
			}

			if ("characterFilter".equals(sortColumn)) {
				orderBy = "E.CHARACTERFILTER_" + a_x;
			}

			if ("analyzerName".equals(sortColumn)) {
				orderBy = "E.ANALYZERNAME_" + a_x;
			}

			if ("analyzerType".equals(sortColumn)) {
				orderBy = "E.ANALYZERTYPE_" + a_x;
			}

			if ("filter".equals(sortColumn)) {
				orderBy = "E.FILTER_" + a_x;
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
		addColumn("serverURL", "SERVERURL_");
		addColumn("indexName", "INDEXNAME_");
		addColumn("type", "TYPE_");
		addColumn("tokenizer", "TOKENIZER_");
		addColumn("tokenFilter", "TOKENFILTER_");
		addColumn("characterFilter", "CHARACTERFILTER_");
		addColumn("analyzerName", "ANALYZERNAME_");
		addColumn("analyzerType", "ANALYZERTYPE_");
		addColumn("filter", "FILTER_");
		addColumn("locked", "LOCKED_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

}