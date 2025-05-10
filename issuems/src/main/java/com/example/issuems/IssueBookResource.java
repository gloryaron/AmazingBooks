package com.example.issuems;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping ("/issue")
public class IssueBookResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(IssueBookResource.class);
	
	@Autowired
	private IssueBookRepo repo;
	
	@Autowired
    private WebClient webClient; //load-balanced
	
	@GetMapping("/{bookId}/{customerId}")
	public ResponseEntity<?> getBookDetails(@PathVariable Integer bookId, Integer customerId){
		LOGGER.info("Getting all the book details with availablecopies");
		
		Optional<IssueBookResponse> issueBook = repo.findById(bookId);
		if(customerId.equals(issueBook.get().getCustId())) {
			LOGGER.info("Same customer cannot be issued the book twice customerId:{}, bookId:{}",customerId, bookId);
			return ResponseEntity.status(404).body("{\"error\": \"This book has already been issued to the same customer\"");
		}
		Book book= webClient.get().uri("/books" + "/" + bookId).retrieve().bodyToMono(Book.class).block();
		
		IssueBookResponse response = new IssueBookResponse();
		response.setBookId(bookId);
		response.setCustId(customerId);
		response.setIsbn(book.getIsbn());
		response.setTitle(book.getTitle());
		int availableCopies= setAvailableCopies(book.getTotalCopies(), book.getIssuedCopies());
		
		if (availableCopies==0){
			return ResponseEntity.status(404).body("{\"error\": \"The book is not available\"");
		}
		response.setAvailableCopies(availableCopies);
		response.setIssuedCopies(book.getIssuedCopies()+1);
		repo.save(response);
		
		book.setIssuedCopies(response.getIssuedCopies());
		LOGGER.info("book.getIssuedCopies() {}",book.getIssuedCopies());
		
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
		int availableCopies=totalCopies-issuedCopies;
		
		if (availableCopies < 1) {
			availableCopies =0;
			LOGGER.error("Insufficient available copies {}");
			return availableCopies;
	    }else {
	    	availableCopies = availableCopies-1;
	    }
		LOGGER.error("availableCopies {}:",availableCopies);
		return availableCopies;
	}
	
	@GetMapping
	public List<IssueBookResponse> getAllBookDetails(){
		
		LOGGER.info("Getting all books from Database");
		return repo.findAll();
		
	}
	
}
