package com.example.videorental.service;

import com.example.videorental.model.Rental;
import com.example.videorental.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service // Сервис для работы с выдачами
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    // Возвращает список выдач по идентификатору клиента
    public List<Rental> getRentalsByCustomerId(Long customerId) {
        return rentalRepository.findByCustomer_Id(customerId);
    }

    // Возвращает список выдач по идентификатору фильма
    public List<Rental> getRentalsByFilmId(Long filmId) {
        return rentalRepository.findByFilm_Id(filmId);
    }

    // Возвращает список выдач с указанным статусом
    public List<Rental> getRentalsByStatus(Rental.RentalStatus status) {
        return rentalRepository.findByStatus(status);
    }
}
