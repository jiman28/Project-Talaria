package com.noelwon.service;

import java.util.List;
import java.util.Optional;

public interface TotalRepositoryInterface {

	public <T> Optional<T> findById(T findbyid); 
	
	public <T> List<T> findAll(); 
	
	public <T> void delete(T delete); 
	
	public void deleteAll();
	
	
}
