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

import com.apptivedeals.entity.Brand;

@Component
public class BrandDao {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	private static final String INSERT_BRAND = "insert into brands (name, create_date, update_date) "
			+ "values (:brand, now(), now()) " 
			+ "on conflict (lower(name)) do update set name = :brand "
			+ "returning id, name, create_date, update_date";
	
	public Brand insert(String brand) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("brand", brand, Types.VARCHAR);
		
		return namedParameterJdbcTemplate.query(INSERT_BRAND, params, new ResultSetExtractor<Brand>() {

			@Override
			public Brand extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.next();
				
				Brand brand = new Brand();
				brand.id = rs.getLong("id");
				brand.name = rs.getString("name");
				brand.createDate = rs.getTimestamp("create_date");
				brand.updateDate = rs.getTimestamp("update_date");
				
				return brand;
			}

		});
				
	}
	
}
