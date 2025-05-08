package com.example.issuerms;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.bookms.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping ("/issue")
public class IssueBookResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(IssueBookResource.class);
	
	@Autowired
	private IssueBookRepo repo;
	
	/*
	 * @Autowired private BookRepo bookRepo;
	 */
	
	@Autowired
    private WebClient webClient; //load-balanced
	
	@GetMapping("/{bookId}")
	public ResponseEntity<IssueBookResponse> getBookDetails(@PathVariable Integer bookId){
		LOGGER.info("Getting all the book details with availablecopies");
		
		Book book = webClient.get().uri("/books" + "/" + bookId).retrieve().bodyToMono(Book.class).block();
		
		Optional<IssueBook> issueBook = repo.findById(bookId);
		IssueBookResponse response = new IssueBookResponse();
		response.setAuthor(book.getAuthor());
		response.setBookId(book.getBookId());
		response.setIsbn(book.getIsbn());
		response.setPublishedDate(book.getPublishedDate());
		response.setTitle(book.getTitle());
		int availableCopies= setAvailableCopies(book.getTotalCopies(), book.getIssuedCopies());
		
		if (availableCopies < issueBook.get().getNoOfCopies()) {
			LOGGER.error("Insufficient available copies {}",book.getBookId());
			return ResponseEntity.badRequest().build();
	    }
		response.setAvailableCopies(availableCopies);
		
		
		
		repo.save(issueBook.get());   
		LOGGER.info("saving to repo {}",book.getBookId());
		
		//bookRepo.findById(bookId);
		
		
		book.setIssuedCopies(book.getIssuedCopies()+1);
		LOGGER.info("book.getIssuedCopies() {}",book.getIssuedCopies());
		
		//bookRepo.save(book);
		//LOGGER.error("saving to book repo {}",book.getBookId());
		//response.setAvailableCopies(book.getTotalCopies()-book.getIssuedCopies());
		updateIssuedCopiesInBookMS(book.getBookId(), book.getIssuedCopies());
		
		return ResponseEntity.ok(response);
		
	}
	
	private void updateIssuedCopiesInBookMS(int bookId, int issuedCopies) {
		LOGGER.info("calling /books/{bookId}/issued-copies?issuedCopies={issuedCopies} {}",issuedCopies);
		LOGGER.info("calling /books/{bookId}/issued-copies?issuedCopies={issuedCopies} bookId: {}",bookId);
		webClient.put().uri("/books/{bookId}/issued-copies?issuedCopies={issuedCopies}", bookId, issuedCopies).retrieve().toBodilessEntity().block();
		LOGGER.info("successfully called issuedCopies");

		
	}

	private int setAvailableCopies(int totalCopies, int issuedCopies) {
		int availableCopies=0;
		availableCopies= totalCopies-issuedCopies;
		
		return availableCopies;
	}
	
	@GetMapping
	public List<IssueBook> getAllBookDetails(){
		
		LOGGER.info("Getting all books from Database");
		return repo.findAll();
		
	}
	

	/*
	 * @PostMapping("/{bookId}/issue") public ResponseEntity<IssueBookResponse>
	 * issueBook(@PathVariable Integer bookId) {
	 * LOGGER.info("Getting all the book details with availablecopies"); Book book =
	 * webClient.get().uri("/books" + "/" +
	 * bookId).retrieve().bodyToMono(Book.class).block(); if (book == null) {
	 * LOGGER.error("book not found {}", bookId); return
	 * ResponseEntity.notFound().build(); } int available = book.getTotalCopies() -
	 * book.getIssuedCopies(); if (available < issueBook.getNoOfCopies()) { return
	 * ResponseEntity.badRequest().body("Insufficient available copies"); }
	 * repo.save(issueBook);
	 * 
	 * webClient.put().uri("/books" + "/" + issueBook.getBookId()+
	 * "/issue?noOfCopies=" +
	 * issueBook.getNoOfCopies()).retrieve().bodyToMono(Book.class).block();
	 * 
	 * return ResponseEntity.ok("Book issued successfully"); }
	 */
	
}
