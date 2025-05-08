package com.example.issuerms;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueBookRepo extends JpaRepository<IssueBook, Integer>{
	
	List <IssueBook> findByIsReturnedFalse();

}
