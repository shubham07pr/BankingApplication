package com.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.entity.User;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Long>{
	
	boolean existsByEmail(String email); 
	
	boolean existsByAccountNumber(String accountNumber);
	
	User findByAccountNumber(String accountNumber);
	User findByEmail(String email);
	

}
