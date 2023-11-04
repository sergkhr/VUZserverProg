package ru.irvindt.Vuz_server_prac5.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.irvindt.Vuz_server_prac5.db.model.Cart;
import ru.irvindt.Vuz_server_prac5.db.repository.BookRepository;
import ru.irvindt.Vuz_server_prac5.db.repository.CartRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	private final CartRepository cartRepository;

	@Autowired
	public CartController(CartRepository cartRepository) {
		this.cartRepository = cartRepository;
	}

	@GetMapping("/")
	public List<Cart> getAllCarts() {
		return cartRepository.findAll();
	}
	//method for getting cart by id
	// with this, we return all the cart items
	@GetMapping("/{id}")
	public Cart getCartById(@PathVariable Long id) {
		return cartRepository.findById(id).orElse(null);
	}
}
