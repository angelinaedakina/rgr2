package com.example.videorental.controller;

import com.example.videorental.model.Rental;
import com.example.videorental.model.Rental.RentalStatus;
import com.example.videorental.model.Film;
import com.example.videorental.model.Customer;
import com.example.videorental.repository.RentalRepository;
import com.example.videorental.repository.FilmRepository;
import com.example.videorental.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rental")
public class RentalController {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // Создание новой выдачи (доступ USER, ADMIN)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/")
    public ResponseEntity<?> createRental(@RequestBody Rental rental) {
        try {
            // Получить фильм по ID
            Film film = filmRepository.findById(rental.getFilm().getId())
                    .orElseThrow(() -> new RuntimeException("Film not found with id: " + rental.getFilm().getId()));
            // Получить клиента по ID
            Customer customer = customerRepository.findById(rental.getCustomer().getId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + rental.getCustomer().getId()));
            // Проверить количество активных/просроченных выдач
            List<Rental> activeRentals = rentalRepository.findByCustomer_IdAndStatusIn(
                    customer.getId(), List.of(Rental.RentalStatus.ACTIVE, Rental.RentalStatus.OVERDUE)
            );
            if (activeRentals.size() >= 2) {
                return new ResponseEntity<>("Customer has at least 2 active/overdue rentals and cannot rent another film.", HttpStatus.BAD_REQUEST);
            }
            // Установить фильм, клиента и статус
            rental.setFilm(film);
            rental.setCustomer(customer);
            rental.setStatus(Rental.RentalStatus.ACTIVE);
            // Сохранить выдачу
            Rental savedRental = rentalRepository.save(rental);
            return new ResponseEntity<>(savedRental, HttpStatus.CREATED);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Получить все выдачи (доступ USER, ADMIN)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<Rental>> getAllRentals() {
        try {
            List<Rental> rentals = rentalRepository.findAll();
            return new ResponseEntity<>(rentals, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Получить выдачу по ID (доступ USER, ADMIN)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Rental> getRentalById(@PathVariable Long id) {
        try {
            Rental rental = rentalRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Rental not found with id: " + id));
            return new ResponseEntity<>(rental, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Получить выдачи клиента (доступ ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/customer/{id}")
    public ResponseEntity<List<Rental>> getRentalsByCustomerId(@PathVariable Long id) {
        try {
            List<Rental> rentals = rentalRepository.findByCustomer_Id(id);
            return new ResponseEntity<>(rentals, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Получить выдачи фильма (доступ ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/film/{id}")
    public ResponseEntity<List<Rental>> getRentalsByFilmId(@PathVariable Long id) {
        try {
            List<Rental> rentals = rentalRepository.findByFilm_Id(id);
            return new ResponseEntity<>(rentals, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Возврат книги: обновить выдачу (доступ ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Rental> returnRental(@PathVariable Long id) {
        try {
            Rental rental = rentalRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Rental not found with id: " + id));
            // Проверить, можно ли вернуть книгу
            if (rental.getStatus() != RentalStatus.ACTIVE && rental.getStatus() != RentalStatus.OVERDUE) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            // Установить дату возврата и обновить статус
            rental.setRentalReturn(LocalDateTime.now());
            rental.setStatus(RentalStatus.COMPLETED);
            Rental updatedRental = rentalRepository.save(rental);
            return new ResponseEntity<>(updatedRental, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
