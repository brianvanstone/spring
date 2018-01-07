package tech.notpaper.spring.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tech.notpaper.spring.dao.CustomerDAO;
import tech.notpaper.spring.model.Customer;

@RestController
public class CustomerRestController {
	
	@Autowired
	private CustomerDAO customerDAO;
	
	@GetMapping("/customers")
	public List<Customer> getCustomers() {
		return customerDAO.list();
	}
	
	@GetMapping("/customer/{id}")
	public ResponseEntity<? extends Object> getCustomer(@PathVariable("id") Long id) {
		Optional<Customer> opt = customerDAO.get(id);
		if (opt.isPresent()) {
			return new ResponseEntity<Customer>(opt.get(), HttpStatus.OK);
		} else {
			return customerNotFound(id);
		}
	}
	
	private static ResponseEntity<String> customerNotFound(Long id) {
		return new ResponseEntity<>("No Customer found for ID " + id, HttpStatus.NOT_FOUND);
	}
	
	@PostMapping(value = "/customers")
	public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
		customerDAO.create(customer);
		
		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/customer/{id}")
	public ResponseEntity<? extends Object> deleteCustomer(@PathVariable Long id) {
		if (customerDAO.delete(id)) {
			return new ResponseEntity<Long>(id, HttpStatus.OK);
		} else {
			return customerNotFound(id);
		}
	}
	
	@PutMapping("/customer/{id}")
	public ResponseEntity<? extends Object> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
		Optional<Customer> opt = customerDAO.update(id, customer);
		
		if (opt.isPresent()) {
			return new ResponseEntity<Customer>(customer, HttpStatus.OK);
		} else {
			return customerNotFound(id);
		}
	}
}
