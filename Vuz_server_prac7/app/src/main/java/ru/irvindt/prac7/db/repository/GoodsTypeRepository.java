package ru.irvindt.prac7.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.irvindt.prac7.db.model.GoodsType;


@Repository
public interface GoodsTypeRepository extends JpaRepository<GoodsType, Long>{
	GoodsType findByName(String name);
}
