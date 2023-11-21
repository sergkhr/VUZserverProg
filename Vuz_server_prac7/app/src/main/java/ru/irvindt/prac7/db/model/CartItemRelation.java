package ru.irvindt.prac7.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRelation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private Long id;

	@JsonIgnore
	@ManyToOne
	@Getter
	@Setter
	private Cart cart;

	@ManyToOne
	@Getter
	@Setter
	private CartItem cartItem;

	@Column(nullable = false)
	@Getter
	@Setter
	private int quantity;
}
