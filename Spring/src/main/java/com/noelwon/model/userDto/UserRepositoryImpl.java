package com.noelwon.model.userDto;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

//@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl {

	private final MongoOperations mongoOperations;

	public UserRepositoryImpl(MongoOperations mongoOperations) {
		super();
		this.mongoOperations = mongoOperations;
	}

	

}
