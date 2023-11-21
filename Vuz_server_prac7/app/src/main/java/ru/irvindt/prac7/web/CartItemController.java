package ru.irvindt.prac7.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.irvindt.prac7.db.model.CartItem;
import ru.irvindt.prac7.db.repository.CartItemRepository;
import ru.irvindt.prac7.db.repository.GoodsTypeRepository;

//imports of entities
import ru.irvindt.prac7.db.model.Book;
import ru.irvindt.prac7.db.repository.BookRepository;
import ru.irvindt.prac7.web.token_cheching.TokenChecker;
import ru.irvindt.prac7.db.model.UserRole;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cartItem")
public class CartItemController {
	private final CartItemRepository cartItemRepository;
	private final GoodsTypeRepository goodsTypeRepository;
	private final TokenChecker tokenChecker;

	//entity repositories
	private final BookRepository bookRepository;

	@Autowired
	public CartItemController(CartItemRepository cartItemRepository,
	                          GoodsTypeRepository goodsTypeRepository,
	                          BookRepository bookRepository,
	                          TokenChecker tokenChecker) {
		this.cartItemRepository = cartItemRepository;
		this.goodsTypeRepository = goodsTypeRepository;
		this.bookRepository = bookRepository;
		this.tokenChecker = tokenChecker;
	}

	@GetMapping("/")
	public List<CartItem> getAllCartItems() {
		return cartItemRepository.findAll();
	}

	@PostMapping("/")
	public CartItem createCartItem(@RequestBody CartItem cartItem,
	                               @RequestHeader("Authorization") String header) {
		//check if user is admin
		if (tokenChecker.getRoleFromTokenHeader(header) != UserRole.ADMIN) {
			CartItem responceCartItem = new CartItem();
			//set id so the number can look like "not logged in" user
			responceCartItem.setId(403L);
			return responceCartItem;
		}
		return cartItemRepository.save(cartItem);
	}

	@PutMapping("/{id}")
	public CartItem updateCartItem(@PathVariable Long id,
	                               @RequestBody CartItem updatedCartItem,
	                               @RequestHeader("Authorization") String header) {
		//check if user is admin
		if(tokenChecker.getRoleFromTokenHeader(header) != UserRole.ADMIN){
			CartItem responceCartItem = new CartItem();
			//set id so the number can look like "not logged in" user
			responceCartItem.setId(403L);
			return responceCartItem;
		}

		Optional<CartItem> optionalCartItem = cartItemRepository.findById(id);
		if (optionalCartItem.isPresent()) {
			CartItem existingCartItem = optionalCartItem.get();

			// Обновляем только необходимые поля
			if (updatedCartItem.getId() != 0) {
				existingCartItem.setId(updatedCartItem.getId());
			}
			if (updatedCartItem.getGoodsType() != null) {
				existingCartItem.setGoodsType(updatedCartItem.getGoodsType());
			}
			if (updatedCartItem.getProductId() != 0){
				existingCartItem.setProductId(updatedCartItem.getProductId());
			}

			return cartItemRepository.save(existingCartItem);
		}
		return null;
	}

	@DeleteMapping("/{id}")
	public String deleteCartItem(@PathVariable Long id,
	                           @RequestHeader("Authorization") String header) {
		//check if user is admin
		if (tokenChecker.getRoleFromTokenHeader(header) != UserRole.ADMIN) {
			return "You are not admin";
		}
		cartItemRepository.deleteById(id);
		return "CartItem deleted";
	}

	public int getInStock(String goodsType, long productId){
		switch (goodsType){
			case "Book":
				Optional<Book> optionalBook = bookRepository.findById(productId);
				if(optionalBook.isPresent()){
					Book book = optionalBook.get();
					int inStock = book.getInStock();
					return Math.max(inStock, 0); //ok, this is genius
				}
				break;
		}
		return 0;
	}

	public void setInStock(String goodsType, long productId, int newInStock){
		//check if user is admin
		switch (goodsType){
			case "Book":
				Optional<Book> optionalBook = bookRepository.findById(productId);
				if(optionalBook.isPresent()){
					Book book = optionalBook.get();
					book.setInStock(newInStock);
					bookRepository.save(book);
				}
				break;
		}
	}
}
