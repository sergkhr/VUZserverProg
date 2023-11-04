package ru.irvindt.Vuz_server_prac5.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.irvindt.Vuz_server_prac5.db.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
