package com.example.videorental.controller;

import com.example.videorental.model.Customer;
import com.example.videorental.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    // Получение всех клиентов (доступ: USER, ADMIN)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    // Получение клиента по ID (доступ: USER, ADMIN)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    // Поиск клиентов по ФИО (без учёта регистра) (доступ: USER, ADMIN)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/name/{name}")
    public ResponseEntity<List<Customer>> getCustomersByName(@PathVariable String name) {
        List<Customer> customers = customerRepository.findByFullNameContainingIgnoreCase(name);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    // Поиск клиента по email (без учёта регистра) (доступ: USER, ADMIN)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/email/{email}")
    public ResponseEntity<Customer> getCustomerByEmail(@PathVariable String email) {
        Customer customer = customerRepository.findByEmailIgnoreCase(email);
        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    // Создание нового клиента (доступ: ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    // Обновление данных клиента (доступ: ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        customer.setFullName(customerDetails.getFullName());
        customer.setEmail(customerDetails.getEmail());
        customer.setPhone(customerDetails.getPhone());
        Customer updatedCustomer = customerRepository.save(customer);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    // Удаление клиента (доступ: ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        customerRepository.delete(customer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
