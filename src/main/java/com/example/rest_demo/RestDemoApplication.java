package com.example.rest_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class RestDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestDemoApplication.class, args);
	}

}

class Coffee {
	private final String id; // 한번 생성한 이후 수정할 일이 없으므로 final 선언
	private String name;

	public Coffee(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public Coffee(String name){
		this(UUID.randomUUID().toString(), name);
	}
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

@RestController
class RestApiDemoController {
	private List<Coffee> coffees = new ArrayList<>();

	public RestApiDemoController() {
		coffees.addAll(List.of(
				new Coffee("Ice Americano"),
				new Coffee("Ice Latte"),
				new Coffee("Ice Capuchin"),
				new Coffee("Ice Hazelnut")
		));
	}

	@RequestMapping(value = "/coffees", method = RequestMethod.GET)
	Iterable<Coffee> getCoffees() {
		return coffees;
	}

	@PostMapping("/coffees")
	Coffee postCoffee (@RequestBody Coffee coffee){
		coffees.add(coffee);
		return coffee;
	}

	@PutMapping("/coffees/{id}")
	Coffee putCoffee (@PathVariable String id, @RequestBody Coffee coffee){
		int coffeeIndex = -1;

		for (Coffee c : coffees) {
			if (c.getId().equals(id)) {
				coffeeIndex = coffees.indexOf(c);
				coffees.set(coffeeIndex, coffee);
			}
		}

		return (coffeeIndex == -1) ? postCoffee(coffee) : coffee;
	}

	@DeleteMapping("/coffees/{id}")
	void deleteCoffee (@PathVariable String id){
		coffees.removeIf(c -> c.getId().equals(id));
	}
}


