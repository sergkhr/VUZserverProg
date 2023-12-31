package ru.irvindt.Vuz_server_prac5.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
