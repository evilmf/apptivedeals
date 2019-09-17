package com.apptivedeals.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.apptivedeals.entity.Price;
import com.apptivedeals.entity.Product;
import com.apptivedeals.entity.Snapshot.SnapshotProduct;
import com.apptivedeals.to.ProductSearchTo;

@Component
public class ProductDao {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	private static final String INSERT_PRODUCT = "insert into products (product_id, name, url, brand_id, gender_id, category_id, create_date, update_date, name_tsvector) "
			+ "values (:product_id, :name, :url, :brand_id, :gender_id, :category_id, now(), now(), to_tsvector('simple', :name)) "
			+ "on conflict (lower(product_id), brand_id) do update "
			+ "set name = EXCLUDED.name, url = EXCLUDED.url, gender_id = EXCLUDED.gender_id, category_id = EXCLUDED.category_id, "
			+ "update_date = "
			+ "case when :name != EXCLUDED.name or :url != EXCLUDED.url or :gender_id != EXCLUDED.gender_id or :category_id != EXCLUDED.category_id "
			+ "	then now() "
			+ "	else EXCLUDED.update_date "
			+ "end "
			+ "returning id, product_id, name, url, brand_id, gender_id, category_id, create_date, update_date";
	
	private static final String GET_PRODUCT = "select id, " 
			+ "       product_id, " 
			+ "       name, " 
			+ "       url, "
			+ "       brand_id, " 
			+ "       gender_id, " 
			+ "       category_id, " 
			+ "       create_date, "
			+ "       update_date " 
			+ "from products " 
			+ "where lower(product_id) = :product_id "
			+ "  and brand_id = :brand_id";
	
	private final static String GET_SNAPSHOT_PRODUCTS = "select " 
			+ "       p.id product_id, "
			+ "       p.name product_name, " 
			+ "       p.url product_url, " 
			+ "       i.url image_url, "
			+ "       b.id brand_id, " 
			+ "       b.name brand, " 
			+ "       g.id gender_id, " 
			+ "       g.name gender, "
			+ "       c.id category_id, " 
			+ "       c.name category " 
			+ "from products p "
			+ "join brands b on b.id = p.brand_id " 
			+ "join genders g on g.id = p.gender_id "
			+ "join categories c on c.id = p.category_id "
			+ "join images i on i.product_id = p.id and i.is_primary is true " 
			+ "where p.id in (:product_ids)";

	private final static String UPDATE_PRODUCT_SUMMARY = "with product_summary_update as ( " 
			+ "    select "
			+ "        product_id as id, " 
			+ "        min(price_discount) price_min, "
			+ "        max(price_discount) price_max, "
			+ "        (select id from images i where i.product_id = sd.product_id limit 1) image_id "
			+ "    from snapshot_detail sd " 
			+ "    where product_id in (:product_ids) " 
			+ "    group by product_id " 
			+ ") "
			+ "insert into product_summary (id, price_min, price_max, image_id, create_date, update_date) "
			+ "select id, price_min, price_max, image_id, now(), now() from product_summary_update "
			+ "on conflict (id) do update set price_min = excluded.price_min, price_max = excluded.price_max, "
			+ "                          update_date = now()";

	private final static String SEARCH_PRODUCT = "select " 
			+ "       p.product_id, " 
			+ "       p.name product_name, "
			+ "       b.name brand_name, " 
			+ "       g.name gender_name, " 
			+ "       c.name category_name, "
			+ "       i.url image_url, " 
			+ "       ps.price_min price_from, " 
			+ "       ps.price_max price_to "
			+ "from products p " 
			+ "join brands b on b.id = p.brand_id " 
			+ "join genders g on g.id = p.gender_id "
			+ "join categories c on c.id = p.category_id " 
			+ "join product_summary ps on ps.id = p.id "
			+ "join images i on i.id = ps.image_id " 
			+ "where name_tsvector @@ to_tsquery('simple', replace(regexp_replace(plainto_tsquery('simple', :keyword)::text, '('' )|(''$)', ''':* ', 'g'), ''' & ''', ' & ')) " 
			+ "order by product_name "
			+ "limit 100";
	
	public List<ProductSearchTo> searchProduct(String keyword) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("keyword", keyword);
		
