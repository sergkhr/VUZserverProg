package ru.irvindt.Vuz_server_prac5.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.irvindt.Vuz_server_prac5.db.model.Book;
import ru.irvindt.Vuz_server_prac5.db.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/book")
public class BookController {
	private final BookRepository bookRepository;

	@Autowired
	public BookController(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
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
	public Book createBook(@RequestBody Book book) {
		return bookRepository.save(book);
	}

	//!!! ВНИМАНИЕ !!!
	// Из-за того, что put запрос теперь может обновлять выборочные поля объекта, пропала возможность сделать цену равной 0
	@PutMapping("/{id}")
	public Book updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
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

			return bookRepository.save(existingBook);
		}
		return null;
	}

	@DeleteMapping("/{id}")
	public void deleteBook(@PathVariable Long id) {
		bookRepository.deleteById(id);
	}
}
