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

import com.apptivedeals.entity.Category;


@Component
public class CategoryDao {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	private static final String INSERT_CATEGORY = "insert into categories (name, create_date, update_date) "
			+ "values (:category, now(), now()) " 
			+ "on conflict (lower(name)) do update set name = :category "
			+ "returning id, name, create_date, update_date";
	
	public Category insert(String category) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("category", category, Types.VARCHAR);
		
		return namedParameterJdbcTemplate.query(INSERT_CATEGORY, params, new ResultSetExtractor<Category>() {

			@Override
			public Category extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.next();
				
				Category category = new Category();
				category.id = rs.getLong("id");
				category.name = rs.getString("name");
				category.createDate = rs.getTimestamp("create_date");
				category.updateDate = rs.getTimestamp("update_date");
				
				return category;
			}

		});
				
	}
}
