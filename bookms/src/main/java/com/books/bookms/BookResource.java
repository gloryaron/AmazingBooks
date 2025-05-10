package com.books.bookms;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/books")
public class BookResource {
	
private static final Logger LOGGER = LoggerFactory.getLogger(BookResource.class);
	
	@Autowired
	private BookRepo repo;
	
	@GetMapping
	public List<Book> getAllBooks(){
		
		LOGGER.info("Getting all books from Database");
		return repo.findAll();
		
	}
	
	@GetMapping("/{bookId}")
	public ResponseEntity<Book> getBookFromID(@PathVariable Integer bookId){
		
		LOGGER.info("Fetching the details of a book from database with bookId {}",bookId);
		
		Optional<Book> bookfound = repo.findById(bookId);
		if(bookfound.isEmpty()) {
			LOGGER.error("book id not found {}",bookId);
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(bookfound.get());
	}
	
	@PostMapping("/add")
	public ResponseEntity<Book> addBook(@RequestBody Book book){
		LOGGER.info("adding a book to database");
		Book savedBook = repo.save(book);
		LOGGER.info("added book to database with bookid {}",savedBook.getBookId());	
		return ResponseEntity.created(URI.create(book.getTitle())).body(savedBook);
			
	}
	
	@PostMapping("/update")
	public ResponseEntity<Book> updateBook(@RequestBody Book book){
		LOGGER.info("Updating book details into database with bookId {}",book.getBookId());
		
		Optional<Book> bookfound = repo.findById(book.getBookId());
		if(bookfound.isEmpty()) {
			LOGGER.error("book id not found {}",book.getBookId());
			return ResponseEntity.notFound().build();
		}
		Book updatedBook = null;
		updatedBook = repo.save(book);	
		LOGGER.info("updated Book to database with bookId{}",book.getBookId());	
		return ResponseEntity.created(URI.create((book.getTitle()))).body(updatedBook);
			
	}
	
	@GetMapping("/delete/{bookId}")
	public ResponseEntity<?> deleteBookById(@PathVariable Integer bookId){
		LOGGER.info("Deleting book from database with bookId {}",bookId);
		
		Optional<Book> bookfound = repo.findById(bookId);
		if(bookfound.isEmpty()) {
			LOGGER.info("book id not found {}",bookId);
			return (ResponseEntity.status(404).body("{\"error\": \"Book Not Found\""));
		}
		repo.deleteById(bookId);
		return ResponseEntity.ok().body("{\"message\": \"Book Deleted\"}");
				
		
	}
	
	@PutMapping("/{bookId}/issued-copies")
	public ResponseEntity<Void> setIssuedCopies(@PathVariable int bookId, @RequestParam int issuedCopies) {
	   
		Optional<Book> bookfound = repo.findById(bookId);
		if(bookfound.isEmpty()) {
			LOGGER.error("book id not found {}",bookId);
			return (ResponseEntity.notFound().build());
		}
		
		Book book = bookfound.get();
		LOGGER.info("book.getBookId() {}",book.getBookId());
		book.setIssuedCopies(issuedCopies);
		LOGGER.info("book.setIssuedCopies() {}",issuedCopies);
		repo.save(book);
		LOGGER.info("saving to repo {}",book.getIssuedCopies());
		return ResponseEntity.ok().build();
	}
	

}
