package ru.irvindt.Vuz_server_prac5.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.irvindt.Vuz_server_prac5.db.model.WashingMachine;
import ru.irvindt.Vuz_server_prac5.db.repository.WashingMachineRepository;

import java.util.List;

@RestController
@RequestMapping("/api/washingmachine")
public class WashingMachineController {
	private final WashingMachineRepository washingMachineRepository;

	@Autowired
	public WashingMachineController(WashingMachineRepository washingMachineRepository) {
		this.washingMachineRepository = washingMachineRepository;
	}

	@GetMapping("/")
	public List<WashingMachine> getAllWashingMachines() {
		return washingMachineRepository.findAll();
	}

	@GetMapping("/{id}")
	public WashingMachine getWashingMachineById(@PathVariable Long id) {
		return washingMachineRepository.findById(id).orElse(null);
	}

	@PostMapping("/")
	public WashingMachine createWashingMachine(@RequestBody WashingMachine washingMachine) {
		return washingMachineRepository.save(washingMachine);
	}

	//!!! ВНИМАНИЕ !!!
	// Из-за того, что put запрос теперь может обновлять выборочные поля объекта, пропала возможность сделать цену равной 0
	@PutMapping("/{id}")
	public WashingMachine updateWashingMachine(@PathVariable Long id, @RequestBody WashingMachine updatedWashingMachine) {
		WashingMachine existingWashingMachine = washingMachineRepository.findById(id).orElse(null);
		if (existingWashingMachine != null) {
			// Обновляем только необходимые поля
			if (updatedWashingMachine.getManufacturer() != null) {
				existingWashingMachine.setManufacturer(updatedWashingMachine.getManufacturer());
			}
			if (updatedWashingMachine.getTankCapacity() != 0){
				existingWashingMachine.setTankCapacity(updatedWashingMachine.getTankCapacity());
			}
			if (updatedWashingMachine.getSellerNumber() != null) {
				existingWashingMachine.setSellerNumber(updatedWashingMachine.getSellerNumber());
			}
			if (updatedWashingMachine.getProductType() != null) {
				existingWashingMachine.setProductType(updatedWashingMachine.getProductType());
			}
			if (updatedWashingMachine.getPrice() != 0) {
				existingWashingMachine.setPrice(updatedWashingMachine.getPrice());
			}
			if (updatedWashingMachine.getName() != null) {
				existingWashingMachine.setName(updatedWashingMachine.getName());
			}

			return washingMachineRepository.save(existingWashingMachine);
		}
		return null;
	}

	@DeleteMapping("/{id}")
	public void deleteWashingMachine(@PathVariable Long id) {
		washingMachineRepository.deleteById(id);
	}
}
