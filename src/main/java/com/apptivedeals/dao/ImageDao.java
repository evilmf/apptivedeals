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

import com.apptivedeals.entity.Image;

@Component
public class ImageDao {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	private static final String INSERT_IMAGE = "insert into images (url, product_id, is_primary, create_date, update_date) "
			+ "values (:url, :product_id, :is_primary, now(), now()) "
			+ "on conflict (product_id, lower(url)) do update set is_primary = :is_primary, "
			+ "update_date = case when EXCLUDED.is_primary = :is_primary then EXCLUDED.update_date else now() end "
			+ "returning id, url, product_id, is_primary, create_date, update_date";
	
	public Image insert(String url, Long productId, Boolean isPrimary) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("url", url, Types.VARCHAR);
		params.addValue("product_id", productId, Types.BIGINT);
		params.addValue("is_primary", isPrimary, Types.BOOLEAN);
		
		return namedParameterJdbcTemplate.query(INSERT_IMAGE, params, new ResultSetExtractor<Image>() {
			
			@Override
			public Image extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.next();
				
				Image image = new Image();
				image.id = rs.getLong("id");
				image.url = rs.getString("url");
				image.productId = rs.getLong("product_id");
				image.isPrimary = rs.getBoolean("is_primary");
				image.createDate = rs.getTimestamp("create_date");
				image.updateDate = rs.getTimestamp("update_date");
				
				return image;
			}
		});
	}
}
