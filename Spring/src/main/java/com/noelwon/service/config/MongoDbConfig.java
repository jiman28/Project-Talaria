package com.noelwon.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableJpaAuditing
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableJpaRepositories(basePackages = "com.noelwon.model.*")
@EnableMongoRepositories(basePackages = "com.noelwon.mongoDb")
public class MongoDbConfig {

	private final MongoMappingContext mongoMappingContext;

	
	
	public MongoDbConfig(MongoMappingContext mongoMappingContext) {
		super();
		this.mongoMappingContext = mongoMappingContext;
	}
//	
//	@Value("${mongodb.test.connectionString}")
//	private String connectionString;
//	
//	@Bean
//	public MongoDatabaseFactory mongoDatabaseFactory() {
//		return new SimpleMongoClientDatabaseFactory(connectionString);
//	}
//
//	@Bean
//	public MongoTemplate mongoTemplate() {
//		return new MongoTemplate(mongoDatabaseFactory());
//	}


	@Bean
	public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory mongoDatabaseFactory,
		MongoMappingContext mongoMappingContext) {
		DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
		MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
		converter.setTypeMapper(new DefaultMongoTypeMapper(null));
		return converter;
	}
}