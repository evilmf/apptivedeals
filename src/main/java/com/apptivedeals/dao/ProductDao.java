package com.apptivedeals.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.apptivedeals.entity.Product;

@Component
public class ProductDao {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	private static final String INSERT_PRODUCT = "insert into products (product_id, name, url, brand_id, gender_id, category_id, create_date, update_date, name_tsvector) "
			+ "values (:product_id, :name, :url, :brand_id, :gender_id, :category_id, now(), now(), to_tsvector('simple', :name)) "
			+ "on conflict (lower(product_id), brand_id) do update set id = EXCLUDED.id "
			+ "returning id, product_id, name, url, brand_id, gender_id, category_id, create_date, update_date";
	
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
			
		});
	}
}
