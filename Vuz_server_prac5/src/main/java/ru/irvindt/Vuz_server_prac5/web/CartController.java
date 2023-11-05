package ru.irvindt.Vuz_server_prac5.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.irvindt.Vuz_server_prac5.db.model.Cart;
import ru.irvindt.Vuz_server_prac5.db.model.CartItem;
import ru.irvindt.Vuz_server_prac5.db.model.CartItemRelation;
import ru.irvindt.Vuz_server_prac5.db.repository.CartItemRelationRepository;
import ru.irvindt.Vuz_server_prac5.db.repository.CartItemRepository;
import ru.irvindt.Vuz_server_prac5.db.repository.CartRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final CartItemRelationRepository cartItemRelationRepository;
	private final CartItemController cartItemController;

	@Autowired
	public CartController(CartRepository cartRepository,
	                      CartItemRepository cartItemRepository,
	                      CartItemRelationRepository cartItemRelationRepository,
	                      CartItemController cartItemController) {
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
		this.cartItemRelationRepository = cartItemRelationRepository;
		this.cartItemController = cartItemController;
	}

	@GetMapping("/")
	public List<Cart> getAllCarts() {
		return cartRepository.findAll();
	}

	@PostMapping("/")
	public Cart createCart() { //managing the carts ids is for other logic
		Cart cart = new Cart();
		return cartRepository.save(cart);
	}

	@GetMapping("/{id}")
	public Cart getCartById(@PathVariable Long id) {
		return cartRepository.findById(id).orElse(null);
	}

	//method for clearing the cart
	@DeleteMapping("/{id}/clear")
	public Cart clearCart(@PathVariable Long id){
		Optional<Cart> optionalCart = cartRepository.findById(id);
		if(optionalCart.isPresent()){
			Cart existingCart = optionalCart.get();
			List<CartItemRelation> itemsToDelete = new ArrayList<>();
			for(CartItemRelation cartItemRelation : existingCart.getCartItemRelations()){
				itemsToDelete.add(cartItemRelation);
			}
			for(CartItemRelation cartItemRelation : itemsToDelete){
				existingCart.getCartItemRelations().remove(cartItemRelation);
				cartItemRelation.getCartItem().getCartItemRelations().remove(cartItemRelation);
				cartItemRelationRepository.delete(cartItemRelation);
			}
			return cartRepository.save(existingCart);
		}
		return null;
	}

	@DeleteMapping("/{id}")
	public void deleteCart(@PathVariable Long id) { //this can be done only if cart is empty
		cartRepository.deleteById(id);
	}


	//method for adding cart item to cart

	//!!!WARNING!!!: when new relation is added it gets a new id even if the previous is free
	// (so we can have id=1 and id=3 and there will be no id=2)
	// this is probably bad, but I'm too lazy to fix it
	// (can be fixed by manually setting id for new relation)
	//!!!END OF WARNING!!!
	@PostMapping("/{id}/add/{cartItem_id}")
	public Cart addCartItemToCart(@PathVariable Long id, @PathVariable Long cartItem_id) {
		Optional<Cart> optionalCart = cartRepository.findById(id);
		if (optionalCart.isPresent()) {
			Cart existingCart = optionalCart.get();
			Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItem_id);
			if (optionalCartItem.isPresent()) {
				CartItem existingCartItem = optionalCartItem.get();
				//cart item relation
				CartItemRelation cartItemRelation = new CartItemRelation();
				cartItemRelation.setCart(existingCart);
				cartItemRelation.setCartItem(existingCartItem);
				cartItemRelation.setQuantity(1); //default quantity
				cartItemRelationRepository.save(cartItemRelation);

				//updating cart and cart item and saving them
				existingCart.getCartItemRelations().add(cartItemRelation);
				existingCartItem.getCartItemRelations().add(cartItemRelation);
				cartItemRepository.save(existingCartItem);

				return cartRepository.save(existingCart);
			}
		}
		return null;
	}

	//method for removing cart item from cart
	@DeleteMapping("/{id}/remove/{cartItem_id}")
	public Cart removeCartItemFromCart(@PathVariable Long id, @PathVariable Long cartItem_id){
		Optional<Cart> optionalCart = cartRepository.findById(id);
		if(optionalCart.isPresent()){
			Cart existingCart = optionalCart.get();
			Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItem_id);
			if(optionalCartItem.isPresent()){
				CartItem existingCartItem = optionalCartItem.get();
				//removing cart item relation
				for(CartItemRelation cartItemRelation : existingCart.getCartItemRelations()){
					if(cartItemRelation.getCartItem().getId() == existingCartItem.getId()){
						existingCart.getCartItemRelations().remove(cartItemRelation);
						existingCartItem.getCartItemRelations().remove(cartItemRelation);
						cartItemRelationRepository.delete(cartItemRelation);
						break;
					}
				}

				//saving cart and cart item
				cartItemRepository.save(existingCartItem);
				return cartRepository.save(existingCart);
			}
		}
		return null;
	}

	//method for changing quantity of cart item in cart
	@PutMapping("/{id}/change/{cartItem_id}/{quantity}")
	public CartItemRelation updateQuantity(@PathVariable Long id,
	                                       @PathVariable Long cartItem_id,
	                                       @PathVariable int quantity){
		Optional<Cart> optionalCart = cartRepository.findById(id);
		if(optionalCart.isPresent()){
			Cart existingCart = optionalCart.get();
			Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItem_id);
			if(optionalCartItem.isPresent()){
				CartItem existingCartItem = optionalCartItem.get();
				//changing quantity
				for(CartItemRelation cartItemRelation : existingCart.getCartItemRelations()){
					if(cartItemRelation.getCartItem().getId() == existingCartItem.getId()){
						cartItemRelation.setQuantity(quantity);
						cartItemRelationRepository.save(cartItemRelation);
						return cartItemRelation;
					}
				}
			}
		}
		return null;
	}

	//this method return responce with a string message
	@GetMapping("/confirm/{id}")
	public String confirmCart(@PathVariable Long id){
		Optional<Cart> optionalCart = cartRepository.findById(id);
		if(optionalCart.isPresent()){
			Cart existingCart = optionalCart.get();
			for(CartItemRelation cartItemRelation : existingCart.getCartItemRelations()){
				CartItem cartItem = cartItemRelation.getCartItem();
				String goodsType = cartItem.getGoodsType().getName();
				long productId = cartItem.getProductId();
				if(cartItemController.getInStock(goodsType, productId) < cartItemRelation.getQuantity()){
					return "Not enough items in stock";
				}
			} //we check that everything is enough in stock, only then we change the stock
			for(CartItemRelation cartItemRelation : existingCart.getCartItemRelations()){
				CartItem cartItem = cartItemRelation.getCartItem();
				String goodsType = cartItem.getGoodsType().getName();
				long productId = cartItem.getProductId();
				int quantity = cartItemRelation.getQuantity();
				cartItemController.setInStock(goodsType, productId,
						cartItemController.getInStock(goodsType, productId) - quantity);
			}
			clearCart(id);
			return "Cart confirmed and cleared";
		}
		return "Cart not found";
	}

}
