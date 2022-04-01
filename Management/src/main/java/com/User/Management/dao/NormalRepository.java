package com.User.Management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.User.Management.entities.Normal;
import com.User.Management.entities.User;
 

public interface NormalRepository extends JpaRepository<Normal,Integer>{

	@Query("select n from Normal n where n.email=:email") 
	public User getUserByUserName(@Param("email") String email);
	
}
