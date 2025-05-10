package com.example.issuems;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "issue_details")
public class IssueBookResponse {
	
	@Id
	@GeneratedValue
	private int bookId;
	private int custId;
	private String isbn;
	private String title;
	public int availableCopies;
	private int issuedCopies;
	
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public int getCustId() {
		return custId;
	}
	public void setCustId(int custId) {
		this.custId = custId;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getAvailableCopies() {
		return availableCopies;
	}
	public void setAvailableCopies(int availableCopies) {
		this.availableCopies = availableCopies;
	}
	public int getIssuedCopies() {
		return issuedCopies;
	}
	public void setIssuedCopies(int issuedCopies) {
		this.issuedCopies = issuedCopies;
	}
	

}
