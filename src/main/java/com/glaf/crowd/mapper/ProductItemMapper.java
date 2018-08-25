package com.glaf.crowd.mapper;

import java.util.List;

import org.springframework.stereotype.Component;
import com.glaf.crowd.domain.*;
import com.glaf.crowd.query.*;

/**
 * 
 * Mapper接口
 *
 */

@Component("com.glaf.crowd.mapper.ProductItemMapper")
public interface ProductItemMapper {

	void bulkInsertProductItem(List<ProductItem> list);

	void bulkInsertProductItem_oracle(List<ProductItem> list);

	void deleteProductItems(ProductItemQuery query);

	void deleteProductItemById(String id);

	ProductItem getProductItemById(String id);

	int getProductItemCount(ProductItemQuery query);

	List<ProductItem> getProductItems(ProductItemQuery query);

	void insertProductItem(ProductItem model);

	void updateProductItem(ProductItem model);

}
