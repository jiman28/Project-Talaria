package com.noelwon.mongoDb;


import org.springframework.data.mongodb.repository.MongoRepository;


public interface ChattRepository extends MongoRepository<ChattIng, String>{

}
