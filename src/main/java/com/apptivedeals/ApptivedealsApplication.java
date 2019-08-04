package com.apptivedeals;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

@EnableScheduling
@SpringBootApplication
public class ApptivedealsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApptivedealsApplication.class, args);
	}

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private PlatformTransactionManager transactionManager;

	@Bean
	public TransactionTemplate getTransactionTemplate() {
		return new TransactionTemplate(transactionManager);
	}

	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
		return new NamedParameterJdbcTemplate(dataSource);
	}

	@Bean
	public ObjectMapper getObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
			private static final long serialVersionUID = 73214488044004671L;

			@Override
		    public JsonProperty.Access findPropertyAccess(Annotated m) {
		        JsonProperty ann = _findAnnotation(m, JsonProperty.class);
		        if (ann != null) {
		        	if (ann.access().equals(JsonProperty.Access.WRITE_ONLY)) {
		        		return JsonProperty.Access.AUTO;
		        	}
		        	else {
		        		return ann.access();
		        	}
		        }
		        return null;
		    }
		});
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		//objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
		
		return objectMapper;
	}
}
