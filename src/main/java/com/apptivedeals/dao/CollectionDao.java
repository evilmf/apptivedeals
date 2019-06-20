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

import com.apptivedeals.entity.Collection;

@Component
public class CollectionDao {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private static final String INSERT_COLLECTION = "insert into collections (collection_id, brand_id, product_id, create_date, update_date) "
			+ "values (:collection_id, :brand_id, :product_id, now(), now()) "
			+ "on conflict (lower(collection_id), brand_id, product_id) do update set id = EXCLUDED.id "
			+ "returning id, collection_id, brand_id, product_id, create_date, update_date";
	
	public Collection insert(String collectionId, Long brandId, Long productId) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("collection_id", collectionId, Types.VARCHAR);
		params.addValue("brand_id", brandId, Types.VARCHAR);
		params.addValue("product_id", productId, Types.VARCHAR);
		
		return namedParameterJdbcTemplate.query(INSERT_COLLECTION, params, new ResultSetExtractor<Collection>() {

			@Override
			public Collection extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.next();
				
				Collection collection = new Collection();
				collection.id = rs.getLong("id");
				collection.collectionId = rs.getString("collection_id");
				collection.brandId = rs.getLong("brand_id");
				collection.productId = rs.getLong("product_id");
				collection.createDate = rs.getTimestamp("create_date");
				collection.updateDate = rs.getTimestamp("update_date");
				
				return collection;
			}
			
		});
	}
}
