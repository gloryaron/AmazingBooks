package com.books.bookms;

import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepo extends JpaRepository<Book, Integer>{
	
}
