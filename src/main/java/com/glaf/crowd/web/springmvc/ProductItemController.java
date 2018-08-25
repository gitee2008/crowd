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

package com.glaf.crowd.web.springmvc;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.modules.sys.model.Dictory;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.SysTreeService;
import com.glaf.core.base.DataFile;
import com.glaf.core.config.SystemProperties;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;
import com.glaf.core.util.UUID32;
import com.glaf.crowd.domain.ProductItem;
import com.glaf.crowd.query.ProductItemQuery;
import com.glaf.crowd.service.ProductItemService;
import com.glaf.matrix.data.domain.DataFileEntity;
import com.glaf.matrix.data.factory.DataFileFactory;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/crowd/productItem")
@RequestMapping("/crowd/productItem")
public class ProductItemController {
	protected static final Log logger = LogFactory.getLog(ProductItemController.class);

	protected DictoryService dictoryService;

	protected SysTreeService sysTreeService;

	protected ProductItemService productItemService;

	public ProductItemController() {

	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String id = RequestUtils.getString(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					ProductItem productItem = productItemService.getProductItem(String.valueOf(x));
					if (productItem != null && (StringUtils.equals(productItem.getCreateBy(), loginContext.getActorId())
							|| loginContext.isSystemAdministrator())) {

					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			ProductItem productItem = productItemService.getProductItem(String.valueOf(id));
			if (productItem != null && (StringUtils.equals(productItem.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {

				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);

		ProductItem productItem = productItemService.getProductItem(request.getParameter("id"));
		if (productItem != null) {
			request.setAttribute("productItem", productItem);
			if (StringUtils.equals(loginContext.getActorId(), productItem.getCreateBy())) {

			}
		}

		request.setAttribute("ts", System.currentTimeMillis());

		String nodeCode = request.getParameter("nodeCode");
		if (StringUtils.isEmpty(nodeCode)) {
			nodeCode = "ProductCategory";
		}
		List<Dictory> dicts = dictoryService.getDictoryList(nodeCode);
		request.setAttribute("categories", dicts);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("productItem.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/crowd/productItem/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		ProductItemQuery query = new ProductItemQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		if (!loginContext.isSystemAdministrator()) {
			query.tenantId(loginContext.getTenantId());
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
					rowJSON.put("productItemId", productItem.getId());
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

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String nodeCode = request.getParameter("nodeCode");
		if (StringUtils.isEmpty(nodeCode)) {
			nodeCode = "ProductCategory";
		}
		List<Dictory> dicts = dictoryService.getDictoryList(nodeCode);
		request.setAttribute("categories", dicts);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/crowd/productItem/list", modelMap);
	}

	@RequestMapping("/save")
	public ModelAndView save(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();

		// 将当前上下文初始化给 CommonsMutipartResolver（多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
			ProductItem productItem = null;
			String id = request.getParameter("id");
			String smallUrl = null;
			String itemUrl = null;
			try {
				if (StringUtils.isNotEmpty(id)) {
					productItem = productItemService.getProductItem(id);
					if (!StringUtils.equals(productItem.getTenantId(), loginContext.getTenantId())) {
						return this.list(request, modelMap);
					}
					smallUrl = productItem.getSmallUrl();
					itemUrl = productItem.getItemUrl();
				} else {
					productItem = new ProductItem();
					productItem.setId(UUID32.getUUID());
					productItem.setTenantId(loginContext.getTenantId());
				}

				Map<String, MultipartFile> fileMap = req.getFileMap();
				Set<Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
				for (Entry<String, MultipartFile> entry : entrySet) {
					MultipartFile mFile = entry.getValue();
					if (mFile.getOriginalFilename() != null && mFile.getSize() > 0
							&& mFile.getSize() <= FileUtils.MB_SIZE * 20) {
						String name = mFile.getOriginalFilename();
						String fileExt = FileUtils.getFileExt(name);
						if (StringUtils.equals(mFile.getName(), "smallUrl")) {
							if (StringUtils.isNotEmpty(smallUrl)) {
								DataFile dataFile = new DataFileEntity();
								dataFile.setBusinessKey(productItem.getId());
								dataFile.setCreateBy(loginContext.getActorId());
								dataFile.setFilename(smallUrl);
								dataFile.setPath(smallUrl);
								dataFile.setServiceKey("ProductItem");
								dataFile.setId(DigestUtils.md5Hex(smallUrl));
								DataFileFactory.getInstance().insertDataFile(loginContext.getTenantId(), dataFile,
										mFile.getBytes());

								FileUtils.save(SystemProperties.getAppPath() + smallUrl, mFile.getBytes());
							} else {
								String path = "/upload/" + DateUtils.getNowYearMonthDay() + "/";
								String filename = path + UUID32.getUUID() + "." + fileExt;

								DataFile dataFile = new DataFileEntity();
								dataFile.setBusinessKey(productItem.getId());
								dataFile.setCreateBy(loginContext.getActorId());
								dataFile.setFilename(filename);
								dataFile.setPath(filename);
								dataFile.setServiceKey("ProductItem");
								dataFile.setId(DigestUtils.md5Hex(filename));
								DataFileFactory.getInstance().insertDataFile(loginContext.getTenantId(), dataFile,
										mFile.getBytes());

								FileUtils.mkdirs(SystemProperties.getAppPath() + path);
								FileUtils.save(SystemProperties.getAppPath() + filename, mFile.getBytes());
								productItem.setSmallUrl(filename);
							}
						}

						if (StringUtils.equals(mFile.getName(), "itemUrl")) {
							if (StringUtils.isNotEmpty(itemUrl)) {
								DataFile dataFile = new DataFileEntity();
								dataFile.setBusinessKey(productItem.getId());
								dataFile.setCreateBy(loginContext.getActorId());
								dataFile.setFilename(itemUrl);
								dataFile.setPath(itemUrl);
								dataFile.setServiceKey("ProductItem");
								dataFile.setId(DigestUtils.md5Hex(itemUrl));
								DataFileFactory.getInstance().insertDataFile(loginContext.getTenantId(), dataFile,
										mFile.getBytes());

								FileUtils.save(SystemProperties.getAppPath() + itemUrl, mFile.getBytes());
							} else {
								String path = "/upload/" + DateUtils.getNowYearMonthDay() + "/";
								String filename = path + UUID32.getUUID() + "." + fileExt;

								DataFile dataFile = new DataFileEntity();
								dataFile.setBusinessKey(productItem.getId());
								dataFile.setCreateBy(loginContext.getActorId());
								dataFile.setFilename(filename);
								dataFile.setPath(filename);
								dataFile.setServiceKey("ProductItem");
								dataFile.setId(DigestUtils.md5Hex(filename));
								DataFileFactory.getInstance().insertDataFile(loginContext.getTenantId(), dataFile,
										mFile.getBytes());

								FileUtils.mkdirs(SystemProperties.getAppPath() + path);
								FileUtils.save(SystemProperties.getAppPath() + filename, mFile.getBytes());
								productItem.setItemUrl(filename);
							}
						}
					}
				}

				productItem.setCategory(req.getParameter("category"));
				productItem.setItemName(req.getParameter("itemName"));
				productItem.setItemLocation(req.getParameter("itemLocation"));
				productItem.setItemTitle(req.getParameter("itemTitle"));
				productItem.setItemContent(req.getParameter("itemContent"));
				productItem.setItemMoney(RequestUtils.getDouble(req, "itemMoney"));
				productItem.setItemDay(RequestUtils.getInt(req, "itemDay"));
				productItem.setCreateBy(actorId);
				productItem.setUpdateBy(actorId);

				this.productItemService.save(productItem);

			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return this.list(request, modelMap);
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
