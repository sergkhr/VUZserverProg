package ru.irvindt.prac7.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.irvindt.prac7.db.model.CartItemRelation;
import ru.irvindt.prac7.db.repository.CartItemRelationRepository;
import ru.irvindt.prac7.db.repository.CartItemRepository;
import ru.irvindt.prac7.db.repository.CartRepository;

import java.util.List;

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
