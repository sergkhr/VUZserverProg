package ru.irvindt.prac7.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.irvindt.prac7.db.model.CartItemRelation;

@Repository
public interface CartItemRelationRepository extends JpaRepository<CartItemRelation, Long> {
}
