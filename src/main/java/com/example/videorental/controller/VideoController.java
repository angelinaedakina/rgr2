package com.example.videorental.controller;

import com.example.videorental.repository.FilmRepository;
import com.example.videorental.repository.RentalRepository;
import com.example.videorental.repository.CustomerRepository;
import com.example.videorental.model.Rental.RentalStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/video")
public class VideoController {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // Получение общей статистики видеопроката (доступ USER, ADMIN)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getVideoStats() {
        Map<String, Object> stats = new HashMap<>();
        long filmCount = filmRepository.count(); // Количество фильмов
        long totalRentals = rentalRepository.count(); // Общее число выдач
        long activeRentals = rentalRepository.findByStatus(RentalStatus.ACTIVE).size() +
                rentalRepository.findByStatus(RentalStatus.OVERDUE).size(); // Активные/просроченные выдачи
        long debtorsCount = customerRepository.findDebtors().size(); // Число клиентов-должников
        long neverRentedCount = filmRepository.findAll().stream()
                .filter(f -> f.getRentals() == null || f.getRentals().isEmpty())
                .count(); // Фильмы, которые ни разу не выдавались

        // Заполняем объект статистики
        stats.put("filmCount", filmCount);
        stats.put("totalRentals", totalRentals);
        stats.put("activeRentals", activeRentals);
        stats.put("debtorsCount", debtorsCount);
        stats.put("neverRentedCount", neverRentedCount);

        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
