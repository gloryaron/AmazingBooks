package com.example.issuems;

import java.time.LocalDate;

import jakarta.persistence.Id;

public class Book {

	@Id
	private int bookId;
	private String isbn;
	private String title;
	private String author;
	private LocalDate publishedDate;
	private int totalCopies;
	private int issuedCopies;
	
	
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
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
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public LocalDate getPublishedDate() {
		return publishedDate;
	}
	public void setPublishedDate(LocalDate publishedDate) {
		this.publishedDate = publishedDate;
	}
	public int getTotalCopies() {
		return totalCopies;
	}
	public void setTotalCopies(int totalCopies) {
		this.totalCopies = totalCopies;
	}
	public int getIssuedCopies() {
		return issuedCopies;
	}
	public void setIssuedCopies(int issuedCopies) {
		this.issuedCopies = issuedCopies;
	}
	
	
}
