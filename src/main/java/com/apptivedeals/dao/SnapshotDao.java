package com.apptivedeals.dao;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.apptivedeals.entity.Price;
import com.apptivedeals.entity.Snapshot;
import com.apptivedeals.entity.Snapshot.SnapshotProduct;
import com.apptivedeals.to.ProductSnapshot;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
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
	
	private final static String INSERT_SNAPSHOT = "insert into snapshots (id, snapshot, create_date, update_date, snapshot_date) " 
			+ "values (?, ?, now(), now(), ?) "
			+ "returning id, snapshot, create_date, update_date, snapshot_date";
	
	private final static String GET_SNAPSHOT_BY_ID = "select id, snapshot, create_date, update_date, snapshot_date from snapshots where id = ?";
	
	private final static String GET_LATEST_SNAPSHOT = "select id, snapshot, create_date, update_date, snapshot_date from snapshots order by 1 desc limit 1";
	
	private final static String GET_PRODUCT_SNAPSHOTS = "select " 
			+ "    sd.id id, " 
			+ "    sd.product_id, "
			+ "    p.url product_url, " 
			+ "    b.name brand_name, " 
			+ "    g.name gender_name, "
			+ "    c.name category_name, " 
			+ "    sd.snapshot_id, " 
			+ "    sd.price_discount, "
			+ "    sd.price_regular, " 
			+ "    sd.is_active, " 
			+ "    sd.create_date " 
			+ "from snapshot_detail sd "
			+ "join products p on p.id = sd.product_id " 
			+ "join brands b on b.id = p.brand_id "
			+ "join genders g on g.id = p.gender_id " 
			+ "join categories c on c.id = p.category_id "
			+ "where sd.product_id = :product_id " 
			+ "order by sd.id";
	
	public List<ProductSnapshot> getProductSnapshots(Long productId) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("product_id", productId);
		
		return namedParameterJdbcTemplate.query(GET_PRODUCT_SNAPSHOTS, params, new ResultSetExtractor<List<ProductSnapshot>>() {

			@Override
			public List<ProductSnapshot> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<ProductSnapshot> productSnapshots = new LinkedList<ProductSnapshot>();
				while (rs.next()) {
					ProductSnapshot productSnapshot = new ProductSnapshot();
					productSnapshot.id = rs.getLong("id");
					productSnapshot.productId = rs.getLong("product_id");
					productSnapshot.productUrl = rs.getString("product_url");
					productSnapshot.brand = rs.getString("brand_name");
					productSnapshot.gender = rs.getString("gender_name");
					productSnapshot.category = rs.getString("category_name");
					productSnapshot.snapshotId = rs.getLong("snapshot_id");
					productSnapshot.priceDiscount = rs.getFloat("price_discount");
					productSnapshot.priceRegular = rs.getFloat("price_regular");
					productSnapshot.isActive = rs.getBoolean("is_active");
					productSnapshot.createDate = rs.getTimestamp("create_date");
					
					productSnapshots.add(productSnapshot);
				} 
				
				return productSnapshots;
			}
			
		});
	} 
	
	public Snapshot getLatestSnapshot() {
		return jdbcTemplate.query(GET_LATEST_SNAPSHOT, new ResultSetExtractor<Snapshot>() {

			@Override
			public Snapshot extractData(ResultSet rs) throws SQLException, DataAccessException {
				Snapshot snapshot = null;
				
				if (rs.next()) {
					snapshot = mapSnapshotResultSet(rs);
				}
				
				return snapshot;
			}
			
		});
	}
	
	public Snapshot getSnapshot(Long id) {
		return jdbcTemplate.query(GET_SNAPSHOT_BY_ID, new Object[] { id }, new ResultSetExtractor<Snapshot>() {

			@Override
			public Snapshot extractData(ResultSet rs) throws SQLException, DataAccessException {
				Snapshot snapshot = null;
				
				if (rs.next()) {
					snapshot = mapSnapshotResultSet(rs);
				}
				
				return snapshot;
			}
			
		});
	}
	
	private Snapshot mapSnapshotResultSet(ResultSet rs) throws SQLException {
		Snapshot snapshot = new Snapshot();
		snapshot.id = rs.getLong("id");
		snapshot.createDate = rs.getTimestamp("create_date");
		snapshot.updateDate = rs.getTimestamp("update_date");
		snapshot.snapshotDate = rs.getTimestamp("snapshot_date");
		
		String snapshotString = rs.getString("snapshot");
		TypeReference<Map<Long, Map<Long, Map<Long, List<SnapshotProduct>>>>> typeRef =
				new TypeReference<Map<Long, Map<Long, Map<Long, List<SnapshotProduct>>>>>() {};
		try {
			snapshot.snapshotProducts = objectMapper.readValue(snapshotString, typeRef);
		} catch (JsonParseException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return snapshot;
	}
	
	public Snapshot insertSnapshot(Long id, Map<Long, Map<Long, Map<Long, List<SnapshotProduct>>>> snapshotProducts, Date snapshotDate) {
		PGobject pgObject = new PGobject();
		pgObject.setType("jsonb");
		Snapshot snapshot = null;
		
		try {
			pgObject.setValue(objectMapper.writeValueAsString(snapshotProducts));
			snapshot = jdbcTemplate.queryForObject(INSERT_SNAPSHOT, new Object[] { id, pgObject, snapshotDate }, new RowMapper<Snapshot>() {

				@Override
				public Snapshot mapRow(ResultSet rs, int rowNum) throws SQLException {
					return mapSnapshotResultSet(rs);
				}
				
			});
		} catch (JsonProcessingException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return snapshot;
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