		return namedParameterJdbcTemplate.query(SEARCH_PRODUCT, params, new ResultSetExtractor<List<ProductSearchTo>>() {

			@Override
			public List<ProductSearchTo> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<ProductSearchTo> productSearchResult = new LinkedList<ProductSearchTo>();
				while (rs.next()) {
					ProductSearchTo productSearchTo = new ProductSearchTo();
					productSearchTo.productId = rs.getLong("product_id");
					productSearchTo.productName = rs.getString("product_name");
					productSearchTo.brand = rs.getString("brand_name");
					productSearchTo.gender = rs.getString("gender_name");
					productSearchTo.category = rs.getString("category_name");
					productSearchTo.imageUrl = rs.getString("image_url");
					productSearchTo.minPrice = rs.getFloat("price_from");
					productSearchTo.maxPrice = rs.getFloat("price_to");
					
					productSearchResult.add(productSearchTo);
				}
				
				return productSearchResult;
			}
			
		});
	}
	
	public int updateProductSummary(List<String> productIds) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("product_ids", productIds);
		
		return namedParameterJdbcTemplate.update(UPDATE_PRODUCT_SUMMARY, params);
	}
	
	public Product insert(String productId, String name, String url, Long brandId, Long genderId, Long categoryId) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("product_id", productId, Types.VARCHAR);
		params.addValue("name", name, Types.VARCHAR);
		params.addValue("url", url, Types.VARCHAR);
		params.addValue("brand_id", brandId, Types.BIGINT);
		params.addValue("gender_id", genderId, Types.BIGINT);
		params.addValue("category_id", categoryId, Types.BIGINT);
		
		return namedParameterJdbcTemplate.query(INSERT_PRODUCT, params, new ResultSetExtractor<Product>() {

			@Override
			public Product extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.next();
				
				Product product = mapResultSetToProduct(rs);
				
				return product;
			}
			
		});
	}

	public Product get(String productId, Long brandId) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("product_id", productId);
		params.addValue("brand_id", brandId);
		
		return namedParameterJdbcTemplate.query(GET_PRODUCT, params, new ResultSetExtractor<Product>() {

			@Override
			public Product extractData(ResultSet rs) throws SQLException, DataAccessException {
				Product product = null;
				if (rs.next()) {
					product = mapResultSetToProduct(rs);
				}
				
				return product;
			}
			
		});
	}
	
	private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
		Product product = new Product();
		product.id = rs.getLong("id");
		product.productId = rs.getString("product_id");
		product.name = rs.getString("name");
		product.url = rs.getString("url");
		product.brandId = rs.getLong("brand_id");
		product.genderId = rs.getLong("gender_id");
		product.categoryId = rs.getLong("category_id");
		product.createDate = rs.getTimestamp("create_date");
		product.updateDate = rs.getTimestamp("update_date");
		
		return product;
	}

	public Map<Long, Map<Long, Map<Long, List<SnapshotProduct>>>> getSnapshotProduct(Map<Long, Price> newActiveProducts, Map<Long, Price> existingSnapshotProducts) {
		Map<Long, Map<Long, Map<Long, List<SnapshotProduct>>>> snapshotProducts = new HashMap<Long, Map<Long, Map<Long, List<SnapshotProduct>>>>();
		if (newActiveProducts != null && !newActiveProducts.isEmpty()) {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("product_ids", newActiveProducts.keySet());
			
			namedParameterJdbcTemplate.query(GET_SNAPSHOT_PRODUCTS, params, new SnapshotProductRowCallbackHandler(snapshotProducts, newActiveProducts, true));
		}
		
		if (existingSnapshotProducts != null && !existingSnapshotProducts.isEmpty()) {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("product_ids", existingSnapshotProducts.keySet());
			
			namedParameterJdbcTemplate.query(GET_SNAPSHOT_PRODUCTS, params, new SnapshotProductRowCallbackHandler(snapshotProducts, existingSnapshotProducts, false));
		}
		
		return snapshotProducts;
	}
	
	static class SnapshotProductRowCallbackHandler implements RowCallbackHandler {
		
		private Map<Long, Price> products;
		private Map<Long, Map<Long, Map<Long, List<SnapshotProduct>>>> snapshotProducts;
		private Boolean isNew;
		
		public SnapshotProductRowCallbackHandler(Map<Long, Map<Long, Map<Long, List<SnapshotProduct>>>> snapshotProducts,
				Map<Long, Price> products, Boolean isNew) {
			this.products = products;
			this.snapshotProducts = snapshotProducts;
			this.isNew = isNew;
		}

		@Override
		public void processRow(ResultSet rs) throws SQLException {
			SnapshotProduct snapshotProduct = new SnapshotProduct();
			snapshotProduct.productId = rs.getLong("product_id");
			snapshotProduct.productName = rs.getString("product_name");
			snapshotProduct.productURL = rs.getString("product_url");
			snapshotProduct.imageURL = rs.getString("image_url");
			Long brandId = rs.getLong("brand_id");
			snapshotProduct.brandId = brandId;
			snapshotProduct.brand = rs.getString("brand");
			Long genderId = rs.getLong("gender_id");
			snapshotProduct.genderId = genderId;
			snapshotProduct.gender = rs.getString("gender");
			Long categoryId = rs.getLong("category_id"); 
			snapshotProduct.categoryId = categoryId;
			snapshotProduct.category = rs.getString("category");
			snapshotProduct.priceRegular = products.get(snapshotProduct.productId).priceRegular;
			snapshotProduct.priceDiscount = products.get(snapshotProduct.productId).priceDiscount;
			snapshotProduct.discount = (snapshotProduct.priceRegular - snapshotProduct.priceDiscount) / snapshotProduct.priceRegular;
			snapshotProduct.isNew = isNew;
			
			if (!snapshotProducts.containsKey(brandId)) {
				snapshotProducts.put(brandId, new HashMap<Long, Map<Long, List<SnapshotProduct>>>());
			}
			
			if (!snapshotProducts.get(brandId).containsKey(genderId)) {
				snapshotProducts.get(brandId).put(genderId, new HashMap<Long, List<SnapshotProduct>>());
			}
			
			if (!snapshotProducts.get(brandId).get(genderId).containsKey(categoryId)) {
				snapshotProducts.get(brandId).get(genderId).put(categoryId, new LinkedList<SnapshotProduct>());
			}
			
			snapshotProducts.get(brandId).get(genderId).get(categoryId).add(snapshotProduct);
		}
		
	}
}
