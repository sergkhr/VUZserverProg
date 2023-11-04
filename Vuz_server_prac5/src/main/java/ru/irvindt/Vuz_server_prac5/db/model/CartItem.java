package ru.irvindt.Vuz_server_prac5.db.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


@Entity
//@Table(name = "cart_item")
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private Long id;

	@ManyToMany(mappedBy = "cartItems") //wow this thing is smart
	@Getter
	@Setter
	private Set<Cart> carts = new HashSet<>();

	@ManyToOne
	@Getter
	@Setter
	private GoodsType goodsType;

	@Column(nullable = false)
	@Getter
	@Setter
	private Long productId;

	@Column(nullable = false)
	@Getter
	@Setter
	private int quantity;
}
