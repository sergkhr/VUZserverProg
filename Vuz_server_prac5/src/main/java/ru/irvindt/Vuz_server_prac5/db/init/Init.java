package ru.irvindt.Vuz_server_prac5.db.init;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import ru.irvindt.Vuz_server_prac5.db.model.Book;
import ru.irvindt.Vuz_server_prac5.db.model.Cart;
import ru.irvindt.Vuz_server_prac5.db.model.CartItem;
import ru.irvindt.Vuz_server_prac5.db.model.GoodsType;
import ru.irvindt.Vuz_server_prac5.db.repository.BookRepository;
import ru.irvindt.Vuz_server_prac5.db.repository.CartRepository;
import ru.irvindt.Vuz_server_prac5.db.repository.GoodsTypeRepository;

@Component
public class Init {
	private final BookRepository bookRepository;
	private final GoodsTypeRepository goodsTypeRepository;
	private final CartRepository cartRepository;

	@Autowired
	public Init(BookRepository bookRepository, GoodsTypeRepository goodsTypeRepository, CartRepository cartRepository) {
		this.bookRepository = bookRepository;
		this.goodsTypeRepository = goodsTypeRepository;
		this.cartRepository = cartRepository;
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
			initBook1.setInStock(10);
			bookRepository.save(initBook1);

			Book initBook2 = new Book();
			initBook2.setTitle("Init Book 2");
			initBook2.setAuthor("Author 2");
			initBook2.setSellerNumber("Seller Number 2");
			initBook2.setProductType("Product Type 2");
			initBook2.setPrice(2.0);
			initBook2.setInStock(10);
			bookRepository.save(initBook2);

			// Добавьте другие начальные данные, если необходимо
		}


		//initializing goods type
		if (goodsTypeRepository.findByName("Book") == null) {
			GoodsType bookType = new GoodsType();
			bookType.setName("Book");
			goodsTypeRepository.save(bookType);
		}


		//initialize a test cart and put there a book of id 1 if such a book exists
		if(cartRepository.count() == 0 && bookRepository.findById(1L).isPresent()
				&& goodsTypeRepository.findByName("Book") != null){
			Cart initCart = new Cart();
			CartItem book1CartItem = new CartItem();

			GoodsType bookType = goodsTypeRepository.findByName("Book");
			book1CartItem.setGoodsType(bookType);
			book1CartItem.setProductId(1L);
			book1CartItem.setQuantity(2);

			initCart.getCartItems().add(book1CartItem);

			cartRepository.save(initCart);
		}

	}
}
