package ru.irvindt.Vuz_server_prac5.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.irvindt.Vuz_server_prac5.db.model.Telephone;
import ru.irvindt.Vuz_server_prac5.db.repository.TelephoneRepository;

import java.util.List;

@RestController
@RequestMapping("/api/telephone")
public class TelephoneController {
	private final TelephoneRepository telephoneRepository;

	@Autowired
	public TelephoneController(TelephoneRepository telephoneRepository) {
		this.telephoneRepository = telephoneRepository;
	}

	@GetMapping("/")
	public List<Telephone> getAllTelephones() {
		return telephoneRepository.findAll();
	}

	@GetMapping("/{id}")
	public Telephone getTelephoneById(@PathVariable Long id) {
		return telephoneRepository.findById(id).orElse(null);
	}

	@PostMapping("/")
	public Telephone createTelephone(@RequestBody Telephone telephone) {
		return telephoneRepository.save(telephone);
	}

	//!!! ВНИМАНИЕ !!!
	// Из-за того, что put запрос теперь может обновлять выборочные поля объекта, пропала возможность сделать цену равной 0
	@PutMapping("/{id}")
	public Telephone updateTelephone(@PathVariable Long id, @RequestBody Telephone updatedTelephone) {
		Telephone existingTelephone = telephoneRepository.findById(id).orElse(null);
		if (existingTelephone != null) {
			// Обновляем только необходимые поля
			if(updatedTelephone.getManufacturer() != null) {
				existingTelephone.setManufacturer(updatedTelephone.getManufacturer());
			}
			if(updatedTelephone.getBatteryCapacity() != 0) {
				existingTelephone.setBatteryCapacity(updatedTelephone.getBatteryCapacity());
			}
			if(updatedTelephone.getSellerNumber() != null) {
				existingTelephone.setSellerNumber(updatedTelephone.getSellerNumber());
			}
			if(updatedTelephone.getProductType() != null) {
				existingTelephone.setProductType(updatedTelephone.getProductType());
			}
			if(updatedTelephone.getPrice() != 0) {
				existingTelephone.setPrice(updatedTelephone.getPrice());
			}
			if(updatedTelephone.getName() != null) {
				existingTelephone.setName(updatedTelephone.getName());
			}

			return telephoneRepository.save(existingTelephone);
		}
		return null;
	}

	@DeleteMapping("/{id}")
	public void deleteTelephone(@PathVariable Long id) {
		telephoneRepository.deleteById(id);
	}
}
