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

package com.glaf.search.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.glaf.search.domain.SearchField;
import com.glaf.search.query.SearchFieldQuery;

/**
 * 
 * Mapper接口
 *
 */

@Component("com.glaf.search.mapper.SearchFieldMapper")
public interface SearchFieldMapper {

	void bulkInsertSearchField(List<SearchField> list);

	void bulkInsertSearchField_oracle(List<SearchField> list);

	void deleteSearchFields(SearchFieldQuery query);

	void deleteSearchFieldById(String id);

	SearchField getSearchFieldById(String id);

	int getSearchFieldCount(SearchFieldQuery query);

	List<SearchField> getSearchFields(SearchFieldQuery query);

	void insertSearchField(SearchField model);

	void updateSearchField(SearchField model);

}
