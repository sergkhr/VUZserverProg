package ru.irvindt.prac7.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.irvindt.prac7.db.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
