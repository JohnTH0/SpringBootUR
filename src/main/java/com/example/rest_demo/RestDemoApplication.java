package com.example.rest_demo;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class RestDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestDemoApplication.class, args);
	}

}

@NoArgsConstructor
@Entity
@Getter
@Setter
class Coffee {
	@Id
	private String id;
	private String name;

	public Coffee(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public Coffee(String name){
		this(UUID.randomUUID().toString(), name);
	}

}

@Component
class DataLoader {
	private final CoffeeRepository coffeeRepository;
	public DataLoader(CoffeeRepository coffeeRepository) {
		this.coffeeRepository = coffeeRepository;
	}

	@PostConstruct
	private void loadData() {
		coffeeRepository.saveAll(List.of(
				new Coffee("Ice Americano"),
				new Coffee("Ice Latte"),
				new Coffee("Ice Capuchin"),
				new Coffee("Ice Hazelnut")
		));
	}
}

@RestController
@RequestMapping("/greeting")
class GreetingController {
	@Value("${greeting-name:Mirage}")
	private String name;

	@Value("${greeting-coffee: ${greeting-name} is drinking Cafe Ganador}")
	private String coffee;

	@GetMapping
	String getGreeting() {
		return name;
	}

	@GetMapping("/coffee")
	String getNameAndCoffee(){
		return coffee;
	}
}


@RestController
@RequestMapping("/coffees")
class RestApiDemoController {
	private final CoffeeRepository coffeeRepository;

	public RestApiDemoController(CoffeeRepository coffeeRepository ) {
		this.coffeeRepository = coffeeRepository;

	}

	@GetMapping
	Iterable<Coffee> getCoffees() {
		return coffeeRepository.findAll();
	}

	@GetMapping("/{id}")
	Optional<Coffee> getCoffeeById(@PathVariable String id) {
		return coffeeRepository.findById(id);
	}

	@PostMapping
	Coffee postCoffee (@RequestBody Coffee coffee){
		return coffeeRepository.save(coffee);
	}

	@PutMapping("/{id}")
	ResponseEntity<Coffee> putCoffee (@PathVariable String id, @RequestBody Coffee coffee){
		return (coffeeRepository.existsById(id))
				? new ResponseEntity<>(coffeeRepository.save(coffee), HttpStatus.OK)
				: new ResponseEntity<>(coffeeRepository.save(coffee), HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	void deleteCoffee (@PathVariable String id){
		coffeeRepository.deleteById(id);
	}
}


