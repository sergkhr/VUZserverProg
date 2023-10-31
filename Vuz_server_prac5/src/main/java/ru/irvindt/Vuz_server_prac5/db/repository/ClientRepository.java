package ru.irvindt.Vuz_server_prac5.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.irvindt.Vuz_server_prac5.db.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
