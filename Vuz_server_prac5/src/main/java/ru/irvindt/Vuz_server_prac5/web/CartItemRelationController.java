package ru.irvindt.Vuz_server_prac5.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.irvindt.Vuz_server_prac5.db.model.Cart;
import ru.irvindt.Vuz_server_prac5.db.model.CartItem;
import ru.irvindt.Vuz_server_prac5.db.model.CartItemRelation;
import ru.irvindt.Vuz_server_prac5.db.repository.BookRepository;
import ru.irvindt.Vuz_server_prac5.db.repository.CartItemRelationRepository;
import ru.irvindt.Vuz_server_prac5.db.repository.CartItemRepository;
import ru.irvindt.Vuz_server_prac5.db.repository.CartRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/relations/cartItem")
public class CartItemRelationController {
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final CartItemRelationRepository cartItemRelationRepository;

	@Autowired
	public CartItemRelationController(CartRepository cartRepository, CartItemRepository cartItemRepository, CartItemRelationRepository cartItemRelationRepository) {
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
		this.cartItemRelationRepository = cartItemRelationRepository;
	}

	@GetMapping("/")
	public List<CartItemRelation> getAllCartItemRelations() {
		return cartItemRelationRepository.findAll();
	}
}
