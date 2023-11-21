package ru.irvindt.prac7.db.init;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.irvindt.prac7.db.model.*;
import ru.irvindt.prac7.db.repository.*;
import ru.irvindt.prac7.db.model.UserAccount;
import ru.irvindt.prac7.db.model.UserRole;
import ru.irvindt.prac7.db.repository.UserRepository;

@Component
public class Init {
	private final BookRepository bookRepository;
	private final GoodsTypeRepository goodsTypeRepository;
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final CartItemRelationRepository cartItemRelationRepository;
	private final UserRepository userRepository;

	@Autowired
	public Init(BookRepository bookRepository, GoodsTypeRepository goodsTypeRepository,
	            CartRepository cartRepository, CartItemRepository cartItemRepository,
	            CartItemRelationRepository cartItemRelationRepository,
	            UserRepository userRepository) {
		this.bookRepository = bookRepository;
		this.goodsTypeRepository = goodsTypeRepository;
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
		this.cartItemRelationRepository = cartItemRelationRepository;
		this.userRepository = userRepository;

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


		if (cartRepository.count() == 0 && bookRepository.findById(1L).isPresent()) {
			//yes a duplicate, but I'm too tired
			GoodsType bookType = goodsTypeRepository.findByName("Book");
			if (bookType == null) {
				bookType = new GoodsType();
				bookType.setName("Book");
				goodsTypeRepository.save(bookType);
			}

			Cart initCart = new Cart();
			CartItem book1CartItem = new CartItem();
			CartItemRelation book1CartItemRelation = new CartItemRelation();

			book1CartItem.setGoodsType(bookType);
			book1CartItem.setProductId(1L);

			book1CartItemRelation.setCart(initCart);
			book1CartItemRelation.setCartItem(book1CartItem);
			book1CartItemRelation.setQuantity(1);

			book1CartItem.getCartItemRelations().add(book1CartItemRelation);
			initCart.getCartItemRelations().add(book1CartItemRelation);

			cartItemRepository.save(book1CartItem);
			cartRepository.save(initCart);
			cartItemRelationRepository.save(book1CartItemRelation);
		}

		//initializing admin
		if (userRepository.findByLogin("admin").isEmpty()) {
			UserAccount admin = new UserAccount();
			admin.setLogin("admin"); //о да
			admin.setPassword("admin");
			admin.setRole(UserRole.ADMIN);
			userRepository.save(admin);
		}

	}
}
