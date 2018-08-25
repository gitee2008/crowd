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

package com.glaf.crowd.website.springmvc;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.modules.sys.model.Dictory;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.SysTreeService;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;

import com.glaf.core.util.Tools;
import com.glaf.crowd.domain.ProductItem;
import com.glaf.crowd.query.ProductItemQuery;
import com.glaf.crowd.service.ProductItemService;

/**
 * http://127.0.0.1:8080/glaf/website/my/crowd/productItem/json?tenantId=9661a001bcc04ef0955c1408c857164c
 *
 */

@Controller("/my/crowd/productItem")
@RequestMapping("/my/crowd/productItem")
public class MyProductItemController {

	protected DictoryService dictoryService;

	protected SysTreeService sysTreeService;

	protected ProductItemService productItemService;

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request) throws IOException {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		ProductItemQuery query = new ProductItemQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);

		String tenantId = request.getParameter("tenantId");
		query.tenantId(tenantId);

		String category = request.getParameter("category");
		if (StringUtils.isNotEmpty(category)) {
			query.category(category);
		}

		String nameLike = request.getParameter("nameLike_enc");
		if (StringUtils.isNotEmpty(nameLike)) {
			query.setItemTitleLike(nameLike);
		}

		String nameLike_enc = request.getParameter("nameLike_enc");
		if (StringUtils.isNotEmpty(nameLike_enc)) {
			query.setItemTitleLike(RequestUtils.decodeString(nameLike));
		}

		int start = 0;
		int limit = 10;
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
			limit = 10;
		}

		JSONObject result = new JSONObject();
		int total = productItemService.getProductItemCountByQueryCriteria(query);
		if (total > 0) {
			result.put("total", total);
			result.put("totalCount", total);
			result.put("totalRecords", total);
			result.put("start", start);
			result.put("startIndex", start);
			result.put("limit", limit);
			result.put("pageSize", limit);

			if (StringUtils.isNotEmpty(orderName)) {
				query.setSortOrder(orderName);
				if (StringUtils.equals(order, "desc")) {
					query.setSortOrder(" desc ");
				}
			}

			List<ProductItem> list = productItemService.getProductItemsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				String nodeCode = request.getParameter("nodeCode");
				if (StringUtils.isEmpty(nodeCode)) {
					nodeCode = "ProductCategory";
				}
				List<Dictory> dicts = dictoryService.getDictoryList(nodeCode);

				Map<String, String> nameMap = new HashMap<String, String>();
				if (dicts != null && !dicts.isEmpty()) {
					for (Dictory dict : dicts) {
						nameMap.put(dict.getCode(), dict.getName());
					}
				}

				for (ProductItem productItem : list) {
					JSONObject rowJSON = productItem.toJsonObject();
					rowJSON.put("id", productItem.getId());
					rowJSON.put("itemId", productItem.getId());
					if (nameMap.get(productItem.getCategory()) != null) {
						rowJSON.put("categoryName", nameMap.get(productItem.getCategory()));
					}
					rowJSON.put("startIndex", ++start);
					rowsJSON.add(rowJSON);
				}

				result.put("rows", rowsJSON);

			}
		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", total);
		}
		return result.toJSONString().getBytes("UTF-8");
	}

	@javax.annotation.Resource
	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	@javax.annotation.Resource(name = "com.glaf.crowd.service.productItemService")
	public void setProductItemService(ProductItemService productItemService) {
		this.productItemService = productItemService;
	}

	@javax.annotation.Resource
	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}

}
