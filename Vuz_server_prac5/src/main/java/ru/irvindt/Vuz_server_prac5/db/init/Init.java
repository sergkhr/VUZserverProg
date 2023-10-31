package ru.irvindt.Vuz_server_prac5.db.init;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import ru.irvindt.Vuz_server_prac5.db.model.Book;
import ru.irvindt.Vuz_server_prac5.db.repository.BookRepository;

@Component
public class Init {
	private final BookRepository bookRepository;

	@Autowired
	public Init(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@PostConstruct
	public void initializeDatabase() {
		// Проверяем, есть ли уже записи в таблице книг
		if (bookRepository.count() == 0) {
			// Если нет, добавляем начальные данные
			Book initBook1 = new Book();
			initBook1.setTitle("Init Book 1");
			initBook1.setAuthor("Author 1");
			initBook1.setSellerNumber("Seller Number 1");
			initBook1.setProductType("Product Type 1");
			initBook1.setPrice(1.0);
			bookRepository.save(initBook1);

			Book initBook2 = new Book();
			initBook2.setTitle("Init Book 2");
			initBook2.setAuthor("Author 2");
			initBook2.setSellerNumber("Seller Number 2");
			initBook2.setProductType("Product Type 2");
			initBook2.setPrice(2.0);
			bookRepository.save(initBook2);

			// Добавьте другие начальные данные, если необходимо
		}
	}
}
