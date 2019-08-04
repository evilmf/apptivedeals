package com.apptivedeals.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.apptivedeals.entity.Price;
import com.apptivedeals.entity.Snapshot.SnapshotProduct;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SnapshotDao {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private TransactionTemplate transactionTemplate;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SnapshotDao.class);
	
	private final static String GET_CURRENT_SNAPSHOT_PRODUCTS = "select "
			+ "  id, product_id, price_regular, price_discount, snapshot_id, is_active, create_date " 
			+ "from ( "
			+ "  select distinct on (product_id) "
			+ "    id, product_id, price_regular, price_discount, snapshot_id, is_active, create_date "
			+ "  from snapshot_detail " + "  order by product_id, create_date desc " 
			+ ") t where is_active is true";
	
	private final static String LOCK_SNAPSHOT_DETAIL = "lock table only snapshot_detail in ACCESS EXCLUSIVE mode";
	
	private final static String GET_NEXT_SNAPSHOT_ID = "select coalesce(max(snapshot_id) + 1, 1) snapshot_id from snapshot_detail";
	
	private final static String INSERT_SNAPSHOT_DETAIL = "insert into snapshot_detail "
			+ "(product_id, price_regular, price_discount, snapshot_id, is_active, create_date) "
			+ "values (?, ?, ?, ?, ?, now())";
	
	private final static String INSERT_SNAPSHOT = "insert into snapshots (id, snapshot, create_date, update_date) " 
			+ "values (?, ?, now(), now())";
	
	public int insertSnapshot(Long id, Map<Long, Map<Long, Map<Long, List<SnapshotProduct>>>> snapshotProducts) {
		PGobject pgObject = new PGobject();
		pgObject.setType("jsonb");
		
		try {
			pgObject.setValue(objectMapper.writeValueAsString(snapshotProducts));
			return jdbcTemplate.update(INSERT_SNAPSHOT, new Object[] { id, pgObject });
		} catch (JsonProcessingException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return 0;
	}
	
	public Map<Long, Price> getLatestSnapshotPrices() {
		return namedParameterJdbcTemplate.query(GET_CURRENT_SNAPSHOT_PRODUCTS,
				new ResultSetExtractor<Map<Long, Price>>() {

					@Override
					public Map<Long, Price> extractData(ResultSet rs) throws SQLException, DataAccessException {
						Map<Long, Price> prices = new HashMap<Long, Price>();
						while (rs.next()) {
							Long productId = rs.getLong("product_id");
							Price price = new Price();
							price.priceRegular = rs.getFloat("price_regular");
							price.priceDiscount = rs.getFloat("price_discount");
							
							prices.put(productId, price);
						}
						
						return prices;
					}
				});
	}
	
	private void lockSnapshotDetail() {
		jdbcTemplate.execute(LOCK_SNAPSHOT_DETAIL);
	}

	private Long getNextSnapshotId() {
		return jdbcTemplate.queryForObject(GET_NEXT_SNAPSHOT_ID, Long.class);
	}
	
	private int insertSnapshotDetail(Long productId, float priceRegular, float priceDiscount, Long snapshotId,
			Boolean isActive) {
		return jdbcTemplate.update(INSERT_SNAPSHOT_DETAIL,
				new Object[] { productId, priceRegular, priceDiscount, snapshotId, isActive });
	}
	
	public Long insertSnapshotDetails(Map<Long, Price> newActiveProducts, Map<Long, Price> newInactiveProducts) {
		return transactionTemplate.execute(new TransactionCallback<Long>() {

			@Override
			public Long doInTransaction(TransactionStatus status) {
				lockSnapshotDetail();
				
				Long snapshotId = null;
				
				if (newActiveProducts != null && !newActiveProducts.isEmpty()) {
					Boolean isActive = true;
					snapshotId = getNextSnapshotId();
					for (Long productId : newActiveProducts.keySet()) {
						Price price = newActiveProducts.get(productId);
						insertSnapshotDetail(productId, price.priceRegular, price.priceDiscount, snapshotId, isActive);
					}
				}
				
				if (newInactiveProducts != null && !newInactiveProducts.isEmpty()) {
					Boolean isActive = false;
					if (snapshotId == null) {
						snapshotId = getNextSnapshotId();
					}
					for (Long productId : newInactiveProducts.keySet()) {
						Price price = newInactiveProducts.get(productId);
						insertSnapshotDetail(productId, price.priceRegular, price.priceDiscount, snapshotId, isActive);
					}
				}
				
				return snapshotId;
			}
		});
	}
	
}
