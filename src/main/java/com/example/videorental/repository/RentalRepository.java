package com.example.videorental.repository;

import com.example.videorental.model.Rental;
import com.example.videorental.model.Rental.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    // Поиск аренды по статусу
    List<Rental> findByStatus(RentalStatus status);

    // Поиск аренды по ID клиента
    List<Rental> findByCustomer_Id(Long customerId);

    // Поиск аренды по ID фильма
    List<Rental> findByFilm_Id(Long filmId);

    // Поиск аренды клиента по списку статусов
    List<Rental> findByCustomer_IdAndStatusIn(Long customerId, List<RentalStatus> statuses);
}
