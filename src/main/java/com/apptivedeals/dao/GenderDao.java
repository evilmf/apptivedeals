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

import com.apptivedeals.entity.Gender;

@Component
public class GenderDao {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	private static final String INSERT_GENDER = "insert into genders (name, create_date, update_date) "
			+ "values (:gender, now(), now()) " 
			+ "on conflict (lower(name)) do update set name = :gender "
			+ "returning id, name, create_date, update_date";
	
	public Gender insert(String gender) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("gender", gender, Types.VARCHAR);
		
		return namedParameterJdbcTemplate.query(INSERT_GENDER, params, new ResultSetExtractor<Gender>() {

			@Override
			public Gender extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.next();
				
				Gender gender = new Gender();
				gender.id = rs.getLong("id");
				gender.name = rs.getString("name");
				gender.createDate = rs.getTimestamp("create_date");
				gender.updateDate = rs.getTimestamp("update_date");
				
				return gender;
			}

		});
				
	}
}
