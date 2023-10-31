package ru.irvindt.Vuz_server_prac5.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.irvindt.Vuz_server_prac5.db.model.Client;
import ru.irvindt.Vuz_server_prac5.db.repository.ClientRepository;

import java.util.List;

@RestController
@RequestMapping("/api/client")
public class ClientController {
	private final ClientRepository clientRepository;

	@Autowired
	public ClientController(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	@GetMapping("/")
	public List<Client> getAllClients() {
		return clientRepository.findAll();
	}

	@GetMapping("/{id}")
	public Client getClientById(@PathVariable Long id) {
		return clientRepository.findById(id).orElse(null);
	}

	@PostMapping("/")
	public Client createClient(@RequestBody Client client) {
		return clientRepository.save(client);
	}

	//!!! ВНИМАНИЕ !!!
	// Из-за того, что put запрос теперь может обновлять выборочные поля объекта, пропала возможность сделать цену равной 0
	@PutMapping("/{id}")
	public Client updateClient(@PathVariable Long id, @RequestBody Client updatedClient) {
		Client existingClient = clientRepository.findById(id).orElse(null);
		if (existingClient != null) {
			// Обновляем только необходимые поля
			if (updatedClient.getName() != null) {
				existingClient.setName(updatedClient.getName());
			}
			if (updatedClient.getEmail() != null) {
				existingClient.setEmail(updatedClient.getEmail());
			}
			if (updatedClient.getUsername() != null) {
				existingClient.setUsername(updatedClient.getUsername());
			}
			if (updatedClient.getPassword() != null) {
				existingClient.setPassword(updatedClient.getPassword());
			}

			return clientRepository.save(existingClient);
		}
		return null;
	}

	@DeleteMapping("/{id}")
	public void deleteClient(@PathVariable Long id) {
		clientRepository.deleteById(id);
	}
}
