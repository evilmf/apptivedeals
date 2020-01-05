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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.apptivedeals.entity.Category;


@Component
public class CategoryDao {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final String INSERT_CATEGORY = "insert into categories (name, create_date, update_date) "
			+ "values (:category, now(), now()) " 
			+ "on conflict (lower(name)) do update set name = :category "
			+ "returning id, name, create_date, update_date";
	
	private static final String GET_DEFAULT_CATEGORIES = "select distinct p.brand_id, p.gender_id, p.category_id " 
			+ "from products p " 
			+ "join categories c on c.id = p.category_id " 
			+ "where c.name ilike '%jacket%' or c.name ilike '%puffer%' or c.name ilike '%polo%' or c.name ilike '%hoodie%'";
	
	public Map<Long, Map<Long, List<Long>>> getDefaultCategories() {
		return jdbcTemplate.query(GET_DEFAULT_CATEGORIES, new ResultSetExtractor<Map<Long, Map<Long, List<Long>>>>() {

			@Override
			public Map<Long, Map<Long, List<Long>>> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<Long, Map<Long, List<Long>>> defaultCategories = new HashMap<Long, Map<Long, List<Long>>>();
				while (rs.next()) {
					Long brandId = rs.getLong("brand_id");
					Long genderId = rs.getLong("gender_id");
					Long categoryId = rs.getLong("category_id");
					
					if (!defaultCategories.containsKey(brandId)) {
						defaultCategories.put(brandId, new HashMap<Long, List<Long>>());
					}
					
					if (!defaultCategories.get(brandId).containsKey(genderId)) {
						defaultCategories.get(brandId).put(genderId, new LinkedList<Long>());
					}
					
					defaultCategories.get(brandId).get(genderId).add(categoryId);
				}
				
				return defaultCategories;
			}
			
		});
	}
	
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
