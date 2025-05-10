package com.example.issuems;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueBookRepo extends JpaRepository<IssueBookResponse, Integer>{
	

}
