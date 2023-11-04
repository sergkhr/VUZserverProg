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
//@Table(name = "cart")
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private Long id;

	//many to many with CartItem
	@ManyToMany
	@JoinTable(
			name = "cart_cartitem",
			joinColumns = @JoinColumn(name = "cart_id"),
			inverseJoinColumns = @JoinColumn(name = "cartitem_id")
	)
	@Getter
	@Setter
	private Set<CartItem> cartItems = new HashSet<>();
}
