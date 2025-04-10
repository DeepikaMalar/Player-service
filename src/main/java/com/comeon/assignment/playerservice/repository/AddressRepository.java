package com.comeon.assignment.playerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.comeon.assignment.playerservice.bean.Address;
	

public interface AddressRepository extends JpaRepository<Address, Long>{

}
