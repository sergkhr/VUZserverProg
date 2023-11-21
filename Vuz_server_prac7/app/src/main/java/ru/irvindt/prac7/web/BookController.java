package ru.irvindt.prac7.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.irvindt.prac7.db.model.Book;
import ru.irvindt.prac7.db.repository.BookRepository;
import ru.irvindt.prac7.web.token_cheching.TokenChecker;
import ru.irvindt.prac7.db.model.UserRole;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/book")
public class BookController {
	private final BookRepository bookRepository;
	private final TokenChecker tokenChecker;

	@Autowired
	public BookController(BookRepository bookRepository,
	                      TokenChecker tokenChecker) {
		this.bookRepository = bookRepository;
		this.tokenChecker = tokenChecker;
	}

	@GetMapping("/")
	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	@GetMapping("/{id}")
	public Book getBookById(@PathVariable Long id) {
		return bookRepository.findById(id).orElse(null);
	}

	@PostMapping("/")
	public Book createBook(@RequestBody Book book,
	                       @RequestHeader("Authorization") String header) {
		//check if user is admin
		if (tokenChecker.getRoleFromTokenHeader(header) != UserRole.ADMIN) {
			Book responceBook = new Book();
			responceBook.setAuthor("You are not admin");
			return responceBook;
		}

		return bookRepository.save(book);
	}

	//!!! ВНИМАНИЕ !!!
	// Из-за того, что put запрос теперь может обновлять выборочные поля объекта, пропала возможность сделать цену равной 0
	@PutMapping("/{id}")
	public Book updateBook(@PathVariable Long id, @RequestBody Book updatedBook,
	                       @RequestHeader("Authorization") String header) {
		if(tokenChecker.getRoleFromTokenHeader(header) != UserRole.ADMIN){
			Book responceBook = new Book();
			responceBook.setAuthor("You are not admin");
			return responceBook;
		}

		Optional<Book> optionalBook = bookRepository.findById(id);
		if (optionalBook.isPresent()) {
			Book existingBook = optionalBook.get();

			// Обновляем только необходимые поля
			if (updatedBook.getAuthor() != null) {
				existingBook.setAuthor(updatedBook.getAuthor());
			}
			if (updatedBook.getSellerNumber() != null) {
				existingBook.setSellerNumber(updatedBook.getSellerNumber());
			}
			if (updatedBook.getProductType() != null) {
				existingBook.setProductType(updatedBook.getProductType());
			}
			if (updatedBook.getPrice() != 0) {
				existingBook.setPrice(updatedBook.getPrice());
			}
			if (updatedBook.getTitle() != null) {
				existingBook.setTitle(updatedBook.getTitle());
			}
			if (updatedBook.getInStock() != 0){
				existingBook.setInStock(updatedBook.getInStock());
			}

			return bookRepository.save(existingBook);
		}
		return null;
	}

	@DeleteMapping("/{id}")
	public String deleteBook(@PathVariable Long id,
	                       @RequestHeader("Authorization") String header) {
		//check if user is admin
		if (tokenChecker.getRoleFromTokenHeader(header) != UserRole.ADMIN) {
			return "You are not admin";
		}
		bookRepository.deleteById(id);
		return "Book deleted";
	}
}
