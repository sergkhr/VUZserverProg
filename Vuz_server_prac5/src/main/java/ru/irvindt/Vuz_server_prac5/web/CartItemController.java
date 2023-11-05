package ru.irvindt.Vuz_server_prac5.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.irvindt.Vuz_server_prac5.db.model.CartItem;
import ru.irvindt.Vuz_server_prac5.db.repository.CartItemRepository;
import ru.irvindt.Vuz_server_prac5.db.repository.CartRepository;
import ru.irvindt.Vuz_server_prac5.db.repository.GoodsTypeRepository;

//imports of entities
import ru.irvindt.Vuz_server_prac5.db.model.Book;
import ru.irvindt.Vuz_server_prac5.db.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cartItem")
public class CartItemController {
	private final CartItemRepository cartItemRepository;
	private final GoodsTypeRepository goodsTypeRepository;

	//entity repositories
	private final BookRepository bookRepository;

	@Autowired
	public CartItemController(CartItemRepository cartItemRepository,
	                          GoodsTypeRepository goodsTypeRepository,
	                          BookRepository bookRepository) {
		this.cartItemRepository = cartItemRepository;
		this.goodsTypeRepository = goodsTypeRepository;
		this.bookRepository = bookRepository;
	}

	@GetMapping("/")
	public List<CartItem> getAllCartItems() {
		return cartItemRepository.findAll();
	}

	@PostMapping("/")
	public CartItem createCartItem(@RequestBody CartItem cartItem) {
		return cartItemRepository.save(cartItem);
	}

	@PutMapping("/{id}")
	public CartItem updateCartItem(@PathVariable Long id, @RequestBody CartItem updatedCartItem) {
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
	public void deleteCartItem(@PathVariable Long id) {
		cartItemRepository.deleteById(id);
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
