package ru.irvindt.prac7.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private Long id;

	@JsonIgnore
	@OneToMany(mappedBy = "cartItem")
	@Getter
	@Setter
	private List<CartItemRelation> cartItemRelations = new ArrayList<>();

	@ManyToOne
	@Getter
	@Setter
	private GoodsType goodsType;

	@Column(nullable = false)
	@Getter
	@Setter
	private Long productId;
}
